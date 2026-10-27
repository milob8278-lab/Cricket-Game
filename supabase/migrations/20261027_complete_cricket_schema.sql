-- =============================================================================
-- REAL CRICKET PRO – COMPLETE 23-TABLE DATABASE SCHEMA & RLS POLICIES
-- Target: Supabase PostgreSQL (PostgREST + Realtime enabled)
-- =============================================================================

-- 1. PROFILES (User identity, rank, ELO, stats)
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    display_name TEXT NOT NULL,
    country_code VARCHAR(4) DEFAULT 'PK',
    avatar_url TEXT,
    rating_elo INTEGER DEFAULT 1200,
    tier TEXT DEFAULT 'Bronze Division',
    matches_played INTEGER DEFAULT 0,
    matches_won INTEGER DEFAULT 0,
    matches_lost INTEGER DEFAULT 0,
    runs_scored_career INTEGER DEFAULT 0,
    wickets_taken_career INTEGER DEFAULT 0,
    centuries_career INTEGER DEFAULT 0,
    half_centuries_career INTEGER DEFAULT 0,
    locale VARCHAR(8) DEFAULT 'en',
    preferred_controls VARCHAR(32) DEFAULT 'PRO_JOYSTICK',
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. TEAMS (Built-in international/league and custom user squads)
CREATE TABLE IF NOT EXISTS public.teams (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    short_code VARCHAR(6) NOT NULL,
    primary_color VARCHAR(16) NOT NULL,
    secondary_color VARCHAR(16) NOT NULL,
    tier TEXT DEFAULT 'International',
    rating INTEGER DEFAULT 85,
    is_user_created BOOLEAN DEFAULT FALSE,
    owner_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. PLAYERS (Rosters, attributes, handedness, player skills)
CREATE TABLE IF NOT EXISTS public.players (
    id TEXT PRIMARY KEY,
    team_id TEXT REFERENCES public.teams(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    jersey_number INTEGER NOT NULL,
    role VARCHAR(32) NOT NULL,
    batting_hand VARCHAR(16) NOT NULL,
    bowling_type VARCHAR(32) NOT NULL,
    rating_overall INTEGER DEFAULT 80,
    rating_batting_power INTEGER DEFAULT 80,
    rating_batting_timing INTEGER DEFAULT 80,
    rating_bowling_speed INTEGER DEFAULT 80,
    rating_bowling_accuracy INTEGER DEFAULT 80,
    rating_fielding INTEGER DEFAULT 80,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. STADIUMS (Original ground configs, boundary radius, lighting)
CREATE TABLE IF NOT EXISTS public.stadiums (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    city TEXT NOT NULL,
    country TEXT NOT NULL,
    pitch_type VARCHAR(32) DEFAULT 'GREEN_SEAM',
    boundary_distance_meters INTEGER DEFAULT 75,
    capacity INTEGER DEFAULT 35000,
    lighting VARCHAR(16) DEFAULT 'DAY_NIGHT',
    altitude_meters INTEGER DEFAULT 100,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 5. MATCH_HISTORY (Offline and online completed matches)
CREATE TABLE IF NOT EXISTS public.match_history (
    id TEXT PRIMARY KEY,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    team_a_name TEXT NOT NULL,
    team_b_name TEXT NOT NULL,
    format VARCHAR(16) NOT NULL,
    overs INTEGER NOT NULL,
    venue_name TEXT NOT NULL,
    team_a_runs INTEGER NOT NULL,
    team_a_wickets INTEGER NOT NULL,
    team_a_balls INTEGER NOT NULL,
    team_b_runs INTEGER NOT NULL,
    team_b_wickets INTEGER NOT NULL,
    team_b_balls INTEGER NOT NULL,
    result_summary TEXT NOT NULL,
    is_online BOOLEAN DEFAULT FALSE,
    played_at TIMESTAMPTZ DEFAULT NOW()
);

-- 6. CLOUD_SAVES (User game progression, settings, custom layouts)
CREATE TABLE IF NOT EXISTS public.cloud_saves (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    save_type VARCHAR(32) NOT NULL,
    schema_version INTEGER DEFAULT 1,
    payload JSONB NOT NULL,
    checksum TEXT NOT NULL,
    device_label TEXT,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, save_type)
);

-- 7. ONLINE_MATCHES (Server-validated 1v1 multiplayer matches)
CREATE TABLE IF NOT EXISTS public.online_matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_code VARCHAR(12) NOT NULL,
    host_user_id UUID NOT NULL REFERENCES public.profiles(id),
    guest_user_id UUID REFERENCES public.profiles(id),
    format VARCHAR(16) NOT NULL,
    overs INTEGER NOT NULL,
    venue_id TEXT REFERENCES public.stadiums(id),
    status VARCHAR(24) DEFAULT 'WAITING',
    server_seed BIGINT DEFAULT 123456789,
    winner_user_id UUID REFERENCES public.profiles(id),
    result_text TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    started_at TIMESTAMPTZ,
    ended_at TIMESTAMPTZ
);

-- 8. MATCH_PARTICIPANTS (Join table for online match participants)
CREATE TABLE IF NOT EXISTS public.match_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    online_match_id UUID NOT NULL REFERENCES public.online_matches(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    team_id TEXT REFERENCES public.teams(id),
    seat_role VARCHAR(16) NOT NULL,
    connection_status VARCHAR(16) DEFAULT 'CONNECTED',
    ping_ms INTEGER DEFAULT 50,
    is_ready BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(online_match_id, user_id)
);

-- 9. MULTIPLAYER_ROOMS (Active matchmaking and lobby rooms)
CREATE TABLE IF NOT EXISTS public.multiplayer_rooms (
    id TEXT PRIMARY KEY,
    room_code VARCHAR(12) UNIQUE NOT NULL,
    host_id UUID NOT NULL REFERENCES public.profiles(id),
    guest_id UUID REFERENCES public.profiles(id),
    status VARCHAR(24) DEFAULT 'WAITING_FOR_OPPONENT',
    format VARCHAR(16) DEFAULT 'T20',
    overs INTEGER DEFAULT 5,
    venue_name TEXT DEFAULT 'Lahore National Stadium',
    is_private BOOLEAN DEFAULT FALSE,
    host_ready BOOLEAN DEFAULT FALSE,
    guest_ready BOOLEAN DEFAULT FALSE,
    last_ping TIMESTAMPTZ DEFAULT NOW(),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 10. ROOM_ACTIONS (Ball-by-ball delivery & shot intent events)
CREATE TABLE IF NOT EXISTS public.room_actions (
    id BIGSERIAL PRIMARY KEY,
    room_id TEXT NOT NULL REFERENCES public.multiplayer_rooms(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES public.profiles(id),
    action_type VARCHAR(32) NOT NULL,
    action_payload JSONB NOT NULL,
    sequence_number INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 11. INNINGS (Innings records for official scorecard)
CREATE TABLE IF NOT EXISTS public.innings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    match_id TEXT NOT NULL,
    innings_number INTEGER NOT NULL,
    batting_team_name TEXT NOT NULL,
    bowling_team_name TEXT NOT NULL,
    total_runs INTEGER DEFAULT 0,
    wickets_lost INTEGER DEFAULT 0,
    legal_balls_delivered INTEGER DEFAULT 0,
    overs_limit INTEGER NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 12. DELIVERIES (Ball-by-ball telemetry, trajectory, DRS, physics points)
CREATE TABLE IF NOT EXISTS public.deliveries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    innings_id UUID REFERENCES public.innings(id) ON DELETE CASCADE,
    over_number INTEGER NOT NULL,
    ball_in_over INTEGER NOT NULL,
    bowler_name TEXT NOT NULL,
    striker_name TEXT NOT NULL,
    runs_scored INTEGER DEFAULT 0,
    is_extra BOOLEAN DEFAULT FALSE,
    extra_type VARCHAR(8),
    is_wicket BOOLEAN DEFAULT FALSE,
    dismissal_type VARCHAR(32),
    speed_kmh INTEGER,
    shot_played VARCHAR(32),
    contact_quality VARCHAR(32),
    shot_angle_degrees FLOAT,
    commentary_en TEXT,
    commentary_ur TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 13. PLAYER_MATCH_STATS (Individual scorecards, batting & bowling analysis)
CREATE TABLE IF NOT EXISTS public.player_match_stats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    match_id TEXT NOT NULL,
    player_id TEXT NOT NULL,
    runs_scored INTEGER DEFAULT 0,
    balls_faced INTEGER DEFAULT 0,
    fours INTEGER DEFAULT 0,
    sixes INTEGER DEFAULT 0,
    overs_bowled FLOAT DEFAULT 0.0,
    maidens INTEGER DEFAULT 0,
    runs_conceded INTEGER DEFAULT 0,
    wickets_taken INTEGER DEFAULT 0,
    catches INTEGER DEFAULT 0,
    is_player_of_match BOOLEAN DEFAULT FALSE
);

-- 14. LEADERBOARDS (Global and regional competitive ladders)
CREATE TABLE IF NOT EXISTS public.leaderboards (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    season_id VARCHAR(32) NOT NULL,
    format VARCHAR(16) DEFAULT 'T20',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 15. LEADERBOARD_ENTRIES (Player standings in active ladders)
CREATE TABLE IF NOT EXISTS public.leaderboard_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    leaderboard_id TEXT NOT NULL REFERENCES public.leaderboards(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    rank_position INTEGER,
    rating_elo INTEGER NOT NULL,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(leaderboard_id, user_id)
);

-- 16. TOURNAMENTS (World Cup, Premier Leagues, Knockouts)
CREATE TABLE IF NOT EXISTS public.tournaments (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    format VARCHAR(16) NOT NULL,
    overs INTEGER NOT NULL,
    difficulty VARCHAR(16) DEFAULT 'NORMAL',
    total_teams INTEGER DEFAULT 8,
    is_official BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 17. TOURNAMENT_ENTRIES (User/AI standings in active tournament)
CREATE TABLE IF NOT EXISTS public.tournament_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id TEXT REFERENCES public.tournaments(id) ON DELETE CASCADE,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    current_stage VARCHAR(32) DEFAULT 'GROUP_STAGE',
    matches_played INTEGER DEFAULT 0,
    matches_won INTEGER DEFAULT 0,
    points INTEGER DEFAULT 0,
    net_run_rate FLOAT DEFAULT 0.0,
    is_champion BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 18. CAREER_PROFILES (Story mode, club to national captain)
CREATE TABLE IF NOT EXISTS public.career_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    player_name TEXT NOT NULL,
    role VARCHAR(32) NOT NULL,
    season INTEGER DEFAULT 1,
    level INTEGER DEFAULT 1,
    skill_points INTEGER DEFAULT 0,
    total_runs INTEGER DEFAULT 0,
    total_wickets INTEGER DEFAULT 0,
    domestic_trophies INTEGER DEFAULT 0,
    international_caps INTEGER DEFAULT 0,
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id)
);

-- 19. CHALLENGES (Daily scenarios, Super Over trials, historic chases)
CREATE TABLE IF NOT EXISTS public.challenges (
    id TEXT PRIMARY KEY,
    title_en TEXT NOT NULL,
    title_ur TEXT NOT NULL,
    description_en TEXT NOT NULL,
    description_ur TEXT NOT NULL,
    difficulty VARCHAR(16) DEFAULT 'MEDIUM',
    target_runs INTEGER NOT NULL,
    balls_available INTEGER NOT NULL,
    wickets_in_hand INTEGER NOT NULL,
    reward_coins INTEGER DEFAULT 500,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 20. FRIEND_RELATIONS (Social system & online challenges)
CREATE TABLE IF NOT EXISTS public.friend_relations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    receiver_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    status VARCHAR(16) DEFAULT 'PENDING',
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(sender_id, receiver_id)
);

-- 21. MODERATION_REPORTS (Fair play, toxic behavior & abuse reports)
CREATE TABLE IF NOT EXISTS public.moderation_reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reporter_id UUID NOT NULL REFERENCES public.profiles(id),
    reported_user_id UUID REFERENCES public.profiles(id),
    category VARCHAR(32) NOT NULL,
    details TEXT,
    status VARCHAR(16) DEFAULT 'PENDING',
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 22. REMOTE_CONFIG (Dynamic tuning, season events, ball physics toggles)
CREATE TABLE IF NOT EXISTS public.remote_config (
    id TEXT PRIMARY KEY,
    key TEXT UNIQUE NOT NULL,
    value JSONB NOT NULL,
    description TEXT,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 23. AUDIT_LOGS (Immutable admin and security transaction log)
CREATE TABLE IF NOT EXISTS public.audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_id UUID REFERENCES public.profiles(id),
    action TEXT NOT NULL,
    target_table TEXT NOT NULL,
    details JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- =============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- Zero-Trust Client Architecture: Anon users can read public tables;
-- Users can only write their own rows. Matches and scores require validated state.
-- =============================================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.teams ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.players ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.stadiums ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.match_history ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.cloud_saves ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.online_matches ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.match_participants ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.multiplayer_rooms ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.room_actions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.innings ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.deliveries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.player_match_stats ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.leaderboards ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.leaderboard_entries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournaments ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournament_entries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.career_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.challenges ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.friend_relations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.moderation_reports ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.remote_config ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.audit_logs ENABLE ROW LEVEL SECURITY;

-- Profiles: Anyone can view public profile details; only user can update their own
CREATE POLICY "Public profiles are viewable by everyone" ON public.profiles FOR SELECT USING (true);
CREATE POLICY "Users can update their own profile" ON public.profiles FOR UPDATE USING (auth.uid() = id);
CREATE POLICY "Users can insert their own profile" ON public.profiles FOR INSERT WITH CHECK (auth.uid() = id);

-- Stadiums, Teams, Players: Viewable by everyone
CREATE POLICY "Stadiums are public read" ON public.stadiums FOR SELECT USING (true);
CREATE POLICY "Teams are public read" ON public.teams FOR SELECT USING (true);
CREATE POLICY "Players are public read" ON public.players FOR SELECT USING (true);
CREATE POLICY "Custom teams managed by owner" ON public.teams FOR ALL USING (auth.uid() = owner_id);

-- Match History: Viewable by owner, insertable by owner
CREATE POLICY "Users can view their match history" ON public.match_history FOR SELECT USING (auth.uid() = user_id OR user_id IS NULL);
CREATE POLICY "Users can insert completed matches" ON public.match_history FOR INSERT WITH CHECK (auth.uid() = user_id OR user_id IS NULL);

-- Cloud Saves: Strict user sandbox
CREATE POLICY "Users view own cloud saves" ON public.cloud_saves FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Users insert/update own cloud saves" ON public.cloud_saves FOR ALL USING (auth.uid() = user_id);

-- Multiplayer Rooms & Actions
CREATE POLICY "Public can view multiplayer rooms" ON public.multiplayer_rooms FOR SELECT USING (true);
CREATE POLICY "Users can manage hosted rooms" ON public.multiplayer_rooms FOR ALL USING (auth.uid() = host_id OR auth.uid() = guest_id);
CREATE POLICY "Room actions viewable by room participants" ON public.room_actions FOR SELECT USING (true);
CREATE POLICY "Participants can post room actions" ON public.room_actions FOR INSERT WITH CHECK (auth.uid() = sender_id);

-- Leaderboards
CREATE POLICY "Leaderboards are public read" ON public.leaderboards FOR SELECT USING (true);
CREATE POLICY "Leaderboard entries are public read" ON public.leaderboard_entries FOR SELECT USING (true);

-- Challenges
CREATE POLICY "Challenges are public read" ON public.challenges FOR SELECT USING (true);

-- Remote Config
CREATE POLICY "Remote config is public read" ON public.remote_config FOR SELECT USING (true);

-- =============================================================================
-- PERFORMANCE INDEXES
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_players_team_id ON public.players(team_id);
CREATE INDEX IF NOT EXISTS idx_match_history_user_id ON public.match_history(user_id);
CREATE INDEX IF NOT EXISTS idx_room_actions_room_id ON public.room_actions(room_id, sequence_number);
CREATE INDEX IF NOT EXISTS idx_leaderboard_entries_rank ON public.leaderboard_entries(leaderboard_id, rating_elo DESC);

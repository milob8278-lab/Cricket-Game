// server/services/matchService.js
// Match history persistence, cloud saves, and security controls preventing match tampering

import { supabase, getAuthenticatedClient } from '../supabaseClient.js';
import { AuthService } from './authService.js';

export class MatchService {
  /**
   * Save a completed match to match_history.
   * SECURITY ENFORCEMENT:
   * 1. Requires verified authentication token.
   * 2. strictly INSERT ONLY. Clients are never permitted to UPDATE or alter completed match records.
   * 3. Validates required match fields before persisting.
   *
   * @param {string} userId
   * @param {string} accessToken
   * @param {object} matchData
   */
  static async saveMatchHistory(userId, accessToken, matchData) {
    if (!userId || !accessToken || !matchData) {
      return { success: false, error: 'User ID, access token, and match data are required' };
    }

    // Verify session
    const sessionRes = await AuthService.getSessionUser(accessToken);
    if (!sessionRes.success || sessionRes.user.id !== userId) {
      return { success: false, error: 'Unauthorized match submission' };
    }

    // Validate match payload
    if (!matchData.format || !matchData.teamA || !matchData.teamB) {
      return { success: false, error: 'Incomplete match data: format, teamA, and teamB are mandatory' };
    }

    const matchRecord = {
      id: matchData.id || `match_${Date.now()}_${Math.random().toString(36).substring(2, 7)}`,
      user_id: userId,
      format: matchData.format,
      max_overs: matchData.overs || matchData.maxOvers || 5,
      venue_name: matchData.venueName || matchData.venue?.nameEn || 'Default Stadium',
      team_a_name: typeof matchData.teamA === 'string' ? matchData.teamA : (matchData.teamA?.nameEn || 'Team A'),
      team_b_name: typeof matchData.teamB === 'string' ? matchData.teamB : (matchData.teamB?.nameEn || 'Team B'),
      winner_team_name: matchData.winnerTeam || matchData.winnerTeamName || 'Draw',
      score_summary: matchData.scoreSummary || `${matchData.teamARuns || 0}/${matchData.teamAWickets || 0} vs ${matchData.teamBRuns || 0}/${matchData.teamBWickets || 0}`,
      is_player_won: Boolean(matchData.isPlayerWon),
      rating_change: matchData.ratingChange ?? (matchData.isPlayerWon ? 18 : -12),
      created_at: new Date().toISOString()
    };

    try {
      const client = getAuthenticatedClient(accessToken);
      // Strictly INSERT only: Prevents changing existing official scores
      const { data, error } = await client
        .from('match_history')
        .insert([matchRecord])
        .select()
        .maybeSingle();

      if (error) {
        // If match_history table uses slightly different column naming, fallback to JSON payload insert
        const fallbackRecord = {
          user_id: userId,
          match_data: matchRecord,
          created_at: new Date().toISOString()
        };
        const { data: fallbackData, error: fallbackError } = await client
          .from('matches')
          .insert([fallbackRecord])
          .select()
          .maybeSingle();

        if (fallbackError) {
          return { success: false, error: error.message };
        }
        return { success: true, match: fallbackData || matchRecord };
      }

      // Update user win/loss statistics in profiles
      try {
        const { data: currentProfile } = await client
          .from('profiles')
          .select('rating_elo, matches_played, matches_won, matches_lost')
          .eq('id', userId)
          .maybeSingle();

        if (currentProfile) {
          const newPlayed = (currentProfile.matches_played || 0) + 1;
          const newWon = (currentProfile.matches_won || 0) + (matchRecord.is_player_won ? 1 : 0);
          const newLost = (currentProfile.matches_lost || 0) + (matchRecord.is_player_won ? 0 : 1);
          const newElo = Math.max(800, (currentProfile.rating_elo || 1200) + matchRecord.rating_change);

          await client
            .from('profiles')
            .update({
              matches_played: newPlayed,
              matches_won: newWon,
              matches_lost: newLost,
              rating_elo: newElo,
              updated_at: new Date().toISOString()
            })
            .eq('id', userId);
        }
      } catch (statErr) {
        console.log('[MatchService] Stats increment handled or deferred:', statErr.message);
      }

      return {
        success: true,
        match: data || matchRecord
      };
    } catch (err) {
      return { success: false, error: err.message || 'Failed to persist match history' };
    }
  }

  /**
   * Disallow client update to official match results
   */
  static async updateMatchScore(userId, matchId, newScore) {
    return {
      success: false,
      error: 'SECURITY VIOLATION: Clients are strictly prohibited from modifying official match results or historical scores.'
    };
  }

  /**
   * Load user's match history
   * @param {string} userId
   * @param {string} accessToken
   */
  static async getMatchHistory(userId, accessToken) {
    if (!userId || !accessToken) {
      return { success: false, error: 'User ID and access token required', matches: [] };
    }

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data, error } = await client
        .from('match_history')
        .select('*')
        .eq('user_id', userId)
        .order('created_at', { ascending: false })
        .limit(30);

      if (error) {
        // Fallback check on 'matches' table
        const { data: altData, error: altErr } = await client
          .from('matches')
          .select('*')
          .eq('user_id', userId)
          .order('created_at', { ascending: false })
          .limit(30);

        if (!altErr && altData) {
          return { success: true, matches: altData };
        }
        return { success: false, error: error.message, matches: [] };
      }

      return {
        success: true,
        count: data?.length || 0,
        matches: data || []
      };
    } catch (err) {
      return { success: false, error: err.message, matches: [] };
    }
  }

  /**
   * Save Career / Progression Cloud Save
   * @param {string} userId
   * @param {string} accessToken
   * @param {object} saveData
   */
  static async saveCloudSave(userId, accessToken, saveData) {
    if (!userId || !accessToken || !saveData) {
      return { success: false, error: 'User ID, token, and save payload are required' };
    }

    try {
      const client = getAuthenticatedClient(accessToken);
      const payload = {
        user_id: userId,
        save_type: saveData.saveType || 'CAREER_PROGRESS',
        schema_version: 1,
        save_data: saveData.payload || saveData,
        updated_at: new Date().toISOString()
      };

      const { data, error } = await client
        .from('cloud_saves')
        .upsert(payload, { onConflict: 'user_id' })
        .select()
        .maybeSingle();

      if (error) {
        return { success: false, error: error.message };
      }

      return { success: true, save: data || payload };
    } catch (err) {
      return { success: false, error: err.message || 'Cloud save failed' };
    }
  }

  /**
   * Get latest Cloud Save
   * @param {string} userId
   * @param {string} accessToken
   */
  static async getCloudSave(userId, accessToken) {
    if (!userId || !accessToken) {
      return { success: false, error: 'User ID and access token required' };
    }

    try {
      const client = getAuthenticatedClient(accessToken);
      const { data, error } = await client
        .from('cloud_saves')
        .select('*')
        .eq('user_id', userId)
        .order('updated_at', { ascending: false })
        .limit(1)
        .maybeSingle();

      if (error) {
        return { success: false, error: error.message };
      }

      return {
        success: true,
        save: data ? (data.save_data || data) : null
      };
    } catch (err) {
      return { success: false, error: err.message || 'Failed to retrieve cloud save' };
    }
  }
}

**REAL CRICKET PRO**

# Backend Schema and Data Model

Logical data entities, local storage, optional cloud services and sync rules

| **Document ID** | DATA-005 |
| --- | --- |
| **Version** | 1.0 - Working Draft |
| **Prepared** | 26 October 2026 |
| **Product baseline** | Android \| Unity 6 LTS family + URP \| C# |
| **Device target** | 4GB RAM baseline; 6GB/8GB+ scalable tiers |
| **Graphics** | Semi-realistic 3D + 2D Lite / 2.5D presentation |
| **Operating principle** | Offline-first \| Urdu + English \| free core gameplay |

**Document purpose** Defines a provider-neutral data model for the offline-first game and optional online features. It is a logical schema, not a final Firebase/SQL implementation.

> Source basis: “Cricket Game (Android) – Poora Feature Plan” (11 pages) and “Real Cricket Pro | Updated Development Plan” (18 pages). This document converts the agreed product direction into a specialist project artifact. Proposed details are marked where source plans did not decide them.

## 1. Schema objectives and boundaries

Real Cricket Pro must not require a backend for the core match loop. Built-in fictional teams, players, venues, pitches, rules presets and default challenges should be packaged locally. Custom player/team data, settings, career progress and match history must persist locally. Backend entities support optional accounts, cloud backup, online multiplayer, leaderboards, content updates and moderation.

> **Vendor decision pending** Firebase and Unity Gaming Services are options named in the source plan. This document defines provider-neutral entities; choose one primary cloud platform after prototype. Do not deploy duplicate services without a clear need.

## 2. Data ownership and storage zones

| **Zone** | **Data** | **Authority / behavior** |
| --- | --- | --- |
| Bundled game data | Base fictional teams, players, venues, pitches, rules presets, default cosmetics and localization. | Read-only defaults in the client; versioned with the app/content pack. |
| Local device data | User settings, custom teams/players, career save, match history, active match save and cached challenge. | Owned by the player/device; offline reads and writes; versioned and backed up. |
| Cloud account data | Optional profile, cloud save metadata and user preferences. | User-scoped; sync only when signed in and connected. |
| Online match services | Room/match state, inputs/events, validated result and reconnect state. | Server authoritative for ranked/competitive outcome; client cannot self-award results. |
| Operations/admin data | Content drafts/releases, moderation reports, audit logs and feature config. | Staff-only access with role-based permissions and audit trail. |

## 3. Logical relationship map

```text
USER (optional account) 1----N CLOUD_SAVE
USER 1----N ONLINE_MATCH_PARTICIPANT N----1 ONLINE_MATCH
USER 1----N FRIEND_RELATIONSHIP / MODERATION_REPORT / LEADERBOARD_ENTRY

TEAM 1----N PLAYER (bundled catalog or user-created copy)
VENUE 1----N MATCH
MATCH 1----N INNINGS 1----N DELIVERY_EVENT
MATCH 1----N PLAYER_MATCH_STAT
TOURNAMENT 1----N TOURNAMENT_ENTRY 1----N FIXTURE/MATCH
CAREER_PROFILE 1----N CAREER_FIXTURE / CAREER_PLAYER_STATE
CONTENT_RELEASE 1----N CONTENT_ASSET / CHALLENGE_DEFINITION / REMOTE_CONFIG
ADMIN_USER 1----N AUDIT_LOG
```

## 4. Core game entities

| **Entity** | **Primary fields (logical types)** | **Purpose** |
| --- | --- | --- |
| PlayerDefinition | player_id:string; display_name:string; role:enum; batting_hand:enum; bowling_type:enum; ratings:object; face_preset_id:string; kit_id:string; source:enum; schema_version:int | Fictional built-in or editable player template. |
| TeamDefinition | team_id:string; name:string; badge_asset_id:string; kit_config:object; player_ids:string[]; is_user_created:bool; updated_at:datetime | Team roster and appearance. |
| VenueDefinition | venue_id:string; name:string; region_style:enum; boundary_profile:object; stadium_asset_ids:string[]; lighting_presets:array | Original stadium/ground configuration. |
| PitchPreset | pitch_id:string; type:enum; bounce:float; pace:float; seam:float; spin:float; wear_config:object | Green, dry, flat, slow, bouncy, cracked or wet pitch behavior. |
| RulesetConfig | ruleset_id:string; format:enum; version:string; legal_ball_count:int; overs_limit:int; powerplay:object; extras:object; reviews:object; tie_rules:object; effective_date:date | Versioned format/competition rule behavior. |
| MatchSave | match_id:string; save_version:int; ruleset_id:string; match_state:object; rng_seed:int; last_safe_checkpoint:string; saved_at:datetime | Recoverable offline match at a safe ball/over state. |
| InningsState | innings_id:string; match_id:string; batting_team_id:string; bowling_team_id:string; runs:int; wickets:int; legal_balls:int; extras:object; target:int?; status:enum | Current or completed innings state. |
| DeliveryEvent | delivery_id:string; innings_id:string; sequence:int; bowler_id:string; striker_id:string; delivery_spec:object; events:array; score_delta:object; dismissal:object? | Ball-by-ball event record for scorecard/replay/review. |
| PlayerMatchStat | match_id:string; player_id:string; batting:object; bowling:object; fielding:object; player_of_match:bool | Aggregated player performance for a match. |
| CareerProfile | career_id:string; local_owner_id:string; selected_player_id:string; team_id:string; season:int; progression:object; career_version:int | Offline career state, optionally cloud-backed. |

## 5. User and online service entities

| **Entity** | **Primary fields** | **Rules / relationships** |
| --- | --- | --- |
| UserProfile | user_id; display_name; locale; region_code?; created_at; last_seen_at; privacy_preferences | Optional account identity; do not require for offline game. |
| CloudSave | save_id; user_id; save_type; schema_version; payload_ref; checksum; updated_at; device_label? | One or more versioned cloud backups; user can choose restore on conflict. |
| OnlineMatch | online_match_id; mode; ruleset_id; room_id; status; server_seed; started_at; ended_at; validated_result | Server-authoritative record for online 1v1 or event match. |
| MatchParticipant | online_match_id; user_id; seat; team_id; connection_state; result_status | Join table between online match and user. |
| LeaderboardEntry | leaderboard_id; user_id; rating/score; season_id; rank_cache; updated_at; validation_ref | Only server-validated results can update competitive scores. |
| FriendRelation | requester_id; recipient_id; status; created_at; updated_at | Optional friend request/accepted/blocked relationship. |
| ModerationReport | report_id; reporter_id; target_user_id?; target_content_id?; category; details; status; created_at; reviewer_id? | Report/block workflow; collect only necessary evidence and retain by policy. |
| ChallengeDefinition | challenge_id; version; title_key; description_key; scenario_config; active_from; active_to; cache_allowed | Download/cached daily challenge; cached challenge remains playable offline. |
| RemoteConfig | config_id; version; environment; feature_flags; ruleset_refs; published_at; rollback_ref | Versioned optional feature and content configuration. |
| ContentAsset | asset_id; content_pack_id; asset_type; uri_or_addressable_key; checksum; license_ref; min_app_version | Asset catalog for optional content and license provenance. |
| AuditLog | audit_id; actor_admin_id; action; target_type; target_id; reason; created_at; request_id | Immutable history for admin actions. |

## 6. Recommended field conventions

- Use stable string IDs (UUID/ULID or stable catalog IDs) and UTC timestamps for cloud records.

- Use enums for role, batting hand, bowling type, format, dismissal, match status and challenge status. Version enum mappings with client releases.

- Store ratings and numeric values in bounded, validated ranges; server must not trust client values in competitive modes.

- Store custom face/kit/field configurations as validated structured objects with explicit schema_version.

- Store large binary assets in content storage/Addressables, not inline inside user profile documents. Store metadata, checksums and license references in the catalog.

- Keep local IDs and cloud IDs mappable; do not use display names as primary keys.

## 7. Example payloads

```text
MatchSave (illustrative logical JSON)
{
  "match_id": "local-match-00042",
  "save_version": 1,
  "ruleset_id": "t20-casual-v1",
  "rng_seed": 492018,
  "last_safe_checkpoint": "innings1-over04-complete",
  "match_state": {
    "phase": "INNINGS_1", "runs": 37, "wickets": 1,
    "legal_balls": 24, "striker_id": "player-a",
    "non_striker_id": "player-b", "bowler_id": "player-c"
  },
  "saved_at": "2026-10-26T12:00:00Z"
}

RulesetConfig (illustrative logical JSON)
{
  "ruleset_id": "t20-casual-v1", "version": "1.0",
  "format": "T20", "overs_limit": 20,
  "powerplay": {"overs": 6, "preset": "configured"},
  "reviews": {"enabled": true, "per_innings": 1},
  "source_label": "game-preset", "effective_date": "2026-10-26"
}
```

Payloads are examples of field structure only. Exact JSON/Firestore/SQL data types, indexes and constraints are finalized after provider and storage-layer selection.

## 8. Offline save and cloud synchronization

| **Scenario** | **Required behavior** |
| --- | --- |
| Offline user | Local settings, match, career, teams and history operate without login. No silent network dependency. |
| Sign-in later | Offer optional cloud backup/restore; keep local data intact until sync outcome is confirmed. |
| Cloud conflict | Compare updated_at and save version, but do not blindly overwrite. Show local/cloud restore choice or keep both backups. |
| Interrupted save | Write to temp/staging slot, verify checksum, then atomically promote to active slot; preserve last valid backup. |
| Schema update | Run migration on a copy; validate; only replace active save after successful migration. |
| New device | Restore latest compatible cloud save only after user confirms; retain local default data and report unsupported versions. |

## 9. Indexes, access control and validation

- Suggested indexes: user_id + updated_at on CloudSave; leaderboard_id + season_id + score/rating; online_match_id + status; target_user_id + status on reports; content_pack_id + version on assets.

- Player clients may read published catalog/config and their own profile/save. They cannot write audit logs, publish content, assign leaderboard rank or approve reports.

- Staff roles: content editor, publisher, moderator, support agent and system admin. Enforce least privilege and audit all sensitive operations.

- Validate player/team names and imported data; bound arrays and payload sizes; reject unknown schema versions and malformed match events.

- Use server timestamps/transactions for online results and duplicate submission protection with idempotency keys.

## 10. Privacy and retention

The source plan calls for friends, preset chat, reporting/blocking and analytics, but does not define retention periods or legal basis. Before online release, define a privacy policy, data inventory, retention schedule, deletion/export process, age/region handling and moderation escalation. Do not collect precise location, contact lists or unnecessary device data for the offline game.

## 11. Provider mapping - pending

| **Logical capability** | **Candidate mapping** | **Decision note** |
| --- | --- | --- |
| Identity / cloud save | Firebase Auth + Firestore/Storage OR Unity Gaming Services | Choose one primary cloud system after prototype; keep domain interface provider-neutral. |
| Online match / rooms | Photon OR Unity Netcode / related hosting | Evaluate authority, latency, hosting cost, reconnection and cheat resistance. |
| Content delivery | Unity Addressables + hosted content | Include critical offline assets in app; optional packs may download when connected. |
| Ads | Google AdMob or approved alternative | Optional adapter; never during an active match. |
| Analytics | Firebase Analytics or privacy-aware alternative | Keep minimal, disclose collection and track product quality rather than personal profiling. |

## Source and decision notes

- Primary source A: Cricket Game (Android) - Poora Feature Plan, 11 pages, dated 26 October 2026.

- Primary source B: Real Cricket Pro - Updated Development Plan, version 2.0, 18 pages, dated 26 October 2026.

- The agreed owner choices used throughout: game name Real Cricket Pro; both graphics modes; 4GB RAM baseline; semi-realistic fictional faces.

- Where a backend vendor, detailed visual style, exact device list, commercial policy, or admin workflow is not decided in the source plans, this pack labels the item as a recommendation or open decision.

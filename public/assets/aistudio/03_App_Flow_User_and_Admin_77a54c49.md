**REAL CRICKET PRO**

# App Flow - User and Admin Experience

Navigation, screen transitions, match journeys and internal operations

| **Document ID** | FLOW-003 |
| --- | --- |
| **Version** | 1.0 - Working Draft |
| **Prepared** | 26 October 2026 |
| **Product baseline** | Android \| Unity 6 LTS family + URP \| C# |
| **Device target** | 4GB RAM baseline; 6GB/8GB+ scalable tiers |
| **Graphics** | Semi-realistic 3D + 2D Lite / 2.5D presentation |
| **Operating principle** | Offline-first \| Urdu + English \| free core gameplay |

**Document purpose** Describes how players move through the Android app and how a proposed internal operations team manages content, reports and services.

> Source basis: “Cricket Game (Android) – Poora Feature Plan” (11 pages) and “Real Cricket Pro | Updated Development Plan” (18 pages). This document converts the agreed product direction into a specialist project artifact. Proposed details are marked where source plans did not decide them.

## 1. Scope and roles

This flow document covers the Android player experience and a separate, proposed internal admin experience. The game client must remain playable offline; admin tools and online services are not required to start or complete a local match.

| **Role** | **Where used** | **Capabilities** |
| --- | --- | --- |
| Player / guest | Android app | Quick Match, practice, local tournament/career, settings, custom team/player and offline save. |
| Signed-in player (optional) | Android app online | Cloud sync, online 1v1, leaderboard, friend invite, preset chat and report/block when available. |
| Content operator (proposed) | Internal web console | Prepare fictional teams, players, venues, challenges and content configs; submit/publish approved packs. |
| Support/moderator (proposed) | Internal web console | Review reports, block abuse, handle account/support cases and inspect audit history. |
| Administrator (proposed) | Internal web console | Manage staff roles, feature flags, remote config and publishing permissions. |

> **Admin scope note** The source plan asks for user and admin experience but does not define an admin product. The console flows below are recommendations for safe operations, not an MVP player-facing requirement.

## 2. Player app information architecture

```text
APP LAUNCH
  -> First run (language / privacy notices / graphics suggestion)
  -> HOME
       |-- Quick Match -> Match Setup -> Toss -> Team Setup -> Live Match -> Result
       |-- Tournament / League -> Create or Select -> Fixtures -> Match -> Table / Final
       |-- Career -> Profile -> Fixtures / Training -> Match -> Progress / Records
       |-- Practice / Nets -> Drill Select -> Practice Session -> Results / Retry
       |-- Challenges -> Challenge Select -> Scenario Match -> Result / Retry
       |-- Multiplayer -> Local Hot-seat / Online (when enabled)
       |-- Profile / Player Editor / Team Editor
       |-- Settings / Tutorial / Match History
```

## 3. First run and home flow

| **Step** | **Screen / state** | **User action and system response** |
| --- | --- | --- |
| 1 | Splash / loading | Load local config, required content and save index. Do not require login or network. |
| 2 | Language | Choose Urdu or English; remember preference locally. Urdu layout must support RTL. |
| 3 | Privacy / notices | Show privacy and permissions information appropriate to actual features; request permissions only when needed. |
| 4 | Graphics suggestion | Offer Lite / Balanced 3D recommendation; auto-detect is advisory, player can change in Settings. |
| 5 | Tutorial prompt | Offer guided batting/bowling tutorial; allow skip and replay later. |
| 6 | Home | Show only available features; disabled online feature should explain that internet/service is required or feature is coming later. |

## 4. Quick Match journey

```text
HOME -> QUICK MATCH
  -> Match Setup: format / overs / teams / difficulty / venue / pitch / weather / day-night / rules preset
  -> Select Playing XI, captain, keeper, batting order and bowling plan
  -> Toss animation -> winner chooses bat or bowl
  -> Innings intro / broadcast camera
  -> LIVE MATCH (repeat delivery loop)
  -> Innings break (target, summary, permitted changes)
  -> Result presentation -> scorecard -> highlights -> save history -> rematch / home
```

## 5. Live match sub-flows

| **Situation** | **Flow** | **Important behavior** |
| --- | --- | --- |
| Bowling delivery | Choose line/length and delivery type -> run-up -> release -> ball flight -> pitch/contact -> result. | Keep target and ball path visible in Lite mode; no menu should block a live delivery. |
| Batting | Read release -> footwork and shot intent -> timing/action -> bat/pad/body contact -> ball trajectory. | Show short feedback for timing/contact; do not overload HUD with meters. |
| Running | Call Yes/No/Wait -> both batters run/turn -> fielder pickup/throw -> crease decision. | Support run-out, overthrow and boundary awareness. |
| Appeal | Fielding team Appeal -> rules/umpire evaluates -> signal and score update. | Appeal-dependent decisions must track whether an appeal occurred. |
| Review | Eligible team selects review within timer -> ball tracking/replay views -> outcome -> review count updates. | Review count and retain/lose logic come from selected ruleset. |
| Pause | Pause -> resume / controls / graphics / sound / quit. | Quit confirms save behavior; safe autosave prevents duplicate runs/wickets. |
| Replay | Auto or manual replay -> angle / slow motion -> return to live/result. | Replay must not mutate the simulation state. |

## 6. Core screen inventory

| **Screen** | **Primary content/actions** | **States / edge cases** |
| --- | --- | --- |
| Home | Main modes, continue match, settings, profile. | No save; feature not available; offline. |
| Match setup | Format, rules, venue, pitch, weather, teams, difficulty. | Invalid team size; unsupported format rules. |
| Team selection | Squad, XI, roles, batting order, captain, keeper. | Not enough eligible players; restore default XI. |
| Live match | Score HUD, ball, controls, field map, appeal/review. | Pause, inning transition, network loss only in online mode. |
| Scorecard/result | Batting/bowling figures, extras, fall of wickets, player of match, highlights. | Tie, Super Over, abandoned/rain-revised result. |
| Practice | Drill categories, control tutorial, retry. | No pressure; repeat drill; tips on/off. |
| Player/team editor | Name, ratings, face presets, hair/beard, kit and equipment. | Invalid name; reset preset; unsaved changes. |
| Settings | Graphics, FPS, controls, audio, language, accessibility, battery saver. | Device profile suggestion; setting reset. |
| Multiplayer | Hot-seat, local network, online invite/matchmaking. | Offline/unsupported; reconnect; invite expired; opponent left. |

## 7. Save, resume and failure flows

- **Autosave:** Save after each over and safe state transition. A quit or app interruption should resume from a stable ball/over boundary.

- **Corrupt save:** Preserve a backup slot; offer restore from the last valid save and do not silently overwrite all local progress.

- **Low storage:** Warn before a save/export fails; do not report success until persistence completes.

- **Offline:** Hide or explain online-only actions but keep local modes available.

- **Connectivity loss during online play:** Show reconnect state, apply match reconnection policy and avoid duplicate result submission.

- **Graphics switch:** Allow from menu or safe pause only, not during active input/collision; preserve current score and match state.

## 8. Optional online user flows

```text
MULTIPLAYER -> Select mode
  HOT-SEAT: Local player 1 -> innings/turn -> hand over device -> player 2 -> result
  ONLINE 1v1: Optional sign-in -> friend invite or matchmaking -> room validation -> match ->
              reconnect if needed -> server validates result -> leaderboard/history
  SOCIAL: Friends list -> invite -> preset chat/emojis -> report/block
```

Do not require sign-in for same-device hot-seat or offline features. Online tournaments, leaderboard and friend/chat workflows should ship only after server authority, privacy and moderation are tested.

## 9. Proposed admin operations flow

```text
STAFF SIGN-IN (MFA recommended)
  -> Role-based dashboard
       |-- Content Library -> draft team/player/venue/challenge/config -> validate -> review -> publish
       |-- Rules/Remote Config -> version -> test -> staged rollout -> rollback
       |-- Reports Queue -> inspect evidence -> warn / mute / block / close -> audit record
       |-- Account/Support -> lookup by internal ID -> permitted action -> reason + audit
       |-- Operations -> service health / crash trends / matchmaking status / release flags
       |-- Staff & Audit -> roles / access review / immutable action log
```

## 10. Admin permissions and safeguards

| **Role** | **Allowed actions** | **Restrictions** |
| --- | --- | --- |
| Content editor | Create/edit draft teams, players, stadium content and challenge definitions. | Cannot publish to production without approval. |
| Publisher/reviewer | Validate and publish approved content/config versions; rollback. | Cannot modify player account/security data unless separately authorized. |
| Moderator | View reports and take policy actions (hide content, mute/block if service supports). | Every action requires reason and audit entry; least privilege. |
| Support agent | View limited account/support data and guide recovery. | No access to passwords, payment credentials or unnecessary private data. |
| System admin | Manage staff roles, service config, feature flags and audit access. | MFA, approval for sensitive actions, logged access. |

## 11. Notifications and empty/error states

- Daily challenge is downloaded/cached when connected; last cached challenge can remain playable offline.

- Use concise error language: “Internet required for online match”; “Save failed - free up storage and retry”; “This feature will be available in a future update.”

- Notifications should be limited to useful events such as a friend invite or event; avoid excessive promotional alerts.

- Every destructive action (delete save, quit match, reset editor) requires clear confirmation and explains what will be lost.

## Source and decision notes

- Primary source A: Cricket Game (Android) - Poora Feature Plan, 11 pages, dated 26 October 2026.

- Primary source B: Real Cricket Pro - Updated Development Plan, version 2.0, 18 pages, dated 26 October 2026.

- The agreed owner choices used throughout: game name Real Cricket Pro; both graphics modes; 4GB RAM baseline; semi-realistic fictional faces.

- Where a backend vendor, detailed visual style, exact device list, commercial policy, or admin workflow is not decided in the source plans, this pack labels the item as a recommendation or open decision.

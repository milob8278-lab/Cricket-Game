# Real Cricket Pro — 3D Online Rebuild Prompt

**Purpose:** Use this prompt in Google AI Studio Build mode to replace the current flat/top-down cricket prototype with a real-time 3D online cricket game prototype.

## Important platform decision

Google AI Studio has two relevant Build paths:

- **Web app:** React frontend + Node.js server runtime. Use this path in AI Studio when the immediate goal is a shareable, online multiplayer 3D prototype. Render the 3D match with Three.js / React Three Fiber in the browser.
- **Native Android app:** Kotlin + Jetpack Compose. The Android Build path is client-side oriented and does not provide the general server runtime needed for real-time multiplayer. Do not pretend that a Compose-only project is a complete online sports game.

For a production-quality, native Android 3D cricket game, the recommended game engine is **Unity 6 LTS + URP + C#**, with a server-authoritative multiplayer architecture. AI Studio can help create the web prototype, backend contracts, tools and code, but it is not a replacement for the Unity 3D game engine.

---

# COPY FROM HERE INTO GOOGLE AI STUDIO (choose Web App)

You are rebuilding my project **Real Cricket Pro**. The existing preview looks like a flat 2D/top-down cricket field with small icons. That is NOT the product I want. Treat this as a major 3D rebuild, not a cosmetic reskin.

## 1. Non-negotiable result

Build a mobile-first, landscape, full-screen, real-time **3D cricket game** with online accounts and online matches. Use a real 3D scene with perspective camera, geometry, lighting, materials, shadows, animated characters and a moving 3D ball. The cricket pitch must NOT be a flat 2D drawing or a static background image. Do NOT fake 3D by placing 2D player icons on a green rectangle.

For this AI Studio Web project use:

- React + TypeScript for menus and HUD.
- Three.js through React Three Fiber for the 3D match world.
- Drei for cameras, loaders and 3D helpers where appropriate.
- A suitable 3D physics library such as Rapier for physical interactions, with cricket-specific deterministic ball-flight logic where that is more reliable.
- Node.js server runtime for authentication/session APIs and authoritative online match state.
- Socket.IO or an equivalent supported real-time transport for live match events. Keep server code separate from client rendering code.
- Store secrets only on the server. Never put private keys, admin credentials or trusted scoring logic in browser code.

If this project is currently a native Android/Compose project, stop and clearly report that this Build target cannot provide a general multiplayer server. Do not silently create fake online buttons. Create this as a **Web App** in Build mode, or provide a Unity migration plan for the native production game.

## 2. 3D visual direction

Create a believable semi-realistic cricket broadcast world:

- Full 3D oval stadium, boundary rope, grass, pitch strip, crease lines, stumps, bails, sightscreen, pavilion, dugouts, LED boards and stands.
- Perspective broadcast camera as the default, with behind-bowler, batting, side-on, close-up and high field cameras.
- Real 3D humanoid players with full bodies, heads, hands, feet, helmets, pads, gloves, shoes and team kits.
- Semi-realistic original/fictitious faces. Do not imitate a real cricketer's exact face or use protected team, league or stadium logos without permission.
- Proper 3D ball mesh with visible seam, spin, flight, bounce, rolling and collision effects.
- Stadium lighting, baked/static environment lighting where possible, shadows, ambient occlusion only where performance allows, crowd sections and restrained particles.
- Crowd can use instanced low-detail models/cards at distance, but the pitch, ball and active players must be genuine 3D objects.
- Use placeholder 3D models only when licensed/owned final assets are not available. Make the asset interface ready for later GLB/GLTF player and stadium models. Never substitute a flat picture and call it a 3D player.

## 3. Player animation system

Use skinned/rigged character models and animation state machines. Required animation states:

- Idle, ready stance, guard, bat raise, backlift, front-foot movement, back-foot movement.
- Defensive block, straight drive, cover drive, pull, hook, sweep, reverse sweep, lofted shot, ramp/scoop and miss/leave.
- Fast bowler run-up, gather, jump, delivery stride, release and follow-through.
- Spin bowler approach, gather, wrist/finger release and follow-through.
- Jog, sprint, turn, slide, dive, jump, pickup, underarm throw, overarm throw, relay throw and direct-hit attempt.
- Wicketkeeper crouch, take, catch, collect, stumping and appeal.
- Catch reactions, dropped catch, boundary save, celebration, disappointment, injury reaction and umpire signals.
- Face/upper-body actions: blink, look direction, head turn and basic expressions where the model supports them.

Animations must be driven by gameplay events and player state, not random visual loops. Add animation blending and foot-planting. Do not let a celebration interrupt a live ball or a required input.

## 4. Camera and match presentation

- Default to a TV/broadcast-style 3D camera, not a vertical overhead map.
- Keep the live ball visible and readable at all times.
- Use a camera director to switch between delivery, batting contact, running, boundary and replay angles.
- Add manual camera selection and sensitivity settings.
- Replay modes: bowler-end, batter-end, side-on, stump camera and high angle; slow motion 0.25x and 0.5x.
- The HUD may be 2D overlay, but the actual field and players behind it must remain 3D.

## 5. Full-screen Android touch layout

- Landscape orientation, edge-to-edge immersive gameplay. Respect camera cutouts and system insets so buttons are never hidden.
- Use large thumb-reachable controls with adjustable size, opacity, position and left/right-handed layouts.
- Batting: left thumb for shot direction/footwork or a configurable shot map; right thumb for defend, normal, loft, power and timing. Support swipe, buttons and joystick presets.
- Bowling: aim/length target, line selection, pace/variation controls and release gesture/button.
- Fielding: movement joystick, dive, slide, jump, pickup and throw controls; use contextual buttons so the screen is not overcrowded.
- Running: Yes / No / Wait controls and quick cancel, with clear non-striker state.
- Pause, settings, camera, DRS/review and scorecard controls must be reachable without covering the ball or batter.
- Add haptic feedback for bat contact, edge, catch, wicket and boundary where supported.

## 6. Online-only product requirements

The game requires internet and an authenticated session. Do not present offline match modes as playable. Include:

- Sign-in/guest account flow with a safe account upgrade path.
- Player profile, avatar, display name, region, skill rating, statistics and cloud saves.
- Quick Match matchmaking, ranked/unranked 1v1, private room code, friend invite and reconnect.
- Online tournaments/leagues with fixtures, points table, net run rate, standings and finals.
- Online career progression and cloud-saved player/team customization.
- Leaderboards, match history, scorecards and shareable result cards.
- Spectator mode and live match state as a later milestone, not a fake MVP button.
- Presence, ready status, lobby countdown, match start, innings break, result and rematch flows.
- Network status indicator, latency display where useful, reconnect grace period and clear handling when a player disconnects.

A player may practise against online-hosted AI, but the session and saved progress must still be authenticated and server-managed. Every mode must clearly show whether it is PvP, online AI or a tournament fixture.

## 7. Server-authoritative cricket simulation

Never trust the client to decide the final score, wickets, match result, rating or rewards.

The server must own and validate:

- Match creation, room membership, toss, teams, batting order and innings state.
- Delivery state, legal/illegal delivery, ball position/trajectory, bat contact outcome, runs, boundaries, wickets, extras and scorecard.
- Appeals, umpire decision, review availability and final DRS result.
- Match timer, innings completion, tie/super-over logic and final result.
- Rating changes, tournament points, career records and rewards.

Clients send player intentions/inputs with timestamps and receive authoritative state updates. Add input validation, rate limits, duplicate-event protection, disconnect handling and anti-cheat checks. Use interpolation/prediction for smooth visuals, but reconcile to server-authoritative outcomes. Never allow graphics quality or network ping to change a player's ratings or cricket rules.

## 8. Cricket gameplay scope

### Batting

Timing windows (perfect/good/early/late), footwork, shot direction, loft/ground intent, power risk, inside/outside edge, misses, leave, running, backing up, run-out risk, strike rotation and player-specific stance/handedness.

Shot library: cover drive, off drive, straight drive, back-foot punch, square cut, late cut, pull, hook, sweep, reverse sweep, flick, glance, scoop/ramp, helicopter-style shot, switch hit and defensive blocks.

### Bowling

Fast, medium, off-spin, leg-spin and left-arm spin archetypes. Yorker, bouncer, slower ball, cutter, swing, seam, off-break, leg-break, googly, flipper, top-spinner and other rule-appropriate variations. Line, length, pace, release, swing/spin, fatigue and bowler limits.

### Fielding

Editable field presets, manual fielder selection, chase/intercept AI, pickup, catch, dive, slide, jump, throws, direct hits, relay throws, overthrows, boundary saves and wicketkeeper actions.

### Umpire and rules

Bowled, caught, LBW, run-out, stumped, hit wicket, caught behind, obstructing the field and applicable other dismissal cases. Wides, no-balls, byes, leg-byes, free hit, powerplay, boundary laws, Super Over, rain interruptions and configured competition playing conditions. Keep rules data-driven and versioned; do not hard-code one competition's rules as universal.

### Formats and modes

T5, T10, T20, ODI, Test, The Hundred, custom matches, Quick Match, Practice/Nets (online-hosted), Series, Tournament/League, Career, Challenge, Knockout and Super Over. Release in stages; no feature should be a dead button.

## 9. Graphics and 4GB Android target

All graphics profiles remain real-time 3D:

- **3D Lite:** lower-poly 3D models, lower texture resolution, baked lighting, fewer crowd instances, reduced shadows/effects and 30 FPS target.
- **Balanced 3D:** better player models, controlled shadows, moderate crowd and 30 FPS target; 60 FPS only on devices that pass profiling.
- **High 3D:** highest supported detail, shadows and effects on tested devices.

Do not create a 2D top-down substitute for Lite mode. Use LOD, frustum/occlusion culling, instancing, texture compression, object pooling, baked lighting and adaptive quality. Profile on physical 4GB Android devices. A 4GB RAM label alone does not guarantee compatibility.

## 10. UI screens

Create complete, functional screens for:

1. Splash/loading and login.
2. Home hub: Play, Quick Match, Ranked, Tournament, Career, Practice, Challenges, Teams, Profile, Settings.
3. Matchmaking and lobby.
4. Team selection, playing XI, batting order and kit selection.
5. Stadium, pitch, weather and rules selection where allowed.
6. Toss.
7. Live 3D match HUD.
8. Pause/settings.
9. Innings break and tactics.
10. DRS/replay.
11. Result, full scorecard and highlights.
12. Tournament table, career hub and player editor.
13. Friends, invite, leaderboard and match history.
14. Network error/reconnect and maintenance states.

Every visible button must work. Do not create decorative controls that do nothing. If a feature is not implemented yet, hide it or label it clearly as coming later.

## 11. Delivery process — do not attempt every feature in one pass

Build and verify these milestones in order:

**Milestone A — 3D proof of gameplay**
- One 3D stadium, pitch, batter, bowler, wicketkeeper, fielders and ball.
- One broadcast camera and one batting camera.
- Ball release, flight, bounce, bat collision and boundary detection.
- At least four working shots and basic runs/wickets.
- Real rigged/animated 3D characters, not 2D icons.

**Milestone B — online 1v1 vertical slice**
- Authentication, room creation, invite code and two clients joining the same match.
- Server-authoritative toss, delivery, score, wicket and result.
- Reconnect after temporary network loss.
- Test using two separate browser sessions/devices.

**Milestone C — complete T20 match**
- Batting, bowling, fielding, umpire, scorecard, innings break, result and rematch.
- Both graphics profiles remain 3D and use identical rules/physics.

**Milestone D — content and progression**
- More teams, players, stadiums, tournament, career, customization, commentary and replays.

**Milestone E — production hardening**
- Security, anti-cheat, network load tests, device profiling, save recovery, accessibility, localization and release checks.

## 12. Acceptance tests (must demonstrate, not merely claim)

- The match view visibly contains true 3D geometry and animated 3D players.
- Camera perspective and shadows clearly show depth; the pitch is not a flat top-down map.
- A ball can be bowled, hit, fielded and scored through real interactions.
- Two separate users can join one match and see the same authoritative ball/score/result.
- Disconnect/reconnect does not duplicate runs, balls or wickets.
- Server rejects invalid score/result submissions from a client.
- 3D Lite is still 3D and remains readable on a tested 4GB device.
- Touch controls work in landscape and are not obscured by system bars/camera cutouts.
- No visible button is fake; incomplete features are clearly marked.

First inspect the current codebase and report its platform and architecture. Then implement Milestone A in working code. Do not only provide a plan, static screenshot or UI mockup. After Milestone A passes, implement Milestone B. Do not claim the game is complete until the acceptance tests pass.

# END COPY-PASTE PROMPT

---

## Production Android recommendation

If the final deliverable must be a native Android sports game with high-quality 3D animation and reliable competitive online play, build the match client in **Unity 6 LTS + URP + C#**. Use rigged GLB/FBX characters, Unity Animator, Cinemachine, a controlled ball simulation and a server-authoritative multiplayer stack (for example Unity Netcode with Lobby/Matchmaker/Relay for the suitable session model, or a dedicated authoritative server for ranked play). Use AI Studio Web mode for a shareable online prototype/backend tools, not as a substitute for Unity's 3D runtime.

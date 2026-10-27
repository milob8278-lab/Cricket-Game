# REAL CRICKET PRO — Complete Game Feature Specification

| Document | Value |
|---|---|
| Document ID | RCP-FEATURES-007 |
| Version | 1.0 — Expanded feature baseline |
| Date | 26 October 2026 |
| Platform | Android first |
| Engine direction | Unity 6 LTS family, C#, Universal Render Pipeline (URP) |
| Device target | 4GB RAM baseline; scale upward for 6GB/8GB+ devices |
| Visual direction | Semi-realistic 3D players; 3D High, 3D Balanced and 3D Lite/2.5D profiles |
| Input | Full-screen landscape touch controls; optional gamepad support later |
| Product rules | Offline-first, Urdu + English, core gameplay free and unlocked |

> **Purpose.** This is the detailed feature-level scope for the Real Cricket Pro development team. It expands the original 11-page feature plan and the 18-page updated development blueprint. It describes what the game should do, how users interact with it, and how the team can test it. It is a product specification, not a claim that a playable Unity game has already been built.

## 0. Product decisions and non-negotiable rules

1. **Game identity:** Real Cricket Pro is an original cricket game. It may take inspiration from familiar mobile cricket-game interaction patterns, but it must use its own visual identity, UI artwork, animations, player identities, team names, kits, logos, stadiums, music and commentary unless appropriate written licenses exist.
2. **Real 3D players in both modes:** Every on-field player remains a 3D character. Lite mode uses lower-detail 3D meshes, fewer bones/animation updates, smaller textures, reduced lighting and fewer crowd actors; it must not turn the active batter/bowler into a flat 2D cut-out.
3. **Same cricket in every graphics mode:** Ball simulation, collisions, scoring, laws, player ratings, AI decisions and match outcomes must not change when the user changes graphics quality.
4. **4GB RAM target:** 4GB Android devices are the baseline test tier, not a guarantee for every phone with 4GB RAM. Chipset, GPU, Android version, memory pressure and heat must be tested on physical devices.
5. **Full-screen touch-first experience:** Match gameplay is landscape-first and immersive. All essential controls are on screen, support multi-touch, and can be resized/repositioned. Full-screen backgrounds may extend to display edges, while important controls remain inside the device safe area.
6. **Offline-first:** Quick Match, Practice, local career/tournament progress, custom teams, settings and match history must work without login or internet. Online play and cloud services are optional.
7. **Free core game:** No coins, gems, energy timers, paid gameplay stats or paid mode unlocks. Ads must never interrupt an active delivery, over, review, innings or match.
8. **Release staging is not feature locking:** The full feature vision remains free. Features can arrive in phases after they are developed and tested; staging must not be presented as a permanent paywall.
9. **Rules are data-driven:** Laws and competition playing conditions are versioned presets, not scattered hard-coded UI rules. Check the selected ruleset for each competition.
10. **No false realism claims:** If a feature is an approximation (for example, a simplified rain-revised target), label it accurately. Do not call a proprietary or simplified calculation official DLS unless its implementation and rights have been verified.

## 1. Game modes and main navigation

### 1.1 Home screen

The home screen is a full-bleed cricket presentation with a featured fictional player, stadium background, current local profile and clear primary actions. It must remain responsive on the 4GB device tier.

Primary tiles:

- **Quick Match:** Start a match with a short setup flow.
- **Tournament:** Create or continue a cup/league.
- **Career:** Continue a player or captain career.
- **Practice / Nets:** Practice batting, bowling, fielding and controls.
- **Challenges:** Situational cricket challenges and skill tests.
- **Multiplayer:** Hot-seat immediately; online/local network modes appear when released.
- **Teams & Players:** Choose, edit or create teams and players.
- **Stadiums:** Preview venues and available pitch/day-night settings.
- **Settings:** Graphics, controls, sound, language, accessibility and save options.
- **Profile / Records:** Local profile, match history, achievements and career records.

The home screen must include a clear offline indicator when network services are unavailable. No login prompt may block offline play.

### 1.2 First launch and tutorial

1. Splash and original game logo.
2. Language selection: Urdu or English.
3. Privacy/consent screens only for services actually used.
4. Graphics auto-detection with an initial recommended profile; user may override it.
5. Control preference: Swipe, Buttons or Joystick + Buttons.
6. Left-hand/right-hand control layout selection.
7. Short interactive tutorial: defend, drive, time a shot, bowl at a target, field a ball and complete a run.
8. Enter the home screen without requiring account registration.

Tutorial must be replayable from Settings. Teach one action at a time and allow skip.

### 1.3 Match setup flow

1. Select format: T5, T10, T20, ODI, Test, The Hundred or Custom (availability depends on release phase).
2. Select teams and optionally edit Playing XI.
3. Choose stadium, boundary size, pitch, weather and match time (day/night/day-night).
4. Choose difficulty and control scheme.
5. Choose rules/competition preset and optional custom rules.
6. Confirm match summary.
7. Toss animation; winner chooses bat or bowl.
8. Final team sheet, batting order and bowling plan.
9. Match begins with broadcast camera and touch HUD.

Setup selections must be validated. For example, bowling-over limits and field restrictions come from the selected format preset.

## 2. Batting feature set

### 2.1 Shot library

All listed shots are intended product scope. Animation variants can be added progressively, but each shot type must have an understandable gameplay purpose.

| Shot family | Shot types |
|---|---|
| Off side | Cover drive, off drive, square drive, square cut, late cut, back-foot punch |
| Straight | Straight drive, checked drive, forward defence, back-foot defence, leave |
| Leg side | Flick, glance, pull, hook, leg glance, sweep |
| Sweep family | Sweep, slog sweep, reverse sweep, paddle sweep |
| Advanced / special | Scoop, ramp, upper cut, switch hit, helicopter-style shot |
| Defensive | Forward block, back-foot block, soft hands, leave |

The shot selector must consider ball line, length, pace, bounce, batter stance, foot movement, timing, bat angle, batter skill and selected intent. Do not guarantee a boundary simply because a user taps a power button.

### 2.2 Batting mechanics

- **Timing window:** Perfect, Good, Early and Late. The timing result affects contact quality, power transfer, direction, elevation, edge chance and mis-hit chance.
- **Footwork:** Front foot, back foot, advance down the pitch and stay deep in the crease. Footwork should be chosen before or during the delivery according to the control preset.
- **Shot intent:** Defensive, Balanced and Aggressive. Aggression increases scoring potential and risk.
- **Power / loft:** A controlled loft option and a high-risk power option. Hold/release behavior must be configurable and must not be the only viable way to score.
- **Contact outcome:** Middle, inside edge, outside edge, toe-end, leading edge, pad/body contact or clean miss.
- **Running:** Yes / No / Wait call; single, double, triple and all-run four where rules/field geometry permit. Runners must turn, ground the bat and respond to cancellation.
- **Non-striker behavior:** Back up, respond to call, turn, make ground and face relevant run-out situations.
- **Batting hand:** Right-handed and left-handed stances with mirrored controls and animations.
- **Batter roles:** Opener, anchor, middle order, finisher, wicketkeeper-batter and all-rounder.
- **Fatigue/form (optional):** May influence timing consistency in long formats/career. It must be adjustable or disabled for quick casual matches.
- **Leave decision:** Batter can leave a delivery; the rules engine determines whether it is legal, a wide/no-ball or another result.
- **Shot feedback:** Show timing quality, contact quality, ball speed and a short coaching hint when enabled.

### 2.3 Shot map and shot presets

- Offer a directional shot map that shows playable regions around the batter.
- Allow users to save custom shot preferences/presets (for example, safe rotation, off-side attack or leg-side power).
- Allow a preset to be selected before a match and, where fair, during a pause/innings break.
- Presets influence user intent mapping, not hidden player statistics or guaranteed outcomes.
- Provide a reset-to-default option.

## 3. Bowling feature set

### 3.1 Bowling archetypes and deliveries

| Archetype | Deliveries |
|---|---|
| Fast | Yorker, bouncer, full toss, slower ball, inswinger, outswinger, cutter, knuckle ball |
| Medium | Seam-up, swing, cutters, wide yorker, change of pace |
| Off spin | Off break, doosra, carrom ball, arm ball |
| Leg spin | Leg break, googly, flipper, top spinner |
| Left-arm spin | Orthodox and left-arm wrist-spin/chinaman style |
| Left-arm pace | Over/around the wicket, swing and seam variations according to bowler attributes |

### 3.2 Bowling input and delivery sequence

1. Choose bowler and bowling end.
2. Choose delivery family/type.
3. Drag/tap a landing marker to set line and length.
4. Adjust speed/effort within the bowler's capability.
5. Set swing/spin amount and direction where applicable.
6. Start run-up.
7. Release using a timing window; the release point and action affect delivery accuracy.
8. Follow through and return to fielding position.

### 3.3 Bowling mechanics

- Landing zones: short, good length, full, yorker, wide line, off/middle/leg stump targets.
- Speed display in km/h; maximum speed depends on player rating and delivery type.
- Swing, seam and spin depend on release, bowler attributes, ball condition, weather and pitch.
- Fatigue gradually affects control, pace and variation quality.
- Each bowling archetype has a different run-up, arm path, release, follow-through and recovery animation.
- No-ball and wide checks are performed by the rules engine, not by a cosmetic indicator.
- Bowler spell/over limits are ruleset-specific.
- AI bowling plans adapt to batter weakness, scoring rate, field placement, pitch and innings phase.

## 4. Fielding and wicketkeeping

### 4.1 Field placement

Support named positions and drag placement: slip, gully, third man, point, cover, extra cover, mid-off, mid-on, midwicket, square leg, fine leg, long-on, long-off, deep cover and other standard positions.

Preset layouts:

- Attacking field
- Defensive field
- Powerplay field
- Pace attack
- Spin attack
- Death-over boundary protection
- Custom saved field

The rules engine must validate restrictions for the selected format/phase. The game must explain invalid field positions.

### 4.2 Fielder actions

- AI chase and intercept.
- Manual fielder selection and movement in supported control mode.
- Sprint, dive, slide, jump, pickup, gather, throw, relay throw and boundary save.
- Overarm/underarm throw choice where supported.
- Direct hit, keeper collection, missed throw and overthrow.
- Catch attempts: simple, high, diving, one-handed and boundary catch.
- Dropped catch probability based on skill, difficulty, movement and difficulty of chance—not arbitrary scripting.
- Fielder collision avoidance and no visible clipping through other players.

### 4.3 Wicketkeeper

- Ready stance changes with bowler type and field setup.
- Collect pace deliveries, take spin takes, glove edges and misses.
- Stumping attempt, bails animation, run-out collection and throw to relevant end.
- Keeper can be selected as a manual-control focus in advanced mode.
- Helmet/pads/gloves and stance fit must remain correct during animation.

## 5. Captaincy and team tactics

- Toss decision: bat or bowl.
- Playing XI, captain, wicketkeeper and batting order.
- Bowling changes and over allocation.
- Field presets and individual fielder changes.
- Batter intent and bowling plan suggestions.
- Powerplay strategy and death-over strategy.
- Strategic timeout where the chosen competition supports it.
- Substitute/impact-player behavior only in a ruleset that explicitly uses it.
- AI captain considers score, required run rate, wickets, pitch, weather, batter hand/strength, bowler fatigue and remaining overs.
- Human player may override AI suggestions.

## 6. Cricket rules, umpire, appeals and DRS

### 6.1 Scoring and dismissals

The rules module must resolve legal/illegal deliveries, runs, extras and dismissals. Core dismissal coverage:

- Bowled
- Caught
- LBW
- Run out
- Stumped
- Hit wicket
- Caught behind (scorecard wording may be caught; event source may identify keeper/slip)
- Obstructing the field
- Hit the ball twice, timed out, retired out and other applicable cases under selected rules

Do not implement “Handled the ball” as an old standalone dismissal without checking the current Law treatment. Use current Law wording and applicable playing conditions.

### 6.2 Umpire system

- Standing and movement positions; front-on and square-leg umpire.
- Signals: out, not out, four, six, wide, no-ball, bye, leg bye, dead ball, short run and third-umpire referral.
- Appeal button and optional “Howzat” fielder/crowd audio.
- Appeal-dependent decisions must check whether an appeal occurred when required by the ruleset.
- Casual mode may include an optional umpire-error setting; competitive mode should use consistent deterministic rules.
- The umpire must never make a decision inconsistent with the event log or rules engine.

### 6.3 DRS / review

- Review availability and remaining reviews come from the selected competition preset.
- Batter/captain review input within a configurable timer.
- Ball tracking: pitching point, impact point, projected wicket path and umpire-call zone where applicable.
- Bat-contact visual/audio cue similar in function to an edge-detection replay; do not use a proprietary broadcast mark/brand.
- Optional hotspot-style visualization; clearly marked as an in-game visualization.
- Multiple review angles: bowler end, side-on, stump cam, square leg and close-up.
- Review outcome, retained/lost review and decision update are ruleset-driven.
- Replay must not alter the original ball simulation.

### 6.4 Other law/rules modules

- Wides and no-balls.
- Free-hit rules according to competition preset.
- Powerplays and fielding restrictions.
- Boundary and boundary-catch decisions.
- Overthrows and short runs.
- Super Over / tie resolution.
- Rain interruptions and revised targets.
- Follow-on, innings declarations and new-ball/ball-condition state for Test.
- Non-striker run-out and warning settings where permitted.
- Slow over-rate options if applicable.
- The Hundred ball counting and change-of-ends rules.
- Dead-ball and penalty runs where applicable.

Each rules preset must store: format, competition identifier, rules version, effective date, innings length, over/ball counting, field restrictions, review rules, tie rules, rain rules, bowler limits and dismissal/scoring settings.

## 7. Match formats and game modes

### 7.1 Formats

- T5 and T10 quick formats.
- T20.
- ODI 50 overs plus custom 20/30/40 overs.
- Test: up to five days, two innings per team, follow-on, day/night, bad light, ball condition and pitch wear.
- The Hundred: 100-ball innings and its dedicated ruleset/presentation.
- Custom: choose overs, wickets, powerplay, wides/no-balls, free-hit and other supported match settings.

### 7.2 Modes

- Quick Match.
- Tournament / League with groups, fixtures, points, net run rate, semi-finals and final.
- Bilateral Series of 3, 5 or 7 matches.
- Career as batter, bowler, all-rounder or captain.
- Challenge Mode for chase/defend/skill scenarios.
- Practice / Nets with repeatable batting, bowling and fielding drills.
- Super Over and Knockout.
- Daily Challenge. Download/caching can make a challenge playable offline after it is available on the device.
- Same-device hot-seat for two players.
- Local Wi-Fi/Bluetooth match in a later phase.
- Online 1v1, invite code and matchmaking in a later phase.
- Online tournaments and leaderboards in a later phase.
- Match simulation/fast-forward for AI fixtures.
- Match save/resume at safe state boundaries.

### 7.3 Career mode

- Create a fictional player or select a generated player.
- Select role, batting hand, bowling type, face, kit and starting attributes.
- Skill development: batting timing, shot range, bowling control, pace/spin, fielding, fitness and stamina.
- Squad selection, Playing XI competition and role fit.
- Performance targets, season goals, records and awards.
- Form, fatigue and optional injury system with clear impact and recovery rules.
- Transfer/selection progression only if the project chooses to simulate contracts/teams; this is a recommended extension, not required by the source plan.
- Save career locally; cloud sync optional.
- Retirement summary and career statistics.

## 8. Teams, players and customization

### 8.1 Team and squad editor

- 16–20 fictional teams as the full content target; launch with a smaller polished set.
- Squad up to 15 players per team.
- Edit team name, badge, primary/secondary kit colors, jersey template and home venue.
- Create a team and duplicate an existing fictional template.
- Choose Playing XI, captain, wicketkeeper, batting order and bowling options.
- Import/export documented user-created team/player data where safe and feasible.

### 8.2 Player editor

- Player name, number, batting hand, bowling type and role.
- Semi-realistic face presets and controlled face sliders.
- Skin tone, eyes, eyebrows, nose, lips, jaw, cheeks, chin and forehead.
- Hair, beard and moustache presets.
- Body build and height range (within sensible animation/rig constraints).
- Batting stance, bowling action archetype and celebration choice.
- Jersey, helmet, cap, bat appearance, gloves, pads, shoes, sunglasses and accessories.
- Player ratings: batting, bowling, fielding, fitness and form.
- Save locally; optional share/import after validation.

### 8.3 Cosmetic fairness

Cosmetics must not provide hidden gameplay advantage. All planned core customization should be available without paid stat boosts. Any optional purchase may support the developer or remove ads, but must not gate a required gameplay function.

## 9. Stadiums, pitch, weather and environment

### 9.1 Stadium library

- Full roadmap target: 15–25 original venues with regional identities (South Asian, Australian, English, African, Caribbean and Gulf-inspired).
- Launch target: 2–4 polished original venues.
- Different boundary sizes and ground shapes.
- Pavilion, stands, scoreboard, big screen, dugout, boundary ropes and original sponsor boards.
- Day, night and day/night transition.
- Crowd sections with flags, waves, cheers, reactions and scalable LODs.
- Venue lighting must be readable and not obscure the ball.

### 9.2 Pitch types

- Green/seam-friendly.
- Dry/dusty/spin-friendly.
- Flat/batting-friendly.
- Slow.
- Bouncy.
- Cracked/late-Test.
- Wet/after-rain.

Pitch parameters affect bounce, pace off surface, turn, seam and ball grip. Test pitch wear can accumulate over overs/days. Pitch report describes likely behavior without guaranteeing a result.

### 9.3 Weather and time of day

- Sunny, overcast, light/heavy rain, fog, wind direction/speed and dew.
- Wet ball and reduced grip.
- Covers and rain interruption presentation.
- Bad light and pause for formats that support it.
- Seasonal visual variation as a later content layer.
- Weather can affect simulation only through declared, testable parameters; do not use weather as an unexplained random handicap.

## 10. 3D characters, animation and visual realism

### 10.1 Character direction

- Semi-realistic human proportions and fictional identities.
- Shared skeleton/rig system and reusable animation libraries.
- Face customization through presets and controlled blend shapes/morphs.
- Natural skin shading, eyes, hair/beard and readable expressions.
- Player appearance must remain recognizable in gameplay camera, not only in close-up replay.

### 10.2 Required animation groups

**Batting:** ready stance, guard marking, trigger movement, front-foot/back-foot setup, defence, every shot family, contact follow-through, mistimed shot, edge reaction, leave, beaten animation, run start, sprint, turn, dive/slide bat grounding, milestone reaction and dismissal walk-off.

**Bowling:** mark/run-up, fast/medium/spin delivery actions, release, wrist/finger variation, front-foot landing, follow-through, appeal, celebration, fatigue/breathing and return to mark.

**Fielding:** ready stance, first step, sprint, side-step, gather, pickup, underarm/overarm throw, jump, dive, slide, catch, dropped catch, boundary stop, relay throw, direct-hit attempt and reaction.

**Wicketkeeping:** crouch, take, glove catch, leg-side movement, stumping, bails removal, run-out attempt, appeal and celebration.

**Umpire:** signals, walking to position, boundary signal, out/not-out, review/TV signal, drinks/interval gestures where needed.

**Off-field:** team walk-in, player introductions, anthem-style presentation only if original/licensed, dugout reactions, warm-up, handshake and trophy celebration.

**Face/upper body:** blink, eye tracking, head turn, breathing, focused/relieved/frustrated/happy/disappointed expression, mouth/jaw motion for selected audio cues.

**Crowd:** cheering, standing, clapping, waving flags, reacting to wicket/six/four and subdued crowd after a quiet ball. Use lower update rates for distant crowd.

### 10.3 Animation quality requirements

- Bat must meet ball at the detected contact frame.
- Ball must leave the hand at the correct release event.
- Feet must plant without visible sliding in core actions.
- Hand, bat, glove and equipment IK must stay aligned.
- No visible clipping between bat/body, helmet/face, pads/legs or player/player.
- Run, turn, dive and throw animations must respond to actual ball/player positions.
- Add animation variation and transition blending to avoid repetitive robotic loops.
- Motion capture is recommended for hero actions if budget/rights allow; procedural and keyframed animation can fill the remaining library.

## 11. Camera, replay and match presentation

- Default TV/broadcast camera.
- Behind bowler, behind batter, side-on, stump cam, aerial/drone-style and boundary camera.
- Follow-ball camera with readable zoom and framing.
- User camera preference and sensitivity where appropriate.
- Auto replay on wicket, boundary, catch and milestone; toggle in settings.
- Slow motion: 0.25x, 0.5x and normal speed.
- Manual replay pause/rewind/forward where replay buffer supports it.
- Third-umpire replay for run-out, stumping, catch and boundary.
- Pitch map, wagon wheel, beehive/line-length and speed gun overlays.
- Highlights reel from saved key events; local video export/share only after performance and Android permission tests.
- Camera must not hide the live ball or place touch controls over critical ball/batter visibility.

## 12. Scoreboard, statistics and commentary

### 12.1 Live HUD information

- Team score and wickets.
- Overs/balls and innings.
- Striker and non-striker names, runs and balls faced.
- Bowler figures and current over.
- Target, required runs and required rate in a chase.
- Current run rate, partnership and recent-ball sequence.
- Extras and powerplay/field phase where relevant.
- Pause, settings and control hints in designated touch zones.

### 12.2 Scorecards and records

- Full batting and bowling scorecard.
- Extras breakdown, fall of wickets and partnerships.
- Match summary and player of the match.
- Career average, strike rate, 50s/100s, wickets, economy, best figures, catches and run-outs.
- Team records and player records: fastest milestones, most sixes, best bowling and other defined stats.
- Wagon wheel, Manhattan chart and pitch map.
- Match history and saved scorecard image.

### 12.3 Commentary and audio

- Urdu and English text commentary at launch.
- Event-based lines for dot ball, boundary, six, wicket, appeal, dropped catch, review, milestone and close finish.
- Selectable commentary personalities (for example cheerful, serious and humorous) using original voices/text.
- Audio commentary can be added in stages; use original or properly licensed voice recordings.
- Separate volume controls for commentary, crowd, bat/ball/stumps, UI, music and ambience.
- Stump microphone sound cues and crowd reaction intensity.
- Subtitles/captions for voice content.

## 13. Offline, online and social features

### 13.1 Offline

- Quick Match, Practice, local career, local tournament, custom teams, settings, saved scorecards and match history.
- No mandatory account creation.
- Save files versioned and recoverable; autosave at over/innings/result safe states.
- If app closes mid-delivery, resume from last stable state unless a fully replayable in-flight state has been implemented.

### 13.2 Online (later phase)

- Optional account and cloud save.
- Invite-code friend match and 1v1 matchmaking.
- Local Wi-Fi/Bluetooth if supported by the chosen networking stack.
- Online tournaments and ranked/unranked modes.
- Leaderboards by region/friends/global.
- Friends list and safe preset chat/emojis.
- Report/block tools and moderation workflow.
- Server-side validation for competitive results, rate limits and basic anti-cheat.
- Reconnect and match-abandonment policy.
- Do not launch ranked play until desync, cheating and server-authority tests pass.

## 14. Settings, accessibility and monetization

### 14.1 Graphics and device settings

- 3D Lite, Balanced 3D and High 3D.
- 30 FPS target as baseline; 60 FPS only on tested devices.
- Render scale, shadows, crowd, effects and anti-aliasing controlled by profile.
- Auto-detect plus manual override.
- Battery saver and thermal-aware scaling.
- Graphics changes from menu or safe pause point, not during a critical delivery/collision.

### 14.2 Touch/control settings

- Swipe, Buttons and Joystick + Buttons.
- Custom button position, size, opacity and sensitivity.
- Left-handed layout.
- Control reset and per-scheme tutorial.
- Haptic intensity/on-off.
- Hold/auto-run/auto-fielding preferences where supported.

### 14.3 Accessibility

- Large text and scalable UI.
- Color-blind-safe indicators; never communicate only by color.
- Subtitles and independent audio controls.
- Reduced-motion option for non-essential camera/effects.
- High-contrast HUD.
- Remappable controls and larger touch targets.
- Pause tutorial and contextual hints.

### 14.4 Monetization rules

- Core game, modes and gameplay functions remain free/unlocked.
- Optional banner only on non-match menu screens if it does not obstruct navigation.
- Optional rewarded video may provide cosmetic/convenience extras only; never a required review, player stat or match win.
- No ad during delivery, over, review, innings, active match or result animation.
- Optional one-time remove-ads/support purchase may exist without gating gameplay.

## 15. Admin and content operations (recommended internal tool)

The source plan does not define a full admin console. The following is a recommended internal web tool, not a mandatory player-facing Android screen:

- Secure admin login, role-based permissions and audit log.
- Create/edit fictional teams, players, ratings, kits and venue metadata.
- Manage ruleset/content version and staged content releases.
- Upload approved original/licensed assets with license/source metadata.
- Manage daily challenge content and event schedule.
- View crash/performance aggregates and report queue.
- Moderate user-generated imports and user reports.
- Publish/rollback a content pack.

Admin must not have a hidden ability to alter a live competitive match result. Sensitive actions need confirmation and audit history.

## 16. Content and asset production checklist

| Asset group | Required content |
|---|---|
| Player body | Batter, bowler, fielder, wicketkeeper rigs; body/height variants; LODs |
| Player head | Semi-realistic fictional head presets, face blend shapes, skin/hair/beard materials |
| Equipment | Bat, helmet, gloves, pads, shoes, cap, sunglasses, stumps, bails and ball variants |
| Animation | Batting, bowling, running, fielding, catching, diving, throwing, umpire, celebrations and crowd |
| Venues | Stadium shell, pitch, boundary, pavilion, stands, lights, screens, dugouts and crowd sections |
| UI | HUD icons, team badges, menu panels, control buttons, diagrams, overlays and localizations |
| Audio | Bat impact, ball bounce, stumps, crowd, umpire, ambience, UI, music and commentary |
| Data | Teams, player attributes, format presets, competition rules, pitch profiles, challenge templates and localization strings |

Every third-party asset must have a recorded license and intended use. Every original voice recording needs talent permission/release.

## 17. Release scope and priorities

| Priority | Deliverable |
|---|---|
| P0 — Must prove first | Full-screen landscape HUD, touch input, ball physics, 4 core shots, one pace + one spin delivery, one over, basic score, one 3D batter/bowler, Lite 3D profile, physical 4GB test |
| P1 — First playable match | Complete T20 flow, toss, two fictional teams, AI, batting/bowling/fielding loop, main dismissals, scorecard, save/resume, Urdu/English text and graphics parity |
| P2 — Version 1.0 candidate | Quick Match, Practice, small tournament, team/player selection, basic face editor, control customization, audio, settings, local records and core QA |
| P3 — Free updates | ODI/Test/The Hundred, deep DRS, expanded career, more stadiums, weather, advanced replay, hot-seat/local network/online and social tools |

A feature is not complete merely because its button exists. It is complete only when behavior, animation, feedback, save/rules logic, performance and test cases are implemented.

## 18. Global acceptance checklist

- [ ] The app opens in immersive landscape mode with no unnecessary system bar interruption.
- [ ] Notch/cutout safe areas do not hide important buttons, score or text.
- [ ] Every essential match action is available via touch.
- [ ] Multi-touch works (for example movement/shot action plus run call where configured).
- [ ] Buttons can be moved/resized and reset.
- [ ] Both graphics modes show 3D active players.
- [ ] Same match inputs/state produce the same outcome in Lite and High 3D.
- [ ] Ball release, bounce, bat contact and fielding animations are synchronized to simulation events.
- [ ] No major animation clipping, foot sliding or floating equipment in the core action set.
- [ ] Score, overs, extras, wickets and match results pass rule regression tests.
- [ ] Offline quick match and local save work with network disabled.
- [ ] A 4GB physical device completes a 30-minute Lite-mode test without repeatable crash/OOM.
- [ ] Stable 30 FPS is targeted in normal Lite scenes; 60 FPS is enabled only on devices that pass profiling.
- [ ] Ads never appear during active play.
- [ ] Original/licensed assets and privacy/store disclosures are verified before release.

## 19. Important open decisions

These must be finalized during pre-production; do not silently assume them:

1. Minimum Android version and final supported device/GPU list.
2. Exact Unity editor and package versions to pin.
3. Final portrait/landscape policy for non-match menus (recommended: landscape throughout for consistent cricket presentation).
4. Whether local Wi-Fi/Bluetooth is in the first release or a later update.
5. Cloud provider and networking provider (Firebase/Unity Gaming Services; Photon/Unity Netcode or another tested option).
6. Final rules preset list and competition-specific legal review.
7. Art/animation budget and whether hero animations use motion capture.
8. Audio commentary production budget and language rollout.
9. Admin-console scope and access policy.
10. Final monetization and privacy policy.

## 20. External reference notes

These are reference points, not instructions to copy another game's proprietary implementation:

- Real Cricket's current official product material highlights a large batting-shot library, motion-captured fielding/catching and a customizable shot-map concept. Treat these as quality/feature benchmarks; define and produce original content for Real Cricket Pro. [KRAFTON Real Cricket](https://krafton.in/games/real-cricket/) and [Google Play listing](https://play.google.com/store/apps/details?id=com.nautilus.realcricket).
- Unity provides Android touchscreen/gamepad input through the Input System. [Unity Android input](https://docs.unity.com/en-us/engine/6000.3/manual/platform-specific/android/developing/input).
- Unity immersive mode can hide system bars for full-screen presentation; safe-area handling is still required for cutouts/insets. [Unity Android window insets](https://docs.unity.com/en-us/engine/7000.0/manual/platform-specific/android/developing/edge-to-edge-rendering/window-insets) and [Screen.safeArea](https://docs.unity.com/en-us/engine/6000.7/script-reference/unityengine/screen/safearea).
- URP performance settings must be profiled and tuned for target devices. [Unity URP performance guidance](https://docs.unity.cn/6000.1/Documentation/Manual/urp/configure-for-better-performance.html).

---

**Final product direction:** Real Cricket Pro is a touch-first, immersive Android cricket simulation with semi-realistic 3D fictional players, scalable 3D Lite visuals, authentic-feeling cricket systems, full match presentation and free offline-first play. The first engineering milestone is a polished one-over prototype—not a menu full of inactive buttons.

**REAL CRICKET PRO**

# Technical Requirements Document (TRD)

Recommended architecture, platforms, modules, performance and quality controls

| **Document ID** | TRD-002 |
| --- | --- |
| **Version** | 1.0 - Working Draft |
| **Prepared** | 26 October 2026 |
| **Product baseline** | Android \| Unity 6 LTS family + URP \| C# |
| **Device target** | 4GB RAM baseline; 6GB/8GB+ scalable tiers |
| **Graphics** | Semi-realistic 3D + 2D Lite / 2.5D presentation |
| **Operating principle** | Offline-first \| Urdu + English \| free core gameplay |

**Document purpose** Translates the approved product direction into technical requirements and implementation boundaries for the engineering team.

> Source basis: “Cricket Game (Android) – Poora Feature Plan” (11 pages) and “Real Cricket Pro | Updated Development Plan” (18 pages). This document converts the agreed product direction into a specialist project artifact. Proposed details are marked where source plans did not decide them.

## 1. Technical overview

Recommended baseline: Unity 6 LTS family, Universal Render Pipeline (URP), C#, Android-first build pipeline and Unity Input System. Exact editor and package versions must be selected during pre-production and locked for reproducible builds. Do not start with a 2D project and later convert it: create one 3D-capable project with a scalable Lite presentation profile.

## 2. Architecture principles

- **Simulation/presentation separation:** Cricket simulation is independent from render mode, camera, UI and network presentation.

- **Single source of truth:** One match-state/rules module owns score, wickets, ball count, player state and results.

- **Offline-first:** Core content, AI and local persistence must not depend on authentication or remote services.

- **Data-driven content:** Teams, players, venues, pitches, formats, cosmetics and commentary are configuration/content data, not hard-coded scene logic.

- **Modular services:** Cloud, multiplayer, ads, analytics and remote content are adapters that can be disabled without breaking offline match play.

- **Versioned rules and saves:** Rules presets and save data include schema/version identifiers to support future changes.

## 3. High-level system architecture

```text
ANDROID CLIENT
  Presentation: UI / broadcast camera / 3D profile / Lite profile / audio
                    |
  Gameplay orchestration: Match flow / input / AI / animation events
                    |
  Core simulation: ball flight / contact / running / fielding / umpire / rules / score
                    |
  Local data: save slots / career / teams / settings / match history
                    |
  Optional service adapters: auth / cloud save / online match / leaderboard / ads / analytics
                    |
REMOTE SERVICES (later phases)
  account + sync | matchmaking + authoritative result validation | leaderboard | moderation
```

The online service architecture is a logical target, not a vendor decision. The source plan names Firebase for login/leaderboards/cloud save and Photon or Unity Netcode for multiplayer; the updated plan recommends choosing one primary cloud platform after prototype and evaluating networking options before committing.

## 4. Platform and build requirements

| **Area** | **Requirement / recommendation** |
| --- | --- |
| Engine | Unity 6 LTS family. Select exact patch/editor version at project start and pin it in version control/CI. |
| Rendering | URP with Low/Lite, Balanced 3D and High 3D quality profiles. Avoid HDRP for Android-first scope. |
| Language | C#; gameplay simulation, presentation, persistence and service layers separated by assembly/module boundaries. |
| Input | Unity Input System; touch-first gestures, buttons, virtual stick, remapping, left-hand mode and accessibility sizes. |
| Android build | Android App Bundle for store release; define min/target API and ABI/device support after Unity/Play requirements are verified at release. |
| Content | Addressables or equivalent for optional stadium, voice, language and visual content packs; essential offline assets included locally. |
| Version control | Git with Unity-friendly ignore rules, Git LFS where large binary assets require it; protected main branch and release tags. |
| CI/build | Automated Android development build on merges; versioned release candidate builds; archive symbols, build metadata and test logs. |

## 5. Core software modules

| **Module** | **Responsibilities** | **Key interfaces / outputs** |
| --- | --- | --- |
| MatchFlow | Setup, toss, innings transitions, pause, result and resume. | MatchState, MatchPhase, MatchCommand |
| RulesEngine | Format presets, extras, dismissals, appeals, powerplay, tie, reviews and playing conditions. | RulesetConfig, DecisionResult, RuleEvent |
| BallSimulation | Release, velocity, swing/seam/spin, bounce, bat/pad/body contact, ground roll and boundary. | DeliveryInput, BallState, ContactEvent |
| BattingController | Shot choice, timing, footwork, intent, running calls and stance. | BatterInput, ShotIntent, ContactQuality |
| BowlingController | Line/length, delivery type, speed, release and fatigue. | BowlerInput, DeliverySpec |
| FieldingAI | Positioning, pursuit, intercept, catch, throw, keeper and run-out. | FieldingEvent, ThrowResult |
| Scoring/Stats | Runs, extras, wickets, scorecards, partnerships, career and records. | ScoreState, MatchSummary, PlayerStats |
| Presentation | 3D/Lite render, camera, animation, HUD, overlays, sound and replay. | PresentationProfile, UIEvent, AudioCue |
| Persistence | Versioned local save, autosave, migration, backup and cloud-sync adapter. | SaveEnvelope, SaveResult |
| OnlineAdapters | Authentication, cloud save, matchmaking, authoritative results and moderation. | Service interfaces; provider-specific adapters |

## 6. Simulation and game-state requirements

- **Match state:** Represent innings, over, legal/illegal delivery count, striker/non-striker, bowler, fielders, score, wickets, target, extras, reviews, fatigue and pending decision.

- **Ball state:** Position, velocity, spin/seam/swing parameters, pitch contact, bat/pad/body contact, fielder contact and boundary state.

- **Event-based rules:** Simulation emits events (delivery released, pitch contact, bat contact, catch, throw, appeal); RulesEngine resolves legal scoring and dismissal result.

- **Fair visual parity:** Presentation profiles consume the same simulation state and events. Visual update rate, mesh LOD or crowd count cannot alter collision or score logic.

- **Fixed/update timing:** Use a controlled simulation timestep for ball and match-critical events. Keep render FPS independent; define deterministic/replay expectations during prototype.

- **AI:** Use player ratings, batting hand, role, bowler type, field setup, pitch and match context. Avoid scripted outcomes that ignore physical/rule state.

## 7. Dual graphics and character requirements

| **Profile** | **Required behavior** | **Constraints** |
| --- | --- | --- |
| 2D Lite / 2.5D | Simplified player visuals or sprites for distant/background actors, simple face textures, baked lighting, minimal particles, shadows off or simple, reduced crowd and effects. | Critical ball, crease, boundary and fielder positions remain visible; target 30 FPS on tested 4GB devices. |
| Balanced 3D | Optimized 3D player rigs, semi-realistic face presets, limited crowd animation, one main light and simple shadows. | Target stable 30 FPS; 60 FPS only where physical profiling passes. |
| High 3D | Higher-detail models/materials, more facial detail, crowd animation, particles, shadows and camera effects. | Only enable on devices proven capable; target 60 FPS where supported. |

Face pipeline: create a small set of semi-realistic fictional base heads and body presets; use controlled presets/sliders for jaw, cheeks, chin, forehead, eyes, brows, nose, lips, ears and skin tone; add hair/beard variants and basic expressions. Use shared rigs/animation clips and reduced facial animation for distant players. Avoid building each player as a unique high-poly asset.

## 8. Performance and memory requirements

| **Technique** | **Requirement** |
| --- | --- |
| LOD/culling | Player and stadium LODs; frustum/occlusion culling where beneficial; distant crowd updates less frequently. |
| Textures/materials | Compressed textures, mipmaps, shared materials, texture atlases for small assets, limited shader variants. |
| Lighting | Bake static stadium lighting; keep dynamic lights limited; scalable shadows and post-processing. |
| Pooling | Pool balls, particles, field markers, UI effects and repeat objects; avoid frequent instantiate/destroy in match loop. |
| Rendering | Scale render resolution, anti-aliasing, extra lights and effects by profile; use SRP Batcher where appropriate. |
| Thermal/battery | Profile long sessions; provide stable 30 FPS Lite and battery saver; reduce workload as device heats if needed. |
| Measurement | Use Unity Profiler and physical GPU/device tools; capture CPU/GPU frame time, memory, load times, thermal and battery notes. |

## 9. Persistence and service requirements

- **Local save:** Versioned save envelope, atomic/safe write, backup slot, checksum or integrity marker, migration path and corruption recovery.

- **Match resume:** Autosave after each over and safe state transitions. If the app closes mid-delivery, resume from last stable ball boundary unless full simulation replay state is deliberately implemented.

- **Cloud:** Optional provider adapter; do not duplicate Firebase and Unity Gaming Services responsibilities without a specific reason. Define conflict handling and user-visible restore choice.

- **Multiplayer:** Evaluate Photon and Unity Netcode against latency, server authority, hosting cost, reconnect, cheat resistance and team skills. No client-trusted final score in ranked play.

- **Ads/analytics:** Optional SDK adapters; ads outside live play only. Minimize analytics and disclose data collection accurately.

## 10. Security, privacy and legal

- No mandatory account for offline play. Protect any login token and cloud-save content with platform-secure storage practices.

- Server validates competitive match results, leaderboard writes and matchmaking state. Rate-limit remote APIs and reject malformed payloads.

- Report/block and preset chat moderation are required before enabling social features. Free-text chat can be postponed.

- Maintain asset provenance and license records for character models, kits, logos, stadium props, fonts, music, commentary and SDKs.

- Store policy, privacy policy, data safety, age rating and permissions must match actual release behavior.

## 11. Technical verification / open decisions

| **Decision** | **Status** | **Owner / exit condition** |
| --- | --- | --- |
| Exact Unity editor/package versions | Open | Tech lead locks versions during pre-production after Android compatibility review. |
| Minimum Android API / ABI matrix | Open | Tech lead defines against current Google Play and Unity requirements; QA verifies physical devices. |
| Cloud provider | Open: Firebase or Unity Gaming Services | Choose one primary stack after offline prototype and service needs review. |
| Multiplayer stack | Open: Photon vs Unity Netcode | Prototype latency/authority/cost and select before online implementation. |
| Save format / storage layer | Recommended: versioned local data store | Tech lead chooses JSON/binary/SQLite based on size, migrations and performance. |
| Graphics budgets | Open | Set per profile after 4GB vertical slice profiling. |
| Official DLS implementation | Open / verification required | Only use official DLS label after method/rights/accuracy verification. |

## Source and decision notes

- Primary source A: Cricket Game (Android) - Poora Feature Plan, 11 pages, dated 26 October 2026.

- Primary source B: Real Cricket Pro - Updated Development Plan, version 2.0, 18 pages, dated 26 October 2026.

- The agreed owner choices used throughout: game name Real Cricket Pro; both graphics modes; 4GB RAM baseline; semi-realistic fictional faces.

- Where a backend vendor, detailed visual style, exact device list, commercial policy, or admin workflow is not decided in the source plans, this pack labels the item as a recommendation or open decision.

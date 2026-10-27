**REAL CRICKET PRO**

# Implementation Plan

Phases, milestones, engineering backlog, QA gates and release readiness

| **Document ID** | IMPL-006 |
| --- | --- |
| **Version** | 1.0 - Working Draft |
| **Prepared** | 26 October 2026 |
| **Product baseline** | Android \| Unity 6 LTS family + URP \| C# |
| **Device target** | 4GB RAM baseline; 6GB/8GB+ scalable tiers |
| **Graphics** | Semi-realistic 3D + 2D Lite / 2.5D presentation |
| **Operating principle** | Offline-first \| Urdu + English \| free core gameplay |

**Document purpose** Turns the PRD and TRD into a sequenced work plan with deliverables, dependencies, acceptance gates and risk controls.

> Source basis: “Cricket Game (Android) – Poora Feature Plan” (11 pages) and “Real Cricket Pro | Updated Development Plan” (18 pages). This document converts the agreed product direction into a specialist project artifact. Proposed details are marked where source plans did not decide them.

## 1. Delivery strategy

Build in vertical slices. The first deliverable is a playable one-over prototype, not a content-heavy full game. Prove the feel of batting, ball physics, controls and Lite performance before building 15-25 stadiums, deep career, DRS or online multiplayer.

> **Planning estimate** The updated source plan estimates roughly 8-14 months for an experienced small team to reach a polished first release. A solo developer or learning team may take significantly longer. Full scope, advanced DRS, multi-format career and robust online services can extend beyond this estimate.

## 2. Phases and milestone plan

| **Phase** | **Indicative duration** | **Main deliverables** | **Exit gate** |
| --- | --- | --- | --- |
| 0. Pre-production | 2-3 weeks | Scope lock, requirements traceability, rules matrix, device matrix, art direction, tech spike, project/repo setup. | Scope, controls and target device strategy approved. |
| 1. Core prototype | 4-6 weeks | One pitch, one batter, one bowler, one over, ball flight/contact, four core shots, pace + spin, basic score UI, initial Lite/3D render. | Playable one-over match on physical device; touch and ball feel accepted. |
| 2. Vertical slice | 6-10 weeks | Complete sample T20 match, two teams, 1-2 venues, toss, basic AI, semi-realistic face/animation, audio, UI, save/resume and one replay. | End-to-end match works on physical 4GB test devices; same outcome across graphics modes. |
| 3. Core gameplay | 8-12 weeks | Shot library, bowling archetypes, fielding AI, keeper, umpire, appeal, scorecard, dismissals, team selection, tutorial and difficulty. | Core cricket scenarios and rules regression tests pass. |
| 4. Modes and content | 8-12 weeks | Practice, challenge, tournament foundation, team/player editor, additional pitches/venues, series/career foundation. | User completes full offline play loop and progression save. |
| 5. Polish and optimization | 6-8 weeks | Graphics tiers, memory/thermal tuning, Urdu/English localization, accessibility, crash fixes, store prep and legal review. | Target device matrix and release acceptance criteria pass. |
| 6. Online expansion | Separate 8-16+ weeks | Optional login/cloud save, matchmaking, online 1v1, leaderboards, moderation and server result validation. | Network/security/load/reconnect tests pass. |

## 3. Workstream ownership (recommended)

| **Workstream** | **Primary responsibility** | **Key output** |
| --- | --- | --- |
| Product/design | Scope, requirement priority, rules and player journeys. | PRD, acceptance criteria, design decisions and release scope. |
| Gameplay engineering | Batting, bowling, ball simulation, fielding, AI, scoring and rules. | Shared simulation core and playable match. |
| Client/technical engineering | Unity project, modules, input, save, Android builds and profiling. | Build pipeline, architecture, persistence and device stability. |
| Art/animation | Stadium, fictional player models, semi-realistic faces, rigs, kits and effects. | Hero player, reusable rigs, LOD assets and visual tiers. |
| UI/UX | Navigation, match HUD, settings, localization and accessibility. | Figma flows, UI components and implemented screens. |
| Backend/network (later) | Cloud save, matchmaking, authoritative results, leaderboard and moderation. | Optional online services and operations tools. |
| QA | Device matrix, cricket rules, save reliability, usability and release checks. | Test cases, bug triage, regression reports and sign-off. |

## 4. Build-by-build checklist

### Build 0.1 - one-over prototype

- Create Unity project with URP and Android build pipeline; pin editor/package versions.

- Create one fictional stadium and pitch, one batter, one bowler, one wicketkeeper and a small fielding group.

- Implement ball release/velocity, bounce, bat collision, ground roll and boundary detection.

- Implement at least defence, drive, pull and loft; add a bowling line/length target and one pace/one spin delivery.

- Add basic single/double running, bowled, caught and run-out events.

- Add over, score and wicket UI plus end-of-over state.

- Render one player as 3D and simplified Lite visual using the same simulation.

- Build and test on a physical 4GB Android device; record FPS, memory, heat and input observations.

### Build 0.2 - vertical slice

- Complete a T20 sample flow including toss, batting order, innings break and basic AI.

- Add semi-realistic face preset, batting/bowling animation, sample crowd, stadium lighting and audio.

- Add graphics mode switching and prove identical score/physics outcomes.

- Add Urdu/English text, pause, autosave/resume, result, scorecard and one highlight replay.

### Build 0.3 - MVP candidate

- Add Quick Match, Practice/Nets and one tournament path.

- Add multiple teams, player roles/ratings and basic player editor.

- Complete core dismissals, appeals, wide/no-ball and configured powerplay rules.

- Add difficulty, control customization, accessibility basics and settings persistence.

- Run QA on target devices; do not release with open critical bugs or known data-loss defects.

## 5. Engineering backlog by epic

| **Epic** | **Priority** | **Stories / tasks** | **Depends on** |
| --- | --- | --- | --- |
| EP-01 Project foundation | P0 | Unity/URP setup, folder/assembly structure, input, build pipeline, scene bootstrap, version control. | Approved TRD decisions. |
| EP-02 Match state + rules | P0 | Match phases, innings, legal ball counter, score, extras, wicket, ruleset config, state events. | EP-01. |
| EP-03 Ball + batting | P0 | Delivery, bounce, contact, timing windows, shot intents, edge/mis-hit, running. | EP-02. |
| EP-04 Bowling + fielding | P0 | Pace/spin archetypes, target, release, fielder AI, catching, keeper, throws. | EP-02/03. |
| EP-05 UI + controls | P0 | Home, match setup, toss, HUD, control schemes, pause, result and scorecard. | EP-01/02. |
| EP-06 Graphics profiles | P0 | Lite/3D profiles, LOD, face preset, lighting, camera and profile parity tests. | EP-01/03. |
| EP-07 Local persistence | P0 | Versioned settings, match save, autosave, backup, resume and migration. | EP-02/05. |
| EP-08 Offline modes | P1 | Practice, quick T20, small tournament, local history and challenge foundation. | EP-02 through EP-07. |
| EP-09 Player/team content | P1 | Fictional roster, ratings, player/team editor, face customization, kits and venues. | EP-06/07. |
| EP-10 Quality/release | P0/P1 | Device profiling, rules regression, localization, accessibility, privacy/IP and store readiness. | All MVP epics. |
| EP-11 Online services | P2 | Auth, cloud sync, matchmaking, server validation, leaderboard, report/block. | Stable MVP and provider decision. |

## 6. Milestone gates and definitions of done

| **Gate** | **Pass criteria** |
| --- | --- |
| Gate A - Controls | Ball visible; batting/bowling controls predictable; camera never obscures critical play; tutorial demonstrates selected control scheme. |
| Gate B - Simulation | Same input/seed and match state result in the same score/rule outcome in Lite and 3D; no render mode affects physics or ratings. |
| Gate C - Content | Teams, players, venues, pitches and rules presets can be added from data/config without changing core gameplay code. |
| Gate D - Device | Memory, FPS, load, battery and thermal behavior measured on physical 4GB, 6GB and 8GB+ Android tiers with varied chipsets/GPUs. |
| Gate E - Release | Privacy/IP, store policy, crash quality, save recovery, local/offline behavior and complete match flow signed off. |

Definition of Done for each feature: acceptance criteria met; automated/unit or manual test added; localization-ready text; accessibility and Lite/3D behavior checked; performance impact measured if gameplay/render-related; no critical/high unresolved bug; documentation/config updated.

## 7. QA test plan

| **Test area** | **Coverage** |
| --- | --- |
| Devices | At least 3 physical tiers: 4GB baseline, 6GB mid-range and 8GB+ high-tier; different GPU/chipset families. |
| Graphics | Lite/Balanced/High; app suspend/resume, low battery, heat, long session, background/foreground and resolution changes. |
| Gameplay | Right/left-handed batters, pace/spin, shot list, edges, runs, catches, every core dismissal and all match endings. |
| Rules | T20, ODI, Test and The Hundred presets separately; wide, no-ball, free hit, powerplay, boundary, tie, review and follow-on cases. |
| Save | App close, low storage, interrupted writes, old-version migration, checksum failure and corrupt-save restore. |
| Network | Offline launch, intermittent internet, reconnect, duplicate result, match desync and leaderboard tampering. |
| Accessibility | Large text, color-blind mode, left-hand controls, subtitles, remapping and reduced motion. |

## 8. Risk register

| **Risk** | **Likelihood / impact** | **Response / mitigation** |
| --- | --- | --- |
| Scope too large | High / High | Ship vertical slice and core match first; move advanced formats and online into separate phases. |
| 4GB performance | Medium / High | URP profiles, LOD, pooling, compression, baked lighting and real-device profiling. |
| Weak face/animation quality | Medium / Medium | Prototype one hero player and validate at gameplay distance before full roster. |
| Cricket rules bugs | Medium / High | Central rules engine, versioned presets and MCC/competition rules checklist plus regression tests. |
| Online cheating/desync | Medium / High | Delay online launch; server-authoritative result and input/event validation. |
| Copyright takedown | Medium / High | Original IP; maintain licenses, asset provenance and permissions before store submission. |
| Thermal throttling/battery | Medium / Medium | Stable 30 FPS Lite, battery saver, adaptive workload and long-session QA. |

## 9. Release readiness checklist

- Complete match path from app launch through result and saved scorecard.

- Offline quick match and practice tested in airplane mode; no mandatory sign-in.

- Lite profile tested on agreed 4GB devices for 30-minute sessions; supported device list grounded in test results.

- Graphics parity verified: same simulation results across profile changes.

- No critical crash, data-loss or save corruption defect in release candidate.

- Urdu and English screens proofread; accessibility checks completed.

- All bundled assets, audio, fonts, logos, character designs and store screenshots are original or licensed.

- Privacy policy, data-safety form, permissions, content rating, store listing and SDK disclosures match actual behavior.

## 10. Open decisions before production

| **Decision** | **Needed by** | **Recommendation / trigger** |
| --- | --- | --- |
| Exact Unity version + Android support matrix | Phase 0 | Lock after current compatibility review and device test selection. |
| Final control tuning and input timing | Phase 1 | Use hands-on prototype sessions; do not tune only in Editor. |
| Art style/hero face approval | Phase 1-2 | Approve one hero player and face preset at broadcast camera distance. |
| Cloud provider | Before online work | Choose Firebase or Unity Gaming Services as primary after service scope review. |
| Multiplayer stack | Before online work | Benchmark Photon vs Unity Netcode for authority, latency, cost and reconnection. |
| Official DLS method/rights | Before DLS release | Verify implementation; until then label as rain-revised target. |
| Admin console scope | Before online/social content operations | Start with minimal role-based console only when content/moderation volume needs it. |

## Source and decision notes

- Primary source A: Cricket Game (Android) - Poora Feature Plan, 11 pages, dated 26 October 2026.

- Primary source B: Real Cricket Pro - Updated Development Plan, version 2.0, 18 pages, dated 26 October 2026.

- The agreed owner choices used throughout: game name Real Cricket Pro; both graphics modes; 4GB RAM baseline; semi-realistic fictional faces.

- Where a backend vendor, detailed visual style, exact device list, commercial policy, or admin workflow is not decided in the source plans, this pack labels the item as a recommendation or open decision.

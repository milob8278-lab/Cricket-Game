**REAL CRICKET PRO**

# Product Requirements Document (PRD)

What Real Cricket Pro must deliver, for whom, and in which release

| **Document ID** | PRD-001 |
| --- | --- |
| **Version** | 1.0 - Working Draft |
| **Prepared** | 26 October 2026 |
| **Product baseline** | Android \| Unity 6 LTS family + URP \| C# |
| **Device target** | 4GB RAM baseline; 6GB/8GB+ scalable tiers |
| **Graphics** | Semi-realistic 3D + 2D Lite / 2.5D presentation |
| **Operating principle** | Offline-first \| Urdu + English \| free core gameplay |

**Document purpose** Defines product scope, user value, functional requirements, quality expectations, release boundaries and measurable acceptance criteria.

> Source basis: “Cricket Game (Android) – Poora Feature Plan” (11 pages) and “Real Cricket Pro | Updated Development Plan” (18 pages). This document converts the agreed product direction into a specialist project artifact. Proposed details are marked where source plans did not decide them.

## 1. Executive summary

Real Cricket Pro is an Android-first cricket game focused on responsive cricket simulation, semi-realistic fictional human characters, TV-style match presentation and broad Android device support. It is offline-first: core matches, practice and local progression must work without account creation or internet access. Online multiplayer, cloud features and leaderboards are optional online services.

The product has two selectable visual profiles: semi-realistic 3D and 2D Lite / 2.5D. Both profiles use the same cricket simulation, rules, player ratings and match outcomes. The planned 4GB RAM baseline is an engineering target that must be proven on physical devices, not a blanket compatibility guarantee.

> **Product promise** A fair, playable cricket match first. High-detail presentation is scalable; gameplay quality and match fairness are not.

## 2. Product goals and principles

| **Goal** | **Product rule / success signal** |
| --- | --- |
| Authentic-feeling cricket | Batting timing, bowling line/length, running, fielding and umpire outcomes respond to match events and player attributes. |
| Accessible Android performance | Lite mode targets stable 30 FPS in normal match scenes on selected 4GB test devices, with no repeatable crash/OOM during a 30-minute test. |
| Visual choice | Players can select 3D, Balanced 3D or Lite; all modes preserve identical game-state and outcome logic. |
| Offline-first | Quick Match, Practice, local saves, custom teams and AI opponents work without login or internet. |
| Free core gameplay | No coins, gems, paid gameplay unlocks or paywalls around core features. Ads never interrupt an active match. |
| Original IP | Use fictional player/team identities and original or properly licensed visual/audio assets. |

## 3. Target users

| **User group** | **Need** | **Design response** |
| --- | --- | --- |
| Casual cricket player | Start a quick match with simple controls. | Quick Match, tutorial, swipe/buttons presets, adjustable difficulty. |
| Cricket enthusiast | More authentic tactics, shots, formats and rules. | Shot library, bowling variations, field placement, scorecards, appeals and format presets. |
| Low/mid-range Android player | Playable match without overheating or memory crashes. | Lite profile, 30 FPS target, scalable crowd/effects, low-memory assets. |
| Progression-focused player | Long-term player or team progression. | Career, series, tournaments, records and local save; staged release. |
| Friend / competitive player | Play another person locally or online. | Hot-seat first; local network and online 1v1/leaderboards in later phases. |

## 4. Scope and release strategy

| **Release stage** | **Included scope** | **Exit condition** |
| --- | --- | --- |
| Prototype 0.1 | One pitch, fictional stadium, batter, bowler, keeper and fielders; one over; four core shots; one pace and one spin delivery; basic score UI; 3D and Lite visual test. | A playable over on a physical 4GB Android device; ball/shot controls feel responsive. |
| Vertical slice 0.2 | Complete sample T20 flow, toss, basic AI, two teams, one or two venues, semi-realistic face, animations, audio, Urdu/English text, save/resume and one replay. | End-to-end sample match completes on target device; 3D and Lite result parity verified. |
| MVP / 1.0 candidate | Quick Match T20, Practice/Nets, core fielding and dismissals, appeal, wides/no-balls, scorecard, small tournament, team/player selection, basic editor, settings and offline save. | Critical match/rule/save tests pass; target device matrix and store/legal checks signed off. |
| Free feature updates | ODI, Test, The Hundred, expanded career, deeper DRS/replay, more venues/teams, local and online multiplayer, leaderboard and challenges. | Each feature passes its own rules, performance, security and UX acceptance tests. |

**All planned features** remain part of the free product vision. Staging controls build order only; it does not imply paid or permanent feature locks.

## 5. Functional requirements

| **ID** | **Area** | **Requirement** | **Priority** |
| --- | --- | --- | --- |
| PR-001 | Launch and navigation | The app shall launch to a home menu with Quick Match, Tournament, Career, Practice, Challenges, Multiplayer and Settings entry points as those features become available. | P0 |
| PR-002 | Match setup | The user shall select format, teams, overs, difficulty, venue, pitch, weather, day/night and rules preset where supported. | P0 |
| PR-003 | Toss and team setup | The match shall support toss choice, batting/bowling decision, Playing XI, captain, wicketkeeper and batting order. | P0 |
| PR-004 | Batting controls | Provide Swipe, Buttons and Joystick + Button schemes with configurable size, position, sensitivity and left-hand layout. | P0 |
| PR-005 | Batting simulation | Support timing (Perfect/Good/Early/Late), footwork, defence, loft/power, edges, mis-hits, running calls and right/left-hand stances. | P0 |
| PR-006 | Shot library | Support cover drive, cuts, straight drive, defence, pull, hook, sweep, reverse sweep, flick, glance, scoop, helicopter-style, switch hit, upper cut and ramp as animation/simulation scope allows. | P1 |
| PR-007 | Bowling | Support fast, medium, off-spin, leg-spin and left-arm spin archetypes, line/length target, speed limits by attributes, run-up/action, swing/seam/spin and fatigue. | P0 |
| PR-008 | Fielding and keeping | Support preset/manual field placement, AI chase, dive/slide/jump, catch/drop, throws, direct hit, overthrow, boundary save, keeper catch/stumping/run-out. | P0 |
| PR-009 | Captaincy | Support bowling/field changes, toss decision and tactical presets; substitutes/impact player only when selected ruleset supports it. | P1 |
| PR-010 | Rules and umpire | Use centralized rules for scoring, extras, dismissals, appeals, umpire signals, powerplay, free hit, boundaries and tie resolution. | P0 |
| PR-011 | DRS and third umpire | Provide configured review allowance, ball-tracking view and replay angles for supported review decisions. | P2 |
| PR-012 | Formats | Plan T5/T10, T20, ODI, Test, The Hundred and Custom. Release formats by phase. | P0/P1 |
| PR-013 | Game modes | Support Quick Match, tournament/league, series, career, challenges, practice/nets, Super Over/knockout, daily challenge, hot-seat and match simulation by phase. | P0/P1/P2 |
| PR-014 | Visual profiles | Provide 3D and Lite modes that share game-state and simulation; visual changes must not change match outcomes. | P0 |
| PR-015 | Player/team editor | Allow player/team names, ratings, batting hand, role, face preset, skin tone, hair/beard, kit colors and equipment customization. | P1 |
| PR-016 | Score and history | Show live score, overs, rate, target, partnership, recent balls, full scorecard, extras, fall of wickets, career records and saved history. | P0 |
| PR-017 | Commentary and sound | Provide Urdu/English text commentary at launch; audio commentary can expand with original/licensed recordings. Independent audio controls. | P1 |
| PR-018 | Offline and save | No mandatory login; autosave match at safe transitions; support local career, custom teams and history. | P0 |
| PR-019 | Online services | Later phases may add account, cloud save, local network/online 1v1, tournaments, leaderboard, friends, preset chat, report/block and anti-cheat. | P2 |
| PR-020 | Ads and monetization | No ad during a delivery, review or active match. Optional menu or post-result ads only; no paid gameplay unlocks. | P1 |

## 6. Cricket rules and content requirements

- **Rule presets:** Store format/competition-specific rules for wides, no-balls, free hits, powerplays, review counts, boundary and tie rules, The Hundred ball counting, follow-on and rain interruptions.

- **Dismissals:** Core set includes bowled, caught, LBW, run out, stumped, hit wicket, caught behind and obstructing the field; additional cases are enabled only where applicable.

- **2026 law note:** The source plan’s separate “Handled the ball” entry is not to be implemented as a standalone dismissal. Map hand-contact/obstruction to the applicable current Law; verify against the 2026 MCC Laws and selected playing conditions.

- **Rain target:** Use the official DLS name only when the approved calculation method is verified/authorized. Otherwise label the in-game feature “Rain-revised target.”

- **Original content:** Do not preload real team/player names, faces, league logos, kits, stadium trademarks, music or commentary without required rights.

## 7. Non-functional requirements and acceptance targets

| **ID** | **Requirement** | **Initial acceptance target** |
| --- | --- | --- |
| NFR-01 | Performance | Lite: target stable 30 FPS in normal match scenes on selected 4GB devices. Balanced 3D: target stable 30 FPS; 60 FPS only on profiled capable devices. |
| NFR-02 | Stability | No repeatable crash or out-of-memory issue in a 30-minute normal-match test on the agreed 4GB baseline matrix. |
| NFR-03 | Fairness | Same inputs/seed and state must produce the same cricket result across graphics profiles; graphics quality must not affect ratings or physics. |
| NFR-04 | Persistence | Saved match resumes at a valid ball/over boundary without duplicating runs or wickets. |
| NFR-05 | Offline availability | Core match, practice and local save must be available without account or network. |
| NFR-06 | Localization/accessibility | Urdu and English; scalable text, subtitles, color-blind-safe indicators, left-hand controls and large touch targets. |
| NFR-07 | Privacy/security | Collect only required analytics; protect account/cloud features; validate online competitive results server-side. |
| NFR-08 | Thermal/battery | Measure heat, battery and performance during long sessions; provide 30 FPS Lite/battery option. |

## 8. Out of scope for initial MVP

- Full 15-25 stadium catalogue and 16-20 teams on day one; launch with a small polished set and add free content packs.

- Robust online tournaments, friends/chat moderation and server-authoritative anti-cheat before core offline play is stable.

- Photorealistic scanned likenesses of real cricketers; initial art direction is semi-realistic fictional faces.

- Guaranteed compatibility with every phone labelled 4GB RAM; publish only device combinations that pass physical tests.

## 9. Product risks and mitigations

| **Risk** | **Mitigation** |
| --- | --- |
| Scope overload | Prove a one-over loop first; gate advanced modes, full roster and online systems. |
| Unconvincing faces/animation | Build one hero player early and inspect at normal gameplay distance before scaling roster. |
| 4GB performance | URP tiers, LOD, pooling, texture compression, baked lighting and physical profiling. |
| Rules defects | Central rules engine, versioned presets and regression test suite. |
| Online cheating/desync | Delay online launch; define authority and validation model before production. |
| IP/store takedown | Original content by default; maintain licenses/permissions and store compliance checklist. |

## Source and decision notes

- Primary source A: Cricket Game (Android) - Poora Feature Plan, 11 pages, dated 26 October 2026.

- Primary source B: Real Cricket Pro - Updated Development Plan, version 2.0, 18 pages, dated 26 October 2026.

- The agreed owner choices used throughout: game name Real Cricket Pro; both graphics modes; 4GB RAM baseline; semi-realistic fictional faces.

- Where a backend vendor, detailed visual style, exact device list, commercial policy, or admin workflow is not decided in the source plans, this pack labels the item as a recommendation or open decision.

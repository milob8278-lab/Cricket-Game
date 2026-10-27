**REAL CRICKET PRO**

# UI/UX Design Brief

Visual direction, screen system, gameplay HUD, controls and accessibility

| **Document ID** | UX-004 |
| --- | --- |
| **Version** | 1.0 - Working Draft |
| **Prepared** | 26 October 2026 |
| **Product baseline** | Android \| Unity 6 LTS family + URP \| C# |
| **Device target** | 4GB RAM baseline; 6GB/8GB+ scalable tiers |
| **Graphics** | Semi-realistic 3D + 2D Lite / 2.5D presentation |
| **Operating principle** | Offline-first \| Urdu + English \| free core gameplay |

**Document purpose** Guides product designers and artists in producing consistent, readable game interfaces and matching the two graphics profiles.

> Source basis: “Cricket Game (Android) – Poora Feature Plan” (11 pages) and “Real Cricket Pro | Updated Development Plan” (18 pages). This document converts the agreed product direction into a specialist project artifact. Proposed details are marked where source plans did not decide them.

## 1. Design objective

Create a modern cricket broadcast experience that makes the ball, score and player actions easy to read on a phone. Semi-realistic fictional characters and stadium presentation should feel lively, but interface clarity and touch response take priority over decoration. The same interface system must work over both full 3D and Lite / 2.5D scenes.

## 2. Art direction

| **Element** | **Direction** | **Avoid** |
| --- | --- | --- |
| Player faces | Semi-realistic human proportions, expressive eyes, natural skin shading, hairstyle/beard variety and readable silhouettes. | Photorealistic scans of real players or uncanny high-detail faces not readable at gameplay distance. |
| 3D mode | Broadcast-style stadium, controlled lighting, optimized crowd clusters and purposeful replay camera. | Unnecessary post-processing, excessive particles or dark scenes that hide the ball. |
| Lite mode | 2D-style / simplified 2.5D presentation, simplified faces and crowd cards/sprites; preserve ball and fielder readability. | A mode that changes physics, hit timing, AI or match outcomes. |
| UI | Broadcast scoreboard language, strong hierarchy, clear button states, minimal clutter. | Tiny labels, low contrast, controls over the ball or critical play area. |
| Motion | Short purposeful transitions and readable celebrations/replays. | Long unskippable animations that lock user input. |

## 3. Recommended visual system

Suggested starting palette (not a locked brand decision): deep navy for broadcast surfaces, off-white for primary information, green for active cricket states, warm gold for milestones and neutral grey for secondary controls. Use color with text/icons; never communicate wicket, review or score state by color alone.

| **Token** | **Suggested use** | **Example value** |
| --- | --- | --- |
| Brand / primary | Header, navigation, selected tabs | Deep navy #14283F |
| Accent / cricket | Active selection, confirmation, positive state | Green #27866B |
| Secondary accent | Milestone, highlight, player of match | Warm gold #D7A84B |
| Surface | Cards, panels, score overlays | Pale #F3F7F9 |
| Text primary | Score, heading, important labels | Dark #23313D |
| Text secondary | Helper text, metadata | Grey #61717D |

> **Brand decision** These are UI direction suggestions only. Final logo, typeface, kit palette and visual identity require owner approval.

## 4. Navigation and screen hierarchy

| **Navigation level** | **Screens** | **Design priority** |
| --- | --- | --- |
| Primary home | Quick Match, Tournament, Career, Practice, Challenges, Multiplayer, Settings. | Quick access to the match loop; visually distinguish offline-ready from online-only features. |
| Match setup | Format, teams, venue/pitch/weather, difficulty, rules, Playing XI. | Chunk choices into short steps; remember last selections. |
| Live match | Broadcast HUD, action controls, field placement, appeal/review, pause. | Maximum ball visibility and reachable touch controls. |
| Between innings/result | Target, summary, scorecard, replay/highlights, continue/rematch. | Celebrate result without hiding detailed statistics. |
| Customization | Player face editor, team editor, kits/equipment. | Instant visual preview; simple presets first, advanced controls optional. |
| Settings | Graphics/FPS, controls, sound, language, accessibility, battery saver. | Explain performance impact; allow manual override of auto recommendation. |

## 5. Live match HUD layout

```text
TOP ZONE      Team / score / wickets / overs / target / run rate / required rate
TOP SECONDARY Last 6 balls / partnership / batter and bowler mini stats

CENTER        Live play area - ball, stumps, batter, fielders and crease always visible

LEFT / EDGE   Optional field map, line-length indicator or tactical controls
RIGHT / LOWER Batting: shot direction / defend / normal / loft / power / run calls
              Bowling: delivery type / line-length target / pace-spin control

BOTTOM        Pause, appeal/review when eligible, commentary toggle / short feedback
```

Exact placement must be tuned on common Android aspect ratios and both one-hand/two-hand use. Keep primary actions within thumb reach while avoiding overlap with the ball flight and batter contact area.

## 6. Control design

| **Control scheme** | **UX behavior** | **Design notes** |
| --- | --- | --- |
| Swipe | Swipe direction selects shot direction; timing/length can influence aggression/loft according to preset. | Show a short directional guide in tutorial; avoid false gestures from accidental touch. |
| Buttons | Defend, Normal, Loft and Power; direction via zones or directional buttons. | Large touch targets, pressed/disabled states and haptic feedback option. |
| Joystick + button | Left virtual stick sets direction/movement; right controls trigger action. | Adjustable dead zone/sensitivity; right/left-hand layouts. |
| Bowling target | Tap/drag target area for line and length; select delivery type and control power/variation. | Clear target circle; avoid too many simultaneous meters. |
| Fielding | AI chase default; optional manual fielder switch and action button. | Show selected fielder and throw target; simple assisted mode first. |

## 7. Player face and customization UX

- Start with curated semi-realistic face presets that look believable at normal broadcast camera distance.

- Offer face shape, jaw, cheeks, chin, forehead, eye/brow, nose, lips, ears and skin tone using a limited number of understandable controls.

- Hair, beard and moustache presets should be easy to preview; helmet/cap/sunglasses must fit without clipping.

- Provide expressions such as neutral, focused, happy, angry, disappointed and celebration; keep subtle during normal play.

- Use “Randomize”, “Reset”, “Save player” and clear unsaved-change prompts.

- Represent fictional identities; do not imply real-player likeness rights or preload unlicensed player imagery.

## 8. Graphics settings UX

| **Mode** | **User-facing label** | **Suggested description** |
| --- | --- | --- |
| Lite | 2D Lite | Best performance / simplified visuals / target 30 FPS. |
| Balanced | Balanced 3D | Semi-realistic 3D / optimized effects / target stable 30 FPS. |
| High | High 3D | More detail and effects / best on supported devices / target 60 FPS only if tested. |

On first launch, recommend a mode based on device profiling but let users override. Explain that graphics modes change visuals only, never batting timing, ball physics, player ratings or match fairness. Switching during a match should be allowed only from safe pause/menu state.

## 9. Accessibility, localization and audio

- Urdu and English at launch. Urdu screens must support right-to-left direction, suitable font shaping, mirrored alignment where appropriate and careful mixed Urdu/number formatting.

- Large text, high contrast, color-blind-friendly indicators, subtitles for commentary/tutorial, reduced motion and adjustable audio channels.

- Independent controls for music, commentary, crowd, bat/ball effects and UI sounds.

- Use consistent icon plus label for critical actions; support remappable controls and left-hand mode.

- Do not rely only on color, sound or motion to communicate a wicket, free hit, review eligibility or result.

## 10. Motion, feedback and camera

| **Moment** | **Feedback** | **UX rule** |
| --- | --- | --- |
| Ball release/contact | Small visual/audio cue; timing feedback after shot. | Never obscure ball or delay control response. |
| Four/six/wicket | Short celebration, crowd response, umpire signal and optional replay. | Allow skip/disable auto-replay; preserve match pace. |
| Review | Clear timer, review count, trajectory and decision. | Show evidence views in understandable stages; no false precision claim. |
| Result | Scoreboard, player of match, highlights and records. | Provide share/export after result; permissions requested only when needed. |
| Pause/settings | Readable overlay with Resume, Controls, Graphics, Sound and Quit. | Do not allow accidental quit; confirm save behavior. |

## 11. UI component library / handoff

- Define components for score pill, over/ball indicator, player card, team badge, selected state, button, slider, modal, toast, tabs, list row, toggle, loading, offline banner and error state.

- Provide design tokens for spacing, type scale, touch target size, corner radius, elevation, color and animation timing.

- Deliver Figma pages for flows, screen layouts, interactive prototypes, responsive breakpoints, Lite/3D states and Urdu/English variants.

- Attach behavior notes: hit area, disabled states, loading, validation, back behavior, save confirmation and accessibility labels.

- Provide exportable asset inventory with source/license field and target resolution/compression guidance.

## 12. UX acceptance checklist

- A new player can start Quick Match and understand batting controls through the tutorial.

- Score, wickets, overs, striker, bowler and ball remain readable on supported device aspect ratios.

- Graphics switching does not change input response or simulation outcomes.

- Every primary action has visible pressed, disabled, loading and error states.

- Urdu RTL layout is reviewed by a fluent reader; no clipped glyphs or broken number order.

- Large text, color-blind indicators, left-hand layout and subtitles are checked on-device.

## Source and decision notes

- Primary source A: Cricket Game (Android) - Poora Feature Plan, 11 pages, dated 26 October 2026.

- Primary source B: Real Cricket Pro - Updated Development Plan, version 2.0, 18 pages, dated 26 October 2026.

- The agreed owner choices used throughout: game name Real Cricket Pro; both graphics modes; 4GB RAM baseline; semi-realistic fictional faces.

- Where a backend vendor, detailed visual style, exact device list, commercial policy, or admin workflow is not decided in the source plans, this pack labels the item as a recommendation or open decision.

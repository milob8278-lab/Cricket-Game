# REAL CRICKET PRO — Full-Screen Touch UI & 3D Animation Specification

| Document | Value |
|---|---|
| Document ID | RCP-UI-008 |
| Version | 1.0 — Interaction and animation specification |
| Orientation | Landscape-first; immersive full-screen |
| Input | Multi-touch; Swipe / Buttons / Joystick + Buttons |
| Visual modes | 3D Lite, Balanced 3D, High 3D — active players remain 3D in all modes |

## 1. Full-screen experience

### 1.1 Display behavior

- Match screens use landscape orientation and immersive full-screen presentation.
- Hide Android status/navigation bars during active gameplay where the Android version and system policy allow it.
- Background art and stadium rendering use the full available display area.
- Important UI controls, text and score data remain inside `Screen.safeArea`; do not place a required action under a camera cutout or gesture area.
- Handle different aspect ratios, rounded corners, punch-hole cameras, notches and tablets.
- If a system bar reappears after an OS gesture, the HUD must not permanently shift or become unusable.
- Use optimized frame pacing and profile on physical devices.

### 1.2 Orientation policy

Recommended: keep the full game in landscape to preserve consistent cricket camera framing and avoid rotating the field/HUD between menus and play. If portrait menus are later chosen, the transition must be explicit and must never rotate during a live delivery.

### 1.3 Global HUD rules

- The ball, batter/bowler, crease and relevant fielder remain visually clear.
- Controls are semi-transparent but become more visible while touched.
- Avoid placing a button over the ball's expected flight path or the batter's hands/bat contact point.
- Critical actions have distinct shapes/icons and optional text labels.
- Touch target size scales with screen density; small icons must not be the only way to perform a critical action.
- Support at least two simultaneous touch points; the input layer must resolve touch ownership so a drag on the joystick does not trigger a nearby button.
- Provide left-handed layout, resize, reposition, opacity, sensitivity and reset.

## 2. Live match HUD layout

The following is a logical layout, not a fixed pixel mockup. The HUD must adapt to aspect ratio and safe area.

```text
┌─────────────────────────────────────────────────────────────────────┐
│ [PAUSE] [TEAM  126/3  15.2] [CRR 8.21] [TARGET 178 / RRR 9.75] [DRS] │
│                                                                     │
│                                                                     │
│                        LIVE 3D CRICKET FIELD                        │
│                bowler → pitch → batter → fielders                   │
│                                                                     │
│                                                                     │
│ [BATTER / BOWLER INFO]                               [SHOT / ACTION] │
│ [FOOTWORK / INTENT]                                   [POWER / LOFT] │
│ [RUN: YES / NO / WAIT]                                [DEFEND]       │
└─────────────────────────────────────────────────────────────────────┘
```

### 2.1 Top information bar

- Pause/settings icon at the left.
- Team score, wickets and overs.
- Current run rate.
- Target and required run rate in a chase.
- Compact striker/non-striker and bowler summary may open on tap.
- Review/DRS indicator appears only when available and relevant.
- Recent-ball strip can slide down/expand after the delivery, not cover live action.

### 2.2 Lower information/control band

- Batter identity and current statistics on one side.
- Bowler type/figures and delivery feedback in a compact panel.
- Footwork/intent controls near the left thumb zone (configurable).
- Shot direction/action controls near the right thumb zone (configurable).
- Running calls remain easy to reach and visually separate from shot controls.

## 3. Batting touch controls

### 3.1 Control preset A — Swipe

- User swipes toward the intended shot direction.
- Swipe angle chooses a direction sector; swipe timing determines timing quality.
- Optional short/long swipe maps to controlled/lofted intent only when enabled in settings.
- A separate defend button remains available; defence must not require a precise swipe.
- A cancel/leave action appears for deliveries that can be left.

### 3.2 Control preset B — Buttons

Primary on-screen buttons:

- **DEFEND** — play a defensive stroke/block.
- **NORMAL** — play a controlled scoring shot.
- **LOFT** — attempt an aerial shot.
- **POWER** — aggressive, higher-risk shot.
- **SHOT MAP** — opens directional selector or configured favorite zones.
- **FOOTWORK** — Front / Back / Advance / Stay (compact toggle or radial selector).
- **RUN** — YES / NO / WAIT call cluster, shown after contact or when a run is possible.
- **SPECIAL** — opens context-valid special shot options; hide/disable impossible selections based on line/length and stance.

The buttons must not all fire on a simple tap if the batter is still in a locked animation. Input timing windows and buffering must be tuned and communicated.

### 3.3 Control preset C — Joystick + Button

- Left virtual stick controls shot direction or manual fielder movement depending on current phase.
- Right-side action buttons trigger defend/normal/loft/power.
- Optional modifiers select front-foot/back-foot or aggressive/defensive intent.
- Joystick dead zone, sensitivity, response curve and button positions are adjustable.

### 3.4 Shot-map interaction

- Tap SHOT MAP to open a compact 360-degree direction guide around the batter.
- Select a target zone (fine leg, square leg, midwicket, straight, mid-off, cover, point, third man, etc.).
- Confirm preferred shot family if multiple are valid.
- Show skill/risk feedback (for example, “high-risk aerial zone”) without promising the result.
- Let users save a small number of named personal presets locally.
- Close/cancel without committing a shot.

### 3.5 Multi-touch examples

- Hold movement/joystick while tapping action button.
- Swipe shot direction while another finger can choose a permitted run call after contact.
- Drag a control only while in control-layout editor; never during live play unless explicitly supported.
- A touch that starts on one control must remain owned by that control until release/cancel.

## 4. Bowling touch controls

### 4.1 Bowling setup HUD

- Delivery-type selector: Fast, Swing, Seam, Yorker, Bouncer, Slower, Off Spin, Leg Spin and supported variants.
- Pitch target: drag a landing marker over a line/length grid.
- Speed/effort slider bounded by bowler stats and delivery type.
- Swing/spin amount and direction control for supported deliveries.
- Run-up/release button with timing meter.
- Optional quick presets: Yorker, Good Length, Short Ball, Defensive Line and Attacking Line.

### 4.2 Release process

1. Bowler enters set position.
2. Player chooses line/length and delivery.
3. Run-up animation begins.
4. Release zone appears at the correct moment.
5. User taps/releases within the timing window.
6. Accuracy is calculated using timing, bowler control, fatigue and delivery parameters.
7. Bowler follow-through animation completes.

A speed slider must not imply unlimited pace; a bowler with low pace rating cannot throw 150+ km/h merely by dragging to maximum.

## 5. Fielding touch controls

### 5.1 Auto fielding

Default for casual players: AI selects the likely fielder, chases/intercepts and performs routine actions. User can choose throw target or take over for a close chance.

### 5.2 Manual fielding

- Left virtual stick: move selected fielder.
- Sprint button: accelerate; stamina and angle affect result.
- Dive/slide button: context-sensitive dive or slide.
- Jump button: high catch/boundary attempt.
- Pickup/throw button: gather then select target.
- Throw target indicators: striker's end, non-striker's end or wicketkeeper.
- Direct-hit timing/accuracy feedback.
- Switch fielder button or auto-switch preference.

### 5.3 Wicketkeeper controls

- Auto mode by default.
- Optional manual take/stumping/throw controls for advanced users.
- Keeper action must not require several buttons in a fraction of a second on small screens; use context-sensitive actions.

## 6. Match pause, innings break and result screens

### Pause menu

- Resume.
- Controls layout/settings.
- Graphics quality (apply only at safe pause state).
- Camera selection.
- Sound and commentary volume.
- Match controls/tutorial help.
- Save and exit (confirm; explain last safe save point).
- Restart match (confirm separately).

### Innings break

- First-innings score and full mini-scorecard.
- Target and required rate.
- Partnership/top scorers/top bowlers.
- Change Playing XI only if permitted by the selected rules.
- Set bowling plan/field preset.
- Continue to next innings.

### Result

- Winner/result summary.
- Match-winning presentation.
- Player of the Match.
- Full scorecard and fall of wickets.
- Wagon wheel/pitch map/Manhattan chart.
- Highlights and replay entry.
- Save scorecard/share image.
- Rematch, new match or return home.

## 7. Touch layout editor

The user can choose a preset layout or enter edit mode.

- Drag a button to reposition it.
- Resize with a handle/slider.
- Adjust opacity.
- Adjust joystick size and dead zone.
- Choose left-handed or right-handed layout.
- Preview in batting, bowling and fielding states.
- Save multiple control profiles (recommended: Casual, Advanced, Custom).
- Reset current layout or reset all controls.
- Prevent overlapping critical actions; warn when two controls collide.
- Provide a default restore option if a layout makes the game difficult to control.

## 8. 3D player model and rig requirements

### 8.1 Active player quality

- Active batter, bowler, wicketkeeper and nearby fielders use 3D models in every graphics profile.
- Balanced/High use more detailed body/head meshes and facial materials.
- Lite uses lower-poly LOD meshes, lower-resolution textures and reduced facial/bone updates; it remains three-dimensional.
- Distant crowd may use impostors/cards or reduced-detail representations; active gameplay characters may not.

### 8.2 Character rig

- Reusable humanoid skeleton with consistent retargeting.
- Hands/wrists and bat use IK for grip/contact alignment.
- Feet use ground alignment/IK for bowling crease and batting stance.
- Equipment attachments: helmet, gloves, pads, shoes, bat, cap and sunglasses.
- Face controls: eyes, brows, jaw/mouth and expression blend shapes/presets.
- LOD rig reduces unnecessary bones/updates for distant characters.

### 8.3 Animation state groups

**Batting states:** idle/ready, guard, trigger, front-foot, back-foot, defence, drives, cuts, pull/hook, sweep/reverse sweep, scoop/ramp, switch hit, contact, follow-through, beaten, edge, leave, run-start, sprint, turn, dive, ground bat, appeal reaction, milestone, celebration and dismissal.

**Bowling states:** mark, set, run-up, gather, jump/bound, delivery stride, arm acceleration, release, follow-through, appeal, celebration and recovery. Build distinct action families for fast, medium, off-spin, leg-spin and left-arm spin.

**Fielding states:** ready, first move, sprint, stop, pickup, catch, one-hand catch, high catch, dive, slide, jump, throw, relay, boundary save, dropped catch reaction and celebration.

**Keeper states:** crouch, receive, take, dive, collect, stumping, remove bails, throw and appeal.

**Umpire states:** stand, walk, signal four/six/out/not-out/wide/no-ball/dead-ball, consult third umpire and TV/review signal.

**Face states:** blink, look-at, focused, confident, nervous, happy, angry, disappointed, pain and celebration. Use restrained facial animation during normal gameplay to avoid distracting the user.

### 8.4 Motion quality

- Use motion capture for hero batting/bowling/fielding actions if budget and performer rights allow.
- Use keyframed/procedural variations for broad shot coverage and transitions.
- Synchronize hit/contact animation to physics contact event.
- Avoid floating feet, knee snapping, bat-through-body, equipment clipping and abrupt state changes.
- Blend transitions based on player velocity and current action, not only animation name.
- Test at gameplay distance and replay close-up distance.

## 9. Haptics, audio and feedback

- Light haptic on button confirmation; stronger short pulse for bat contact, wicket, catch or boundary where user enables it.
- Separate sound channels: commentary, crowd, bat/ball, stumps, ambience, UI and music.
- Do not vibrate constantly or during every frame; allow full haptics-off setting.
- Visual feedback complements audio: timing ring, contact indicator, run call status, target marker and umpire signal.
- Color is never the only signal; use icon/shape/text.

## 10. Device/performance design rules

- 4GB RAM baseline: 3D Lite profile targets stable 30 FPS in normal match scenes; validate on physical phones.
- Balanced 3D targets stable 30 FPS; allow 60 FPS only on proven devices.
- High 3D only appears as selectable when the device passes capability checks or user explicitly chooses manual override.
- Reduce crowd animation, shadows, render scale, post-processing and particle count before reducing ball/player readability.
- Use LODs, object pooling, texture compression, baked stadium lighting and profiler-driven tuning.
- Use a short loading screen and stream nonessential content carefully; essential offline assets must be local.
- Profile heat and battery during a 30-minute match, not only a one-minute test.

## 11. Interaction acceptance tests

- [ ] Full-screen landscape match display works on devices with notches/punch holes and different aspect ratios.
- [ ] Safe-area margins keep all critical controls visible.
- [ ] Every batting shot can be triggered using the chosen control scheme.
- [ ] Controls support simultaneous touch without accidental neighboring inputs.
- [ ] Button layout can be customized, saved and reset.
- [ ] Bowling target can be dragged accurately and released.
- [ ] A fielder can be manually moved, sprinted, dived and made to throw without UI collision.
- [ ] Pause/resume does not lose match state.
- [ ] HUD remains legible over day, night, rain and replay scenes.
- [ ] Active player models are 3D in Lite, Balanced and High modes.
- [ ] Ball release/contact/catch/throw animation events match the simulation events.
- [ ] A 4GB device completes an extended Lite-mode playtest without repeated crash or memory failure.

## 12. Engineering reference links

- [Unity Android input](https://docs.unity.com/en-us/engine/6000.3/manual/platform-specific/android/developing/input)
- [Unity immersive mode and Android window insets](https://docs.unity.com/en-us/engine/7000.0/manual/platform-specific/android/developing/edge-to-edge-rendering/window-insets)
- [Unity Screen.safeArea](https://docs.unity.com/en-us/engine/6000.7/script-reference/unityengine/screen/safearea)
- [Unity URP performance guidance](https://docs.unity.cn/6000.1/Documentation/Manual/urp/configure-for-better-performance.html)
- [Real Cricket official feature reference](https://krafton.in/games/real-cricket/) — feature benchmark only; do not copy protected UI, assets or content.

---

**Design rule:** It should feel like a full-screen cricket broadcast controlled by two thumbs: the pitch and ball remain the visual focus, the buttons stay predictable, and the 3D players look and move naturally without sacrificing response time.

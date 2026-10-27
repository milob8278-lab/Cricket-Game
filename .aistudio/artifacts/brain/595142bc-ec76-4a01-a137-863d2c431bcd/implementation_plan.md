# Real Cricket Pro — 3D Online Rebuild Plan

Real Cricket Pro is transitioning from a 2D canvas view to a true **3D Real-Time Android Cricket Game** featuring perspective broadcast cameras, full 3D oval stadiums, 3D animated humanoid players (batsman, bowler, keeper, fielders), millisecond-accurate physics, and online 1v1 multiplayer rooms synchronized via Firebase.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> **Confirmed Architectural Choices:**
> - **Platform:** Native Android 3D with perspective rendering pipeline and Firebase real-time state synchronization.
> - **Milestone Execution:** End-to-end full delivery covering Milestone A (3D gameplay, stadium, physics), Milestone B (Online 1v1 room & live sync), and Milestone C/D (Full match loop, broadcast HUD, tournament, and career progression).
> - **Zero 2D Fallback:** Pitch, ball trajectory, and players will be rendered in authentic 3D perspective with realistic depth, lighting, and animation state transitions.

---

### 1. Overview & Core Concept

- **What It Does:** Delivers an immersive, broadcast-quality cricket match simulation with multiple camera views (Broadcast, Behind-Bowler, Batting POV, Stump Cam), authentic batting footwork, real seam/swing/spin ball physics, dynamic fielding, and online 1v1 lobbies where two players compete in real time.
- **Target Audience:** Cricket enthusiasts, competitive mobile gamers, and fans of franchise leagues seeking console-like cricket mechanics on mobile devices.
- **Key Value:** True 3D depth and authentic ball-to-bat collision physics combined with smooth 30–60 FPS mobile performance on Android devices, complete with both online multiplayer and offline career/practice modes.

---

### 2. User Experience & Visual Design

#### Visual Identity & Theme
- **Theme:** Stadium Night Floodlit Atmosphere — Deep Navy (`#0A192F`), Turf Green (`#15803D`), Gold Milestone Accents (`#F59E0B`), and crisp White typography (`#F8FAFC`).
- **Camera Presentation:**
  - **Broadcast TV Camera (Default):** Elevated perspective tracking bowler run-up, delivery arc, pitch bounce, and outfield ball chase.
  - **Batting Close-Up POV:** High-immersion angle behind batsman for precise footwork and timing control.
  - **Stump-Vision & Replay:** Low-angle trajectory tracking with Ultra-Edge audio waveform and 3D DRS ball-tracking.
- **Player Models & Animations:**
  - 3D Humanoid mesh anatomy: Detailed jersey, pads, batting gloves, helmet faceguard grill, and spiked shoes.
  - State machine animations: Stance, backlift, front-foot drive, pull shot, cut, helicopter shot, bowler run-up, delivery gather, leap, release follow-through, wicketkeeper crouch, fielder dive and throw.

#### Key User Flows & Touch HUD (Landscape)
- **Batting Controls:** Left thumb for 360° shot placement and footwork (Front-foot / Back-foot); Right thumb for shot intent (Defend, Ground, Loft, Power) and timing tap.
- **Bowling Controls:** Drag-to-target pitch landing marker, swing/spin arc slider, speed gauge, and delivery release timing.
- **Running & Fielding:** Big, accessible "Run / Wait / Turn Back" buttons with non-striker tracking and haptic vibration feedback.
- **Online Lobby & Matchmaking:** Room code generator, 1v1 matchmaking queue, ready status, coin toss, live scoreboard sync, and post-match scorecard.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Native 3D Rendering Engine in Compose**
  - *Chosen Approach:* Perspective 3D Graphics Pipeline with custom 3D vector transformations, depth-sorting, shaded polygons, and texture mapping on hardware-accelerated Compose Canvas + SurfaceView.
  - *Why:* Guarantees seamless compatibility with the Android runtime and device emulators while rendering real 3D geometry without requiring bulky external native game engines.
  - *Alternative Considered:* Web Three.js wrapper inside WebView (discarded due to input latency and touch control lag on Android).

- **Decision 2: Online Multiplayer Architecture (Firebase Firestore & Realtime Sync)**
  - *Chosen Approach:* Server-authoritative Room & Match state model in Firestore with real-time snapshot listeners (`addSnapshotListener`) for delivery intent, ball trajectory events, and score validation.
  - *Why:* Delivers robust cross-device synchronization, room matchmaking, automatic reconnects, and zero-trust scorecard integrity.

- **Decision 3: Device Optimization (4GB RAM Target)**
  - *Chosen Approach:* Adaptive level of detail (LOD) with 3 visual profiles: **High 3D** (full stadium geometry, floodlight shadows, particle dust), **Balanced 3D**, and **3D Lite** (low-poly stadium geometry, baked grass lighting, optimized crowd instances) ensuring smooth 30–60 FPS.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Real Cricket Pro App                             │
├───────────────────────────────────┬────────────────────────────────────┤
│           UI & HUD Layer          │           3D World Engine          │
│  - Landscape Edge-to-Edge HUD     │  - 3D Oval Stadium & Grass Mesh    │
│  - 360° Batting Shot Map          │  - Clay Pitch, Stumps & Creases    │
│  - Bowling Target & Speed Gauges  │  - Rigged Humanoid Cricketer Mesh  │
│  - Broadcast Ribbon & Scorecard   │  - Dynamic 3D Camera Director      │
│  - DRS / Ultra-Edge Visualizer    │  - Real-Time Ball Physics & Bounce │
├───────────────────────────────────┴────────────────────────────────────┤
│                       Game Simulation Core                             │
│  - CricketPhysicsEngine (22+ Shots, Seam/Swing, Timing, Contact)       │
│  - Match State Manager (Innings, Fall of Wickets, Overs, DRS)          │
│  - Audio Engine (Bat Crack, Ball Bounce, Crowd Cheer, Sound Track)     │
├────────────────────────────────────────────────────────────────────────┤
│                     Online Multiplayer & Cloud Layer                   │
│  - Firebase Firestore Room Sync (`/match_rooms/{roomId}`)             │
│  - Authoritative Delivery & Outcome Validation                         │
│  - Online Matchmaking, Tournaments, Career Cloud Save                  │
└────────────────────────────────────────────────────────────────────────┘
```

#### Step-by-Step Implementation Sequence

1. **Phase 1: 3D Geometry & Perspective Engine (`Cricket3DScene.kt`)**
   - Implement authentic 3D projection matrix with Camera Director (Broadcast, Batting, Bowler, Side-on).
   - Build 3D stadium oval, tiered stands, boundary rope, LED boards, clay pitch strip, popping crease, and 3D wickets with flying bails.
2. **Phase 2: Rigged 3D Humanoid Player Models & Animation State Machine**
   - Batsman 3D skeleton with stance, backlift, shot swing animations (cover drive, pull, cut, sweep, lofted six).
   - Bowler 3D skeleton with run-up, mid-air gather, release stride, and follow-through.
   - Wicketkeeper crouch and diving fielder meshes.
3. **Phase 3: 3D Ball Flight, Bat-Ball Collision & Dynamic Camera Tracking**
   - Parabolic trajectory calculations with real-time air drag, Magnus effect swing, and pitch bounce restitution.
   - Smooth camera switching: Bowler delivery -> Bat impact -> Outfield ball chase -> Umpire call.
4. **Phase 4: Online 1v1 Multiplayer Lobby & Live Synchronization**
   - Room creation with 6-character room codes and quick-match queue.
   - Delivery intent exchange (bowler sets line/length -> batsman selects shot/timing -> outcome synchronized).
   - Reconnection grace period and disconnect handling.
5. **Phase 5: Full Game Modes, Broadcast Polish & Unit Testing**
   - Quick Match, Tournament, Career Mode, Practice Nets, Scenario Challenges.
   - Audio feedback, bilingual commentary (Urdu + English), and Robolectric test verification.

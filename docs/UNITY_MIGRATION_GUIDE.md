# REAL CRICKET PRO – UNITY 3D MIGRATION ARCHITECTURE & BLUEPRINT (UaaL)

This document provides the definitive, production-ready engineering blueprint for integrating **Unity 3D** into the **Real Cricket Pro** Android project via **Unity as a Library (UaaL)**.

---

## 1. Executive Summary & Architecture Overview

Real Cricket Pro uses a dual-engine architecture:
- **Presentation & Navigation Layer:** Android Jetpack Compose (M3) handles broadcast menus, player/team customizers, settings, leaderboards, and Supabase cloud operations.
- **3D Gameplay Simulation Layer:** Unity 6 LTS (URP) embedded as an Android Library (`unityLibrary`) rendering high-fidelity 3D stadiums, realistic rigged cricketers, physics, dynamic shadows, and camera kinematics.

```
┌─────────────────────────────────────────────────────────────┐
│                 Real Cricket Pro (Android)                  │
│                                                             │
│   ┌─────────────────────────────────────────────────────┐   │
│   │        Jetpack Compose M3 UI (Menus / HUD)          │   │
│   └──────────────────────────┬──────────────────────────┘   │
│                              │ JNI / UnityPlayer Interface   │
│   ┌──────────────────────────▼──────────────────────────┐   │
│   │              Unity as a Library (UaaL)              │   │
│   │  - Universal Render Pipeline (URP Mobile)           │   │
│   │  - Humanoid Mecanim Animated Cricketers             │   │
│   │  - 3D Stadium Geometry & Turf Shaders               │   │
│   │  - Ball Flight Dynamics & Contact Detection         │   │
│   └──────────────────────────┬──────────────────────────┘   │
│                              │ HTTPS / REST + Realtime       │
│   ┌──────────────────────────▼──────────────────────────┐   │
│   │              Supabase Cloud Backend                 │   │
│   └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Unity as a Library (UaaL) Integration Steps

### Step 1: Exporting from Unity
1. In Unity Editor (`Unity 2023.2+` or `Unity 6 LTS`), open **File > Build Settings**.
2. Select **Platform: Android**.
3. Check the **Export Project** checkbox.
4. Set **Scripting Backend: IL2CPP** with Target Architectures: `ARMv7`, `ARM64`.
5. Click **Export** to target folder: `export/unityExport`.

### Step 2: Incorporating `unityLibrary` into Android Studio
In your root `settings.gradle.kts`:
```kotlin
include(":app")
include(":unityLibrary")
project(":unityLibrary").projectDir = file("unityLibrary")
```

In `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation(project(":unityLibrary"))
    implementation(fileTree(mapOf("dir" to "../unityLibrary/libs", "include" to listOf("*.jar"))))
}
```

### Step 3: Hosting the Unity View in Jetpack Compose
To embed the 3D surface seamlessly inside Compose:
```kotlin
@Composable
fun UnityCricketView(
    modifier: Modifier = Modifier,
    onUnityLoaded: (UnityPlayer) -> Unit
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            val unityPlayer = UnityPlayer(context)
            onUnityLoaded(unityPlayer)
            unityPlayer.view
        },
        update = { /* Handle lifecycle updates */ }
    )
}
```

---

## 3. C# Bridge Script (`CricketBridgeManager.cs`)

This C# script resides inside Unity (`Assets/Scripts/CricketBridgeManager.cs`) to process JNI messages sent from Android:

```csharp
using UnityEngine;
using System;
using System.Runtime.InteropServices;

public class CricketBridgeManager : MonoBehaviour
{
    public static CricketBridgeManager Instance { get; private set; }

    [Header("Engine Components")]
    public Camera broadcastCamera;
    public Camera behindBatterCamera;
    public Transform bowlerRoot;
    public Transform batterRoot;
    public Rigidbody cricketBallRb;

    private void Awake()
    {
        if (Instance == null) Instance = this;
        else Destroy(gameObject);
        DontDestroyOnLoad(gameObject);
    }

    // Called from Android via: UnitySendMessage("CricketBridgeManager", "StartDelivery", jsonPayload)
    public void StartDelivery(string deliveryJson)
    {
        DeliveryData data = JsonUtility.FromJson<DeliveryData>(deliveryJson);
        Debug.Log($"[UnityBridge] Executing delivery: {data.speedKmh} km/h, Line: {data.targetLine}");
        
        // Trigger bowler runup animation
        Animator bowlerAnim = bowlerRoot.GetComponent<Animator>();
        bowlerAnim.SetTrigger("BowlStride");
    }

    // Called from Android when user touches shot control
    public void ExecuteBattingShot(string shotJson)
    {
        ShotData data = JsonUtility.FromJson<ShotData>(shotJson);
        Animator batterAnim = batterRoot.GetComponent<Animator>();
        batterAnim.SetInteger("ShotType", data.shotIndex);
        batterAnim.SetTrigger("PlayShot");
    }

    // Sends contact / wicket events back to Android Kotlin
    public void SendMatchEventToAndroid(string eventType, string payload)
    {
        using (AndroidJavaClass jc = new AndroidJavaClass("com.example.ui.bridge.UnityEventReceiver"))
        {
            jc.CallStatic("onUnityGameEvent", eventType, payload);
        }
    }
}

[System.Serializable]
public class DeliveryData
{
    public string deliveryType;
    public float targetLine;
    public float targetLength;
    public int speedKmh;
    public float swing;
}

[System.Serializable]
public class ShotData
{
    public int shotIndex;
    public string intent;
    public float timingDeltaMs;
}
```

---

## 4. 3D Stadium & Player Asset Pipeline

### 4.1 3D Player Models & Rigging
1. **Geometry:** Semi-realistic athletic proportioned mesh (~12,000 to 18,000 triangles per player).
2. **Skeleton Rig:** Standard Humanoid Mecanim bone hierarchy:
   - Root -> Hips -> Spine -> Chest -> Neck -> Head
   - Clavicle -> Shoulder -> UpperArm -> Forearm -> Hand -> 5 articulated fingers
   - Thigh -> Calf -> Foot -> Toe
3. **Gear Meshes:**
   - Detachable batting pads (L/R) with knee rolls.
   - Helmet with metal faceguard grill.
   - English willow bat with grip & custom company stickers.
   - Athletic spiked cricket footwear.

### 4.2 Universal Render Pipeline (URP) Setup for 4GB RAM Devices
- **Resolution Scale:** 0.85x native with MSAA 2x.
- **Lighting:**
   - 1 Directional Key Light (Floodlight tower / Sun) with 2-cascade Soft Shadows.
   - Precomputed Lightmap baked into stadium grandstands.
   - PBR Materials using Albedo + Metallic/Smoothness + Normal maps (1024x1024 max).
- **Target Frame Rate:** 60 FPS in Ultra 3D mode; 30 FPS in Battery Saver / 4GB RAM Lite mode.

---

## 5. Summary of Migration Benefits
- Enables true GPU vertex skinning for realistic bowling strides and batting strokes.
- Physics-driven boundary collisions and realistic ball trajectories.
- Seamless coexistence with the existing Supabase backend and Jetpack Compose frontend.

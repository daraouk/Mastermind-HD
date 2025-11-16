# Mastermind HD

**A complete puzzle game with 100 challenging levels**

![Version](https://img.shields.io/badge/version-2.0-blue)
![Framework](https://img.shields.io/badge/framework-libGDX%201.12.1-green)
![Android](https://img.shields.io/badge/Android-5.0%2B-brightgreen)
![Levels](https://img.shields.io/badge/levels-100-purple)
![Code](https://img.shields.io/badge/code-2840_lines-orange)

## About

Mastermind HD is a **full-featured digital implementation** of the classic Mastermind board game, transformed from a high school prototype into a commercial-quality puzzle game with professional features and polish.

**Classic Gameplay:** Break the secret code by analyzing feedback pegs:
- **Black pegs** 🔴: Correct color in correct position
- **White pegs** ⚪: Correct color in wrong position

## What's New in v2.0

### 🎮 **100 Levels** Across 6 Difficulty Tiers
- **Tutorial** (1-5): Learn the basics
- **Easy** (6-25): Build confidence with 4-6 colors
- **Medium** (26-50): Introduce duplicates, more colors
- **Hard** (51-75): Longer codes, timed challenges
- **Expert** (76-90): Maximum difficulty
- **Master** (91-100): Ultimate challenges with unique twists

### ⭐ **3-Star Rating System**
- Earn 1-3 stars based on efficiency
- Replay levels to improve your rating
- Aim for perfect 300 stars (3 × 100 levels)

### 🔓 **Progressive Unlocking**
- Complete levels to unlock new challenges
- Save progress automatically
- Track stats: completion %, total stars

### 💡 **Hint System**
- Limited hints per level (varies by difficulty)
- Strategic assistance when stuck
- Reveals correct colors/positions

### ⏱️ **Timed Challenges**
- Some levels add time pressure
- 90 seconds to 5 minutes
- Beat the clock for extra challenge

### 🎨 **Professional UI**
5 polished screens:
- **Main Menu**: Animated title, progress summary
- **Level Select**: Scrollable grid, color-coded by difficulty
- **Game Screen**: Timer, hints, move counter, pause
- **Win Screen**: Animated stars, next level progression
- **Lose Screen**: Retry with encouragement

## Features

✅ **100 unique, hand-crafted levels**
✅ **6 difficulty tiers** (Tutorial → Master)
✅ **3-star rating system** (300 stars total)
✅ **Progressive level unlocking**
✅ **Hint system** with limited hints
✅ **Timed challenges** for extra difficulty
✅ **Complete save system** (never lose progress)
✅ **Professional UI** with 5 screens
✅ **Smooth animations** and visual feedback
✅ **Variable difficulty**:
  - 3-8 colors
  - 3-6 peg codes
  - 8-20 attempts
  - Duplicates on/off
  - Timed/untimed

✅ **Beautiful wood-themed graphics**
✅ **Portrait orientation** optimized for phones
✅ **Clean, maintainable codebase** (2,840 lines)

## Project Evolution

| Aspect | v1.0 (High School) | v2.0 (Complete Game) |
|--------|-------------------|----------------------|
| **Framework** | AndEngine (2013) | libGDX (2024) |
| **Build** | Eclipse ADT | Gradle |
| **Code** | 473 lines, 1 file | 2,840 lines, 11 files |
| **Screens** | 1 (game only) | 5 (menu, select, game, win, lose) |
| **Levels** | 1 generic | 100 unique configurations |
| **Progression** | None | Full unlock system + saves |
| **Features** | Basic gameplay | Hints, timer, stars, stats |
| **Quality** | Prototype | Commercial-grade |

See [MIGRATION.md](MIGRATION.md) for the modernization story and [FEATURES.md](FEATURES.md) for complete feature documentation.

## Level Highlights

**🎓 Tutorial (Levels 1-5)**
- "First Steps": 3 colors, 3 pegs, 5 hints
- "Tutorial Complete": 6 colors, 4 pegs, 2 hints

**🟢 Easy (Levels 6-25)**
- 4-6 colors, 4 pegs, no duplicates
- Perfect for learning strategies

**🔵 Medium (Levels 26-50)**
- 5-8 colors, duplicates introduced
- Balanced challenge

**🟠 Hard (Levels 51-75)**
- 6-8 colors, 4-5 pegs
- Some timed (3 minutes)

**🟣 Expert (Levels 76-90)**
- 8 colors, up to 6 pegs
- Timed challenges (4 minutes)

**🔴 Master (Levels 91-100)**
Named ultimate challenges:
- Level 91: **"Speed Run"** - 2 minutes, 8 turns
- Level 92: **"Long Code"** - 6 pegs, 15 turns
- Level 93: **"No Hints"** - 5 pegs, 0 hints
- Level 94: **"Time Trial"** - 90 seconds
- Level 95: **"Rainbow"** - 8 colors, no duplicates
- Level 100: **"MASTERMIND"** - Ultimate challenge!

## Building

### Requirements
- JDK 17+
- Android SDK (API 34)
- Gradle 8.4+ (wrapper included)

### Build Commands

```bash
# Build debug APK
./gradlew android:assembleDebug

# Build release APK
./gradlew android:assembleRelease

# Install on connected device
./gradlew android:installDebug
```

The APK will be in: `android/build/outputs/apk/`

## Project Structure

```
Mastermind-HD/
├── android/                          # Android-specific code
│   ├── src/                         # AndroidLauncher
│   ├── assets/gfx/                  # Game graphics (29 files)
│   └── res/                         # Android resources
├── core/src/main/java/.../core/     # Core game code (platform-independent)
│   ├── Level.java                   # Level configuration (115 lines)
│   ├── LevelManager.java            # 100 level definitions (299 lines)
│   ├── GameProgress.java            # Save/load system (188 lines)
│   ├── MastermindGame.java          # Core game logic (443 lines)
│   ├── MastermindHDGame.java        # libGDX manager (41 lines)
│   ├── MainMenuScreen.java          # Main menu UI (203 lines)
│   ├── LevelSelectScreen.java       # Level browser (317 lines)
│   ├── EnhancedGameScreen.java      # Main gameplay (429 lines)
│   ├── WinScreen.java               # Victory screen (222 lines)
│   └── LoseScreen.java              # Game over screen (192 lines)
├── build.gradle                     # Build configuration
├── README.md                        # This file
├── FEATURES.md                      # Complete feature documentation
└── MIGRATION.md                     # Migration story
```

**Total Core Code: 2,840 lines** across 11 classes

## Architecture

### Core Systems
```
Level System
├── Level - Configuration for each level
├── LevelManager - All 100 level definitions
└── GameProgress - Save/load, progression tracking

Game Logic
├── MastermindGame - Rules, state, feedback
├── Hint system - Strategic assistance
└── Timer system - Timed challenge support

UI Layer (5 Screens)
├── MainMenuScreen - Entry point with animations
├── LevelSelectScreen - Scrollable level grid
├── EnhancedGameScreen - Full gameplay experience
├── WinScreen - Victory celebration + progression
└── LoseScreen - Retry prompt
```

### Design Principles
- **Clean separation** of game logic and UI
- **Framework-independent** core (testable)
- **Progressive disclosure** (unlock as you play)
- **Player-friendly** (never lose progress)
- **Extensible** (easy to add features)

## Gameplay Flow

```
App Launch
    ↓
Main Menu (Play / Settings / Quit)
    ↓
Level Select (Grid of 100 levels, color-coded)
    ↓
Game Screen (Make guesses, use hints, beat timer)
    ↓
Win? → Win Screen (Stars! Next Level unlocked)
    ↓
Lose? → Lose Screen (Retry / Return to menu)
```

## Statistics Tracked

- Highest unlocked level (1-100)
- Stars per level (0-3)
- Levels completed (X/100)
- Total stars (X/300)
- Completion percentage
- Best performance per level

## Future Enhancements

**Potential additions:**
- 🔊 Sound effects and background music
- 🎆 Particle effects for wins
- 🏆 Achievement system
- 📊 Statistics screen (fastest times, etc.)
- 🎨 Alternative themes and color schemes
- 🌍 Online leaderboards
- 📱 Daily challenge mode
- ↩️ Undo last move
- 🎯 Color-blind mode

## Technology Stack

- **Language**: Java 8
- **Framework**: libGDX 1.12.1
- **Build**: Gradle 8.4+
- **Platform**: Android 5.0+ (API 21-34)
- **Graphics**: OpenGL ES 2.0
- **Screen**: 480×800 (scales to device)
- **Orientation**: Portrait

## Controls

- **Tap** - Select colors, press buttons
- **Drag** - Scroll level selection
- **Back** - Return to previous screen

## Credits

**Original Author**: Dara Ouk
- **v1.0** (High School): AndEngine prototype
- **v2.0** (2025): Complete libGDX game

**Framework**: [libGDX](https://libgdx.com/) - Cross-platform game development
**Graphics**: Original wood-themed artwork

## Documentation

- [README.md](README.md) - This file (overview)
- [FEATURES.md](FEATURES.md) - Complete feature list and level details
- [MIGRATION.md](MIGRATION.md) - AndEngine → libGDX migration story

## License

Personal/educational project.

---

## From Prototype to Production

**2013**: High school project with AndEngine
- Single level
- Basic gameplay
- 473 lines of code

**2025**: Complete game with libGDX
- 100 unique levels
- Full progression system
- Professional UI
- 2,840 lines of code

**This is what "finishing" a project looks like.** 🌟

---

**Ready to break the code? Download and play Mastermind HD!**

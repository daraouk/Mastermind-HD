# Mastermind HD

A classic code-breaking game for Android, modernized with libGDX.

![Version](https://img.shields.io/badge/version-2.0-blue)
![Framework](https://img.shields.io/badge/framework-libGDX%201.12.1-green)
![Android](https://img.shields.io/badge/Android-5.0%2B-brightgreen)

## About

Mastermind HD is a digital implementation of the classic Mastermind board game. The codebreaker tries to guess a secret 4-color code within 10 turns. After each guess, feedback pegs show:
- **Black pegs**: Correct color in correct position
- **White pegs**: Correct color in wrong position

## Project History

- **v1.0** (High School): Built with AndEngine, Eclipse ADT, targeting Android 2.2+
- **v2.0** (2025): Completely modernized with libGDX, Gradle, targeting Android 5.0+

See [MIGRATION.md](MIGRATION.md) for the full modernization story.

## Features

✅ Classic Mastermind gameplay
✅ 8 color choices (Red, Blue, Green, Purple, Yellow, Orange, Black, White)
✅ 10 turns to crack the code
✅ Visual feedback with black and white pegs
✅ Beautiful wood-themed graphics
✅ Portrait orientation optimized for phones

### Upcoming Features
- Doubles mode (allow repeated colors in code)
- Win screen
- Sound effects and haptic feedback
- Scoring and leaderboards
- Menu system

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
├── android/               # Android-specific code
│   ├── src/              # AndroidLauncher
│   ├── assets/           # Game graphics (PNG, JPG)
│   └── res/              # Android resources
├── core/                 # Core game code (platform-independent)
│   └── src/
│       ├── MastermindGame.java      # Pure game logic
│       ├── MastermindHDGame.java    # libGDX game manager
│       └── GameScreen.java          # Rendering & input
├── build.gradle          # Root build configuration
└── settings.gradle       # Multi-module setup
```

## Architecture

The project follows clean architecture principles:

**Core Game Logic** (`MastermindGame.java`)
- Framework-independent
- Pure Java, no Android dependencies
- Unit-testable
- Handles all game rules and state

**Presentation Layer** (`GameScreen.java`)
- libGDX rendering
- Touch input handling
- Visual effects (blinking cursor)
- Asset management

**Platform Layer** (`AndroidLauncher.java`)
- Android-specific initialization
- Lifecycle management

## Technology Stack

- **Language**: Java 8
- **Game Framework**: libGDX 1.12.1
- **Build System**: Gradle 8.4
- **Target Platform**: Android 5.0+ (API 21-34)
- **Graphics**: OpenGL ES 2.0

## Development

### Adding Features

All game logic lives in `MastermindGame.java`. To add features:

1. Modify game logic (e.g., enable duplicates mode)
2. Update UI in `GameScreen.java` if needed
3. Test without building APK (unit tests)

Example - Enable duplicates mode:
```java
// In GameScreen.java constructor:
this.gameLogic = new MastermindGame(true); // true = allow duplicates
```

### Testing

The core game logic is unit-testable:

```java
MastermindGame game = new MastermindGame();
int[] guess = {0, 1, 2, 3}; // Red, Blue, Green, Purple
MastermindGame.Feedback feedback = game.makeGuess(guess);
System.out.println("Black: " + feedback.blackPegs + ", White: " + feedback.whitePegs);
```

## Credits

**Original Author**: Dara Ouk
**Original Version**: 1.0 (AndEngine)
**Modernization**: 2.0 (libGDX)
**Framework**: [libGDX](https://libgdx.com/)

## License

Personal/educational project.

## Links

- [libGDX Documentation](https://libgdx.com/wiki/)
- [Migration Guide](MIGRATION.md)

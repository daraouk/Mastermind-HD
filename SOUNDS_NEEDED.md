# Sound Effects Needed for Mastermind HD

The game is fully integrated with a sound system, but requires actual sound files to be added.

## Required Sound Files

Create these sound files and place them in `android/assets/sounds/`

### Core Interaction Sounds

| File | Description | When Played | Duration | Suggested Style |
|------|-------------|-------------|----------|-----------------|
| `tap.mp3` | Generic tap | Any UI tap | 0.1s | Light click |
| `select.mp3` | Item selection | Selecting level | 0.15s | Soft ding |
| `button.mp3` | Button press | Main buttons | 0.2s | Button click |
| `place.mp3` | Place game piece | Color ball placed | 0.15s | Soft thud |

### Gameplay Sounds

| File | Description | When Played | Duration | Suggested Style |
|------|-------------|-------------|----------|-----------------|
| `hint.mp3` | Hint used | Using a hint | 0.3s | Mystery chime |
| `correct.mp3` | Correct guess | Black pegs scored | 0.4s | Success tone |
| `wrong.mp3` | Wrong/denied | Locked level tap | 0.2s | Error beep |
| `complete.mp3` | Row complete | Submitted guess | 0.3s | Completion sound |

### Result Sounds

| File | Description | When Played | Duration | Suggested Style |
|------|-------------|-------------|----------|-----------------|
| `win.mp3` | Level won | Win screen shown | 1-2s | Victory fanfare |
| `lose.mp3` | Level lost | Lose screen shown | 1s | Defeat sound |
| `star.mp3` | Star earned | Each star shown | 0.3s | Sparkle/ding |
| `unlock.mp3` | Level unlocked | Unlocking next level | 0.5s | Unlock chime |

### Transition Sounds

| File | Description | When Played | Duration | Suggested Style |
|------|-------------|-------------|----------|-----------------|
| `whoosh.mp3` | Screen transition | Changing screens | 0.3s | Swoosh |
| `tick.mp3` | Timer tick | Low time warning | 0.1s | Clock tick |

### Background Music

| File | Description | When Played | Duration | Loop |
|------|-------------|-------------|----------|------|
| `bgm.mp3` | Background music | Throughout game | 2-3min | Yes |

## Sound Specifications

### Technical Requirements

- **Format**: MP3 or OGG (MP3 recommended for compatibility)
- **Sample Rate**: 44.1 kHz or 48 kHz
- **Bit Rate**: 128-192 kbps (good quality/size balance)
- **Channels**: Mono (stereo for music)
- **Max File Size**: 500 KB per sound, 5 MB for music

### Audio Guidelines

1. **Volume Normalization**
   - All sounds should be normalized to -3dB
   - Consistent volume across all effects
   - Music should be quieter than sound effects

2. **Style Consistency**
   - Cohesive audio theme throughout
   - Wood/organic sounds match visual theme
   - Not too loud or jarring

3. **Quality Over Quantity**
   - Professional quality recordings
   - No background noise or artifacts
   - Clean start/end (no pops or clicks)

## Sources for Sound Effects

### Free Resources

1. **Freesound.org**
   - Large library of CC-licensed sounds
   - Search for: button click, wood tap, success chime

2. **Zapsplat.com**
   - Free sound effects library
   - Registration required

3. **OpenGameArt.org**
   - Game-specific sounds
   - Many CC0 (public domain) options

### Paid Resources

1. **AudioJungle (Envato)**
   - Professional quality
   - $1-5 per sound

2. **Epidemic Sound**
   - Subscription service
   - Includes music

### Custom Creation

1. **Audacity** (Free)
   - Record and edit your own
   - Great for simple sounds

2. **BFXR** (Free)
   - Generate retro game sounds
   - Web-based tool

## Integration Status

✅ **Sound system fully integrated** - All code is ready
✅ **Sound manager implemented** - Centralized control
✅ **All screens have sound triggers** - Gameplay, menus, etc.
✅ **Settings screen** - Volume and mute controls
✅ **Graceful fallback** - Works silently if sounds missing

## Testing Without Sounds

The game will run perfectly fine without sound files:
- Missing sounds are logged but don't crash
- Game remains fully playable
- Add sounds incrementally during testing

## Adding Sounds to the Project

1. Create directory:
   ```bash
   mkdir -p android/assets/sounds
   mkdir -p android/assets/music
   ```

2. Add sound files:
   ```bash
   cp your-sounds/*.mp3 android/assets/sounds/
   cp your-music/bgm.mp3 android/assets/music/
   ```

3. Test in game:
   - Launch game
   - Check Settings → Sound/Music toggles
   - Play through levels to hear effects

## Sound Mapping Reference

### Main Menu Screen
- `playMusic()` - Start background music
- `playButton()` - Play/Settings/Quit buttons
- `playWhoosh()` - Screen transition

### Level Select Screen
- `playButton()` - Back button
- `playSelect()` - Level selected
- `playWhoosh()` - Transition to game
- `playWrong()` - Locked level tapped

### Game Screen
- `playPlace()` - Color ball placed
- `playComplete()` - Row submitted
- `playCorrect()` - Black pegs scored
- `playTap()` - White pegs scored
- `playHint()` - Hint used successfully
- `playWrong()` - Hint unavailable
- `playButton()` - Back button
- `playTap()` - Pause button

### Win Screen
- `playWin()` - Victory sound
- `playComplete()` - Screen shown
- `playButton()` - Button taps
- `playUnlock()` - Next level unlocked
- `playStar()` - (Future: Each star)

### Lose Screen
- `playLose()` - Defeat sound
- `playButton()` - Retry/Menu buttons

### Settings Screen
- `playButton()` - All buttons
- `playTap()` - Toggle switches

## Volume Controls

The game includes:
- **Sound Effects**: Controlled by Settings → Sound toggle
- **Background Music**: Controlled by Settings → Music toggle
- **Master Volume**: Default 70% (sounds), 50% (music)

Players can:
- Toggle sound on/off
- Toggle music on/off
- Settings persist across sessions

## Future Enhancements

Potential additions:
- Volume sliders (not just on/off)
- Different sound packs (themes)
- Haptic feedback (vibration)
- Spatial audio (stereo positioning)

## Beta Testing Notes

**For beta testers:**
- Test with sound ON and OFF
- Report any:
  - Sounds too loud/quiet
  - Missing sound effects
  - Audio glitches
  - Music not looping properly

**Known limitations:**
- No sound files included (add your own)
- No volume sliders (just on/off)
- All sounds same volume (no per-sound control)

## License Considerations

⚠️ **Important**: Ensure all sounds are:
- Properly licensed for commercial/distribution use
- Credited if required
- Not copyrighted by others

Recommended licenses:
- CC0 (Public Domain)
- CC-BY (Attribution required)
- Licensed for game use

---

**Status**: Sound system ready, awaiting sound files
**Priority**: Medium (game works without sounds)
**Effort**: 2-4 hours to source and integrate quality sounds

# Mastermind HD - Beta Release Guide

**Version**: 2.0 Beta
**Target**: Small audience testing
**Platform**: Android 5.0+ (API 21+)

---

## Pre-Release Checklist

### ✅ Features Complete

- [x] 100 unique levels
- [x] Level progression system
- [x] 3-star rating system
- [x] Save/load functionality
- [x] Hint system
- [x] Timed challenges
- [x] Professional UI (5 screens)
- [x] Sound system integrated
- [x] Particle effects
- [x] Settings screen
- [x] About/credits screen

### ⚠️ Assets Needed

- [ ] Sound effects (14 files) - See [SOUNDS_NEEDED.md](SOUNDS_NEEDED.md)
- [ ] Background music (optional)
- [ ] App icon (high-res)
- [ ] Feature graphic (for Play Store)
- [ ] Screenshots (5-8 images)

### 📝 Documentation

- [x] README.md
- [x] FEATURES.md
- [x] MIGRATION.md
- [x] SOUNDS_NEEDED.md
- [x] BETA_RELEASE.md (this file)

---

## Building for Beta Release

### 1. Update Version

Edit `android/build.gradle`:

```gradle
defaultConfig {
    applicationId "com.eklypze.android.mastermdhd"
    minSdk 21
    targetSdk 34
    versionCode 2      // Increment for each release
    versionName "2.0-beta"  // Beta indicator
}
```

### 2. Build Release APK

```bash
# Clean previous builds
./gradlew clean

# Build release APK
./gradlew android:assembleRelease

# Find APK at:
# android/build/outputs/apk/release/android-release-unsigned.apk
```

### 3. Sign the APK (Production)

```bash
# Generate keystore (first time only)
keytool -genkey -v -keystore mastermind-release.keystore \
  -alias mastermind-key -keyalg RSA -keysize 2048 -validity 10000

# Sign APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore mastermind-release.keystore \
  android/build/outputs/apk/release/android-release-unsigned.apk \
  mastermind-key

# Align APK
zipalign -v 4 android-release-unsigned.apk MastermindHD-v2.0-beta.apk
```

### 4. Build Debug APK (Testing)

For quick testing without signing:

```bash
./gradlew android:assembleDebug

# Install directly to device:
./gradlew android:installDebug
```

---

## Distribution Methods

### Option 1: Google Play Internal Testing

**Best for**: Organized beta testing with analytics

1. **Create Google Play Console account**
   - Developer registration fee: $25 (one-time)
   - https://play.google.com/console

2. **Upload to Internal Testing track**
   - Internal testing = up to 100 testers
   - Closed testing = up to 1000s of testers
   - No review required for internal

3. **Add testers by email**
   - Testers receive email invitation
   - Can provide feedback via Play Console

4. **Benefits**:
   - Crash reporting
   - Download analytics
   - Easy updates
   - Professional distribution

### Option 2: Direct APK Distribution

**Best for**: Quick testing with friends

1. **Share APK file directly**
   - Email, Dropbox, Google Drive, etc.
   - File size: ~10-20 MB (without sounds)

2. **Testers must enable "Unknown Sources"**
   - Settings → Security → Unknown Sources
   - Or Settings → Apps → Special Access → Install Unknown Apps

3. **Considerations**:
   - Manual updates required
   - No automated crash reports
   - Requires trust from testers

### Option 3: Firebase App Distribution

**Best for**: Private beta with analytics

1. **Set up Firebase project**
   - Free tier sufficient
   - https://console.firebase.google.com

2. **Integrate Firebase SDK**
   - Analytics
   - Crash reporting
   - Remote config

3. **Benefits**:
   - Professional analytics
   - Crash reporting
   - Easy tester management
   - No Play Store submission

---

## Beta Testing Plan

### Phase 1: Internal Testing (Week 1)
**Testers**: 5-10 close friends/family

**Goals**:
- Basic functionality testing
- Crash detection
- UX feedback
- Balance testing (level difficulty)

**Metrics**:
- Levels completed
- Time per level
- Hints used
- Crashes/bugs

### Phase 2: Closed Beta (Weeks 2-3)
**Testers**: 25-50 recruited beta testers

**Goals**:
- Broader device testing
- Performance on different devices
- Battery usage
- Network behavior (if applicable)

**Metrics**:
- Device compatibility
- Performance metrics
- User engagement
- Feature requests

### Phase 3: Polish & Iterate (Week 4)
**Actions**:
- Fix critical bugs
- Adjust difficulty
- Add/refine features
- Prepare for wider release

---

## Beta Tester Guidelines

### What to Send Testers

```markdown
# Mastermind HD Beta Test

Thank you for testing Mastermind HD!

**Installation:**
1. Download APK from [link]
2. Enable "Install from Unknown Sources" if needed
3. Install and launch

**What to Test:**
- Complete at least 10 levels
- Try different difficulty levels
- Test all menu screens
- Use hints when stuck
- Play a timed challenge

**Feedback Requested:**
- Bugs or crashes
- Confusing UI elements
- Level difficulty balance
- Performance issues
- Missing features
- General impressions

**Reporting Bugs:**
- Email: beta@example.com
- Include:
  - Device model
  - Android version
  - What you were doing
  - Screenshot if possible

**Timeline:**
- Beta period: 2-4 weeks
- Feedback deadline: [date]

Thank you!
```

### Feedback Form Template

Create a Google Form with:

1. **Device Info**
   - Device model
   - Android version
   - Screen size

2. **Usage**
   - Levels completed
   - Hours played
   - Favorite/least favorite levels

3. **Experience**
   - Overall rating (1-5 stars)
   - What did you like most?
   - What needs improvement?
   - Any bugs encountered?

4. **Features**
   - Which features did you use?
   - Which features were confusing?
   - What features are missing?

5. **Performance**
   - Any lag or stuttering?
   - Battery drain noticeable?
   - App crashes?

---

## Known Issues / Limitations

Document known issues for testers:

### Current Limitations

1. **No Sound Files**
   - System integrated but silent
   - Sounds will be added in next version
   - Game fully playable without

2. **No Haptic Feedback**
   - Could enhance experience
   - Planned for future update

3. **No Undo Function**
   - Once piece placed, it's final
   - Consider adding if requested

4. **No Daily Challenge**
   - Would add replay value
   - Potential future feature

5. **No Social Features**
   - No leaderboards
   - No sharing
   - Could add based on feedback

### Bug Tracking

Track bugs in a spreadsheet:

| # | Description | Severity | Device | Status |
|---|-------------|----------|--------|--------|
| 1 | Crash on level 50 | High | Pixel 6 | Fixed |
| 2 | UI overlap on small screens | Medium | Samsung A10 | Investigating |

---

## Success Metrics

### Quantitative

- **Completion Rate**: % of testers who finish tutorial
- **Engagement**: Average levels completed per tester
- **Session Length**: Average playtime per session
- **Retention**: % returning after first day
- **Crash-Free Rate**: Target >99.5%

### Qualitative

- **User Satisfaction**: Average rating target >4.0/5.0
- **Feature Requests**: Track most requested features
- **UI/UX Feedback**: Confusion points to fix
- **Difficulty Balance**: Too easy/hard levels

---

## Post-Beta Actions

### Before Public Release

1. **Fix Critical Bugs**
   - Crashes
   - Game-breaking issues
   - Major UI problems

2. **Add Sound Files**
   - See SOUNDS_NEEDED.md
   - Test all sound triggers

3. **Polish Graphics**
   - App icon (1024x1024)
   - Feature graphic (1024x500)
   - Screenshots (phone + tablet)

4. **Update Documentation**
   - Privacy policy (if collecting data)
   - Terms of service
   - Credits

5. **Prepare Store Listing**
   - App description
   - Keywords
   - Promotional text
   - Video trailer (optional)

### Versioning Strategy

- **2.0-beta** - Current beta release
- **2.0** - First public release (after beta fixes)
- **2.1** - Minor updates (sounds, small features)
- **2.2** - Feature updates (daily challenge, etc.)
- **3.0** - Major overhaul (if ever)

---

## Launch Preparation

### Google Play Store Requirements

**Required Assets:**
- App icon: 512x512 PNG (hi-res)
- Feature graphic: 1024x500 JPG/PNG
- Screenshots: 2-8 images (phone)
- Screenshots: 1-8 images (tablet, optional)

**Store Listing:**
- Title: "Mastermind HD - Puzzle Game" (max 30 chars)
- Short description: 80 chars
- Full description: 4000 chars
- Category: Puzzle
- Content rating: Everyone
- Privacy policy URL (required if collecting data)

**Technical:**
- APK signed with release keystore
- Version code incremented
- ProGuard enabled (optional, for code shrinking)

### Marketing Materials

Consider creating:
- **Trailer Video** (30-60 seconds)
  - Gameplay highlights
  - Feature showcase
  - Available on YouTube

- **Website/Landing Page**
  - Game overview
  - Screenshots
  - Download links
  - Press kit

- **Social Media**
  - Twitter/Facebook announcement
  - Game GIFs
  - Beta tester testimonials

---

## Contact & Support

**Developer**: Dara Ouk
**Email**: [your-email]
**GitHub**: https://github.com/[your-username]/Mastermind-HD
**Website**: [your-website]

**Beta Feedback**: beta@example.com
**Bug Reports**: bugs@example.com
**General Inquiries**: contact@example.com

---

## Timeline

### Suggested Schedule

**Week 1**: Internal alpha testing (5-10 testers)
**Week 2-3**: Closed beta (25-50 testers)
**Week 4**: Bug fixes and polish
**Week 5**: Final testing and preparation
**Week 6**: Public release on Google Play

**Total**: ~6 weeks from beta start to public launch

---

## Legal Considerations

### Before Public Release

1. **Privacy Policy**
   - Required if collecting any user data
   - Even analytics requires disclosure
   - Template: https://app-privacy-policy-generator.firebaseapp.com/

2. **Terms of Service**
   - Liability protection
   - Usage terms
   - Account deletion (if applicable)

3. **Licenses**
   - Graphics: Verify all assets are yours or licensed
   - Sounds: Check licenses (see SOUNDS_NEEDED.md)
   - Code: MIT/Apache/GPL (choose one)
   - Fonts: If using custom fonts

4. **Trademarks**
   - "Mastermind" is trademarked by Hasbro
   - Ensure fair use (educational/transformative)
   - Consider alternative name if commercial

---

## Conclusion

**The game is beta-ready!**

✅ All core features implemented
✅ 100 levels playable
✅ Professional UI complete
✅ Sound system integrated (needs assets)
✅ Ready for testing

**Next steps:**
1. Add sound effects (optional but recommended)
2. Build release APK
3. Recruit beta testers
4. Gather feedback
5. Polish and iterate
6. Prepare for public launch

**This is a complete, professional-quality puzzle game ready for real users!** 🎉

---

*Last updated: January 2025*
*Version: 2.0 Beta*

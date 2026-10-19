# Mihon API 24 (Android 7.0+ arm64-v8a Backport)

## 1. Project Overview & Purpose

This project is a fork of [Mihon](https://github.com/mihonapp/mihon) (the open-source manga reader for Android).

- **Objective**: Adapt and backport Mihon to install and run reliably on **Android 7.0 / 7.1 devices (API level 24 / 25)** specifically targeting the **`arm64-v8a`** CPU architecture.
- **Upstream Baseline**: Official Mihon requires Android 8.0+ (API 26+).
- **Version Identifier**: `24.0` (indicates API 24 minimum SDK target).
- **Current Status**: The generated APK installs and runs cleanly on Android 7.0+ devices.

---

## 2. Architecture & Compatibility Adaptations

### 2.1 SDK & Build Configuration
- **Minimum SDK**: Lowered `android-sdk-min` from `26` to `24` in `gradle/mihon.versions.toml`.
- **Target ABI Splitting**: Configured `splits.abi` in `app/build.gradle.kts` to strictly compile and generate `arm64-v8a` APKs (`isUniversalApk = false`, `include("arm64-v8a")`).
- **Legacy Native Packaging**: Enabled `packaging { jniLibs { useLegacyPackaging = true } }` in `app/build.gradle.kts` to ensure `.so` shared libraries are properly extracted into the app directory on Android 7.x systems.
- **Widget Module Removal**: The home screen widget module (`presentation-widget`) relies on Jetpack Glance (which requires API 26+). It has been excluded from `settings.gradle.kts`, `app/build.gradle.kts`, and its initialization has been stripped from `App.kt`.

### 2.2 Extension Management & Permissions
- **Default Extension Installer**: In `ExtensionInstallerPreference.kt`, Android versions prior to 8.0 (`Build.VERSION.SDK_INT < Build.VERSION_CODES.O`) default to `ExtensionInstaller.PRIVATE`. Extensions are installed internally into private storage (`filesDir/exts`), completely avoiding the need for system-level unknown sources installation permissions.
- **Package Install Permissions**: In `Permissions.kt` and `PermissionStep.kt`, calls to `packageManager.canRequestPackageInstalls()` (introduced in API 26) are skipped for API < 26 to prevent runtime `NoSuchMethodError` crashes.
- **Settings Navigation Fallbacks**:
  - `ContextExtensions.kt`: When requesting unknown sources permission on API < 26, navigates to `Settings.ACTION_SECURITY_SETTINGS` instead of `Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES`.
  - `SettingsAdvancedScreen.kt`: Notification management navigation falls back to application details settings (`Settings.ACTION_APPLICATION_DETAILS_SETTINGS`) on Android < 8.0.

### 2.3 Graphics & Image Rendering Compatibility
- **Hardware Bitmaps**: In `MangaCoverDialog.kt`, bitmap copying guards against `Bitmap.Config.HARDWARE` (API 26+) by falling back to `Bitmap.Config.ARGB_8888` on Android < 8.0.
- **HDR Image Decoders**: In `ImageDecoder.kt` and `ImageUtil.kt`, `Bitmap.Config.RGBA_F16` (API 26+) is guarded and falls back to `Bitmap.Config.ARGB_8888` on API < 26.
- **Adaptive Icons & XML Resources**:
  - Android 8.0 introduced `<adaptive-icon>`, which fails XML inflation on Android 7.
  - All `<adaptive-icon>` resources were moved to `-v26` qualifier folders (`drawable-v26/`, `mipmap-anydpi-v26/`).
  - Standard `layer-list` fallbacks are provided in default `drawable/` and `mipmap/` directories to prevent startup/launcher crashes.

### 2.4 System & Network Backports
- **WebView Package Inspection**: In `WebViewUtil.kt`, `WebView.getCurrentWebViewPackage()` (API 26+) is replaced with `WebViewCompat.getCurrentWebViewPackage(context)` from `androidx.webkit`.
- **Notification Channels**: In `Notifications.kt`, `createChannels()` immediately returns on `Build.VERSION.SDK_INT < Build.VERSION_CODES.O`, avoiding unnecessary channel registration and group creation overhead.
- **Active Notifications Safety**: In `NotificationReceiver.kt`, access to `notificationManager.activeNotifications` is safely caught with `runCatching` / `try-catch` to guard against `SecurityException` on OEM-modified Android 7 ROMs.
- **Network Capabilities Enum**: In `NetworkExtensions.kt`, transport capability checks avoid accessing constants like `TRANSPORT_WIFI_AWARE` and `TRANSPORT_LOWPAN` on API levels below 26/27.
- **TLS 1.3 / Network Security**: Bundled Conscrypt is initialized on Android < 10 in `App.kt` to ensure modern TLS 1.3 and HTTPS cipher suite support.

---

## 3. Automated Build & Release CI Workflow

The workflow file is located at [`.github/workflows/mihon-api24.yml`](.github/workflows/mihon-api24.yml).

- **Trigger Method**: Triggered manually via `workflow_dispatch` from the GitHub Actions tab.
- **Inputs**:
  - `dist`: Distribution type (`ci` or `local`, default: `ci`).
  - `create_release`: Whether to publish to GitHub Releases (default: `true`).
  - `tag_name`: Release tag name (default: `v24.0`).
- **Pipeline Steps**:
  1. Checks out repository.
  2. Sets up JDK 21 (from `.github/.java-version`).
  3. Executes `./gradlew assembleRelease -Pdist=ci`.
  4. Generates and renames release APK to `mihon-${tag_name}-arm64-v8a.apk`.
  5. Uploads the APK to GitHub Actions Artifacts.
  6. Publishes or updates GitHub Releases with the arm64 APK asset attached.

---

## 4. Key Rules for Future Modifications

When adding features, upgrading libraries, or modifying code:
1. **Never use Android 8.0+ (API 26+) APIs unconditionally**. Always guard with `if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)` and provide a functional fallback for API 24/25.
2. **Watch for XML resources**: Do not introduce `<adaptive-icon>`, `android:fontFamily` without compat namespace, or Oreo-only XML attributes into unqualified resource directories.
3. **Keep ABI restricted to `arm64-v8a`**: Do not remove ABI splits or change `useLegacyPackaging = true` unless intentionally refactoring build outputs.
4. **Preserve private extension loading**: Keep `ExtensionInstaller.PRIVATE` functional as Android 7 lacks modern package installation session APIs.

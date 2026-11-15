# PKHosting Native Android App

A native Android application built with **Kotlin** and **Jetpack Compose**. The main app screens are native Android UI; this project does not embed the PKHosting website in a WebView.

## Native app features

- Native home screen and bottom navigation.
- Native hosting-plan overview.
- Native domain-search entry screen.
- Native support screen with phone and email actions.
- Customer dashboard, live domain availability, current pricing, login, billing and checkout open in secure Chrome Custom Tabs so the existing PKHosting account and payment systems remain in use.
- HTTPS-only external links.

## Build a debug APK

The GitHub Actions workflow builds the app with Gradle and publishes `PKHosting-Native.apk` as a GitHub Release asset and workflow artifact whenever code is pushed to `main`.

1. Open **Actions** and select the latest **Build Native PKHosting Android APK** run.
2. Confirm the build succeeds.
3. Open **Releases** and download `PKHosting-Native.apk`.
4. Install it on an Android phone. Android may ask you to allow installation from your browser or file manager.

## Free distribution

GitHub Actions and GitHub Releases provide free build and APK distribution for this public repository. Google Play publishing is separate and requires a developer account.

## Important

The app's main navigation and information screens are native. Operations that require PKHosting's existing live backend (account login, billing, checkout and final domain availability) are securely handed off to the official website in a browser tab. The app requires an internet connection for those services.

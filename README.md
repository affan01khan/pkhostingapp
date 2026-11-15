# PKHosting Android App

An Android application built with Capacitor that opens the official PKHosting website inside an Android WebView. Website pages, hosting plans, domain search, customer login, checkout and support remain served by PKHosting, so website updates appear in the app without rebuilding the APK.

## Build

GitHub Actions builds a debug APK on every push to `main` and publishes it as a GitHub Release.

1. Open the repository's **Actions** tab and check the latest **Build PKHosting Android APK** run.
2. When the run succeeds, open **Releases**.
3. Download `PKHosting.apk` and install it on an Android phone.

## Free distribution

GitHub Actions and GitHub Releases provide a free build and APK download route for this public repository. Publishing on Google Play is separate and requires a developer account.

## App behavior

- Loads the official HTTPS site at https://www.pkhosting.com/.
- Uses the website's live content, prices, login and checkout.
- Requires an internet connection to load the website.
- `www/index.html` is a local connection-help fallback page.

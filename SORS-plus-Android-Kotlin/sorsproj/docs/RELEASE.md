# Build dan Rilis

## Build lokal
Di Android Studio, buka proyek dan jalankan **Build > Build App Bundle(s) / APK(s) > Build APK(s)**. Dari terminal dengan Gradle terpasang, jalankan:

```bash
gradle --no-daemon assembleDebug
```

APK debug biasanya berada di `app/build/outputs/apk/debug/app-debug.apk`.

## Build GitHub Actions
Workflow `.github/workflows/build-apk.yml` berjalan pada push ke `main`/`master` dan dapat dijalankan manual. Ia memakai JDK 17, menyiapkan Gradle, menjalankan `assembleDebug`, lalu mengunggah APK sebagai artifact.

## Rilis bertanda tangan
Jangan unggah signing key atau kata sandi ke Git. Untuk distribusi publik:
1. Buat keystore release secara lokal.
2. Simpan file keystore dan kata sandi menggunakan GitHub Actions Secrets atau secret manager yang sesuai.
3. Konfigurasikan signing release pada Gradle tanpa menulis rahasia di source.
4. Uji release build dan simpan cadangan keystore dengan aman.
5. Pertimbangkan Android App Bundle (`.aab`) bila menerbitkan di Google Play.

## Versi
- `versionCode`: 2
- `versionName`: `2.0.0`
- `minSdk`: 23 (Android 6.0)
- `targetSdk` / `compileSdk`: 35

Versi ini adalah paket pengembangan. Jangan menyebutnya rilis produksi sebelum checklist pengujian lulus.

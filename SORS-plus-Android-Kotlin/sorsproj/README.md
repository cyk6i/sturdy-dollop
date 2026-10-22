# SORS+ — Android Native (Kotlin + Jetpack Compose)

Aplikasi kalkulator Android dengan UI native Jetpack Compose. Versi ini adalah fondasi pengembangan **v2.0.0**; build dan pengujian perangkat masih perlu dilakukan sebelum menyatakan rilis produksi.

## Fitur yang tersedia di kode
- Kalkulator aritmetika dengan parser ekspresi lokal (tanpa `eval`/JavaScript).
- Fungsi ilmiah: `sin`, `cos`, `tan`, `asin`, `acos`, `atan`, `sqrt`, `log`, `ln`, pangkat, persen, faktorial, `π`, dan `e`.
- Mode sudut DEG/RAD.
- Konverter panjang, massa, dan suhu.
- Kalkulasi fisika dasar: kelajuan rata-rata dan gaya.
- Kalkulasi kimia dasar: mol dari massa/massa molar dan mol dari konsentrasi/volume.
- Riwayat hitungan selama sesi aplikasi berjalan.
- Pilihan label Bahasa Indonesia/Inggris dan tema terang/gelap.
- Tidak memerlukan izin internet.

## Persyaratan
- Android Studio versi stabil yang mendukung Android Gradle Plugin 8.7.x.
- JDK 17.
- Android SDK Platform 35.
- Koneksi internet saat Gradle pertama kali mengunduh dependensi.

## Menjalankan di Android Studio
1. Ekstrak ZIP ini.
2. Buka folder `sors-plus` (folder yang berisi `settings.gradle.kts`) di Android Studio.
3. Tunggu Gradle Sync selesai.
4. Pilih perangkat Android atau emulator (Android 6.0 / API 23 atau lebih baru).
5. Tekan **Run**.

## Build APK melalui GitHub Actions
1. Buat repository GitHub baru.
2. Unggah seluruh isi folder proyek ini ke root repository (pastikan `settings.gradle.kts` ada di root, bukan di subfolder tambahan).
3. Buka tab **Actions** dan aktifkan workflow bila GitHub meminta.
4. Jalankan workflow **Build Android APK** melalui **Run workflow**, atau push ke branch `main`/`master`.
5. Setelah job hijau, buka hasil run dan unduh artifact `sors-plus-debug-apk`.
6. Ekstrak artifact dan instal `app-debug.apk` di perangkat uji. Android mungkin meminta izin instalasi dari sumber tersebut.

APK debug bukan rilis Play Store. Untuk distribusi publik, buat signing key sendiri, simpan rahasia signing secara aman, lalu build APK/AAB release yang ditandatangani.

## Struktur folder
```text
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/sorsplus/app/MainActivity.kt
├── docs/
│   ├── ARCHITECTURE.md
│   ├── TESTING.md
│   └── RELEASE.md
├── .github/workflows/build-apk.yml
├── build.gradle.kts
├── gradle.properties
└── settings.gradle.kts
```

## Dokumentasi tambahan
- [Arsitektur aplikasi](docs/ARCHITECTURE.md)
- [Checklist pengujian](docs/TESTING.md)
- [Build dan rilis](docs/RELEASE.md)

## Batasan yang diketahui
- Riwayat dan preferensi tema/bahasa belum dipersistenkan; riwayat hilang saat proses aplikasi dihentikan.
- Bahasa saat ini mengganti label utama, belum seluruh pesan validasi/parser.
- Konverter belum mencakup area, volume, kecepatan, dan data digital seperti versi HTML sebelumnya.
- Belum ada test otomatis unit/UI dan belum ada hasil build yang diverifikasi di perangkat dalam paket ini.
- Untuk hasil ilmiah/teknis, verifikasi angka dan satuan secara independen sebelum digunakan pada keputusan penting.

## Lisensi
Belum ditentukan. Tambahkan file LICENSE sebelum distribusi publik.

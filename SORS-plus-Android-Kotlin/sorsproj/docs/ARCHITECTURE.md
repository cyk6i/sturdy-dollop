# Arsitektur Aplikasi

## Ringkasan
Aplikasi memakai Kotlin, Android Activity, dan Jetpack Compose untuk UI native. Seluruh perhitungan dilakukan secara lokal; manifest tidak meminta izin jaringan.

## Komponen utama
- `MainActivity`: entry point Android dan pemanggil `setContent`.
- `CalculatorApp`: state tampilan, pemilihan layar, tema, bahasa, DEG/RAD, dan daftar riwayat selama sesi.
- `CalculatorPad`: tombol input kalkulator.
- `ExpressionParser`: parser recursive-descent untuk operasi matematika. Tidak menggunakan `eval`.
- `ConverterScreen` dan `convert`: konversi panjang, massa, suhu.
- `PhysicsScreen`: kelajuan rata-rata (`v = s/t`) dan gaya (`F = m·a`).
- `ChemistryScreen`: jumlah mol (`n = m/M`) dan jumlah mol dari molaritas (`n = C·V`).

## Parser ekspresi
Prioritas implementasi: faktorial/persen, pangkat (asosiatif ke kanan), tanda unary, perkalian/pembagian, lalu penjumlahan/pengurangan. Karena itu `-2^2` bernilai `-4`, sedangkan `(-2)^2` bernilai `4`. Fungsi ilmiah memakai kurung, misalnya `sin(30)`. DEG adalah default; tombol DEG/RAD mengganti satuan sudut.

## Batasan desain saat ini
State disimpan menggunakan Compose `remember`, bukan database atau DataStore. Karena itu riwayat, tema, bahasa, dan ekspresi tidak dijamin bertahan setelah proses aplikasi berhenti. Untuk pengembangan lanjut, pisahkan parser, konversi, dan rumus menjadi domain layer serta tambahkan unit test.

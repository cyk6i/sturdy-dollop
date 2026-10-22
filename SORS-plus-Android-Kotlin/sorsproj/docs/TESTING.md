# Checklist Pengujian Manual

Status semua item di bawah ini: **belum diverifikasi pada perangkat/build final**. Tandai lulus hanya setelah menjalankan pengujian.

## Kalkulator
- [ ] `2+3*4` menghasilkan `14`.
- [ ] `(2+3)*4` menghasilkan `20`.
- [ ] `10/0` menampilkan error, bukan hasil valid.
- [ ] `sqrt(9)` menghasilkan `3`.
- [ ] `-2^2` menghasilkan `-4`, sedangkan `(-2)^2` menghasilkan `4`.
- [ ] `2^3^2` menghasilkan `512` dan `2^-2` menghasilkan `0.25`.
- [ ] Dalam DEG, `sin(30)` menghasilkan sekitar `0.5`.
- [ ] Dalam RAD, `sin(π/2)` menghasilkan sekitar `1`.
- [ ] `5!` menghasilkan `120`.
- [ ] Input tidak lengkap seperti `2+` ditangani tanpa crash.
- [ ] Tombol hapus satu karakter dan AC bekerja.
- [ ] Riwayat dapat dipilih untuk memuat kembali ekspresi.

## Konverter
- [ ] `1 m` ke `cm` menghasilkan `100 cm`.
- [ ] `0 °C` ke `°F` menghasilkan `32 °F`.
- [ ] Nilai kosong atau tidak valid tidak membuat aplikasi crash.
- [ ] Nilai sangat besar atau `NaN` tidak membuat aplikasi crash.

## Fisika dan kimia
- [ ] Jarak 100 m / waktu 20 s menghasilkan 5 m/s.
- [ ] Massa 2 kg × percepatan 9.8 m/s² menghasilkan 19.6 N.
- [ ] Massa 18 g / massa molar 18 g/mol menghasilkan sekitar 1 mol.
- [ ] Input nol, negatif, atau kosong ditinjau sesuai konteks rumus.

## UI Android
- [ ] Uji layar kecil dan besar, portrait, serta keyboard terbuka.
- [ ] Semua tombol dapat disentuh dan tidak tertutup navigation bar.
- [ ] Tema terang/gelap tetap terbaca.
- [ ] Bahasa dapat diganti tanpa crash.
- [ ] Uji Android API 23 dan versi target terbaru yang tersedia.

## Build
- [ ] Gradle Sync sukses pada JDK 17.
- [ ] `assembleDebug` sukses.
- [ ] APK terpasang dan dapat dibuka pada perangkat fisik.

# SawitSimulator — Game Tanam Sawit (Studi Kasus PBO)

Studi kasus Pemrograman Berorientasi Objek (PBO) bertema **Game Tanam Sawit** yang mendemonstrasikan penerapan **Abstract Class**, **Abstract Method**, **Interface**, **Inheritance**, **Encapsulation**, dan **Polymorphism** (Compile-time & Runtime) menggunakan bahasa **Java**.

## Identitas Kelompok

- **Kelas** : 3D
- **Anggota 1** : Wildan Afandika (`F1D02510141`)
- **Anggota 2** : *(Isi Nama & NIM teman diskusi)*
- **Topik** : Abstract Class, Interface, Inheritance, dan Polymorphism

---

## Class Diagram

![UML Class Diagram - SawitSimulator](docs/class-diagram.png)

> Sumber diagram juga tersedia dalam format teks UML di [`docs/class-diagram.mmd`](docs/class-diagram.mmd) (Mermaid) dan [`docs/class-diagram.puml`](docs/class-diagram.puml) (PlantUML).

---

## Struktur Proyek

```text
SawitSimulator/
├── docs/
│   ├── class-diagram.png        # Gambar UML Class Diagram (untuk forum & dokumentasi)
│   ├── class-diagram.mmd        # Sumber diagram Mermaid
│   └── class-diagram.puml       # Sumber diagram PlantUML
└── src/com/sawitsimulator/
    ├── Main.java                # Entry point (mode simulasi otomatis & mode interaktif --play)
    ├── interfaces/
    │   ├── DapatDirawat.java    # Interface kontrak perawatan (siram & pupuk)
    │   └── DapatDipanen.java    # Interface kontrak panen & nilai ekonomi TBS
    ├── model/
    │   ├── TanamanSawit.java    # Abstract Class dasar seluruh varietas sawit
    │   ├── SawitTenera.java     # Subclass varietas hibrida unggul (matang 30 bln)
    │   ├── SawitDura.java       # Subclass varietas tahan kering (matang 36 bln)
    │   ├── SawitTopaz.java      # Subclass varietas cepat panen (matang 24 bln)
    │   └── BibitPolybag.java    # Implementor DapatDirawat non-sawit (pembibitan)
    └── service/
        └── KebunSawit.java      # Pengelola kebun (demonstrasi Polymorphism via koleksi)
```

---

## Penjelasan Konsep OOP yang Diterapkan

### 1. Interface (`DapatDirawat` & `DapatDipanen`)

Interface digunakan untuk mendefinisikan **kontrak kemampuan (behavior)** yang dapat diterapkan lintas hierarki class tanpa terikat hubungan pewarisan tunggal (*single inheritance*):

- [`DapatDirawat.java`](src/com/sawitsimulator/interfaces/DapatDirawat.java): Mendeklarasikan `siram(int literAir)`, `beriPupuk(String jenisPupuk)`, dan `getNamaObjek()`.
- [`DapatDipanen.java`](src/com/sawitsimulator/interfaces/DapatDipanen.java): Mendeklarasikan `isSiapPanen()`, `hitungHasilPanenKg()`, dan `panen(int hargaPerKg)`.

Pemisahan dua interface ini mengikuti **Interface Segregation Principle**:
- `TanamanSawit` mengimplementasikan **keduanya** (`DapatDirawat` dan `DapatDipanen`) karena pohon sawit perlu dirawat sekaligus dapat dipanen Tandan Buah Segar (TBS)-nya.
- [`BibitPolybag.java`](src/com/sawitsimulator/model/BibitPolybag.java) **bukan** turunan `TanamanSawit`, namun tetap mengimplementasikan `DapatDirawat` saja karena bibit di polybag perlu disiram dan dipupuk tetapi belum menghasilkan TBS untuk dipanen.

### 2. Abstract Class & Abstract Method (`TanamanSawit`)

[`TanamanSawit.java`](src/com/sawitsimulator/model/TanamanSawit.java) dideklarasikan sebagai `public abstract class TanamanSawit implements DapatDirawat, DapatDipanen`:

- **Tidak dapat diinstansiasi langsung** (`new TanamanSawit(...)` akan error saat kompilasi) karena sebuah pohon sawit di dunia nyata selalu memiliki varietas spesifik.
- **Menyimpan state bersama** (`idBlok`, `namaVarietas`, `umurBulan`, `kesehatan`, `kadarAir`, `siapPanen`) dan **concrete method** yang dapat langsung dipakai ulang oleh seluruh subclass (`siram`, `beriPupuk`, `panen`, `getStatusRingkas`).
- **Memiliki 3 Abstract Method** yang **wajib** diimplementasikan oleh setiap subclass karena tiap varietas sawit memiliki karakter agronomi berbeda:
  - `public abstract void tumbuh(int jumlahBulan);`
  - `public abstract double hitungHasilPanenKg();`
  - `public abstract String getKebutuhanLahan();`
- **Template Method Pattern** pada `panen(int hargaPerKg)`: alur pengecekan kesiapan panen, perhitungan pendapatan, serta efek kelelahan pohon ditangani di `TanamanSawit`, sedangkan rumus berat TBS (`hitungHasilPanenKg()`) didelegasikan secara polimorfik ke subclass.

### 3. Inheritance (Pewarisan) & Method Overriding

Tiga varietas kelapa sawit mewarisi `TanamanSawit` menggunakan keyword `extends`:

| Subclass | Umur Matang | Karakteristik Unik & Method yang Di-override |
| :--- | :---: | :--- |
| [`SawitTenera`](src/com/sawitsimulator/model/SawitTenera.java) | 30 bulan | Hibrida unggul dengan `rendemenMinyak = 0.24` dan `levelNutrisi`. Meng-override `beriPupuk(String, int)`, `tumbuh()`, `hitungHasilPanenKg()`, `getKebutuhanLahan()`. |
| [`SawitDura`](src/com/sawitsimulator/model/SawitDura.java) | 36 bulan | Bercangkang tebal dan tahan kemarau (`ketahananKering = 0.85`). Konsumsi air sangat hemat pada `tumbuh()`, bobot TBS stabil namun lebih rendah. |
| [`SawitTopaz`](src/com/sawitsimulator/model/SawitTopaz.java) | 24 bulan | Varietas klonal cepat panen dengan `faktorProduktivitas = 1.35`, tetapi boros air. Meng-override `siram()` (serapan cepat), `tumbuh()`, `hitungHasilPanenKg()`, `getKebutuhanLahan()`. |

### 4. Polymorphism (Compile-time & Runtime)

1. **Runtime Polymorphism via Abstract Class (`List<TanamanSawit>`)**
   Di [`KebunSawit.java`](src/com/sawitsimulator/service/KebunSawit.java) (`lewatiWaktu()`), seluruh varietas disimpan sebagai referensi `TanamanSawit`. Saat `tanaman.tumbuh(jumlahBulan)` dipanggil, JVM menjalankan implementasi milik `SawitTenera`, `SawitDura`, atau `SawitTopaz` secara dinamis (*dynamic dispatch*).
2. **Runtime Polymorphism via Interface (`List<DapatDirawat>` & `List<DapatDipanen>`)**
   - Method `rawatSemua()` memanggil `objek.siram(...)` dan `objek.beriPupuk(...)` untuk **pohon sawit sekaligus `BibitPolybag`** dalam satu loop tanpa `instanceof` / pengecekan tipe manual.
   - Method `panenSemua()` memproses panen seragam melalui referensi `DapatDipanen`.
3. **Compile-time Polymorphism (Method Overloading)**
   Di `TanamanSawit`, method `beriPupuk(String jenisPupuk)` di-*overload* dengan `beriPupuk(String jenisPupuk, int dosisKg)` untuk pemupukan dengan dosis khusus.

### 5. Encapsulation

Seluruh atribut penting (`idBlok`, `namaVarietas`, `saldoRupiah`, dll.) disembunyikan dengan modifier `private`/`protected` dan perubahan nilai seperti `kesehatan` serta `kadarAir` dijaga dalam rentang aman `0..100` melalui helper `clamp()`.

---

## Ringkasan Perbandingan: Abstract Class vs Interface di Studi Kasus Ini

| Aspek | Abstract Class (`TanamanSawit`) | Interface (`DapatDirawat`, `DapatDipanen`) |
| :--- | :--- | :--- |
| Hubungan | *Is-A* (`SawitTenera` adalah `TanamanSawit`) | *Can-Do* (`BibitPolybag` dapat dirawat) |
| State / Atribut | Menyimpan field (`umurBulan`, `kesehatan`, `kadarAir`) | Tidak menyimpan instance state |
| Pewarisan | Single inheritance (`extends` 1 class) | Multiple implementation (`implements` banyak interface) |
| Kapan dipakai | Saat beberapa class berbagi kerangka, atribut, dan logika dasar | Saat class dari hierarki berbeda berbagi kontrak perilaku |

---

## Cara Kompilasi & Menjalankan Program

Pastikan JDK sudah terpasang (`javac` & `java`).

### 1. Kompilasi

**PowerShell (Windows):**
```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -d out (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

**Bash (Linux/macOS):**
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

### 2. Menjalankan Mode Simulasi Otomatis (Default)

```bash
java -cp out com.sawitsimulator.Main
```

### 3. Menjalankan Mode Game Interaktif

```bash
java -cp out com.sawitsimulator.Main --play
```

---

## Contoh Output Simulasi

```text
======================================================================
               SAWIT SIMULATOR - GAME TANAM SAWIT (PBO)               
======================================================================

=== PENANAMAN VARIETAS SAWIT & PEMBIBITAN ===
  [+] Menanam Sawit Tenera [Blok-A1] -> Kebutuhan: Lahan mineral subur, curah hujan merata, pemupukan rutin.
  [+] Menanam Sawit Dura [Blok-B1] -> Kebutuhan: Lahan marjinal / berbukit, tahan musim kemarau panjang.
  [+] Menanam Sawit Topaz [Blok-C1] -> Kebutuhan: Lahan datar dengan sistem irigasi intensif dan kontrol hama ketat.
  [+] Menambahkan Bibit Polybag [Nursery-01] ke area perawatan.

...

=== PANEN RAYA (Harga TBS: Rp2.850/kg | Polymorphism via DapatDipanen) ===
  [Panen] Sawit Tenera [Blok-A1] BELUM siap panen (Umur: 24 bulan).
  [Panen] Sawit Dura [Blok-B1] BELUM siap panen (Umur: 24 bulan).
  [Panen] Sawit Topaz [Blok-C1] menghasilkan 31,05 kg TBS -> Pendapatan: Rp88.492
  Total Pendapatan Panen: Rp88.492 | Saldo Kebun: Rp5.088.492

...

=== PANEN RAYA (Harga TBS: Rp2.850/kg | Polymorphism via DapatDipanen) ===
  [Panen] Sawit Tenera [Blok-A1] menghasilkan 31,25 kg TBS -> Pendapatan: Rp89.057
  [Panen] Sawit Dura [Blok-B1] menghasilkan 15,52 kg TBS -> Pendapatan: Rp44.218
  [Panen] Sawit Topaz [Blok-C1] menghasilkan 31,05 kg TBS -> Pendapatan: Rp88.492
  Total Pendapatan Panen: Rp221.767 | Saldo Kebun: Rp5.310.259
```

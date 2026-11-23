/**
 * Class GameSawit mengatur seluruh logika permainan Sawit Farm:
 * grid lahan 4x4, koin, hari permainan, tanam, siram, pupuk, hari berikutnya, dan panen.
 *
 * Konsep OOP:
 * - POLYMORPHISM: Menyimpan seluruh tanaman dalam array bertipe TanamanSawit[][].
 *   Pemanggilan tumbuh(), getStatus(), getSimbol(), siram(), beriPupuk(), dan panen()
 *   otomatis mengikuti tipe subclass yang sedang menempati petak tersebut.
 */
public class GameSawit {

    public static final int UKURAN_GRID = 4;
    public static final int KOIN_AWAL = 100;
    public static final int HARGA_BIBIT = 20;
    public static final int HARGA_PUPUK = 5;
    public static final int HASIL_PANEN = SawitDewasa.HASIL_PANEN_KOIN;

    private final TanamanSawit[][] lahan;
    private int koin;
    private int hari;
    private int totalPanen;

    public GameSawit() {
        this.lahan = new TanamanSawit[UKURAN_GRID][UKURAN_GRID];
        this.koin = KOIN_AWAL;
        this.hari = 1;
        this.totalPanen = 0;
    }

    /**
     * Menanam bibit sawit pada petak kosong (biaya 20 koin).
     */
    public String tanam(int baris, int kolom) {
        if (!posisiValid(baris, kolom)) {
            return "Pilih petak lahan terlebih dahulu.";
        }
        if (lahan[baris][kolom] != null) {
            return "Petak (" + (baris + 1) + "," + (kolom + 1) + ") sudah ditanami! Pilih tanah kosong.";
        }
        if (koin < HARGA_BIBIT) {
            return "Koin tidak cukup! Butuh " + HARGA_BIBIT + " koin untuk membeli bibit sawit.";
        }

        koin -= HARGA_BIBIT;
        // Polymorphism: objek BibitSawit disimpan dalam referensi TanamanSawit
        lahan[baris][kolom] = new BibitSawit();
        return "Berhasil menanam Bibit Sawit di petak (" + (baris + 1) + "," + (kolom + 1) + ") [-" + HARGA_BIBIT + " koin].";
    }

    /**
     * Menyiram tanaman pada petak terpilih (gratis / 0 koin) via interface BisaDirawat.
     */
    public String siram(int baris, int kolom) {
        TanamanSawit tanaman = getTanaman(baris, kolom);
        if (tanaman == null) {
            return "Tidak ada tanaman di petak ini untuk disiram.";
        }
        if (tanaman.getAir() >= TanamanSawit.MAKS_AIR) {
            return "Tanaman di petak (" + (baris + 1) + "," + (kolom + 1) + ") sudah cukup air (" + tanaman.getAir() + "/" + TanamanSawit.MAKS_AIR + ").";
        }

        // Memanggil method melalui referensi interface BisaDirawat
        BisaDirawat dirawat = tanaman;
        dirawat.siram();
        return "Menyiram " + tanaman.getSimbol() + " di petak (" + (baris + 1) + "," + (kolom + 1) + ") [Air: " + tanaman.getAir() + "/" + TanamanSawit.MAKS_AIR + "].";
    }

    /**
     * Memberi pupuk pada tanaman di petak terpilih (biaya 5 koin) via interface BisaDirawat.
     */
    public String beriPupuk(int baris, int kolom) {
        TanamanSawit tanaman = getTanaman(baris, kolom);
        if (tanaman == null) {
            return "Tidak ada tanaman di petak ini untuk diberi pupuk.";
        }
        if (tanaman.getPupuk() >= TanamanSawit.MAKS_PUPUK) {
            return "Pupuk tanaman di petak (" + (baris + 1) + "," + (kolom + 1) + ") sudah penuh (" + tanaman.getPupuk() + "/" + TanamanSawit.MAKS_PUPUK + ").";
        }
        if (koin < HARGA_PUPUK) {
            return "Koin tidak cukup! Butuh " + HARGA_PUPUK + " koin untuk memberi pupuk.";
        }

        koin -= HARGA_PUPUK;
        BisaDirawat dirawat = tanaman;
        dirawat.beriPupuk();
        return "Memberi pupuk pada " + tanaman.getSimbol() + " di petak (" + (baris + 1) + "," + (kolom + 1)
                + ") [-" + HARGA_PUPUK + " koin | Pupuk: " + tanaman.getPupuk() + "/" + TanamanSawit.MAKS_PUPUK + "].";
    }

    /**
     * Memajukan permainan 1 hari: memanggil tumbuh() dan cekEvolusi() secara polimorfik
     * pada seluruh tanaman yang ada di grid lahan.
     */
    public String hariBerikutnya() {
        hari++;
        int jumlahTumbuh = 0;
        int jumlahNaikTahap = 0;
        int jumlahSiapPanen = 0;

        for (int b = 0; b < UKURAN_GRID; b++) {
            for (int k = 0; k < UKURAN_GRID; k++) {
                TanamanSawit tanaman = lahan[b][k];
                if (tanaman == null) {
                    continue;
                }
                int umurSebelum = tanaman.getUmur();
                boolean panenSebelum = tanaman.isSiapPanen();

                // POLYMORPHISM: tumbuh() berjalan sesuai subclass (Bibit / Muda / Dewasa)
                tanaman.tumbuh();
                if (tanaman.getUmur() > umurSebelum || (!panenSebelum && tanaman.isSiapPanen())) {
                    jumlahTumbuh++;
                }

                // Evolusi fase ke subclass berikutnya sambil menjaga data umur, air, pupuk
                TanamanSawit hasilEvolusi = tanaman.cekEvolusi();
                if (hasilEvolusi != tanaman) {
                    lahan[b][k] = hasilEvolusi;
                    jumlahNaikTahap++;
                }
                if (lahan[b][k].isSiapPanen()) {
                    jumlahSiapPanen++;
                }
            }
        }

        return "Hari ke-" + hari + " dimulai! "
                + jumlahTumbuh + " tanaman berkembang, "
                + jumlahNaikTahap + " naik tahap, "
                + jumlahSiapPanen + " siap panen.";
    }

    /**
     * Memanen buah sawit dewasa yang sudah siap panen (+50 koin).
     */
    public String panen(int baris, int kolom) {
        TanamanSawit tanaman = getTanaman(baris, kolom);
        if (tanaman == null) {
            return "Tidak ada tanaman di petak ini untuk dipanen.";
        }
        if (!tanaman.isSiapPanen()) {
            return "Belum bisa dipanen! Status: " + tanaman.getStatus();
        }

        int pendapatan = tanaman.panen();
        koin += pendapatan;
        totalPanen++;
        return "Panen berhasil di petak (" + (baris + 1) + "," + (kolom + 1) + ")! Dapat +" + pendapatan
                + " koin. Rawat lagi agar berbuah kembali.";
    }

    public TanamanSawit getTanaman(int baris, int kolom) {
        if (!posisiValid(baris, kolom)) {
            return null;
        }
        return lahan[baris][kolom];
    }

    public boolean posisiValid(int baris, int kolom) {
        return baris >= 0 && baris < UKURAN_GRID && kolom >= 0 && kolom < UKURAN_GRID;
    }

    public int getKoin() {
        return koin;
    }

    public int getHari() {
        return hari;
    }

    public int getTotalPanen() {
        return totalPanen;
    }

    /**
     * Jumlah bibit yang dapat dibeli pemain berdasarkan koin saat ini.
     */
    public int getBibitTersedia() {
        return koin / HARGA_BIBIT;
    }

    public int getJumlahTanamanAktif() {
        int total = 0;
        for (int b = 0; b < UKURAN_GRID; b++) {
            for (int k = 0; k < UKURAN_GRID; k++) {
                if (lahan[b][k] != null) {
                    total++;
                }
            }
        }
        return total;
    }
}

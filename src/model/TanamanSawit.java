package model;

import interfaces.Perawatan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Abstract Class TanamanSawit merupakan kelas dasar untuk seluruh
 * fase tanaman kelapa sawit di sistem manajemen perkebunan.
 *
 * Konsep OOP:
 * - ABSTRACT CLASS: Tidak dapat diinstansiasi langsung; harus diturunkan ke subclass konkret.
 * - ENCAPSULATION: Seluruh atribut bertipe private dan diakses melalui getter/setter tervalidasi.
 * - INTERFACE: Mengimplementasikan kontrak Perawatan.
 * - ABSTRACT METHOD: hitungEstimasiPanen(), getStatusPertumbuhan(), dan hitungKebutuhanPupuk()
 *   wajib di-override oleh setiap subclass sesuai perilaku fase pertumbuhannya.
 */
public abstract class TanamanSawit implements Perawatan {

    private String idTanaman;
    private LocalDate tanggalTanam;
    private double luasLahan;      // dalam satuan hektar (Ha)
    private String lokasiLahan;

    protected TanamanSawit(String idTanaman, LocalDate tanggalTanam, double luasLahan, String lokasiLahan) {
        setIdTanaman(idTanaman);
        setTanggalTanam(tanggalTanam);
        setLuasLahan(luasLahan);
        setLokasiLahan(lokasiLahan);
    }

    // =========================================================================
    // Abstract Methods (wajib diimplementasikan oleh SawitMuda & SawitProduktif)
    // =========================================================================

    /**
     * Menghitung estimasi hasil panen Tandan Buah Segar (TBS) per tahun dalam satuan ton.
     * Nilai bersifat estimasi simulasi berdasarkan luas lahan dan fase pertumbuhan.
     *
     * @return estimasi panen dalam ton TBS per tahun
     */
    public abstract double hitungEstimasiPanen();

    /**
     * Mengembalikan deskripsi status pertumbuhan tanaman sesuai fasenya.
     *
     * @return status pertumbuhan
     */
    public abstract String getStatusPertumbuhan();

    /**
     * Menghitung kebutuhan pupuk tahunan berdasarkan luas lahan dan fase pertumbuhan.
     *
     * @return kebutuhan pupuk dalam kilogram (kg) per tahun
     */
    public abstract double hitungKebutuhanPupuk();

    /**
     * Mengembalikan label jenis/fase tanaman untuk identifikasi di tabel & penyimpanan.
     *
     * @return nama fase tanaman
     */
    public abstract String getJenisFase();

    /**
     * Mengembalikan nilai parameter khusus fase untuk kebutuhan penyimpanan lokal.
     *
     * @return nilai parameter khusus fase
     */
    public abstract double getParameterKhusus();

    /**
     * Mengembalikan keterangan asumsi perhitungan estimasi panen untuk fase ini.
     *
     * @return teks asumsi perhitungan
     */
    public abstract String getKeteranganEstimasi();

    // =========================================================================
    // Helper Method Umum
    // =========================================================================

    /**
     * Menghitung umur tanaman dalam bulan berdasarkan tanggal tanam hingga hari ini.
     *
     * @return umur tanaman dalam bulan (minimal 0)
     */
    public long getUmurBulan() {
        long bulan = ChronoUnit.MONTHS.between(tanggalTanam, LocalDate.now());
        return Math.max(0, bulan);
    }

    // =========================================================================
    // Getter & Setter dengan Validasi Data (Encapsulation)
    // =========================================================================

    public String getIdTanaman() {
        return idTanaman;
    }

    public void setIdTanaman(String idTanaman) {
        if (idTanaman == null || idTanaman.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tanaman tidak boleh kosong.");
        }
        String bersih = idTanaman.trim().toUpperCase();
        if (bersih.contains(",") || bersih.contains("|") || bersih.contains("\n")) {
            throw new IllegalArgumentException("ID tanaman tidak boleh mengandung tanda koma atau karakter pemisah.");
        }
        this.idTanaman = bersih;
    }

    public LocalDate getTanggalTanam() {
        return tanggalTanam;
    }

    public void setTanggalTanam(LocalDate tanggalTanam) {
        if (tanggalTanam == null) {
            throw new IllegalArgumentException("Tanggal tanam tidak boleh kosong.");
        }
        if (tanggalTanam.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tanggal tanam tidak boleh melebihi tanggal hari ini.");
        }
        this.tanggalTanam = tanggalTanam;
    }

    public double getLuasLahan() {
        return luasLahan;
    }

    public void setLuasLahan(double luasLahan) {
        if (Double.isNaN(luasLahan) || Double.isInfinite(luasLahan) || luasLahan <= 0) {
            throw new IllegalArgumentException("Luas lahan harus lebih besar dari 0 hektar.");
        }
        if (luasLahan > 10000) {
            throw new IllegalArgumentException("Luas lahan melebihi batas wajar (maksimal 10.000 Ha per blok).");
        }
        this.luasLahan = luasLahan;
    }

    public String getLokasiLahan() {
        return lokasiLahan;
    }

    public void setLokasiLahan(String lokasiLahan) {
        if (lokasiLahan == null || lokasiLahan.trim().isEmpty()) {
            throw new IllegalArgumentException("Lokasi lahan tidak boleh kosong.");
        }
        String bersih = lokasiLahan.trim();
        if (bersih.contains("|") || bersih.contains("\n")) {
            throw new IllegalArgumentException("Lokasi lahan tidak boleh mengandung karakter '|'.");
        }
        this.lokasiLahan = bersih;
    }

    @Override
    public String toString() {
        return idTanaman + " - " + getJenisFase() + " (" + lokasiLahan + ")";
    }
}

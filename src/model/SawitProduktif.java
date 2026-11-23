package model;

import java.time.LocalDate;

/**
 * Subclass SawitProduktif merepresentasikan tanaman kelapa sawit pada fase
 * Tanaman Menghasilkan (TM) yang sudah dapat dipanen Tandan Buah Segar (TBS).
 *
 * Konsep OOP:
 * - INHERITANCE: Mewarisi atribut dan method umum dari TanamanSawit.
 * - METHOD OVERRIDING & POLYMORPHISM: Mengimplementasikan perhitungan estimasi panen,
 *   kebutuhan pupuk, status pertumbuhan, dan jadwal perawatan khusus fase produktif.
 */
public class SawitProduktif extends TanamanSawit {

    private static final double DOSIS_PUPUK_KG_PER_HA = 650.0; // Asumsi dosis TM per tahun
    private double targetTonPerHa; // Asumsi produktivitas simulasi (Ton TBS / Ha / Tahun)

    public SawitProduktif(String idTanaman, LocalDate tanggalTanam, double luasLahan,
                          String lokasiLahan, double targetTonPerHa) {
        super(idTanaman, tanggalTanam, luasLahan, lokasiLahan);
        setTargetTonPerHa(targetTonPerHa);
    }

    public double getTargetTonPerHa() {
        return targetTonPerHa;
    }

    public void setTargetTonPerHa(double targetTonPerHa) {
        if (Double.isNaN(targetTonPerHa) || targetTonPerHa < 1.0 || targetTonPerHa > 40.0) {
            throw new IllegalArgumentException("Target produktivitas harus antara 1,0 sampai 40,0 ton/Ha/tahun.");
        }
        this.targetTonPerHa = targetTonPerHa;
    }

    private double getFaktorUmur() {
        long umurBulan = getUmurBulan();
        if (umurBulan < 48) {
            return 0.75; // TM awal (produksi bertahap)
        } else if (umurBulan <= 180) {
            return 1.00; // TM puncak (umur 4 - 15 tahun)
        }
        return 0.80; // TM tua (> 15 tahun)
    }

    @Override
    public double hitungEstimasiPanen() {
        // Rumus estimasi simulasi: Luas (Ha) x Asumsi Ton/Ha/Thn x Faktor Umur
        return getLuasLahan() * targetTonPerHa * getFaktorUmur();
    }

    @Override
    public String getStatusPertumbuhan() {
        long umur = getUmurBulan();
        if (umur < 48) {
            return "TM Awal - Mulai Produktif (" + umur + " bln)";
        } else if (umur <= 180) {
            return "TM Puncak - Produktivitas Optimal (" + umur + " bln)";
        }
        return "TM Tua - Produksi Menurun (" + umur + " bln)";
    }

    @Override
    public double hitungKebutuhanPupuk() {
        // Tanaman produktif memerlukan dosis pupuk makro & mikro lebih tinggi
        return getLuasLahan() * DOSIS_PUPUK_KG_PER_HA;
    }

    @Override
    public String jadwalkanPemupukan() {
        return "Setiap 6 bulan sekali (NPK, MOP/KCl, Dolomit & Borate untuk kualitas TBS)";
    }

    @Override
    public String jadwalkanPenyiraman() {
        return "Setiap 2-3 hari sekali pada musim kemarau melalui pengelolaan parit irigasi";
    }

    @Override
    public String getJenisFase() {
        return "Sawit Produktif";
    }

    @Override
    public double getParameterKhusus() {
        return targetTonPerHa;
    }

    @Override
    public String getKeteranganEstimasi() {
        return String.format("Asumsi %.1f ton/Ha/thn x faktor umur %.2f (estimasi simulasi)",
                targetTonPerHa, getFaktorUmur());
    }
}

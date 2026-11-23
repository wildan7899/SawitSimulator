package model;

import java.time.LocalDate;

/**
 * Subclass SawitMuda merepresentasikan tanaman kelapa sawit pada fase
 * Tanaman Belum Menghasilkan (TBM / vegetatif).
 *
 * Konsep OOP:
 * - INHERITANCE: Mewarisi atribut dan method umum dari TanamanSawit.
 * - METHOD OVERRIDING & POLYMORPHISM: Mengimplementasikan abstract method
 *   dan method interface Perawatan dengan perilaku khusus fase muda.
 */
public class SawitMuda extends TanamanSawit {

    private static final double DOSIS_PUPUK_KG_PER_HA = 350.0; // Asumsi dosis vegetatif per tahun
    private double tinggiRataRataCm;

    public SawitMuda(String idTanaman, LocalDate tanggalTanam, double luasLahan,
                     String lokasiLahan, double tinggiRataRataCm) {
        super(idTanaman, tanggalTanam, luasLahan, lokasiLahan);
        setTinggiRataRataCm(tinggiRataRataCm);
    }

    public double getTinggiRataRataCm() {
        return tinggiRataRataCm;
    }

    public void setTinggiRataRataCm(double tinggiRataRataCm) {
        if (Double.isNaN(tinggiRataRataCm) || tinggiRataRataCm <= 0 || tinggiRataRataCm > 600) {
            throw new IllegalArgumentException("Tinggi rata-rata sawit muda harus antara 1 sampai 600 cm.");
        }
        this.tinggiRataRataCm = tinggiRataRataCm;
    }

    @Override
    public double hitungEstimasiPanen() {
        // Fase Tanaman Belum Menghasilkan (TBM) belum menghasilkan TBS komersial
        return 0.0;
    }

    @Override
    public String getStatusPertumbuhan() {
        long umur = getUmurBulan();
        if (umur < 12) {
            return "TBM 1 - Pembentukan Akar & Pelepah Awal (" + umur + " bln, "
                    + String.format("%.0f cm", tinggiRataRataCm) + ")";
        } else if (umur < 24) {
            return "TBM 2 - Pertumbuhan Vegetatif Aktif (" + umur + " bln, "
                    + String.format("%.0f cm", tinggiRataRataCm) + ")";
        }
        return "TBM 3 - Menjelang Fase Produktif (" + umur + " bln, "
                + String.format("%.0f cm", tinggiRataRataCm) + ")";
    }

    @Override
    public double hitungKebutuhanPupuk() {
        // Faktor koreksi ringan bila tinggi tanaman masih di bawah 100 cm
        double faktorTinggi = (tinggiRataRataCm < 100.0) ? 0.85 : 1.0;
        return getLuasLahan() * DOSIS_PUPUK_KG_PER_HA * faktorTinggi;
    }

    @Override
    public String jadwalkanPemupukan() {
        return "Setiap 3 bulan sekali (NPK 15-15-6-4 & Urea untuk pertumbuhan vegetatif)";
    }

    @Override
    public String jadwalkanPenyiraman() {
        return "2 kali sehari (pagi & sore) saat musim kemarau / kelembaban tanah rendah";
    }

    @Override
    public String getJenisFase() {
        return "Sawit Muda";
    }

    @Override
    public double getParameterKhusus() {
        return tinggiRataRataCm;
    }

    @Override
    public String getKeteranganEstimasi() {
        return "Fase TBM (Belum Menghasilkan): estimasi panen 0 ton/tahun.";
    }
}

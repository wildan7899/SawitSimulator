package com.sawitsimulator.model;

/**
 * Subclass SawitTopaz (varietas unggul klonal modern).
 * Karakteristik: Cepat berbuah (matang di umur 24 bulan) dan produktivitas
 * sangat tinggi, tetapi boros air serta sensitif jika kurang disiram.
 *
 * Konsep OOP:
 * - INHERITANCE: Mewarisi TanamanSawit.
 * - METHOD OVERRIDING: Override siram(), tumbuh(), hitungHasilPanenKg(),
 *   dan getKebutuhanLahan().
 */
public class SawitTopaz extends TanamanSawit {

    private static final int UMUR_MATANG_BULAN = 24;
    private final double faktorProduktivitas;

    public SawitTopaz(String idBlok) {
        super(idBlok, "Sawit Topaz");
        this.faktorProduktivitas = 1.35; // Produktivitas 35% di atas rata-rata
    }

    @Override
    public void siram(int literAir) {
        // Topaz menyerap air lebih agresif untuk mendukung pertumbuhan cepat
        if (literAir <= 0) {
            return;
        }
        int tambahanAir = literAir * 3;
        kadarAir = clamp(kadarAir + tambahanAir, 0, 100);
        kesehatan = clamp(kesehatan + 5, 0, 100);
        System.out.printf("  [Siram+] %s (serapan cepat) disiram %dL -> Kadar Air: %d%%, Kesehatan: %d%%%n",
                getNamaObjek(), literAir, kadarAir, kesehatan);
    }

    @Override
    public void tumbuh(int jumlahBulan) {
        if (jumlahBulan <= 0) {
            return;
        }
        umurBulan += jumlahBulan;
        // Konsumsi air lebih tinggi dibanding varietas lain
        kadarAir = clamp(kadarAir - (jumlahBulan * 3), 0, 100);

        if (kadarAir < 35) {
            // Sensitif terhadap kekeringan
            kesehatan = clamp(kesehatan - (jumlahBulan * 2), 0, 100);
        } else {
            kesehatan = clamp(kesehatan + 4, 0, 100);
        }

        if (umurBulan >= UMUR_MATANG_BULAN && kesehatan >= 60) {
            siapPanen = true;
        }
    }

    @Override
    public double hitungHasilPanenKg() {
        double faktorKesehatan = kesehatan / 100.0;
        double bonusKelembaban = (kadarAir >= 60) ? 1.15 : 0.90;
        return 20.0 * faktorKesehatan * faktorProduktivitas * bonusKelembaban;
    }

    @Override
    public String getKebutuhanLahan() {
        return "Lahan datar dengan sistem irigasi intensif dan kontrol hama ketat.";
    }

    public double getFaktorProduktivitas() {
        return faktorProduktivitas;
    }
}

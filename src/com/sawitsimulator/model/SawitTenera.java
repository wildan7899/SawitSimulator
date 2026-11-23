package com.sawitsimulator.model;

/**
 * Subclass SawitTenera (hibrida Dura x Pisifera).
 * Karakteristik: Varietas unggul dengan rendemen minyak tinggi, matang di umur
 * 30 bulan, dan responsif terhadap pemupukan NPK/Organik.
 *
 * Konsep OOP:
 * - INHERITANCE: Mewarisi atribut dan method dari abstract class TanamanSawit.
 * - METHOD OVERRIDING: Mengimplementasikan tumbuh(), hitungHasilPanenKg(),
 *   getKebutuhanLahan(), serta memperluas beriPupuk().
 */
public class SawitTenera extends TanamanSawit {

    private static final int UMUR_MATANG_BULAN = 30;
    private final double rendemenMinyak;
    private int levelNutrisi;

    public SawitTenera(String idBlok) {
        super(idBlok, "Sawit Tenera");
        this.rendemenMinyak = 0.24; // 24% rendemen CPO
        this.levelNutrisi = 1;
    }

    @Override
    public void beriPupuk(String jenisPupuk, int dosisKg) {
        super.beriPupuk(jenisPupuk, dosisKg);
        if (dosisKg > 0) {
            levelNutrisi = clamp(levelNutrisi + 1, 1, 5);
            System.out.printf("          -> Bonus Tenera: Level Nutrisi naik menjadi %d/5%n", levelNutrisi);
        }
    }

    @Override
    public void tumbuh(int jumlahBulan) {
        if (jumlahBulan <= 0) {
            return;
        }
        umurBulan += jumlahBulan;
        kadarAir = clamp(kadarAir - (jumlahBulan * 2), 0, 100);

        if (kadarAir < 30) {
            kesehatan = clamp(kesehatan - (jumlahBulan * 2), 0, 100);
        } else {
            kesehatan = clamp(kesehatan + (levelNutrisi * 2), 0, 100);
        }

        if (umurBulan >= UMUR_MATANG_BULAN && kesehatan >= 50) {
            siapPanen = true;
        }
    }

    @Override
    public double hitungHasilPanenKg() {
        // Berat TBS dasar 18 kg dipengaruhi kesehatan, nutrisi, dan rendemen
        double faktorKesehatan = kesehatan / 100.0;
        double bonusNutrisi = 1.0 + (levelNutrisi * 0.08);
        return 18.0 * faktorKesehatan * bonusNutrisi * (1.0 + rendemenMinyak);
    }

    @Override
    public String getKebutuhanLahan() {
        return "Lahan mineral subur, curah hujan merata, pemupukan rutin.";
    }

    public double getRendemenMinyak() {
        return rendemenMinyak;
    }

    public int getLevelNutrisi() {
        return levelNutrisi;
    }
}

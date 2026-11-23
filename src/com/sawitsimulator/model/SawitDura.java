package com.sawitsimulator.model;

/**
 * Subclass SawitDura (varietas cangkang tebal).
 * Karakteristik: Tahan kekeringan dan cocok untuk lahan marjinal, matang lebih
 * lama (36 bulan), hasil TBS lebih rendah dibanding Tenera/Topaz namun sangat tangguh.
 *
 * Konsep OOP:
 * - INHERITANCE: Mewarisi TanamanSawit.
 * - METHOD OVERRIDING: Logika ketahanan air unik pada tumbuh() dan rumus panen sendiri.
 */
public class SawitDura extends TanamanSawit {

    private static final int UMUR_MATANG_BULAN = 36;
    private final double ketahananKering;

    public SawitDura(String idBlok) {
        super(idBlok, "Sawit Dura");
        this.ketahananKering = 0.85; // Sangat tahan kekurangan air
    }

    @Override
    public void tumbuh(int jumlahBulan) {
        if (jumlahBulan <= 0) {
            return;
        }
        umurBulan += jumlahBulan;

        // Penurunan air lebih lambat karena ketahanan kering tinggi
        int konsumsiAir = Math.max(1, (int) Math.round(jumlahBulan * 2 * (1.2 - ketahananKering)));
        kadarAir = clamp(kadarAir - konsumsiAir, 0, 100);

        // Hanya turun kesehatan jika benar-benar kering (< 20%)
        if (kadarAir < 20) {
            kesehatan = clamp(kesehatan - jumlahBulan, 0, 100);
        } else {
            kesehatan = clamp(kesehatan + 2, 0, 100);
        }

        if (umurBulan >= UMUR_MATANG_BULAN && kesehatan >= 40) {
            siapPanen = true;
        }
    }

    @Override
    public double hitungHasilPanenKg() {
        // Cangkang tebal -> berat TBS stabil namun lebih rendah dari varietas hibrida
        double faktorKesehatan = kesehatan / 100.0;
        return 14.5 * faktorKesehatan * (0.9 + (ketahananKering * 0.2));
    }

    @Override
    public String getKebutuhanLahan() {
        return "Lahan marjinal / berbukit, tahan musim kemarau panjang.";
    }

    public double getKetahananKering() {
        return ketahananKering;
    }
}

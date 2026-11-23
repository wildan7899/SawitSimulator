package com.sawitsimulator.model;

import com.sawitsimulator.interfaces.DapatDipanen;
import com.sawitsimulator.interfaces.DapatDirawat;

/**
 * Abstract Class TanamanSawit merupakan blueprint dasar untuk seluruh
 * varietas pohon kelapa sawit di Game Tanam Sawit.
 *
 * Konsep OOP:
 * - ABSTRACT CLASS: Tidak dapat diinstansiasi langsung (new TanamanSawit),
 *   melainkan harus diturunkan (Inheritance) ke varietas konkret.
 * - ENCAPSULATION: Atribut dilindungi dengan modifier private/protected dan
 *   diubah melalui method terkontrol (clamp nilai 0..100).
 * - IMPLEMENTS INTERFACE: Mengimplementasikan DapatDirawat dan DapatDipanen.
 * - ABSTRACT METHOD: tumbuh(), hitungHasilPanenKg(), dan getKebutuhanLahan()
 *   wajib di-override oleh setiap subclass karena tiap varietas berbeda.
 * - COMPILE-TIME POLYMORPHISM (Overloading): beriPupuk(String) dan
 *   beriPupuk(String, int).
 */
public abstract class TanamanSawit implements DapatDirawat, DapatDipanen {

    private final String idBlok;
    private final String namaVarietas;
    protected int umurBulan;
    protected int kesehatan;   // 0 - 100
    protected int kadarAir;    // 0 - 100
    protected boolean siapPanen;

    protected TanamanSawit(String idBlok, String namaVarietas) {
        this.idBlok = idBlok;
        this.namaVarietas = namaVarietas;
        this.umurBulan = 0;
        this.kesehatan = 80;
        this.kadarAir = 60;
        this.siapPanen = false;
    }

    // =========================================================================
    // Concrete Methods (diwariskan ke semua subclass)
    // =========================================================================

    @Override
    public void siram(int literAir) {
        if (literAir <= 0) {
            return;
        }
        int tambahanAir = literAir * 2;
        kadarAir = clamp(kadarAir + tambahanAir, 0, 100);
        kesehatan = clamp(kesehatan + 3, 0, 100);
        System.out.printf("  [Siram] %s disiram %dL -> Kadar Air: %d%%, Kesehatan: %d%%%n",
                getNamaObjek(), literAir, kadarAir, kesehatan);
    }

    @Override
    public void beriPupuk(String jenisPupuk) {
        // Memanggil versi overloaded dengan dosis standar 2 kg
        beriPupuk(jenisPupuk, 2);
    }

    /**
     * Method Overloading (Compile-time Polymorphism):
     * Versi beriPupuk yang menerima parameter tambahan dosisKg.
     */
    public void beriPupuk(String jenisPupuk, int dosisKg) {
        if (dosisKg <= 0) {
            return;
        }
        int bonusKesehatan = Math.min(dosisKg * 5, 25);
        kesehatan = clamp(kesehatan + bonusKesehatan, 0, 100);
        System.out.printf("  [Pupuk] %s diberi pupuk %s (%dkg) -> Kesehatan: %d%%%n",
                getNamaObjek(), jenisPupuk, dosisKg, kesehatan);
    }

    /**
     * Template Method: Alur panen sama untuk semua varietas sawit, tetapi
     * perhitungan bobot TBS (hitungHasilPanenKg) didelegasikan ke masing-masing
     * subclass secara polimorfik.
     */
    @Override
    public int panen(int hargaPerKg) {
        if (!siapPanen) {
            System.out.printf("  [Panen] %s BELUM siap panen (Umur: %d bulan).%n",
                    getNamaObjek(), umurBulan);
            return 0;
        }

        double beratKg = hitungHasilPanenKg();
        int pendapatan = (int) Math.round(beratKg * hargaPerKg);

        // Setelah panen, tanaman mengalami kelelahan ringan dan masuk siklus buah berikutnya
        siapPanen = false;
        kadarAir = clamp(kadarAir - 15, 0, 100);
        kesehatan = clamp(kesehatan - 5, 0, 100);

        System.out.printf("  [Panen] %s menghasilkan %.2f kg TBS -> Pendapatan: Rp%,d%n",
                getNamaObjek(), beratKg, pendapatan);
        return pendapatan;
    }

    // =========================================================================
    // Abstract Methods (wajib diimplementasikan oleh setiap subclass varietas)
    // =========================================================================

    /**
     * Mensimulasikan pertumbuhan tanaman selama beberapa bulan.
     * Setiap varietas memiliki kecepatan matang dan kebutuhan air berbeda.
     */
    public abstract void tumbuh(int jumlahBulan);

    /**
     * Menghitung estimasi berat panen TBS (kg) sesuai karakteristik varietas.
     */
    @Override
    public abstract double hitungHasilPanenKg();

    /**
     * Menjelaskan karakteristik kebutuhan lahan untuk varietas tersebut.
     */
    public abstract String getKebutuhanLahan();

    // =========================================================================
    // Helper & Getter Methods
    // =========================================================================

    protected int clamp(int nilai, int min, int max) {
        return Math.max(min, Math.min(max, nilai));
    }

    @Override
    public String getNamaObjek() {
        return namaVarietas + " [" + idBlok + "]";
    }

    public String getIdBlok() {
        return idBlok;
    }

    public String getNamaVarietas() {
        return namaVarietas;
    }

    public int getUmurBulan() {
        return umurBulan;
    }

    public int getKesehatan() {
        return kesehatan;
    }

    public int getKadarAir() {
        return kadarAir;
    }

    @Override
    public boolean isSiapPanen() {
        return siapPanen;
    }

    public String getStatusRingkas() {
        return String.format("%-24s | Umur: %2d bln | Air: %3d%% | Sehat: %3d%% | Siap Panen: %s",
                getNamaObjek(), umurBulan, kadarAir, kesehatan, siapPanen ? "YA" : "BELUM");
    }
}

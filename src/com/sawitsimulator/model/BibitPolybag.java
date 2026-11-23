package com.sawitsimulator.model;

import com.sawitsimulator.interfaces.DapatDirawat;

/**
 * Class BibitPolybag merepresentasikan bibit sawit di area pembibitan (nursery).
 *
 * Konsep OOP:
 * - INTERFACE TANPA INHERITANCE DARI TanamanSawit:
 *   BibitPolybag BUKAN turunan TanamanSawit (belum menjadi pohon produktif dan
 *   tidak dapat dipanen TBS-nya), namun tetap mengimplementasikan DapatDirawat.
 * - Membuktikan keunggulan Interface untuk Polymorphism lintas hierarki class.
 */
public class BibitPolybag implements DapatDirawat {

    private final String kodeBatch;
    private int jumlahBibit;
    private int kelembabanMedia; // 0 - 100
    private int tingkatVigor;    // 0 - 100 (kesiapan pindah tanam)

    public BibitPolybag(String kodeBatch, int jumlahBibit) {
        this.kodeBatch = kodeBatch;
        this.jumlahBibit = jumlahBibit;
        this.kelembabanMedia = 55;
        this.tingkatVigor = 60;
    }

    @Override
    public void siram(int literAir) {
        if (literAir <= 0) {
            return;
        }
        kelembabanMedia = Math.min(100, kelembabanMedia + literAir * 4);
        tingkatVigor = Math.min(100, tingkatVigor + 4);
        System.out.printf("  [Siram] %s (%d bibit) disiram %dL -> Kelembaban: %d%%, Vigor: %d%%%n",
                getNamaObjek(), jumlahBibit, literAir, kelembabanMedia, tingkatVigor);
    }

    @Override
    public void beriPupuk(String jenisPupuk) {
        tingkatVigor = Math.min(100, tingkatVigor + 10);
        System.out.printf("  [Pupuk] %s diberi pupuk cair %s -> Vigor: %d%% (Siap tanam >= 80%%)%n",
                getNamaObjek(), jenisPupuk, tingkatVigor);
    }

    @Override
    public String getNamaObjek() {
        return "Bibit Polybag [" + kodeBatch + "]";
    }

    public boolean isSiapPindahTanam() {
        return tingkatVigor >= 80;
    }

    public int getJumlahBibit() {
        return jumlahBibit;
    }

    public int getKelembabanMedia() {
        return kelembabanMedia;
    }

    public int getTingkatVigor() {
        return tingkatVigor;
    }
}

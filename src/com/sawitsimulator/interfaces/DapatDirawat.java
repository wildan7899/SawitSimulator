package com.sawitsimulator.interfaces;

/**
 * Interface DapatDirawat mendefinisikan kontrak perilaku perawatan
 * untuk setiap objek di kebun sawit yang membutuhkan air dan pupuk.
 *
 * Konsep OOP:
 * - INTERFACE: Hanya mendeklarasikan kontrak method tanpa menyimpan state.
 * - Memungkinkan class yang tidak berada dalam satu hierarki inheritance
 *   (misalnya TanamanSawit dan BibitPolybag) untuk dirawat secara seragam
 *   melalui Polymorphism.
 */
public interface DapatDirawat {

    /**
     * Memberikan air untuk menjaga atau menaikkan kadar air objek.
     *
     * @param literAir jumlah air yang disiramkan dalam liter
     */
    void siram(int literAir);

    /**
     * Memberikan pupuk untuk menunjang kesehatan dan pertumbuhan.
     *
     * @param jenisPupuk nama atau jenis pupuk (misalnya NPK, Organik, Urea)
     */
    void beriPupuk(String jenisPupuk);

    /**
     * Mengembalikan identitas ringkas objek yang sedang dirawat.
     *
     * @return nama atau label objek
     */
    String getNamaObjek();
}

package com.sawitsimulator.interfaces;

/**
 * Interface DapatDipanen mendefinisikan kontrak perilaku panen dan
 * perhitungan nilai ekonomi hasil panen Tandan Buah Segar (TBS).
 *
 * Konsep OOP:
 * - INTERFACE: Memisahkan kemampuan "dapat dipanen" dari "dapat dirawat"
 *   (Interface Segregation Principle), sehingga objek seperti BibitPolybag
 *   tidak dipaksa mengimplementasikan method panen.
 */
public interface DapatDipanen {

    /**
     * Mengecek apakah objek sudah memenuhi syarat untuk dipanen.
     *
     * @return true jika siap panen, false jika belum
     */
    boolean isSiapPanen();

    /**
     * Menghitung estimasi berat hasil panen dalam kilogram (kg).
     *
     * @return estimasi berat hasil panen (kg)
     */
    double hitungHasilPanenKg();

    /**
     * Melakukan proses panen dan mengembalikan total pendapatan (Rupiah).
     *
     * @param hargaPerKg harga jual per kilogram TBS dalam Rupiah
     * @return total pendapatan hasil panen (Rupiah), atau 0 jika belum siap panen
     */
    int panen(int hargaPerKg);
}

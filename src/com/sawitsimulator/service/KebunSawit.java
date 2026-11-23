package com.sawitsimulator.service;

import com.sawitsimulator.interfaces.DapatDipanen;
import com.sawitsimulator.interfaces.DapatDirawat;
import com.sawitsimulator.model.TanamanSawit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class KebunSawit mengelola seluruh blok tanaman sawit, area pembibitan,
 * dan saldo keuangan pemain di Game Tanam Sawit.
 *
 * Konsep OOP:
 * - RUNTIME POLYMORPHISM:
 *   1. Menggunakan List<TanamanSawit> (referensi Abstract Class) untuk
 *      menjalankan tumbuh() & tampilkanKebutuhanLahan() pada berbagai varietas.
 *   2. Menggunakan List<DapatDirawat> (referensi Interface) untuk menyiram
 *      dan memupuk pohon sawit sekaligus bibit polybag secara seragam.
 *   3. Menggunakan List<DapatDipanen> (referensi Interface) untuk memanen
 *      seluruh objek yang mendukung kontrak panen.
 */
public class KebunSawit {

    private final String namaKebun;
    private final List<TanamanSawit> daftarTanaman;
    private final List<DapatDirawat> daftarObjekDirawat;
    private int saldoRupiah;
    private int bulanBerjalan;

    public KebunSawit(String namaKebun, int modalAwal) {
        this.namaKebun = namaKebun;
        this.saldoRupiah = modalAwal;
        this.bulanBerjalan = 0;
        this.daftarTanaman = new ArrayList<>();
        this.daftarObjekDirawat = new ArrayList<>();
    }

    /**
     * Menanam pohon sawit baru (subclass TanamanSawit) ke kebun.
     */
    public void tanamSawit(TanamanSawit tanaman) {
        daftarTanaman.add(tanaman);
        daftarObjekDirawat.add(tanaman); // TanamanSawit mengimplementasikan DapatDirawat
        System.out.printf("  [+] Menanam %s -> Kebutuhan: %s%n",
                tanaman.getNamaObjek(), tanaman.getKebutuhanLahan());
    }

    /**
     * Menambahkan objek lain yang dapat dirawat (misalnya BibitPolybag)
     * ke daftar perawatan kebun.
     */
    public void tambahObjekPerawatan(DapatDirawat objek) {
        daftarObjekDirawat.add(objek);
        System.out.printf("  [+] Menambahkan %s ke area perawatan.%n", objek.getNamaObjek());
    }

    /**
     * POLYMORPHISM VIA INTERFACE (DapatDirawat):
     * Merawat semua objek (SawitTenera, SawitDura, SawitTopaz, BibitPolybag)
     * tanpa perlu mengecek tipe konkretnya satu per satu.
     */
    public void rawatSemua(int literAir, String jenisPupuk) {
        System.out.println("\n=== PERAWATAN MASSAL (Polymorphism via Interface DapatDirawat) ===");
        for (DapatDirawat objek : daftarObjekDirawat) {
            objek.siram(literAir);
            objek.beriPupuk(jenisPupuk);
        }
    }

    /**
     * POLYMORPHISM VIA ABSTRACT CLASS (TanamanSawit):
     * Memajukan waktu dan memanggil abstract method tumbuh() yang memiliki
     * implementasi berbeda pada tiap varietas sawit.
     */
    public void lewatiWaktu(int jumlahBulan) {
        bulanBerjalan += jumlahBulan;
        System.out.printf("%n=== SIMULASI PERTUMBUHAN +%d BULAN (Total: Bulan ke-%d) ===%n",
                jumlahBulan, bulanBerjalan);
        for (TanamanSawit tanaman : daftarTanaman) {
            tanaman.tumbuh(jumlahBulan);
            System.out.println("  " + tanaman.getStatusRingkas());
        }
    }

    /**
     * POLYMORPHISM VIA INTERFACE (DapatDipanen):
     * Memanen seluruh objek yang mengimplementasikan DapatDipanen.
     */
    public int panenSemua(int hargaTbsPerKg) {
        System.out.printf("%n=== PANEN RAYA (Harga TBS: Rp%,d/kg | Polymorphism via DapatDipanen) ===%n",
                hargaTbsPerKg);
        int totalPendapatan = 0;

        List<DapatDipanen> daftarPanen = new ArrayList<>(daftarTanaman);
        for (DapatDipanen item : daftarPanen) {
            totalPendapatan += item.panen(hargaTbsPerKg);
        }

        saldoRupiah += totalPendapatan;
        System.out.printf("  Total Pendapatan Panen: Rp%,d | Saldo Kebun: Rp%,d%n",
                totalPendapatan, saldoRupiah);
        return totalPendapatan;
    }

    public void tampilkanRingkasan() {
        System.out.println("\n======================================================================");
        System.out.printf(" RINGKASAN KEBUN: %s (Bulan ke-%d)%n", namaKebun, bulanBerjalan);
        System.out.printf(" Saldo Kas: Rp%,d | Total Blok Sawit: %d | Total Objek Dirawat: %d%n",
                saldoRupiah, daftarTanaman.size(), daftarObjekDirawat.size());
        System.out.println("======================================================================");
        for (TanamanSawit tanaman : daftarTanaman) {
            System.out.println("  " + tanaman.getStatusRingkas());
        }
    }

    public List<TanamanSawit> getDaftarTanaman() {
        return Collections.unmodifiableList(daftarTanaman);
    }

    public int getSaldoRupiah() {
        return saldoRupiah;
    }

    public int getBulanBerjalan() {
        return bulanBerjalan;
    }

    public String getNamaKebun() {
        return namaKebun;
    }
}

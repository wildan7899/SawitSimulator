package com.sawitsimulator;

import com.sawitsimulator.model.BibitPolybag;
import com.sawitsimulator.model.SawitDura;
import com.sawitsimulator.model.SawitTenera;
import com.sawitsimulator.model.SawitTopaz;
import com.sawitsimulator.model.TanamanSawit;
import com.sawitsimulator.service.KebunSawit;

import java.util.Scanner;

/**
 * Main class untuk menjalankan Game Tanam Sawit (SawitSimulator).
 *
 * Mode eksekusi:
 * - Default (tanpa argumen): Menjalankan skenario simulasi lengkap yang
 *   mendemonstrasikan Inheritance, Abstract Class, Interface, Overriding,
 *   Overloading, dan Polymorphism.
 * - Dengan argumen "--play": Menjalankan mode permainan interaktif di terminal.
 */
public class Main {

    private static final int HARGA_TBS_PER_KG = 2_850; // Harga TBS sawit (Rupiah/kg)

    public static void main(String[] args) {
        if (args.length > 0 && "--play".equalsIgnoreCase(args[0])) {
            jalankanModeInteraktif();
        } else {
            jalankanSimulasiDemo();
        }
    }

    private static void jalankanSimulasiDemo() {
        System.out.println("======================================================================");
        System.out.println("               SAWIT SIMULATOR - GAME TANAM SAWIT (PBO)               ");
        System.out.println("======================================================================");

        KebunSawit kebun = new KebunSawit("Kebun Sawit Nusantara", 5_000_000);

        // 1. INHERITANCE & ABSTRACT CLASS:
        // Instansiasi subclass konkret dari abstract class TanamanSawit
        System.out.println("\n=== PENANAMAN VARIETAS SAWIT & PEMBIBITAN ===");
        TanamanSawit blokA = new SawitTenera("Blok-A1");
        TanamanSawit blokB = new SawitDura("Blok-B1");
        TanamanSawit blokC = new SawitTopaz("Blok-C1");
        BibitPolybag nursery = new BibitPolybag("Nursery-01", 150);

        kebun.tanamSawit(blokA);
        kebun.tanamSawit(blokB);
        kebun.tanamSawit(blokC);
        kebun.tambahObjekPerawatan(nursery);

        // 2. COMPILE-TIME POLYMORPHISM (Method Overloading):
        System.out.println("\n=== PEMUPUKAN KHUSUS (Method Overloading: beriPupuk(jenis, dosis)) ===");
        blokA.beriPupuk("NPK Phonska", 4);

        // 3. RUNTIME POLYMORPHISM (Interface DapatDirawat):
        // Merawat ketiga varietas pohon sawit + bibit polybag secara seragam
        kebun.rawatSemua(10, "NPK Mutiara");
        System.out.printf("  [Status Bibit] %s -> Siap pindah tanam: %s%n",
                nursery.getNamaObjek(), nursery.isSiapPindahTanam() ? "YA" : "BELUM");

        // 4. RUNTIME POLYMORPHISM (Abstract Class TanamanSawit -> tumbuh()):
        // Fase 1: 24 bulan -> SawitTopaz sudah mulai matang!
        kebun.lewatiWaktu(12);
        kebun.rawatSemua(15, "Urea");
        kebun.lewatiWaktu(12); // Total 24 bulan

        // 5. RUNTIME POLYMORPHISM (Interface DapatDipanen):
        // Hanya SawitTopaz yang siap panen di bulan ke-24
        kebun.panenSemua(HARGA_TBS_PER_KG);

        // Fase 2: Lanjut sampai bulan ke-36 -> Semua varietas (Tenera, Dura, Topaz) panen!
        kebun.rawatSemua(18, "NPK + Dolomit");
        kebun.lewatiWaktu(12); // Total 36 bulan
        kebun.panenSemua(HARGA_TBS_PER_KG);

        // Ringkasan akhir kebun
        kebun.tampilkanRingkasan();
        System.out.println("\nTip: Jalankan dengan argumen '--play' untuk mencoba mode interaktif.");
    }

    private static void jalankanModeInteraktif() {
        Scanner scanner = new Scanner(System.in);
        KebunSawit kebun = new KebunSawit("Kebun Sawit Pemain", 3_000_000);
        int counterBlok = 1;
        boolean berjalan = true;

        System.out.println("==============================================");
        System.out.println("  SAWIT SIMULATOR - MODE INTERAKTIF");
        System.out.println("==============================================");

        while (berjalan) {
            System.out.printf("%n[Bulan ke-%d | Saldo: Rp%,d]%n",
                    kebun.getBulanBerjalan(), kebun.getSaldoRupiah());
            System.out.println("1. Tanam Sawit Tenera (Matang 30 bln, Rendemen Tinggi)");
            System.out.println("2. Tanam Sawit Dura   (Matang 36 bln, Tahan Kemarau)");
            System.out.println("3. Tanam Sawit Topaz  (Matang 24 bln, Produktivitas Tinggi)");
            System.out.println("4. Tambah Bibit Polybag (Hanya DapatDirawat)");
            System.out.println("5. Siram & Pupuk Semua (Interface DapatDirawat)");
            System.out.println("6. Lewati 6 Bulan (Abstract Method tumbuh)");
            System.out.println("7. Panen Raya (Interface DapatDipanen)");
            System.out.println("8. Lihat Status Kebun");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    kebun.tanamSawit(new SawitTenera("Blok-" + counterBlok++));
                    break;
                case "2":
                    kebun.tanamSawit(new SawitDura("Blok-" + counterBlok++));
                    break;
                case "3":
                    kebun.tanamSawit(new SawitTopaz("Blok-" + counterBlok++));
                    break;
                case "4":
                    kebun.tambahObjekPerawatan(new BibitPolybag("Batch-" + counterBlok++, 100));
                    break;
                case "5":
                    kebun.rawatSemua(12, "NPK");
                    break;
                case "6":
                    kebun.lewatiWaktu(6);
                    break;
                case "7":
                    kebun.panenSemua(HARGA_TBS_PER_KG);
                    break;
                case "8":
                    kebun.tampilkanRingkasan();
                    break;
                case "0":
                    berjalan = false;
                    System.out.println("Terima kasih telah bermain Sawit Simulator.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
        scanner.close();
    }
}

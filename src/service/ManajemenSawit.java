package service;

import interfaces.Perawatan;
import model.RiwayatPerawatan;
import model.SawitMuda;
import model.SawitProduktif;
import model.TanamanSawit;
import repository.PenyimpananData;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class ManajemenSawit mengelola seluruh logika bisnis perkebunan kelapa sawit:
 * CRUD data tanaman, pencarian, statistik dashboard, penjadwalan & riwayat perawatan,
 * serta perhitungan estimasi panen.
 *
 * Konsep OOP:
 * - POLYMORPHISM: Menggunakan List<TanamanSawit> untuk menyimpan objek SawitMuda
 *   dan SawitProduktif secara bersama-sama. Pemanggilan method seperti
 *   hitungEstimasiPanen(), hitungKebutuhanPupuk(), jadwalkanPemupukan(), dan
 *   jadwalkanPenyiraman() otomatis menjalankan implementasi sesuai tipe objek aktual.
 */
public class ManajemenSawit {

    private final List<TanamanSawit> daftarTanaman;
    private final List<RiwayatPerawatan> daftarRiwayat;
    private final PenyimpananData penyimpanan;
    private final List<String> peringatanTerakhir;

    public ManajemenSawit(PenyimpananData penyimpanan) {
        this.penyimpanan = penyimpanan;
        this.daftarTanaman = new ArrayList<>();
        this.daftarRiwayat = new ArrayList<>();
        this.peringatanTerakhir = new ArrayList<>();
    }

    /**
     * Memuat seluruh data dari penyimpanan lokal.
     * Jika file belum pernah dibuat, opsional menginisialisasi data sampel awal.
     */
    public void muatData(boolean buatDataAwalJikaKosong) {
        peringatanTerakhir.clear();
        daftarTanaman.clear();
        daftarRiwayat.clear();

        boolean fileBelumAda = !penyimpanan.fileTanamanAda();

        PenyimpananData.HasilMuat<TanamanSawit> hasilTanaman = penyimpanan.muatTanaman();
        for (TanamanSawit t : hasilTanaman.getData()) {
            if (cariById(t.getIdTanaman()) == null) {
                daftarTanaman.add(t);
            } else {
                peringatanTerakhir.add("Duplikat ID pada file dilewati: " + t.getIdTanaman());
            }
        }
        peringatanTerakhir.addAll(hasilTanaman.getPeringatan());

        PenyimpananData.HasilMuat<RiwayatPerawatan> hasilRiwayat = penyimpanan.muatRiwayat();
        daftarRiwayat.addAll(hasilRiwayat.getData());
        peringatanTerakhir.addAll(hasilRiwayat.getPeringatan());

        if (fileBelumAda && buatDataAwalJikaKosong && daftarTanaman.isEmpty()) {
            isiDataSampelAwal();
        }
    }

    private void isiDataSampelAwal() {
        LocalDate hariIni = LocalDate.now();
        daftarTanaman.add(new SawitMuda("SWT-001", hariIni.minusMonths(10), 2.5, "Blok A1 - Lahan Utara", 115));
        daftarTanaman.add(new SawitMuda("SWT-002", hariIni.minusMonths(22), 3.0, "Blok A2 - Lahan Timur", 185));
        daftarTanaman.add(new SawitProduktif("SWT-003", hariIni.minusMonths(60), 5.0, "Blok B1 - Lahan Tengah", 19.5));
        daftarTanaman.add(new SawitProduktif("SWT-004", hariIni.minusMonths(96), 4.2, "Blok B2 - Lahan Selatan", 21.0));
        daftarTanaman.add(new SawitProduktif("SWT-005", hariIni.minusMonths(42), 3.5, "Blok C1 - Lahan Barat", 17.5));

        daftarRiwayat.add(new RiwayatPerawatan("RW-001", "SWT-001", hariIni.minusDays(14),
                "Pemupukan", "Pemberian pupuk NPK 15-15-6-4 sesuai dosis TBM 1"));
        daftarRiwayat.add(new RiwayatPerawatan("RW-002", "SWT-003", hariIni.minusDays(7),
                "Pemupukan", "Pemupukan semesteran MOP/KCl & Dolomit Blok B1"));
        daftarRiwayat.add(new RiwayatPerawatan("RW-003", "SWT-002", hariIni.minusDays(2),
                "Penyiraman", "Penyiraman rutin pagi dan sore area Blok A2"));

        try {
            simpanData();
        } catch (IOException ex) {
            peringatanTerakhir.add("Gagal menyimpan data sampel awal: " + ex.getMessage());
        }
    }

    public void simpanData() throws IOException {
        penyimpanan.simpanTanaman(daftarTanaman);
        penyimpanan.simpanRiwayat(daftarRiwayat);
    }

    // =========================================================================
    // CRUD Tanaman Sawit
    // =========================================================================

    public void tambahTanaman(TanamanSawit tanaman) throws IOException {
        if (tanaman == null) {
            throw new IllegalArgumentException("Data tanaman tidak boleh null.");
        }
        if (cariById(tanaman.getIdTanaman()) != null) {
            throw new IllegalArgumentException("ID tanaman '" + tanaman.getIdTanaman() + "' sudah terdaftar.");
        }
        daftarTanaman.add(tanaman);
        simpanData();
    }

    public void updateTanaman(String idLama, TanamanSawit tanamanBaru) throws IOException {
        if (tanamanBaru == null) {
            throw new IllegalArgumentException("Data tanaman baru tidak boleh null.");
        }
        int index = cariIndexById(idLama);
        if (index < 0) {
            throw new IllegalArgumentException("Data tanaman dengan ID '" + idLama + "' tidak ditemukan.");
        }
        String idBaru = tanamanBaru.getIdTanaman();
        if (!idLama.equalsIgnoreCase(idBaru) && cariById(idBaru) != null) {
            throw new IllegalArgumentException("ID tanaman '" + idBaru + "' sudah digunakan data lain.");
        }
        daftarTanaman.set(index, tanamanBaru);
        simpanData();
    }

    public boolean hapusTanaman(String idTanaman) throws IOException {
        int index = cariIndexById(idTanaman);
        if (index < 0) {
            return false;
        }
        daftarTanaman.remove(index);
        simpanData();
        return true;
    }

    public TanamanSawit cariById(String idTanaman) {
        if (idTanaman == null) {
            return null;
        }
        for (TanamanSawit t : daftarTanaman) {
            if (t.getIdTanaman().equalsIgnoreCase(idTanaman.trim())) {
                return t;
            }
        }
        return null;
    }

    private int cariIndexById(String idTanaman) {
        if (idTanaman == null) {
            return -1;
        }
        for (int i = 0; i < daftarTanaman.size(); i++) {
            if (daftarTanaman.get(i).getIdTanaman().equalsIgnoreCase(idTanaman.trim())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Mencari tanaman berdasarkan ID tanaman atau lokasi lahan (case-insensitive).
     */
    public List<TanamanSawit> cariTanaman(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getDaftarTanaman();
        }
        String q = keyword.trim().toLowerCase();
        List<TanamanSawit> cocok = new ArrayList<>();
        for (TanamanSawit t : daftarTanaman) {
            if (t.getIdTanaman().toLowerCase().contains(q)
                    || t.getLokasiLahan().toLowerCase().contains(q)) {
                cocok.add(t);
            }
        }
        return cocok;
    }

    // =========================================================================
    // Manajemen Perawatan & Riwayat
    // =========================================================================

    public RiwayatPerawatan catatPerawatan(String idTanaman, LocalDate tanggal,
                                           String jenisKegiatan, String keterangan) throws IOException {
        if (cariById(idTanaman) == null) {
            throw new IllegalArgumentException("Tanaman dengan ID '" + idTanaman + "' tidak ditemukan.");
        }
        String idRiwayat = buatIdRiwayatBerikutnya();
        RiwayatPerawatan baru = new RiwayatPerawatan(idRiwayat, idTanaman, tanggal, jenisKegiatan, keterangan);
        daftarRiwayat.add(baru);
        simpanData();
        return baru;
    }

    public boolean hapusRiwayat(String idRiwayat) throws IOException {
        for (int i = 0; i < daftarRiwayat.size(); i++) {
            if (daftarRiwayat.get(i).getIdRiwayat().equalsIgnoreCase(idRiwayat)) {
                daftarRiwayat.remove(i);
                simpanData();
                return true;
            }
        }
        return false;
    }

    private String buatIdRiwayatBerikutnya() {
        int max = 0;
        for (RiwayatPerawatan r : daftarRiwayat) {
            String id = r.getIdRiwayat();
            if (id.startsWith("RW-")) {
                try {
                    int n = Integer.parseInt(id.substring(3));
                    if (n > max) {
                        max = n;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("RW-%03d", max + 1);
    }

    /**
     * Mengembalikan seluruh tanaman sebagai daftar referensi Interface Perawatan
     * untuk mendemonstrasikan polymorphism melalui interface.
     */
    public List<Perawatan> getDaftarObjekPerawatan() {
        return new ArrayList<>(daftarTanaman);
    }

    // =========================================================================
    // Statistik Dashboard & Agregasi Polimorfik
    // =========================================================================

    public int getTotalTanaman() {
        return daftarTanaman.size();
    }

    public double getTotalLuasLahan() {
        double total = 0.0;
        for (TanamanSawit t : daftarTanaman) {
            total += t.getLuasLahan();
        }
        return total;
    }

    public double getTotalLuasProduktif() {
        double total = 0.0;
        for (TanamanSawit t : daftarTanaman) {
            if (t instanceof SawitProduktif) {
                total += t.getLuasLahan();
            }
        }
        return total;
    }

    public int getJumlahSawitMuda() {
        int count = 0;
        for (TanamanSawit t : daftarTanaman) {
            if (t instanceof SawitMuda) {
                count++;
            }
        }
        return count;
    }

    public int getJumlahSawitProduktif() {
        int count = 0;
        for (TanamanSawit t : daftarTanaman) {
            if (t instanceof SawitProduktif) {
                count++;
            }
        }
        return count;
    }

    /**
     * POLYMORPHISM: Memanggil hitungEstimasiPanen() pada setiap elemen
     * bertipe TanamanSawit tanpa pengecekan tipe manual.
     */
    public double getTotalEstimasiPanen() {
        double total = 0.0;
        for (TanamanSawit t : daftarTanaman) {
            total += t.hitungEstimasiPanen();
        }
        return total;
    }

    /**
     * POLYMORPHISM: Memanggil hitungKebutuhanPupuk() pada setiap elemen
     * bertipe TanamanSawit sesuai implementasi masing-masing subclass.
     */
    public double getTotalKebutuhanPupuk() {
        double total = 0.0;
        for (TanamanSawit t : daftarTanaman) {
            total += t.hitungKebutuhanPupuk();
        }
        return total;
    }

    public List<TanamanSawit> getDaftarTanaman() {
        return Collections.unmodifiableList(daftarTanaman);
    }

    public List<RiwayatPerawatan> getDaftarRiwayat() {
        return Collections.unmodifiableList(daftarRiwayat);
    }

    public List<String> getPeringatanTerakhir() {
        return Collections.unmodifiableList(peringatanTerakhir);
    }

    public PenyimpananData getPenyimpanan() {
        return penyimpanan;
    }
}

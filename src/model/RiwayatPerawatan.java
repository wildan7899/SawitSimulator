package model;

import java.time.LocalDate;

/**
 * Class RiwayatPerawatan menyimpan catatan historis kegiatan pemupukan
 * dan penyiraman yang telah dilakukan pada blok tanaman sawit.
 *
 * Konsep OOP: ENCAPSULATION (atribut private + validasi data).
 */
public class RiwayatPerawatan {

    private final String idRiwayat;
    private final String idTanaman;
    private final LocalDate tanggal;
    private final String jenisKegiatan;
    private final String keterangan;

    public RiwayatPerawatan(String idRiwayat, String idTanaman, LocalDate tanggal,
                            String jenisKegiatan, String keterangan) {
        if (idRiwayat == null || idRiwayat.trim().isEmpty()) {
            throw new IllegalArgumentException("ID riwayat tidak boleh kosong.");
        }
        if (idTanaman == null || idTanaman.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tanaman pada riwayat tidak boleh kosong.");
        }
        if (tanggal == null) {
            throw new IllegalArgumentException("Tanggal kegiatan perawatan tidak boleh kosong.");
        }
        if (jenisKegiatan == null || jenisKegiatan.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis kegiatan tidak boleh kosong.");
        }
        if (keterangan == null || keterangan.trim().isEmpty()) {
            throw new IllegalArgumentException("Keterangan kegiatan tidak boleh kosong.");
        }
        if (keterangan.contains("|") || keterangan.contains("\n")) {
            throw new IllegalArgumentException("Keterangan tidak boleh mengandung karakter '|'.");
        }

        this.idRiwayat = idRiwayat.trim();
        this.idTanaman = idTanaman.trim().toUpperCase();
        this.tanggal = tanggal;
        this.jenisKegiatan = jenisKegiatan.trim();
        this.keterangan = keterangan.trim();
    }

    public String getIdRiwayat() {
        return idRiwayat;
    }

    public String getIdTanaman() {
        return idTanaman;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public String getJenisKegiatan() {
        return jenisKegiatan;
    }

    public String getKeterangan() {
        return keterangan;
    }
}

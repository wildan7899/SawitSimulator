package repository;

import model.RiwayatPerawatan;
import model.SawitMuda;
import model.SawitProduktif;
import model.TanamanSawit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class PenyimpananData menangani persistensi lokal data tanaman sawit
 * dan riwayat perawatan menggunakan file teks terstruktur (pipe-delimited CSV)
 * di folder lokal tanpa database eksternal.
 */
public class PenyimpananData {

    private static final String HEADER_TANAMAN = "TIPE|ID|TANGGAL_TANAM|LUAS_HA|LOKASI|PARAMETER_KHUSUS";
    private static final String HEADER_RIWAYAT = "ID_RIWAYAT|ID_TANAMAN|TANGGAL|JENIS_KEGIATAN|KETERANGAN";

    private final Path fileTanaman;
    private final Path fileRiwayat;

    public PenyimpananData() {
        this(Paths.get("data", "tanaman_sawit.csv"), Paths.get("data", "riwayat_perawatan.csv"));
    }

    public PenyimpananData(Path fileTanaman, Path fileRiwayat) {
        this.fileTanaman = fileTanaman;
        this.fileRiwayat = fileRiwayat;
    }

    /**
     * Hasil pembacaan file yang menyertakan data valid dan daftar peringatan
     * apabila ditemukan baris rusak/tidak valid pada file.
     */
    public static class HasilMuat<T> {
        private final List<T> data;
        private final List<String> peringatan;

        public HasilMuat(List<T> data, List<String> peringatan) {
            this.data = data;
            this.peringatan = peringatan;
        }

        public List<T> getData() {
            return data;
        }

        public List<String> getPeringatan() {
            return peringatan;
        }
    }

    public void simpanTanaman(List<TanamanSawit> daftarTanaman) throws IOException {
        pastikanDirektoriAda(fileTanaman);
        try (BufferedWriter writer = Files.newBufferedWriter(fileTanaman, StandardCharsets.UTF_8)) {
            writer.write(HEADER_TANAMAN);
            writer.newLine();
            for (TanamanSawit t : daftarTanaman) {
                String tipe = (t instanceof SawitMuda) ? "MUDA" : "PRODUKTIF";
                String baris = String.format(Locale.US, "%s|%s|%s|%.2f|%s|%.2f",
                        tipe,
                        t.getIdTanaman(),
                        t.getTanggalTanam(),
                        t.getLuasLahan(),
                        bersihkanField(t.getLokasiLahan()),
                        t.getParameterKhusus());
                writer.write(baris);
                writer.newLine();
            }
        }
    }

    public HasilMuat<TanamanSawit> muatTanaman() {
        List<TanamanSawit> hasil = new ArrayList<>();
        List<String> peringatan = new ArrayList<>();

        if (!Files.exists(fileTanaman)) {
            return new HasilMuat<>(hasil, peringatan);
        }

        try (BufferedReader reader = Files.newBufferedReader(fileTanaman, StandardCharsets.UTF_8)) {
            String baris;
            int nomorBaris = 0;
            while ((baris = reader.readLine()) != null) {
                nomorBaris++;
                String trimmed = baris.trim();
                if (trimmed.isEmpty() || (nomorBaris == 1 && trimmed.startsWith("TIPE|"))) {
                    continue;
                }
                try {
                    String[] bagian = trimmed.split("\\|", -1);
                    if (bagian.length != 6) {
                        throw new IllegalArgumentException("Jumlah kolom tidak sesuai (harus 6 kolom).");
                    }
                    String tipe = bagian[0].trim().toUpperCase();
                    String id = bagian[1].trim();
                    LocalDate tanggal = LocalDate.parse(bagian[2].trim());
                    double luas = Double.parseDouble(bagian[3].trim());
                    String lokasi = bagian[4].trim();
                    double param = Double.parseDouble(bagian[5].trim());

                    TanamanSawit tanaman;
                    if ("MUDA".equals(tipe)) {
                        tanaman = new SawitMuda(id, tanggal, luas, lokasi, param);
                    } else if ("PRODUKTIF".equals(tipe)) {
                        tanaman = new SawitProduktif(id, tanggal, luas, lokasi, param);
                    } else {
                        throw new IllegalArgumentException("Tipe tanaman tidak dikenal: " + tipe);
                    }
                    hasil.add(tanaman);
                } catch (Exception ex) {
                    peringatan.add("Baris " + nomorBaris + " pada file tanaman dilewati: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            peringatan.add("Gagal membaca file tanaman (" + fileTanaman + "): " + ex.getMessage());
        }

        return new HasilMuat<>(hasil, peringatan);
    }

    public void simpanRiwayat(List<RiwayatPerawatan> daftarRiwayat) throws IOException {
        pastikanDirektoriAda(fileRiwayat);
        try (BufferedWriter writer = Files.newBufferedWriter(fileRiwayat, StandardCharsets.UTF_8)) {
            writer.write(HEADER_RIWAYAT);
            writer.newLine();
            for (RiwayatPerawatan r : daftarRiwayat) {
                String baris = String.join("|",
                        bersihkanField(r.getIdRiwayat()),
                        bersihkanField(r.getIdTanaman()),
                        r.getTanggal().toString(),
                        bersihkanField(r.getJenisKegiatan()),
                        bersihkanField(r.getKeterangan()));
                writer.write(baris);
                writer.newLine();
            }
        }
    }

    public HasilMuat<RiwayatPerawatan> muatRiwayat() {
        List<RiwayatPerawatan> hasil = new ArrayList<>();
        List<String> peringatan = new ArrayList<>();

        if (!Files.exists(fileRiwayat)) {
            return new HasilMuat<>(hasil, peringatan);
        }

        try (BufferedReader reader = Files.newBufferedReader(fileRiwayat, StandardCharsets.UTF_8)) {
            String baris;
            int nomorBaris = 0;
            while ((baris = reader.readLine()) != null) {
                nomorBaris++;
                String trimmed = baris.trim();
                if (trimmed.isEmpty() || (nomorBaris == 1 && trimmed.startsWith("ID_RIWAYAT|"))) {
                    continue;
                }
                try {
                    String[] bagian = trimmed.split("\\|", -1);
                    if (bagian.length != 5) {
                        throw new IllegalArgumentException("Jumlah kolom tidak sesuai (harus 5 kolom).");
                    }
                    RiwayatPerawatan riwayat = new RiwayatPerawatan(
                            bagian[0].trim(),
                            bagian[1].trim(),
                            LocalDate.parse(bagian[2].trim()),
                            bagian[3].trim(),
                            bagian[4].trim());
                    hasil.add(riwayat);
                } catch (Exception ex) {
                    peringatan.add("Baris " + nomorBaris + " pada file riwayat dilewati: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            peringatan.add("Gagal membaca file riwayat (" + fileRiwayat + "): " + ex.getMessage());
        }

        return new HasilMuat<>(hasil, peringatan);
    }

    public boolean fileTanamanAda() {
        return Files.exists(fileTanaman);
    }

    public Path getFileTanaman() {
        return fileTanaman;
    }

    public Path getFileRiwayat() {
        return fileRiwayat;
    }

    private void pastikanDirektoriAda(Path path) throws IOException {
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private String bersihkanField(String teks) {
        return teks.replace("|", "-").replace("\r", " ").replace("\n", " ").trim();
    }
}

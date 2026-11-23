package interfaces;

/**
 * Interface Perawatan mendefinisikan kontrak penjadwalan perawatan
 * untuk objek perkebunan kelapa sawit.
 *
 * Konsep OOP: INTERFACE
 * Memisahkan perilaku penjadwalan pemupukan dan penyiraman sebagai kontrak
 * yang harus diimplementasikan oleh kelas yang relevan.
 */
public interface Perawatan {

    /**
     * Menghasilkan rekomendasi jadwal pemupukan sesuai fase tanaman.
     *
     * @return deskripsi jadwal pemupukan
     */
    String jadwalkanPemupukan();

    /**
     * Menghasilkan rekomendasi jadwal penyiraman sesuai fase tanaman.
     *
     * @return deskripsi jadwal penyiraman
     */
    String jadwalkanPenyiraman();
}

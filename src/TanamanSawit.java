/**
 * Abstract Class TanamanSawit menjadi kerangka dasar untuk seluruh tahap
 * pertumbuhan tanaman kelapa sawit (Bibit, Sawit Muda, dan Sawit Dewasa).
 *
 * Konsep OOP:
 * - ABSTRACT CLASS & ABSTRACT METHOD: tumbuh(), getStatus(), getSimbol()
 * - ENCAPSULATION: atribut umur, air, dan pupuk bersifat private
 * - INTERFACE: mengimplementasikan BisaDirawat
 */
public abstract class TanamanSawit implements BisaDirawat {

    public static final int MAKS_AIR = 3;
    public static final int MAKS_PUPUK = 3;

    private int umur;
    private int air;
    private int pupuk;

    public TanamanSawit() {
        this(0, 1, 0);
    }

    public TanamanSawit(int umur, int air, int pupuk) {
        setUmur(umur);
        setAir(air);
        setPupuk(pupuk);
    }

    // =========================================================================
    // Abstract Methods (wajib di-override oleh setiap subclass)
    // =========================================================================

    /**
     * Mensimulasikan pertumbuhan tanaman saat hari berganti.
     */
    public abstract void tumbuh();

    /**
     * Mengembalikan teks status kondisi tanaman saat ini.
     */
    public abstract String getStatus();

    /**
     * Mengembalikan simbol singkat representasi tahap tanaman.
     */
    public abstract String getSimbol();

    // =========================================================================
    // Implementasi Interface BisaDirawat
    // =========================================================================

    @Override
    public void siram() {
        setAir(air + 1);
    }

    @Override
    public void beriPupuk() {
        setPupuk(pupuk + 1);
    }

    // =========================================================================
    // Evolusi Fase & Panen (dapat di-override oleh subclass terkait)
    // =========================================================================

    /**
     * Mengecek apakah tanaman sudah siap naik ke fase (subclass) berikutnya
     * sambil mempertahankan data umur, air, dan pupuk.
     */
    public TanamanSawit cekEvolusi() {
        return this;
    }

    public boolean isSiapPanen() {
        return false;
    }

    public int panen() {
        return 0;
    }

    // =========================================================================
    // Getter & Setter (Encapsulation)
    // =========================================================================

    public int getUmur() {
        return umur;
    }

    protected void setUmur(int umur) {
        this.umur = Math.max(0, umur);
    }

    protected void tambahUmur(int jumlah) {
        this.umur = Math.max(0, this.umur + jumlah);
    }

    public int getAir() {
        return air;
    }

    protected void setAir(int air) {
        this.air = Math.max(0, Math.min(MAKS_AIR, air));
    }

    public int getPupuk() {
        return pupuk;
    }

    protected void setPupuk(int pupuk) {
        this.pupuk = Math.max(0, Math.min(MAKS_PUPUK, pupuk));
    }
}

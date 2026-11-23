/**
 * Abstract Class induk untuk semua tahap tanaman sawit (Bibit, Muda, Dewasa).
 * Mengimplementasikan BisaDirawat dan menerapkan encapsulation pada atributnya.
 */
public abstract class TanamanSawit implements BisaDirawat {

    public static final int MAKS_AIR = 3;
    public static final int MAKS_PUPUK = 3;

    // Atribut private (encapsulation) agar nilainya aman dan terkontrol lewat setter
    private int umur;
    private int air;
    private int pupuk;

    // Constructor default untuk tanaman baru (umur 0, air awal 1, pupuk 0)
    public TanamanSawit() {
        this(0, 1, 0);
    }

    public TanamanSawit(int umur, int air, int pupuk) {
        setUmur(umur);
        setAir(air);
        setPupuk(pupuk);
    }

    // Method abstract: wajib diisi (override) oleh setiap subclass sesuai tahapnya
    public abstract void tumbuh();
    public abstract String getStatus();
    public abstract String getSimbol();

    // Implementasi interface BisaDirawat
    @Override
    public void siram() {
        setAir(air + 1);
    }

    @Override
    public void beriPupuk() {
        setPupuk(pupuk + 1);
    }

    // Cek naik ke tahap berikutnya; default tetap di tahap sekarang
    public TanamanSawit cekEvolusi() {
        return this;
    }

    // Default belum bisa dipanen (hanya di-override oleh SawitDewasa)
    public boolean isSiapPanen() {
        return false;
    }

    public int panen() {
        return 0;
    }

    // Getter & Setter untuk menjaga nilai tetap di batas yang valid
    public int getUmur() {
        return umur;
    }

    protected void setUmur(int umur) {
        this.umur = Math.max(0, umur); // Cegah umur bernilai negatif
    }

    protected void tambahUmur(int jumlah) {
        this.umur = Math.max(0, this.umur + jumlah);
    }

    public int getAir() {
        return air;
    }

    protected void setAir(int air) {
        // Batasi kadar air antara 0 sampai MAKS_AIR
        this.air = Math.max(0, Math.min(MAKS_AIR, air));
    }

    public int getPupuk() {
        return pupuk;
    }

    protected void setPupuk(int pupuk) {
        // Batasi kadar pupuk antara 0 sampai MAKS_PUPUK
        this.pupuk = Math.max(0, Math.min(MAKS_PUPUK, pupuk));
    }
}

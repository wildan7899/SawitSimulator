// Subclass tahap dewasa (Hari 4+), bisa berbuah dan dipanen berulang kali
public class SawitDewasa extends TanamanSawit {

    public static final int HASIL_PANEN_KOIN = 50;
    private static final int TARGET_PROGRES_BUAH = 2; // Batas progres agar buah siap panen

    private int progresBuah;
    private boolean siapPanen;

    public SawitDewasa(int umur, int air, int pupuk) {
        super(umur, air, pupuk);
        this.progresBuah = 1; // Saat baru dewasa, buah langsung mulai terbentuk
        this.siapPanen = false;
    }

    @Override
    public void tumbuh() {
        // Kalau sudah siap panen, cukup tambah umur dan tunggu dipanen
        if (siapPanen) {
            tambahUmur(1);
            return;
        }

        // Butuh air untuk mematangkan buah; pupuk mempercepat (+2 progres)
        if (getAir() >= 1) {
            setAir(getAir() - 1);
            tambahUmur(1);
            if (getPupuk() >= 1) {
                setPupuk(getPupuk() - 1);
                progresBuah += 2;
            } else {
                progresBuah += 1;
            }
            if (progresBuah >= TARGET_PROGRES_BUAH) {
                progresBuah = TARGET_PROGRES_BUAH;
                siapPanen = true;
            }
        }
    }

    @Override
    public boolean isSiapPanen() {
        return siapPanen;
    }

    @Override
    public int panen() {
        if (!siapPanen) {
            return 0;
        }
        // Reset status buah agar pohon tetap hidup dan bisa berbuah lagi
        siapPanen = false;
        progresBuah = 0;
        return HASIL_PANEN_KOIN;
    }

    public int getProgresBuah() {
        return progresBuah;
    }

    @Override
    public String getStatus() {
        if (siapPanen) {
            return "Sawit Dewasa - SIAP PANEN! (+50 Koin)";
        }
        if (getAir() == 0) {
            return "Sawit Dewasa (Kering! Siram & pupuk agar berbuah)";
        }
        return "Sawit Dewasa (Buah " + progresBuah + "/" + TARGET_PROGRES_BUAH + " - Rawat agar siap panen)";
    }

    @Override
    public String getSimbol() {
        return siapPanen ? "PANEN" : "DEWASA";
    }
}

/**
 * Subclass SawitDewasa mewakili tahap matang (Hari 4+) yang dapat menghasilkan
 * tandan buah sawit dan dipanen berulang kali setelah dirawat.
 *
 * Konsep OOP:
 * - INHERITANCE: mewarisi TanamanSawit
 * - POLYMORPHISM (Method Overriding): tumbuh(), getStatus(), getSimbol(), panen()
 */
public class SawitDewasa extends TanamanSawit {

    public static final int HASIL_PANEN_KOIN = 50;
    private static final int TARGET_PROGRES_BUAH = 2;

    private int progresBuah;
    private boolean siapPanen;

    public SawitDewasa(int umur, int air, int pupuk) {
        super(umur, air, pupuk);
        this.progresBuah = 1; // Mulai berbuah saat memasuki fase dewasa
        this.siapPanen = false;
    }

    @Override
    public void tumbuh() {
        // Umur tetap bertambah jika terawat; buah matang lebih cepat dengan air + pupuk.
        if (siapPanen) {
            tambahUmur(1);
            return;
        }

        if (getAir() >= 1) {
            setAir(getAir() - 1);
            tambahUmur(1);
            if (getPupuk() >= 1) {
                setPupuk(getPupuk() - 1);
                progresBuah += 2; // Air + pupuk membuat buah langsung matang
            } else {
                progresBuah += 1; // Tanpa pupuk butuh 2 hari penyiraman
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
        // Setelah panen, pohon tetap ada dan mulai siklus berbuah berikutnya
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

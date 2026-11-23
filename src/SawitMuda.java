/**
 * Subclass SawitMuda mewakili tahap pertumbuhan menengah (Hari 2 - 3).
 *
 * Konsep OOP:
 * - INHERITANCE: mewarisi TanamanSawit
 * - POLYMORPHISM (Method Overriding): tumbuh(), getStatus(), getSimbol()
 */
public class SawitMuda extends TanamanSawit {

    public SawitMuda(int umur, int air, int pupuk) {
        super(umur, air, pupuk);
    }

    @Override
    public void tumbuh() {
        // Sawit muda tumbuh jika cukup air; pupuk mempercepat menuju sawit dewasa.
        if (getAir() >= 1) {
            setAir(getAir() - 1);
            if (getPupuk() >= 1) {
                setPupuk(getPupuk() - 1);
                tambahUmur(2);
            } else {
                tambahUmur(1);
            }
        }
    }

    @Override
    public TanamanSawit cekEvolusi() {
        // Hari ke-4 ke atas, sawit muda berkembang menjadi SawitDewasa
        if (getUmur() >= 4) {
            return new SawitDewasa(getUmur(), getAir(), getPupuk());
        }
        return this;
    }

    @Override
    public String getStatus() {
        if (getAir() == 0) {
            return "Sawit Muda (Haus! Perlu disiram agar tumbuh)";
        }
        return "Sawit Muda (Umur " + getUmur() + " hari - Menuju Sawit Dewasa)";
    }

    @Override
    public String getSimbol() {
        return "MUDA";
    }
}

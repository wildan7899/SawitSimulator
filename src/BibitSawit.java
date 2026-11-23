/**
 * Subclass BibitSawit mewakili tahap awal tanaman (Hari 0 - 1).
 *
 * Konsep OOP:
 * - INHERITANCE: mewarisi TanamanSawit
 * - POLYMORPHISM (Method Overriding): tumbuh(), getStatus(), getSimbol()
 */
public class BibitSawit extends TanamanSawit {

    public BibitSawit() {
        super(0, 1, 0);
    }

    public BibitSawit(int umur, int air, int pupuk) {
        super(umur, air, pupuk);
    }

    @Override
    public void tumbuh() {
        // Bibit hanya tumbuh jika disiram (air >= 1).
        // Jika juga diberi pupuk, pertumbuhan menjadi 2x lebih cepat.
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
        // Hari ke-2 ke atas, bibit berkembang menjadi SawitMuda
        if (getUmur() >= 2) {
            return new SawitMuda(getUmur(), getAir(), getPupuk());
        }
        return this;
    }

    @Override
    public String getStatus() {
        if (getAir() == 0) {
            return "Bibit Sawit (Kering! Siram agar tumbuh)";
        }
        return "Bibit Sawit (Umur " + getUmur() + " hari - Menuju Sawit Muda)";
    }

    @Override
    public String getSimbol() {
        return "BIBIT";
    }
}

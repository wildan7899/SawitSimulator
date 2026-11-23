// Subclass tahap awal (Hari 0-1), mewarisi TanamanSawit
public class BibitSawit extends TanamanSawit {

    // Bibit baru mulai dari umur 0 dengan air awal 1
    public BibitSawit() {
        super(0, 1, 0);
    }

    public BibitSawit(int umur, int air, int pupuk) {
        super(umur, air, pupuk);
    }

    @Override
    public void tumbuh() {
        // Hanya tumbuh kalau punya air; pupuk bikin pertumbuhan 2x lebih cepat
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
        // Umur >= 2 naik jadi SawitMuda sambil bawa sisa air & pupuk
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

package gui;

import model.TanamanSawit;
import service.ManajemenSawit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * PanenPanel menampilkan perhitungan estimasi panen berbasis polymorphism
 * (memanggil TanamanSawit.hitungEstimasiPanen() untuk seluruh subclass),
 * lengkap dengan satuan, asumsi perhitungan, dan disclaimer non-agronomis.
 */
public class PanenPanel extends JPanel {

    private final ManajemenSawit manajemen;

    private final TemaUI.StatCard cardTotalTon;
    private final TemaUI.StatCard cardTotalKg;
    private final TemaUI.StatCard cardRataRata;
    private final DefaultTableModel modelPanen;

    public PanenPanel(ManajemenSawit manajemen) {
        this.manajemen = manajemen;

        setLayout(new BorderLayout(0, 14));
        setBackground(TemaUI.LATAR_UTAMA);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel lblJudul = new JLabel("Estimasi Panen Tandan Buah Segar (TBS)");
        lblJudul.setFont(TemaUI.FONT_JUDUL);
        lblJudul.setForeground(TemaUI.HIJAU_TUA);
        JLabel lblSub = new JLabel("Perhitungan berbasis Polymorphism (TanamanSawit.hitungEstimasiPanen()) sesuai luas lahan dan fase");
        lblSub.setFont(TemaUI.FONT_SUBJUDUL);
        lblSub.setForeground(TemaUI.TEKS_SEKUNDER);
        header.add(lblJudul);
        header.add(Box.createVerticalStrut(3));
        header.add(lblSub);

        // Banner Asumsi & Disclaimer (JTextArea wrap agar seluruh teks terbaca utuh)
        JPanel bannerInfo = new JPanel(new BorderLayout(10, 4));
        bannerInfo.setBackground(TemaUI.HIJAU_SOFT);
        bannerInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.HIJAU_AKSEN, 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        JLabel lblBannerTitle = new JLabel("Satuan, Asumsi Perhitungan & Disclaimer (Simulasi Edukatif):");
        lblBannerTitle.setFont(TemaUI.FONT_LABEL);
        lblBannerTitle.setForeground(TemaUI.HIJAU_TUA);

        JTextArea txtAsumsi = new JTextArea(
                "- Sawit Muda (TBM): Estimasi panen = 0 ton TBS/tahun karena belum memasuki fase menghasilkan.\n"
                        + "- Sawit Produktif (TM): Estimasi = Luas Lahan (Ha) x Asumsi Produktivitas (Ton/Ha/Thn) x Faktor Umur "
                        + "(0,75 untuk <48 bln; 1,00 untuk 48-180 bln; 0,80 untuk >180 bln).\n"
                        + "- Catatan: Angka di bawah ini adalah estimasi simulasi pembelajaran OOP (satuan Ton/Kg TBS per tahun), "
                        + "BUKAN prediksi agronomis aktual di lapangan."
        );
        txtAsumsi.setEditable(false);
        txtAsumsi.setOpaque(false);
        txtAsumsi.setLineWrap(true);
        txtAsumsi.setWrapStyleWord(true);
        txtAsumsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAsumsi.setForeground(TemaUI.HIJAU_TUA);
        txtAsumsi.setBorder(null);

        bannerInfo.add(lblBannerTitle, BorderLayout.NORTH);
        bannerInfo.add(txtAsumsi, BorderLayout.CENTER);

        // 3 Stat Cards
        JPanel panelCards = new JPanel(new GridLayout(1, 3, 14, 14));
        panelCards.setOpaque(false);
        cardTotalTon = new TemaUI.StatCard("TOTAL ESTIMASI (TON/TAHUN)", "0,00 Ton", "Satuan: Ton TBS / Tahun", TemaUI.HIJAU_TUA);
        cardTotalKg = new TemaUI.StatCard("TOTAL ESTIMASI (KG/TAHUN)", "0 Kg", "Konversi: 1 Ton = 1.000 Kg", TemaUI.HIJAU_SEDANG);
        cardRataRata = new TemaUI.StatCard("RATA-RATA LAHAN PRODUKTIF", "0,00 Ton/Ha", "Dari blok Sawit Produktif", TemaUI.HIJAU_AKSEN);
        panelCards.add(cardTotalTon);
        panelCards.add(cardTotalKg);
        panelCards.add(cardRataRata);

        JPanel topSection = new JPanel(new BorderLayout(0, 12));
        topSection.setOpaque(false);
        topSection.add(header, BorderLayout.NORTH);
        topSection.add(bannerInfo, BorderLayout.CENTER);
        topSection.add(panelCards, BorderLayout.SOUTH);
        add(topSection, BorderLayout.NORTH);

        // Tabel Rincian Estimasi Panen
        JPanel kartuTabel = TemaUI.buatKartuPutih(new BorderLayout(0, 10));
        JLabel lblTabel = new JLabel("Rincian Estimasi Panen per Blok Tanaman (Polymorphism List<TanamanSawit>)");
        lblTabel.setFont(TemaUI.FONT_SECTION);
        lblTabel.setForeground(TemaUI.HIJAU_TUA);

        modelPanen = new DefaultTableModel(
                new Object[]{"ID Blok", "Fase", "Lokasi", "Luas (Ha)", "Umur (Bln)",
                        "Estimasi (Ton/Thn)", "Estimasi (Kg/Thn)", "Asumsi Perhitungan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblPanen = new JTable(modelPanen);
        TemaUI.styleTabel(tblPanen);
        tblPanen.getColumnModel().getColumn(0).setPreferredWidth(85);
        tblPanen.getColumnModel().getColumn(0).setMinWidth(80);
        tblPanen.getColumnModel().getColumn(1).setPreferredWidth(115);
        tblPanen.getColumnModel().getColumn(2).setPreferredWidth(155);
        tblPanen.getColumnModel().getColumn(3).setPreferredWidth(75);
        tblPanen.getColumnModel().getColumn(4).setPreferredWidth(75);
        tblPanen.getColumnModel().getColumn(5).setPreferredWidth(125);
        tblPanen.getColumnModel().getColumn(6).setPreferredWidth(115);
        tblPanen.getColumnModel().getColumn(7).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(tblPanen);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDER_HALUS));

        kartuTabel.add(lblTabel, BorderLayout.NORTH);
        kartuTabel.add(scroll, BorderLayout.CENTER);
        add(kartuTabel, BorderLayout.CENTER);
    }

    public void refreshData() {
        double totalTon = manajemen.getTotalEstimasiPanen();
        double totalKg = totalTon * 1000.0;
        double luasProduktif = manajemen.getTotalLuasProduktif();
        double rataRata = (luasProduktif > 0) ? (totalTon / luasProduktif) : 0.0;

        cardTotalTon.setData(String.format("%.2f Ton", totalTon), "Akumulasi seluruh blok sawit");
        cardTotalKg.setData(String.format("%,.0f Kg", totalKg), "Setara kilogram TBS/tahun");
        cardRataRata.setData(String.format("%.2f Ton/Ha", rataRata),
                String.format("Luas TM: %.2f Ha (%d blok)", luasProduktif, manajemen.getJumlahSawitProduktif()));

        modelPanen.setRowCount(0);
        // POLYMORPHISM: setiap elemen bertipe TanamanSawit memanggil hitungEstimasiPanen() masing-masing
        for (TanamanSawit t : manajemen.getDaftarTanaman()) {
            double ton = t.hitungEstimasiPanen();
            modelPanen.addRow(new Object[]{
                    t.getIdTanaman(),
                    t.getJenisFase(),
                    t.getLokasiLahan(),
                    String.format("%.2f", t.getLuasLahan()),
                    t.getUmurBulan(),
                    String.format("%.2f", ton),
                    String.format("%,.0f", ton * 1000.0),
                    t.getKeteranganEstimasi()
            });
        }
    }
}

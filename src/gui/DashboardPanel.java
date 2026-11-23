package gui;

import model.RiwayatPerawatan;
import model.TanamanSawit;
import service.ManajemenSawit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * DashboardPanel menampilkan ringkasan statistik perkebunan sawit
 * dalam bentuk cards, tabel ringkas tanaman, dan ringkasan perawatan terbaru.
 */
public class DashboardPanel extends JPanel {

    private final ManajemenSawit manajemen;
    private final MainFrame mainFrame;

    private final TemaUI.StatCard cardTotalTanaman;
    private final TemaUI.StatCard cardTotalLuas;
    private final TemaUI.StatCard cardKomposisiFase;
    private final TemaUI.StatCard cardEstimasiPanen;

    private final DefaultTableModel modelRingkasTanaman;
    private final DefaultTableModel modelRingkasRiwayat;
    private final JLabel lblInfoPupuk;

    public DashboardPanel(ManajemenSawit manajemen, MainFrame mainFrame) {
        this.manajemen = manajemen;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(0, 18));
        setBackground(TemaUI.LATAR_UTAMA);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblJudul = new JLabel("Dashboard Perkebunan Kelapa Sawit");
        lblJudul.setFont(TemaUI.FONT_JUDUL);
        lblJudul.setForeground(TemaUI.HIJAU_TUA);

        String tglStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID")));
        JLabel lblSub = new JLabel("Ringkasan operasional penanaman, perawatan, dan estimasi panen  |  " + tglStr);
        lblSub.setFont(TemaUI.FONT_SUBJUDUL);
        lblSub.setForeground(TemaUI.TEKS_SEKUNDER);

        JPanel teksHeader = new JPanel();
        teksHeader.setOpaque(false);
        teksHeader.setLayout(new BoxLayout(teksHeader, BoxLayout.Y_AXIS));
        teksHeader.add(lblJudul);
        teksHeader.add(Box.createVerticalStrut(4));
        teksHeader.add(lblSub);
        header.add(teksHeader, BorderLayout.WEST);

        // Baris 4 Stat Cards
        JPanel panelCards = new JPanel(new GridLayout(1, 4, 14, 14));
        panelCards.setOpaque(false);

        cardTotalTanaman = new TemaUI.StatCard("TOTAL DATA TANAMAN", "0 Blok", "Terdaftar di sistem", TemaUI.HIJAU_TUA);
        cardTotalLuas = new TemaUI.StatCard("TOTAL LUAS LAHAN", "0,0 Ha", "Akumulasi seluruh blok", TemaUI.HIJAU_SEDANG);
        cardKomposisiFase = new TemaUI.StatCard("SAWIT MUDA / PRODUKTIF", "0 / 0", "TBM vs TM", TemaUI.HIJAU_AKSEN);
        cardEstimasiPanen = new TemaUI.StatCard("ESTIMASI PANEN / TAHUN", "0,0 Ton", "Simulasi TBS (Polimorfik)", new Color(21, 128, 61));

        panelCards.add(cardTotalTanaman);
        panelCards.add(cardTotalLuas);
        panelCards.add(cardKomposisiFase);
        panelCards.add(cardEstimasiPanen);

        JPanel topWrapper = new JPanel(new BorderLayout(0, 16));
        topWrapper.setOpaque(false);
        topWrapper.add(header, BorderLayout.NORTH);
        topWrapper.add(panelCards, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.NORTH);

        // Panel Konten Bawah (2 Kolom)
        JPanel panelBawah = new JPanel(new GridLayout(1, 2, 16, 16));
        panelBawah.setOpaque(false);

        // Kiri: Ringkasan Tanaman
        JPanel kartuTanaman = TemaUI.buatKartuPutih(new BorderLayout(0, 12));
        JPanel headerKiri = new JPanel(new BorderLayout());
        headerKiri.setOpaque(false);
        JLabel lblKiri = new JLabel("Daftar Blok Tanaman Sawit");
        lblKiri.setFont(TemaUI.FONT_SECTION);
        lblKiri.setForeground(TemaUI.HIJAU_TUA);
        JButton btnKeTanaman = TemaUI.buatTombolAksen("Kelola Tanaman");
        btnKeTanaman.addActionListener(e -> mainFrame.tampilkanHalaman(MainFrame.HALAMAN_TANAMAN));
        headerKiri.add(lblKiri, BorderLayout.WEST);
        headerKiri.add(btnKeTanaman, BorderLayout.EAST);

        modelRingkasTanaman = new DefaultTableModel(
                new Object[]{"ID", "Fase", "Luas (Ha)", "Lokasi", "Panen (Ton/Thn)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblTanaman = new JTable(modelRingkasTanaman);
        TemaUI.styleTabel(tblTanaman);
        JScrollPane scrollTanaman = new JScrollPane(tblTanaman);
        scrollTanaman.setBorder(BorderFactory.createLineBorder(TemaUI.BORDER_HALUS));

        kartuTanaman.add(headerKiri, BorderLayout.NORTH);
        kartuTanaman.add(scrollTanaman, BorderLayout.CENTER);

        // Kanan: Kebutuhan Pupuk & Aktivitas Perawatan Terakhir
        JPanel kartuPerawatan = TemaUI.buatKartuPutih(new BorderLayout(0, 12));
        JPanel headerKanan = new JPanel(new BorderLayout());
        headerKanan.setOpaque(false);
        JLabel lblKanan = new JLabel("Ringkasan Perawatan & Pupuk");
        lblKanan.setFont(TemaUI.FONT_SECTION);
        lblKanan.setForeground(TemaUI.HIJAU_TUA);
        JButton btnKePerawatan = TemaUI.buatTombolAksen("Kelola Perawatan");
        btnKePerawatan.addActionListener(e -> mainFrame.tampilkanHalaman(MainFrame.HALAMAN_PERAWATAN));
        headerKanan.add(lblKanan, BorderLayout.WEST);
        headerKanan.add(btnKePerawatan, BorderLayout.EAST);

        JPanel boxPupuk = new JPanel(new BorderLayout());
        boxPupuk.setBackground(TemaUI.HIJAU_SOFT);
        boxPupuk.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.HIJAU_MUDA, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        lblInfoPupuk = new JLabel("Estimasi total kebutuhan pupuk: 0 kg/tahun");
        lblInfoPupuk.setFont(TemaUI.FONT_LABEL);
        lblInfoPupuk.setForeground(TemaUI.HIJAU_TUA);
        boxPupuk.add(lblInfoPupuk, BorderLayout.CENTER);

        modelRingkasRiwayat = new DefaultTableModel(
                new Object[]{"Tanggal", "ID Blok", "Kegiatan", "Keterangan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblRiwayat = new JTable(modelRingkasRiwayat);
        TemaUI.styleTabel(tblRiwayat);
        JScrollPane scrollRiwayat = new JScrollPane(tblRiwayat);
        scrollRiwayat.setBorder(BorderFactory.createLineBorder(TemaUI.BORDER_HALUS));

        JPanel tengahKanan = new JPanel(new BorderLayout(0, 10));
        tengahKanan.setOpaque(false);
        tengahKanan.add(boxPupuk, BorderLayout.NORTH);
        tengahKanan.add(scrollRiwayat, BorderLayout.CENTER);

        JPanel footerKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footerKanan.setOpaque(false);
        JButton btnKePanen = TemaUI.buatTombolUtama("Lihat Rincian Estimasi Panen");
        btnKePanen.addActionListener(e -> mainFrame.tampilkanHalaman(MainFrame.HALAMAN_PANEN));
        footerKanan.add(btnKePanen);

        kartuPerawatan.add(headerKanan, BorderLayout.NORTH);
        kartuPerawatan.add(tengahKanan, BorderLayout.CENTER);
        kartuPerawatan.add(footerKanan, BorderLayout.SOUTH);

        panelBawah.add(kartuTanaman);
        panelBawah.add(kartuPerawatan);
        add(panelBawah, BorderLayout.CENTER);
    }

    public void refreshData() {
        int total = manajemen.getTotalTanaman();
        double luas = manajemen.getTotalLuasLahan();
        int muda = manajemen.getJumlahSawitMuda();
        int produktif = manajemen.getJumlahSawitProduktif();
        double estimasiPanen = manajemen.getTotalEstimasiPanen();
        double totalPupuk = manajemen.getTotalKebutuhanPupuk();

        cardTotalTanaman.setData(total + " Blok", "Aktif dikelola");
        cardTotalLuas.setData(String.format("%.2f Ha", luas),
                String.format("Lahan produktif: %.2f Ha", manajemen.getTotalLuasProduktif()));
        cardKomposisiFase.setData(muda + " TBM | " + produktif + " TM",
                "Muda (" + muda + ") vs Produktif (" + produktif + ")");
        cardEstimasiPanen.setData(String.format("%.1f Ton", estimasiPanen),
                "Estimasi TBS/tahun (simulasi)");

        lblInfoPupuk.setText(String.format(
                "Total Kebutuhan Pupuk: %,.0f kg/thn (%.2f ton/thn)",
                totalPupuk, totalPupuk / 1000.0));

        // Isi tabel ringkas tanaman
        modelRingkasTanaman.setRowCount(0);
        for (TanamanSawit t : manajemen.getDaftarTanaman()) {
            modelRingkasTanaman.addRow(new Object[]{
                    t.getIdTanaman(),
                    t.getJenisFase(),
                    String.format("%.2f", t.getLuasLahan()),
                    t.getLokasiLahan(),
                    String.format("%.2f", t.hitungEstimasiPanen())
            });
        }

        // Isi tabel riwayat perawatan (terbaru di atas)
        modelRingkasRiwayat.setRowCount(0);
        List<RiwayatPerawatan> riwayat = manajemen.getDaftarRiwayat();
        for (int i = riwayat.size() - 1; i >= 0; i--) {
            RiwayatPerawatan r = riwayat.get(i);
            modelRingkasRiwayat.addRow(new Object[]{
                    r.getTanggal().toString(),
                    r.getIdTanaman(),
                    r.getJenisKegiatan(),
                    r.getKeterangan()
            });
        }
    }
}

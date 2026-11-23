import javax.swing.*;
import javax.swing.table.*;

/**
 * Tampilan GUI game "Sawit Farm" hanya menggunakan 2 package Java:
 * javax.swing.* dan javax.swing.table.* (JTable + DefaultTableModel).
 */
public class GameGUI extends JFrame {

    private final GameSawit game;
    private final DefaultTableModel modelLahan;
    private final JTable tabelLahan;

    // Menyimpan koordinat petak yang sedang dipilih pemain
    private int barisTerpilih = 0;
    private int kolomTerpilih = 0;

    // Label info di header atas
    private final JLabel lblKoin;
    private final JLabel lblHari;
    private final JLabel lblBibit;

    // Komponen info & log di panel kanan
    private final JLabel lblPetakInfo;
    private final JLabel lblStatusTanaman;
    private final JLabel lblDetailAtribut;
    private final JTextArea areaAktivitas;

    public GameGUI(GameSawit game) {
        this.game = game;

        // Pengaturan dasar jendela aplikasi (default layout JFrame adalah BorderLayout)
        setTitle("Sawit Farm - Mini Farming Game (OOP Java Swing & JTable)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1020, 660);
        setLocationRelativeTo(null);

        // 1. Bagian atas: header judul & info koin/hari/bibit
        lblKoin = new JLabel();
        lblHari = new JLabel();
        lblBibit = new JLabel();
        add(buatHeader(), "North");

        // 2. Bagian tengah: tabel lahan 4x4 di kiri & panel info di kanan
        String[] headerKolom = { "Kolom 1", "Kolom 2", "Kolom 3", "Kolom 4" };
        modelLahan = new DefaultTableModel(headerKolom, GameSawit.UKURAN_GRID) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelLahan = buatTabelLahan();

        lblPetakInfo = new JLabel();
        lblStatusTanaman = new JLabel();
        lblDetailAtribut = new JLabel();
        areaAktivitas = new JTextArea(8, 24);

        JSplitPane areaUtama = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buatPanelTabelLahan(),
                buatPanelSamping()
        );
        areaUtama.setDividerLocation(620);
        areaUtama.setResizeWeight(0.65);
        areaUtama.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        add(areaUtama, "Center");

        // 3. Bagian bawah: deretan tombol aksi
        add(buatPanelTombolAksi(), "South");

        // Pilih petak awal (1, 1) dan tampilkan pesan pembuka
        tabelLahan.changeSelection(0, 0, false, false);
        tambahLog("Selamat datang di Sawit Farm! Klik sel lahan lalu tekan 'Tanam' untuk memulai.");
        perbaruiTampilan();
    }

    // Membuat panel header atas berisi judul dan status koin, hari, serta bibit
    private JPanel buatHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        JLabel judul = new JLabel(
                "<html><font size='5' color='#166534'><b>SAWIT FARM - Mini Farming Game</b></font><br>"
                        + "<font size='3' color='#475569'>Tanam, siram, beri pupuk, dan panen kelapa sawitmu! | "
                        + "Wildan Afandika (F1D02510141 - 3D)</font></html>"
        );

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.X_AXIS));
        lblKoin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        lblHari.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        lblBibit.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        panelInfo.add(lblKoin);
        panelInfo.add(Box.createHorizontalStrut(8));
        panelInfo.add(lblHari);
        panelInfo.add(Box.createHorizontalStrut(8));
        panelInfo.add(lblBibit);

        header.add(judul);
        header.add(Box.createHorizontalGlue());
        header.add(panelInfo);
        return header;
    }

    // Konfigurasi JTable 4x4 sebagai representasi petak lahan sawit
    private JTable buatTabelLahan() {
        JTable tabel = new JTable(modelLahan);
        tabel.setRowHeight(105);
        tabel.setCellSelectionEnabled(true);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.setShowGrid(true);

        // Pusatkan teks HTML di setiap sel tabel
        DefaultTableCellRenderer perataTengah = new DefaultTableCellRenderer();
        perataTengah.setHorizontalAlignment(SwingConstants.CENTER);
        perataTengah.setVerticalAlignment(SwingConstants.CENTER);
        for (int k = 0; k < GameSawit.UKURAN_GRID; k++) {
            tabel.getColumnModel().getColumn(k).setCellRenderer(perataTengah);
        }

        // Perbarui petak terpilih setiap kali baris atau kolom sel berubah
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                sinkronkanPetakTerpilih();
            }
        });
        tabel.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                sinkronkanPetakTerpilih();
            }
        });

        return tabel;
    }

    // Membungkus tabel lahan dalam panel berbingkai judul
    private JPanel buatPanelTabelLahan() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Lahan Sawit 4x4 (Klik sel untuk memilih petak)"),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));

        JScrollPane scrollTabel = new JScrollPane(tabelLahan);
        panel.add(scrollTabel);
        return panel;
    }

    // Membuat sidebar kanan berisi status petak, aturan main, dan log aktivitas
    private JPanel buatPanelSamping() {
        JPanel kanan = new JPanel();
        kanan.setLayout(new BoxLayout(kanan, BoxLayout.Y_AXIS));
        kanan.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        // Kartu 1: Status petak yang sedang dipilih
        JPanel kartuStatus = new JPanel();
        kartuStatus.setLayout(new BoxLayout(kartuStatus, BoxLayout.Y_AXIS));
        kartuStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Status Petak Terpilih"),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));
        lblPetakInfo.setAlignmentX(LEFT_ALIGNMENT);
        lblStatusTanaman.setAlignmentX(LEFT_ALIGNMENT);
        lblDetailAtribut.setAlignmentX(LEFT_ALIGNMENT);
        kartuStatus.add(lblPetakInfo);
        kartuStatus.add(Box.createVerticalStrut(6));
        kartuStatus.add(lblStatusTanaman);
        kartuStatus.add(Box.createVerticalStrut(6));
        kartuStatus.add(lblDetailAtribut);

        // Kartu 2: Ringkasan aturan main
        JPanel kartuAturan = new JPanel();
        kartuAturan.setLayout(new BoxLayout(kartuAturan, BoxLayout.Y_AXIS));
        kartuAturan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Aturan Pertumbuhan & Ekonomi"),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));
        JLabel lblAturan = new JLabel(
                "<html>"
                        + "- <b>Hari 0-1</b> : Bibit Sawit (20 koin)<br>"
                        + "- <b>Hari 2-3</b> : Sawit Muda<br>"
                        + "- <b>Hari 4+</b>  : Sawit Dewasa<br>"
                        + "- <b>Siram (Gratis)</b> : wajib agar tanaman tumbuh<br>"
                        + "- <b>Pupuk (5 koin)</b> : tumbuh &amp; berbuah 2x cepat<br>"
                        + "- <b>Panen (+50 koin)</b> : pohon tetap bisa berbuah lagi"
                        + "</html>"
        );
        lblAturan.setAlignmentX(LEFT_ALIGNMENT);
        kartuAturan.add(lblAturan);

        // Kartu 3: Riwayat aktivitas pemain
        JPanel kartuLog = new JPanel();
        kartuLog.setLayout(new BoxLayout(kartuLog, BoxLayout.Y_AXIS));
        kartuLog.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Aktivitas Pemain"),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));
        areaAktivitas.setEditable(false);
        areaAktivitas.setLineWrap(true);
        areaAktivitas.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(areaAktivitas);
        kartuLog.add(scrollLog);

        kartuStatus.setAlignmentX(LEFT_ALIGNMENT);
        kartuAturan.setAlignmentX(LEFT_ALIGNMENT);
        kartuLog.setAlignmentX(LEFT_ALIGNMENT);

        kanan.add(kartuStatus);
        kanan.add(Box.createVerticalStrut(8));
        kanan.add(kartuAturan);
        kanan.add(Box.createVerticalStrut(8));
        kanan.add(kartuLog);
        return kanan;
    }

    // Membuat bar tombol aksi di bagian bawah (JPanel default menggunakan FlowLayout)
    private JPanel buatPanelTombolAksi() {
        JPanel bar = new JPanel();
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton btnTanam = new JButton("<html><font color='#15803d'><b>Tanam</b></font> (20 Koin)</html>");
        JButton btnSiram = new JButton("<html><font color='#0284c7'><b>Siram</b></font> (Gratis)</html>");
        JButton btnPupuk = new JButton("<html><font color='#7c3aed'><b>Pupuk</b></font> (5 Koin)</html>");
        JButton btnHari = new JButton("<html><font color='#b45309'><b>Hari Berikutnya</b></font></html>");
        JButton btnPanen = new JButton("<html><font color='#ea580c'><b>Panen</b></font> (+50 Koin)</html>");

        // Hubungkan klik tombol ke method logika di GameSawit
        btnTanam.addActionListener(e -> jalankanAksi(game.tanam(barisTerpilih, kolomTerpilih)));
        btnSiram.addActionListener(e -> jalankanAksi(game.siram(barisTerpilih, kolomTerpilih)));
        btnPupuk.addActionListener(e -> jalankanAksi(game.beriPupuk(barisTerpilih, kolomTerpilih)));
        btnHari.addActionListener(e -> jalankanAksi(game.hariBerikutnya()));
        btnPanen.addActionListener(e -> jalankanAksi(game.panen(barisTerpilih, kolomTerpilih)));

        bar.add(btnTanam);
        bar.add(btnSiram);
        bar.add(btnPupuk);
        bar.add(btnHari);
        bar.add(btnPanen);
        return bar;
    }

    // Baca sel yang sedang dipilih di JTable lalu sinkronkan tampilan info
    private void sinkronkanPetakTerpilih() {
        int b = tabelLahan.getSelectedRow();
        int k = tabelLahan.getSelectedColumn();
        if (game.posisiValid(b, k) && (b != barisTerpilih || k != kolomTerpilih)) {
            barisTerpilih = b;
            kolomTerpilih = k;
            perbaruiInfoPetak();
        }
    }

    // Catat pesan hasil aksi ke log lalu refresh seluruh tampilan
    private void jalankanAksi(String pesan) {
        tambahLog(pesan);
        perbaruiTampilan();
    }

    // Tambah baris log baru dan otomatis scroll ke paling bawah
    private void tambahLog(String pesan) {
        areaAktivitas.append("[H" + game.getHari() + "] " + pesan + "\n");
        areaAktivitas.setCaretPosition(areaAktivitas.getDocument().getLength());
    }

    // Pindah pilihan ke koordinat petak tertentu
    public void pilihPetak(int baris, int kolom) {
        if (game.posisiValid(baris, kolom)) {
            this.barisTerpilih = baris;
            this.kolomTerpilih = kolom;
            tabelLahan.changeSelection(baris, kolom, false, false);
            perbaruiInfoPetak();
        }
    }

    // Sinkronkan header, isi seluruh sel JTable, dan panel status dengan GameSawit
    public void perbaruiTampilan() {
        lblKoin.setText("<html><b>Koin:</b> <font color='#b45309'>" + game.getKoin() + "</font></html>");
        lblHari.setText("<html><b>Hari ke-</b><font color='#0369a1'>" + game.getHari() + "</font></html>");
        lblBibit.setText("<html><b>Bibit Tersedia:</b> <font color='#15803d'>" + game.getBibitTersedia() + "</font></html>");

        for (int b = 0; b < GameSawit.UKURAN_GRID; b++) {
            for (int k = 0; k < GameSawit.UKURAN_GRID; k++) {
                modelLahan.setValueAt(formatSelTabel(b, k, game.getTanaman(b, k)), b, k);
            }
        }

        perbaruiInfoPetak();
    }

    // Perbarui label detail untuk petak yang sedang dipilih
    private void perbaruiInfoPetak() {
        lblPetakInfo.setText("<html><b>Petak Terpilih: (" + (barisTerpilih + 1) + ", " + (kolomTerpilih + 1) + ")</b></html>");
        TanamanSawit t = game.getTanaman(barisTerpilih, kolomTerpilih);
        if (t == null) {
            lblStatusTanaman.setText("<html>Tanah Kosong<br>Klik <b>Tanam (20 Koin)</b> untuk menanam bibit.</html>");
            lblDetailAtribut.setText("<html><font color='#64748b'>Umur: - | Air: - | Pupuk: -</font></html>");
        } else {
            // Polymorphism: getSimbol() & getStatus() mengikuti subclass tanaman
            lblStatusTanaman.setText("<html><b>[" + t.getSimbol() + "]</b> " + t.getStatus() + "</html>");
            lblDetailAtribut.setText(
                    "<html><font color='#0284c7'><b>Umur: " + t.getUmur() + " hr | Air: " + t.getAir()
                            + "/" + TanamanSawit.MAKS_AIR + " | Pupuk: " + t.getPupuk()
                            + "/" + TanamanSawit.MAKS_PUPUK + "</b></font></html>"
            );
        }
    }

    // Format tampilan HTML di dalam setiap sel JTable berdasarkan tahap tanaman
    private String formatSelTabel(int baris, int kolom, TanamanSawit t) {
        String posisi = "(" + (baris + 1) + "," + (kolom + 1) + ")";
        if (t == null) {
            return "<html><center>"
                    + "<font color='#64748b'><b>[ KOSONG ]</b></font><br>"
                    + "<font size='2' color='#94a3b8'>Petak " + posisi + "</font>"
                    + "</center></html>";
        }

        String simbol = t.getSimbol();
        String warna;
        String labelTahap;
        if ("BIBIT".equals(simbol)) {
            warna = "#16a34a";
            labelTahap = "BIBIT SAWIT";
        } else if ("MUDA".equals(simbol)) {
            warna = "#15803d";
            labelTahap = "SAWIT MUDA";
        } else if ("DEWASA".equals(simbol)) {
            warna = "#166534";
            labelTahap = "SAWIT DEWASA";
        } else {
            warna = "#ea580c";
            labelTahap = "SIAP PANEN!";
        }

        return "<html><center>"
                + "<font color='" + warna + "'><b>" + labelTahap + "</b></font><br>"
                + "<font size='2'>Umur: " + t.getUmur() + " hr " + posisi + "</font><br>"
                + "<font size='2' color='#0284c7'>Air: " + t.getAir() + "/" + TanamanSawit.MAKS_AIR + "</font> | "
                + "<font size='2' color='#7c3aed'>Pupuk: " + t.getPupuk() + "/" + TanamanSawit.MAKS_PUPUK + "</font>"
                + "</center></html>";
    }
}

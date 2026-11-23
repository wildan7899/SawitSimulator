import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Tampilan GUI 2D game "Sawit Farm" menggunakan Java Swing.
 * Bertugas menampilkan lahan 4x4, info status, dan meneruskan klik tombol ke GameSawit.
 */
public class GameGUI extends JFrame {

    // Palet warna utama tampilan game
    private static final Color HIJAU_HEADER = new Color(34, 97, 50);
    private static final Color HIJAU_RUMPUT = new Color(118, 185, 71);
    private static final Color HIJAU_MUDA = new Color(220, 252, 231);
    private static final Color COKELAT_KAYU = new Color(120, 72, 34);
    private static final Color COKELAT_TANAH = new Color(161, 114, 72);
    private static final Color COKELAT_BASAH = new Color(115, 76, 44);
    private static final Color BIRU_AIR = new Color(2, 132, 199);
    private static final Color KUNING_CERAH = new Color(250, 204, 21);
    private static final Color ORANYE_PANEN = new Color(234, 88, 12);
    private static final Color UNGU_PUPUK = new Color(124, 58, 237);
    private static final Color LATAR_PANEL = new Color(247, 250, 245);
    private static final Color TEKS_GELAP = new Color(30, 41, 59);

    private final GameSawit game;
    private final PetakPanel[][] petakGrid;
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
        this.petakGrid = new PetakPanel[GameSawit.UKURAN_GRID][GameSawit.UKURAN_GRID];

        // Pengaturan dasar jendela aplikasi
        setTitle("Sawit Farm - Mini Farming Game (OOP Java Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setMinimumSize(new Dimension(960, 650));
        setLocationRelativeTo(null); // Posisikan di tengah layar
        setLayout(new BorderLayout(0, 0));

        // 1. Bagian atas: header judul & badge info
        lblKoin = buatBadgeInfo("Koin: 100", KUNING_CERAH, TEKS_GELAP);
        lblHari = buatBadgeInfo("Hari ke-1", new Color(186, 230, 253), new Color(12, 74, 110));
        lblBibit = buatBadgeInfo("Bibit Tersedia: 5", HIJAU_MUDA, HIJAU_HEADER);
        add(buatHeader(), BorderLayout.NORTH);

        // 2. Bagian tengah: grid lahan 4x4 di kiri & panel info di kanan
        JPanel areaUtama = new JPanel(new BorderLayout(16, 0));
        areaUtama.setBackground(LATAR_PANEL);
        areaUtama.setBorder(new EmptyBorder(14, 16, 12, 16));

        areaUtama.add(buatPanelGridLahan(), BorderLayout.CENTER);

        lblPetakInfo = new JLabel("Petak Terpilih: (1, 1)");
        lblStatusTanaman = new JLabel("Tanah Kosong");
        lblDetailAtribut = new JLabel("Umur: -  |  Air: -  |  Pupuk: -");
        areaAktivitas = new JTextArea();
        areaUtama.add(buatPanelSamping(), BorderLayout.EAST);

        add(areaUtama, BorderLayout.CENTER);

        // 3. Bagian bawah: deretan tombol aksi
        add(buatPanelTombolAksi(), BorderLayout.SOUTH);

        tambahLog("Selamat datang di Sawit Farm! Klik petak tanah lalu tekan 'Tanam' untuk memulai.");
        perbaruiTampilan();
    }

    // Membuat panel header atas berisi judul dan badge koin/hari/bibit
    private JPanel buatHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(HIJAU_HEADER);
        header.setBorder(new EmptyBorder(12, 18, 12, 18));

        JPanel kiri = new JPanel();
        kiri.setOpaque(false);
        kiri.setLayout(new BoxLayout(kiri, BoxLayout.Y_AXIS));

        JLabel judul = new JLabel("SAWIT FARM - Mini Farming Game");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        judul.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Tanam, siram, beri pupuk, dan panen kelapa sawitmu!  |  Wildan Afandika (F1D02510141 - 3D)");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(HIJAU_MUDA);

        kiri.add(judul);
        kiri.add(Box.createVerticalStrut(2));
        kiri.add(sub);

        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        kanan.setOpaque(false);
        kanan.add(lblKoin);
        kanan.add(lblHari);
        kanan.add(lblBibit);

        header.add(kiri, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);
        return header;
    }

    // Helper untuk membuat label kotak berwarna di header
    private JLabel buatBadgeInfo(String teks, Color bg, Color fg) {
        JLabel badge = new JLabel(teks);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.setOpaque(true);
        badge.setBackground(bg);
        badge.setForeground(fg);
        badge.setBorder(new CompoundBorder(
                new LineBorder(new Color(0, 0, 0, 40), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        return badge;
    }

    // Membuat area kebun berisi 16 petak (4x4) yang bisa diklik
    private JPanel buatPanelGridLahan() {
        JPanel bingkaiKebun = new JPanel(new BorderLayout(0, 8));
        bingkaiKebun.setBackground(HIJAU_RUMPUT);
        bingkaiKebun.setBorder(new CompoundBorder(
                new LineBorder(COKELAT_KAYU, 5, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblJudulKebun = new JLabel("Lahan Sawit 4x4 (Klik petak untuk memilih)");
        lblJudulKebun.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblJudulKebun.setForeground(Color.WHITE);
        bingkaiKebun.add(lblJudulKebun, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(GameSawit.UKURAN_GRID, GameSawit.UKURAN_GRID, 10, 10));
        grid.setOpaque(false);

        for (int b = 0; b < GameSawit.UKURAN_GRID; b++) {
            for (int k = 0; k < GameSawit.UKURAN_GRID; k++) {
                PetakPanel petak = new PetakPanel(b, k);
                petakGrid[b][k] = petak;
                grid.add(petak);
            }
        }

        bingkaiKebun.add(grid, BorderLayout.CENTER);
        return bingkaiKebun;
    }

    // Membuat sidebar kanan berisi 3 kartu: status petak, aturan, dan log aktivitas
    private JPanel buatPanelSamping() {
        JPanel kanan = new JPanel();
        kanan.setOpaque(false);
        kanan.setPreferredSize(new Dimension(320, 0));
        kanan.setLayout(new BoxLayout(kanan, BoxLayout.Y_AXIS));

        // Kartu 1: Status tanaman pada petak yang dipilih
        JPanel kartuStatus = buatKartuInfo("Status Petak Terpilih");
        JPanel isiStatus = new JPanel();
        isiStatus.setOpaque(false);
        isiStatus.setLayout(new BoxLayout(isiStatus, BoxLayout.Y_AXIS));

        lblPetakInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPetakInfo.setForeground(HIJAU_HEADER);

        lblStatusTanaman.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStatusTanaman.setForeground(TEKS_GELAP);

        lblDetailAtribut.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDetailAtribut.setForeground(BIRU_AIR);

        isiStatus.add(lblPetakInfo);
        isiStatus.add(Box.createVerticalStrut(6));
        isiStatus.add(lblStatusTanaman);
        isiStatus.add(Box.createVerticalStrut(6));
        isiStatus.add(lblDetailAtribut);
        kartuStatus.add(isiStatus, BorderLayout.CENTER);
        kartuStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 135));

        // Kartu 2: Ringkasan aturan main
        JPanel kartuAturan = buatKartuInfo("Aturan Pertumbuhan & Ekonomi");
        JTextArea txtAturan = new JTextArea(
                "- Hari 0-1 : Bibit Sawit (20 koin)\n"
                        + "- Hari 2-3 : Sawit Muda\n"
                        + "- Hari 4+  : Sawit Dewasa\n"
                        + "- Siram (Gratis): wajib agar tanaman tumbuh\n"
                        + "- Pupuk (5 koin): tumbuh & berbuah 2x cepat\n"
                        + "- Panen (+50 koin): pohon tetap bisa berbuah lagi!"
        );
        txtAturan.setEditable(false);
        txtAturan.setOpaque(false);
        txtAturan.setLineWrap(true);
        txtAturan.setWrapStyleWord(true);
        txtAturan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAturan.setForeground(TEKS_GELAP);
        kartuAturan.add(txtAturan, BorderLayout.CENTER);
        kartuAturan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        // Kartu 3: Riwayat aksi pemain (bisa di-scroll)
        JPanel kartuLog = buatKartuInfo("Aktivitas Pemain");
        areaAktivitas.setEditable(false);
        areaAktivitas.setLineWrap(true);
        areaAktivitas.setWrapStyleWord(true);
        areaAktivitas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaAktivitas.setBackground(new Color(248, 250, 252));
        areaAktivitas.setForeground(TEKS_GELAP);
        areaAktivitas.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane scrollLog = new JScrollPane(areaAktivitas);
        scrollLog.setBorder(new LineBorder(new Color(203, 213, 225), 1, true));
        kartuLog.add(scrollLog, BorderLayout.CENTER);

        kanan.add(kartuStatus);
        kanan.add(Box.createVerticalStrut(10));
        kanan.add(kartuAturan);
        kanan.add(Box.createVerticalStrut(10));
        kanan.add(kartuLog);
        return kanan;
    }

    // Template kotak kartu putih dengan judul di bagian atas
    private JPanel buatKartuInfo(String judul) {
        JPanel kartu = new JPanel(new BorderLayout(0, 8));
        kartu.setBackground(Color.WHITE);
        kartu.setBorder(new CompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblJudul.setForeground(HIJAU_HEADER);
        kartu.add(lblJudul, BorderLayout.NORTH);
        return kartu;
    }

    // Membuat bar tombol aksi di bagian bawah beserta handler kliknya
    private JPanel buatPanelTombolAksi() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bar.setBackground(new Color(236, 244, 237));
        bar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(187, 214, 191)));

        JButton btnTanam = buatTombolAksi("Tanam (20 Koin)", new Color(22, 163, 74), Color.WHITE, "TANAM");
        JButton btnSiram = buatTombolAksi("Siram (Gratis)", BIRU_AIR, Color.WHITE, "SIRAM");
        JButton btnPupuk = buatTombolAksi("Pupuk (5 Koin)", UNGU_PUPUK, Color.WHITE, "PUPUK");
        JButton btnHari = buatTombolAksi("Hari Berikutnya", KUNING_CERAH, TEKS_GELAP, "HARI");
        JButton btnPanen = buatTombolAksi("Panen (+50 Koin)", ORANYE_PANEN, Color.WHITE, "PANEN");

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

    // Helper styling tombol aksi supaya tampil seragam
    private JButton buatTombolAksi(String teks, Color bg, Color fg, String jenisIkon) {
        JButton btn = new JButton(teks, new IkonTombol(jenisIkon));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setIconTextGap(8);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        return btn;
    }

    // Catat pesan hasil aksi ke log lalu refresh UI
    private void jalankanAksi(String pesan) {
        tambahLog(pesan);
        perbaruiTampilan();
    }

    // Tambah baris log baru dan otomatis scroll ke paling bawah
    private void tambahLog(String pesan) {
        areaAktivitas.append("[H" + game.getHari() + "] " + pesan + "\n");
        areaAktivitas.setCaretPosition(areaAktivitas.getDocument().getLength());
    }

    // Pindah pilihan ke petak yang diklik pemain
    public void pilihPetak(int baris, int kolom) {
        if (game.posisiValid(baris, kolom)) {
            this.barisTerpilih = baris;
            this.kolomTerpilih = kolom;
            perbaruiTampilan();
        }
    }

    // Sinkronkan semua teks info dengan data terbaru di GameSawit dan gambar ulang grid
    public void perbaruiTampilan() {
        lblKoin.setText("Koin: " + game.getKoin());
        lblHari.setText("Hari ke-" + game.getHari());
        lblBibit.setText("Bibit Tersedia: " + game.getBibitTersedia());

        lblPetakInfo.setText("Petak Terpilih: (" + (barisTerpilih + 1) + ", " + (kolomTerpilih + 1) + ")");
        TanamanSawit t = game.getTanaman(barisTerpilih, kolomTerpilih);
        if (t == null) {
            lblStatusTanaman.setText("<html>Tanah Kosong<br>Klik <b>Tanam (20 Koin)</b> untuk menanam bibit.</html>");
            lblDetailAtribut.setText("Umur: -  |  Air: -  |  Pupuk: -");
        } else {
            // Polymorphism: getSimbol() & getStatus() mengikuti subclass tanaman
            lblStatusTanaman.setText("<html>[" + t.getSimbol() + "] " + t.getStatus() + "</html>");
            lblDetailAtribut.setText("Umur: " + t.getUmur() + " hr  |  Air: " + t.getAir()
                    + "/" + TanamanSawit.MAKS_AIR + "  |  Pupuk: " + t.getPupuk() + "/" + TanamanSawit.MAKS_PUPUK);
        }

        for (int b = 0; b < GameSawit.UKURAN_GRID; b++) {
            for (int k = 0; k < GameSawit.UKURAN_GRID; k++) {
                petakGrid[b][k].repaint();
            }
        }
    }

    // Panel custom untuk menggambar 1 petak lahan & ilustrasi 2D tanamannya
    private class PetakPanel extends JPanel {
        private final int baris;
        private final int kolom;

        PetakPanel(int baris, int kolom) {
            this.baris = baris;
            this.kolom = kolom;
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Pilih petak ini saat diklik mouse
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    pilihPetak(baris, kolom);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            TanamanSawit tanaman = game.getTanaman(baris, kolom);
            boolean terpilih = (baris == barisTerpilih && kolom == kolomTerpilih);

            // Tanah berwarna lebih gelap kalau sudah disiram (air > 0)
            Color warnaTanah = (tanaman != null && tanaman.getAir() > 0) ? COKELAT_BASAH : COKELAT_TANAH;
            g2.setColor(warnaTanah);
            g2.fill(new RoundRectangle2D.Double(3, 3, w - 6, h - 6, 16, 16));

            // Garis horizontal tipis sebagai tekstur tanah
            g2.setColor(new Color(255, 255, 255, 26));
            g2.setStroke(new BasicStroke(2f));
            for (int y = h - 30; y > 24; y -= 18) {
                g2.drawLine(14, y, w - 14, y);
            }

            // Pilih gambar 2D sesuai simbol tahap pertumbuhan
            int cx = w / 2;
            int groundY = h - 30;

            if (tanaman == null) {
                gambarTanahKosong(g2, cx, groundY);
            } else {
                String simbol = tanaman.getSimbol();
                if ("BIBIT".equals(simbol)) {
                    gambarBibit(g2, cx, groundY);
                } else if ("MUDA".equals(simbol)) {
                    gambarSawitMuda(g2, cx, groundY);
                } else if ("DEWASA".equals(simbol)) {
                    gambarSawitDewasa(g2, cx, groundY, false);
                } else {
                    gambarSawitDewasa(g2, cx, groundY, true);
                }
                gambarIndikatorPerawatan(g2, w, tanaman);
            }

            // Label tahap di bagian bawah petak (kuning kalau siap panen)
            String labelBawah = (tanaman == null) ? "Kosong" : tanaman.getSimbol();
            Color warnaLabel = (tanaman != null && tanaman.isSiapPanen()) ? KUNING_CERAH : new Color(0, 0, 0, 120);
            Color teksLabel = (tanaman != null && tanaman.isSiapPanen()) ? TEKS_GELAP : Color.WHITE;
            g2.setColor(warnaLabel);
            g2.fill(new RoundRectangle2D.Double(10, h - 25, w - 20, 18, 8, 8));
            g2.setColor(teksLabel);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(labelBawah, (w - fm.stringWidth(labelBawah)) / 2, h - 12);

            // Bingkai petak: kuning tebal jika sedang dipilih
            if (terpilih) {
                g2.setColor(KUNING_CERAH);
                g2.setStroke(new BasicStroke(4.0f));
            } else {
                g2.setColor(new Color(88, 52, 24));
                g2.setStroke(new BasicStroke(2.0f));
            }
            g2.draw(new RoundRectangle2D.Double(3, 3, w - 6, h - 6, 16, 16));

            g2.dispose();
        }

        // Gambar tanda '+' untuk petak kosong
        private void gambarTanahKosong(Graphics2D g2, int cx, int groundY) {
            g2.setColor(new Color(92, 60, 32, 120));
            g2.fillOval(cx - 22, groundY - 8, 44, 10);
            g2.setColor(new Color(255, 255, 255, 110));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString("+", cx - 6, groundY - 22);
        }

        // Gambar tunas kecil untuk BibitSawit
        private void gambarBibit(Graphics2D g2, int cx, int groundY) {
            g2.setColor(new Color(92, 56, 26));
            g2.fillOval(cx - 18, groundY - 7, 36, 10);
            g2.setColor(new Color(74, 222, 128));
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(cx, groundY - 4, cx, groundY - 24);
            g2.setColor(new Color(34, 197, 94));
            g2.fillOval(cx - 18, groundY - 30, 18, 10);
            g2.fillOval(cx, groundY - 32, 18, 10);
        }

        // Gambar pohon kecil berpelepah untuk SawitMuda
        private void gambarSawitMuda(Graphics2D g2, int cx, int groundY) {
            g2.setColor(new Color(101, 67, 33));
            g2.fillRoundRect(cx - 7, groundY - 28, 14, 26, 6, 6);
            g2.setColor(new Color(22, 163, 74));
            g2.fillOval(cx - 34, groundY - 46, 36, 14);
            g2.fillOval(cx - 2, groundY - 46, 36, 14);
            g2.setColor(new Color(34, 197, 94));
            g2.fillOval(cx - 26, groundY - 58, 28, 14);
            g2.fillOval(cx - 2, groundY - 58, 28, 14);
            g2.fillOval(cx - 8, groundY - 64, 16, 24);
        }

        // Gambar pohon tinggi untuk SawitDewasa (plus buah oranye jika siap panen)
        private void gambarSawitDewasa(Graphics2D g2, int cx, int groundY, boolean berbuah) {
            // Batang & garis sisik
            g2.setColor(new Color(92, 61, 34));
            g2.fillRoundRect(cx - 8, groundY - 44, 16, 42, 6, 6);
            g2.setColor(new Color(120, 82, 48));
            for (int y = groundY - 10; y >= groundY - 38; y -= 7) {
                g2.drawLine(cx - 6, y, cx + 6, y);
            }

            // Daun / pelepah rimbun
            int topY = groundY - 46;
            g2.setColor(new Color(21, 128, 61));
            g2.fillOval(cx - 40, topY - 4, 42, 14);
            g2.fillOval(cx - 2, topY - 4, 42, 14);
            g2.setColor(new Color(34, 197, 94));
            g2.fillOval(cx - 34, topY - 15, 36, 13);
            g2.fillOval(cx - 2, topY - 15, 36, 13);
            g2.setColor(new Color(74, 222, 128));
            g2.fillOval(cx - 20, topY - 22, 22, 12);
            g2.fillOval(cx - 2, topY - 22, 22, 12);

            // Tandan buah sawit saat siap panen
            if (berbuah) {
                g2.setColor(ORANYE_PANEN);
                g2.fillOval(cx - 16, topY + 5, 14, 14);
                g2.fillOval(cx + 2, topY + 5, 14, 14);
                g2.setColor(KUNING_CERAH);
                g2.fillOval(cx - 12, topY + 8, 5, 5);
                g2.fillOval(cx + 6, topY + 8, 5, 5);
            }
        }

        // Gambar titik indikator air (biru, kiri atas) dan pupuk (kuning, kanan atas)
        private void gambarIndikatorPerawatan(Graphics2D g2, int w, TanamanSawit t) {
            for (int i = 0; i < TanamanSawit.MAKS_AIR; i++) {
                g2.setColor(i < t.getAir() ? new Color(56, 189, 248) : new Color(0, 0, 0, 70));
                g2.fill(new Ellipse2D.Double(10 + i * 10, 10, 7, 7));
            }
            for (int i = 0; i < TanamanSawit.MAKS_PUPUK; i++) {
                g2.setColor(i < t.getPupuk() ? KUNING_CERAH : new Color(0, 0, 0, 70));
                g2.fill(new Ellipse2D.Double(w - 17 - i * 10, 10, 7, 7));
            }
        }
    }

    // Ikon vektor 2D untuk tombol aksi (pengganti emoji biar tampil rapi di semua OS)
    private static class IkonTombol implements Icon {
        private final String tipe;

        IkonTombol(String tipe) {
            this.tipe = tipe;
        }

        @Override
        public int getIconWidth() {
            return 18;
        }

        @Override
        public int getIconHeight() {
            return 18;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);

            switch (tipe) {
                case "TANAM": // Ikon tunas
                    g2.setColor(HIJAU_MUDA);
                    g2.fillOval(2, 4, 7, 5);
                    g2.fillOval(9, 4, 7, 5);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(9, 8, 9, 16);
                    break;
                case "SIRAM": // Ikon tetes air
                    Path2D tetes = new Path2D.Double();
                    tetes.moveTo(9, 2);
                    tetes.curveTo(15, 9, 15, 16, 9, 16);
                    tetes.curveTo(3, 16, 3, 9, 9, 2);
                    g2.setColor(new Color(224, 242, 254));
                    g2.fill(tetes);
                    break;
                case "PUPUK": // Ikon botol pupuk
                    g2.setColor(KUNING_CERAH);
                    g2.fillRoundRect(4, 5, 10, 11, 4, 4);
                    g2.setColor(Color.WHITE);
                    g2.fillRect(6, 2, 6, 3);
                    break;
                case "HARI": // Ikon matahari
                    g2.setColor(ORANYE_PANEN);
                    g2.fillOval(4, 4, 10, 10);
                    g2.setStroke(new BasicStroke(1.8f));
                    g2.drawLine(9, 0, 9, 3);
                    g2.drawLine(9, 15, 9, 18);
                    g2.drawLine(0, 9, 3, 9);
                    g2.drawLine(15, 9, 18, 9);
                    break;
                case "PANEN": // Ikon keranjang panen
                    g2.setColor(KUNING_CERAH);
                    g2.fillRoundRect(2, 7, 14, 9, 4, 4);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawArc(5, 2, 8, 9, 0, 180);
                    break;
                default:
                    break;
            }
            g2.dispose();
        }
    }
}

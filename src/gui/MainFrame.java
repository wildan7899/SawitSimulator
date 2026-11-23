package gui;

import service.ManajemenSawit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MainFrame adalah jendela utama aplikasi Sistem Manajemen Perkebunan Kelapa Sawit.
 * Menggunakan BorderLayout dengan sidebar navigasi di sebelah kiri dan CardLayout
 * di area utama agar perpindahan antar halaman berjalan mulus tanpa absolute positioning.
 */
public class MainFrame extends JFrame {

    public static final String HALAMAN_DASHBOARD = "DASHBOARD";
    public static final String HALAMAN_TANAMAN = "TANAMAN";
    public static final String HALAMAN_PERAWATAN = "PERAWATAN";
    public static final String HALAMAN_PANEN = "PANEN";

    private final ManajemenSawit manajemen;
    private final CardLayout cardLayout;
    private final JPanel panelKonten;
    private final Map<String, JButton> tombolNavigasi;

    private final DashboardPanel dashboardPanel;
    private final TanamanPanel tanamanPanel;
    private final PerawatanPanel perawatanPanel;
    private final PanenPanel panenPanel;

    public MainFrame(ManajemenSawit manajemen) {
        this.manajemen = manajemen;
        this.cardLayout = new CardLayout();
        this.panelKonten = new JPanel(cardLayout);
        this.tombolNavigasi = new LinkedHashMap<>();

        setTitle("Sistem Manajemen Perkebunan Kelapa Sawit (Sawit Management System)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 740);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inisialisasi 4 halaman utama
        dashboardPanel = new DashboardPanel(manajemen, this);
        tanamanPanel = new TanamanPanel(manajemen, this);
        perawatanPanel = new PerawatanPanel(manajemen, this);
        panenPanel = new PanenPanel(manajemen);

        panelKonten.add(dashboardPanel, HALAMAN_DASHBOARD);
        panelKonten.add(tanamanPanel, HALAMAN_TANAMAN);
        panelKonten.add(perawatanPanel, HALAMAN_PERAWATAN);
        panelKonten.add(panenPanel, HALAMAN_PANEN);

        add(buatSidebar(), BorderLayout.WEST);
        add(panelKonten, BorderLayout.CENTER);

        refreshSemuaPanel();
        tampilkanHalaman(HALAMAN_DASHBOARD);
    }

    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(TemaUI.HIJAU_TUA);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(22, 16, 18, 16));

        // Brand Header
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JLabel lblBrand = new JLabel("SAWIT MANAGER");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblBrand.setForeground(TemaUI.PUTIH);

        JLabel lblSubBrand = new JLabel("Sistem Perkebunan Sawit");
        lblSubBrand.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubBrand.setForeground(TemaUI.HIJAU_MUDA);

        JSeparator sep = new JSeparator();
        sep.setForeground(TemaUI.HIJAU_SEDANG);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        brand.add(lblBrand);
        brand.add(Box.createVerticalStrut(4));
        brand.add(lblSubBrand);
        brand.add(Box.createVerticalStrut(16));
        brand.add(sep);
        brand.add(Box.createVerticalStrut(16));

        // Daftar Menu Navigasi
        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        tambahTombolNav(menu, "Dashboard", HALAMAN_DASHBOARD);
        tambahTombolNav(menu, "Manajemen Tanaman", HALAMAN_TANAMAN);
        tambahTombolNav(menu, "Manajemen Perawatan", HALAMAN_PERAWATAN);
        tambahTombolNav(menu, "Estimasi Panen", HALAMAN_PANEN);

        JPanel atas = new JPanel(new BorderLayout());
        atas.setOpaque(false);
        atas.add(brand, BorderLayout.NORTH);
        atas.add(menu, BorderLayout.CENTER);

        // Footer Sidebar: Simpan Manual + Identitas Mahasiswa
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        JButton btnSimpan = TemaUI.buatTombol("Simpan Data ke File", TemaUI.HIJAU_AKSEN, TemaUI.PUTIH);
        btnSimpan.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSimpan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnSimpan.addActionListener(e -> {
            try {
                manajemen.simpanData();
                JOptionPane.showMessageDialog(this,
                        "Seluruh data tanaman dan riwayat perawatan berhasil disimpan ke folder 'data/'.",
                        "Penyimpanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan data: " + ex.getMessage(),
                        "Penyimpanan Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        JSeparator sepBawah = new JSeparator();
        sepBawah.setForeground(TemaUI.HIJAU_SEDANG);
        sepBawah.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JLabel lblIdentitas = new JLabel(
                "<html><div style='color:#D8F3DC; font-size:11px;'>"
                        + "<b>Wildan Afandika</b><br>"
                        + "NIM: F1D02510141 | Kelas 3D<br>"
                        + "Tugas Individu PBO</div></html>"
        );
        lblIdentitas.setAlignmentX(Component.LEFT_ALIGNMENT);

        footer.add(btnSimpan);
        footer.add(Box.createVerticalStrut(14));
        footer.add(sepBawah);
        footer.add(Box.createVerticalStrut(10));
        footer.add(lblIdentitas);

        sidebar.add(atas, BorderLayout.NORTH);
        sidebar.add(footer, BorderLayout.SOUTH);
        return sidebar;
    }

    private void tambahTombolNav(JPanel container, String label, String kodeHalaman) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(11, 14, 11, 14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> tampilkanHalaman(kodeHalaman));

        tombolNavigasi.put(kodeHalaman, btn);
        container.add(btn);
        container.add(Box.createVerticalStrut(8));
    }

    public void tampilkanHalaman(String namaHalaman) {
        refreshSemuaPanel();
        cardLayout.show(panelKonten, namaHalaman);

        for (Map.Entry<String, JButton> entry : tombolNavigasi.entrySet()) {
            boolean aktif = entry.getKey().equals(namaHalaman);
            JButton btn = entry.getValue();
            if (aktif) {
                btn.setBackground(TemaUI.HIJAU_MUDA);
                btn.setForeground(TemaUI.HIJAU_TUA);
            } else {
                btn.setBackground(TemaUI.HIJAU_SEDANG);
                btn.setForeground(TemaUI.PUTIH);
            }
        }
    }

    public void refreshSemuaPanel() {
        dashboardPanel.refreshData();
        tanamanPanel.refreshData();
        perawatanPanel.refreshData();
        panenPanel.refreshData();
    }
}

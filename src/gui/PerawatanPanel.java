package gui;

import interfaces.Perawatan;
import model.RiwayatPerawatan;
import model.TanamanSawit;
import service.ManajemenSawit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * PerawatanPanel menampilkan jadwal pemupukan, jadwal penyiraman, kebutuhan pupuk
 * (menggunakan Interface Perawatan & Polymorphism), serta form + tabel riwayat perawatan.
 */
public class PerawatanPanel extends JPanel {

    private final ManajemenSawit manajemen;
    private final MainFrame mainFrame;

    private final JLabel lblTotalPupuk;
    private final DefaultTableModel modelJadwal;
    private final DefaultTableModel modelRiwayat;
    private final JTable tblRiwayat;

    private final JComboBox<TanamanSawit> cmbTanaman;
    private final JTextField txtTanggal;
    private final JComboBox<String> cmbJenis;
    private final JTextField txtKeterangan;

    public PerawatanPanel(ManajemenSawit manajemen, MainFrame mainFrame) {
        this.manajemen = manajemen;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(0, 14));
        setBackground(TemaUI.LATAR_UTAMA);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        // Header + Ringkasan Pupuk
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JPanel judulBox = new JPanel();
        judulBox.setOpaque(false);
        judulBox.setLayout(new BoxLayout(judulBox, BoxLayout.Y_AXIS));
        JLabel lblJudul = new JLabel("Manajemen Perawatan Kelapa Sawit");
        lblJudul.setFont(TemaUI.FONT_JUDUL);
        lblJudul.setForeground(TemaUI.HIJAU_TUA);
        JLabel lblSub = new JLabel("Jadwal pemupukan & penyiraman (Interface Perawatan), estimasi pupuk, dan catatan kegiatan");
        lblSub.setFont(TemaUI.FONT_SUBJUDUL);
        lblSub.setForeground(TemaUI.TEKS_SEKUNDER);
        judulBox.add(lblJudul);
        judulBox.add(Box.createVerticalStrut(3));
        judulBox.add(lblSub);

        JPanel badgePupuk = new JPanel(new BorderLayout());
        badgePupuk.setBackground(TemaUI.HIJAU_MUDA);
        badgePupuk.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.HIJAU_AKSEN, 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));
        lblTotalPupuk = new JLabel("Total Kebutuhan Pupuk: 0 kg/tahun");
        lblTotalPupuk.setFont(TemaUI.FONT_LABEL);
        lblTotalPupuk.setForeground(TemaUI.HIJAU_TUA);
        badgePupuk.add(lblTotalPupuk, BorderLayout.CENTER);

        header.add(judulBox, BorderLayout.WEST);
        header.add(badgePupuk, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Konten Utama: Atas = Jadwal & Kebutuhan Pupuk, Bawah = Form + Riwayat
        JPanel konten = new JPanel(new GridLayout(2, 1, 0, 14));
        konten.setOpaque(false);

        // Bagian Atas: Tabel Jadwal Perawatan
        JPanel kartuJadwal = TemaUI.buatKartuPutih(new BorderLayout(0, 10));
        JLabel lblJadwal = new JLabel("Jadwal Pemupukan, Penyiraman & Kebutuhan Pupuk per Blok");
        lblJadwal.setFont(TemaUI.FONT_SECTION);
        lblJadwal.setForeground(TemaUI.HIJAU_TUA);

        modelJadwal = new DefaultTableModel(
                new Object[]{"ID Blok", "Fase", "Luas (Ha)", "Pupuk (Kg/Thn)", "Jadwal Pemupukan", "Jadwal Penyiraman"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblJadwal = new JTable(modelJadwal);
        TemaUI.styleTabel(tblJadwal);
        tblJadwal.getColumnModel().getColumn(0).setPreferredWidth(85);
        tblJadwal.getColumnModel().getColumn(0).setMinWidth(80);
        tblJadwal.getColumnModel().getColumn(1).setPreferredWidth(115);
        tblJadwal.getColumnModel().getColumn(1).setMinWidth(105);
        tblJadwal.getColumnModel().getColumn(2).setPreferredWidth(75);
        tblJadwal.getColumnModel().getColumn(3).setPreferredWidth(105);
        tblJadwal.getColumnModel().getColumn(4).setPreferredWidth(285);
        tblJadwal.getColumnModel().getColumn(5).setPreferredWidth(265);

        JScrollPane scrollJadwal = new JScrollPane(tblJadwal);
        scrollJadwal.setBorder(BorderFactory.createLineBorder(TemaUI.BORDER_HALUS));
        kartuJadwal.add(lblJadwal, BorderLayout.NORTH);
        kartuJadwal.add(scrollJadwal, BorderLayout.CENTER);

        // Bagian Bawah: Form Catat Perawatan (Kiri) + Riwayat Perawatan (Kanan)
        JPanel panelBawah = new JPanel(new BorderLayout(14, 0));
        panelBawah.setOpaque(false);

        JPanel kartuForm = TemaUI.buatKartuPutih(new BorderLayout(0, 10));
        kartuForm.setPreferredSize(new Dimension(380, 0));
        JLabel lblForm = new JLabel("Catat Kegiatan Perawatan");
        lblForm.setFont(TemaUI.FONT_SECTION);
        lblForm.setForeground(TemaUI.HIJAU_TUA);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 3, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        cmbTanaman = new JComboBox<>();
        txtTanggal = new JTextField(LocalDate.now().toString());
        cmbJenis = new JComboBox<>(new String[]{"Pemupukan", "Penyiraman"});
        txtKeterangan = new JTextField();

        TemaUI.styleComboBox(cmbTanaman);
        TemaUI.styleField(txtTanggal);
        TemaUI.styleComboBox(cmbJenis);
        TemaUI.styleField(txtKeterangan);

        tambahBarisForm(formGrid, gbc, 0, "Blok Tanaman:", cmbTanaman);
        tambahBarisForm(formGrid, gbc, 1, "Tanggal:", txtTanggal);
        tambahBarisForm(formGrid, gbc, 2, "Jenis Kegiatan:", cmbJenis);
        tambahBarisForm(formGrid, gbc, 3, "Keterangan:", txtKeterangan);

        JButton btnCatat = TemaUI.buatTombolUtama("Simpan Catatan Perawatan");
        btnCatat.addActionListener(e -> aksiCatatPerawatan());

        kartuForm.add(lblForm, BorderLayout.NORTH);
        kartuForm.add(formGrid, BorderLayout.CENTER);
        kartuForm.add(btnCatat, BorderLayout.SOUTH);

        JPanel kartuRiwayat = TemaUI.buatKartuPutih(new BorderLayout(0, 10));
        JPanel headerRiwayat = new JPanel(new BorderLayout());
        headerRiwayat.setOpaque(false);
        JLabel lblRiwayat = new JLabel("Riwayat Kegiatan Perawatan");
        lblRiwayat.setFont(TemaUI.FONT_SECTION);
        lblRiwayat.setForeground(TemaUI.HIJAU_TUA);
        JButton btnHapusRiwayat = TemaUI.buatTombolBahaya("Hapus Riwayat");
        btnHapusRiwayat.addActionListener(e -> aksiHapusRiwayat());
        headerRiwayat.add(lblRiwayat, BorderLayout.WEST);
        headerRiwayat.add(btnHapusRiwayat, BorderLayout.EAST);

        modelRiwayat = new DefaultTableModel(
                new Object[]{"ID", "Blok", "Tanggal", "Jenis", "Keterangan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblRiwayat = new JTable(modelRiwayat);
        tblRiwayat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TemaUI.styleTabel(tblRiwayat);
        tblRiwayat.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblRiwayat.getColumnModel().getColumn(0).setMinWidth(65);
        tblRiwayat.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblRiwayat.getColumnModel().getColumn(1).setMinWidth(75);
        tblRiwayat.getColumnModel().getColumn(2).setPreferredWidth(95);
        tblRiwayat.getColumnModel().getColumn(2).setMinWidth(90);
        tblRiwayat.getColumnModel().getColumn(3).setPreferredWidth(95);
        tblRiwayat.getColumnModel().getColumn(4).setPreferredWidth(230);

        JScrollPane scrollRiwayat = new JScrollPane(tblRiwayat);
        scrollRiwayat.setBorder(BorderFactory.createLineBorder(TemaUI.BORDER_HALUS));

        kartuRiwayat.add(headerRiwayat, BorderLayout.NORTH);
        kartuRiwayat.add(scrollRiwayat, BorderLayout.CENTER);

        panelBawah.add(kartuForm, BorderLayout.WEST);
        panelBawah.add(kartuRiwayat, BorderLayout.CENTER);

        konten.add(kartuJadwal);
        konten.add(panelBawah);
        add(konten, BorderLayout.CENTER);
    }

    private void tambahBarisForm(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(TemaUI.FONT_LABEL);
        lbl.setForeground(TemaUI.TEKS_UTAMA);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void aksiCatatPerawatan() {
        TanamanSawit dipilih = (TanamanSawit) cmbTanaman.getSelectedItem();
        if (dipilih == null) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada data tanaman. Tambahkan tanaman terlebih dahulu.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tglStr = txtTanggal.getText().trim();
        String jenis = (String) cmbJenis.getSelectedItem();
        String ket = txtKeterangan.getText().trim();

        if (tglStr.isEmpty() || ket.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tanggal dan keterangan kegiatan perawatan wajib diisi.",
                    "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate tanggal;
        try {
            tanggal = LocalDate.parse(tglStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Format tanggal harus YYYY-MM-DD (contoh: 2026-11-23).",
                    "Format Tanggal Salah", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            RiwayatPerawatan baru = manajemen.catatPerawatan(dipilih.getIdTanaman(), tanggal, jenis, ket);
            txtKeterangan.setText("");
            mainFrame.refreshSemuaPanel();
            JOptionPane.showMessageDialog(this,
                    "Riwayat perawatan '" + baru.getIdRiwayat() + "' berhasil dicatat dan disimpan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mencatat perawatan: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiHapusRiwayat() {
        int row = tblRiwayat.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih baris riwayat perawatan yang ingin dihapus terlebih dahulu.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String idRiwayat = modelRiwayat.getValueAt(row, 0).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Hapus catatan riwayat perawatan '" + idRiwayat + "'?",
                "Konfirmasi Hapus Riwayat",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (manajemen.hapusRiwayat(idRiwayat)) {
                mainFrame.refreshSemuaPanel();
                JOptionPane.showMessageDialog(this,
                        "Riwayat perawatan '" + idRiwayat + "' berhasil dihapus.",
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus riwayat: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        double totalPupuk = manajemen.getTotalKebutuhanPupuk();
        lblTotalPupuk.setText(String.format("Total Kebutuhan Pupuk: %,.0f kg/tahun (%.2f ton)",
                totalPupuk, totalPupuk / 1000.0));

        // Memuat tabel jadwal menggunakan referensi TanamanSawit & Interface Perawatan (Polymorphism)
        modelJadwal.setRowCount(0);
        cmbTanaman.removeAllItems();
        for (TanamanSawit t : manajemen.getDaftarTanaman()) {
            Perawatan kontrakPerawatan = t; // Polymorphism via Interface Perawatan
            modelJadwal.addRow(new Object[]{
                    t.getIdTanaman(),
                    t.getJenisFase(),
                    String.format("%.2f", t.getLuasLahan()),
                    String.format("%,.0f", t.hitungKebutuhanPupuk()),
                    kontrakPerawatan.jadwalkanPemupukan(),
                    kontrakPerawatan.jadwalkanPenyiraman()
            });
            cmbTanaman.addItem(t);
        }

        // Memuat riwayat perawatan (terbaru di atas)
        modelRiwayat.setRowCount(0);
        List<RiwayatPerawatan> riwayat = manajemen.getDaftarRiwayat();
        for (int i = riwayat.size() - 1; i >= 0; i--) {
            RiwayatPerawatan r = riwayat.get(i);
            modelRiwayat.addRow(new Object[]{
                    r.getIdRiwayat(),
                    r.getIdTanaman(),
                    r.getTanggal().toString(),
                    r.getJenisKegiatan(),
                    r.getKeterangan()
            });
        }
    }
}

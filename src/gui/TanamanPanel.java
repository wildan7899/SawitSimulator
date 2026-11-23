package gui;

import model.SawitMuda;
import model.SawitProduktif;
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
 * TanamanPanel menyediakan fitur CRUD lengkap (Tambah, Lihat, Edit, Hapus)
 * serta pencarian berdasarkan ID tanaman atau lokasi lahan.
 */
public class TanamanPanel extends JPanel {

    private final ManajemenSawit manajemen;
    private final MainFrame mainFrame;

    private final JTextField txtCari;
    private final JTextField txtId;
    private final JComboBox<String> cmbFase;
    private final JTextField txtTanggal;
    private final JTextField txtLuas;
    private final JTextField txtLokasi;
    private final JLabel lblParamKhusus;
    private final JTextField txtParamKhusus;

    private final DefaultTableModel tableModel;
    private final JTable table;
    private String idTerpilih;

    public TanamanPanel(ManajemenSawit manajemen, MainFrame mainFrame) {
        this.manajemen = manajemen;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(0, 14));
        setBackground(TemaUI.LATAR_UTAMA);
        setBorder(new EmptyBorder(22, 24, 22, 24));

        // Header + Search Bar
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JPanel judulBox = new JPanel();
        judulBox.setOpaque(false);
        judulBox.setLayout(new BoxLayout(judulBox, BoxLayout.Y_AXIS));
        JLabel lblJudul = new JLabel("Manajemen Data Tanaman Sawit");
        lblJudul.setFont(TemaUI.FONT_JUDUL);
        lblJudul.setForeground(TemaUI.HIJAU_TUA);
        JLabel lblSub = new JLabel("Tambah, ubah, hapus, dan cari blok sawit muda (TBM) maupun sawit produktif (TM)");
        lblSub.setFont(TemaUI.FONT_SUBJUDUL);
        lblSub.setForeground(TemaUI.TEKS_SEKUNDER);
        judulBox.add(lblJudul);
        judulBox.add(Box.createVerticalStrut(3));
        judulBox.add(lblSub);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelCari.setOpaque(false);
        txtCari = new JTextField(18);
        TemaUI.styleField(txtCari);
        txtCari.setToolTipText("Cari berdasarkan ID Tanaman atau Lokasi Lahan");
        JButton btnCari = TemaUI.buatTombolAksen("Cari");
        JButton btnResetCari = TemaUI.buatTombolSekunder("Reset");

        btnCari.addActionListener(e -> muatTabel(manajemen.cariTanaman(txtCari.getText())));
        txtCari.addActionListener(e -> muatTabel(manajemen.cariTanaman(txtCari.getText())));
        btnResetCari.addActionListener(e -> {
            txtCari.setText("");
            muatTabel(manajemen.getDaftarTanaman());
        });

        panelCari.add(new JLabel("Cari ID/Lokasi:"));
        panelCari.add(txtCari);
        panelCari.add(btnCari);
        panelCari.add(btnResetCari);

        header.add(judulBox, BorderLayout.WEST);
        header.add(panelCari, BorderLayout.EAST);

        // Form Input Terstruktur (GridBagLayout)
        JPanel kartuForm = TemaUI.buatKartuPutih(new BorderLayout(0, 12));
        JLabel lblFormJudul = new JLabel("Form Input & Edit Data Tanaman");
        lblFormJudul.setFont(TemaUI.FONT_SECTION);
        lblFormJudul.setForeground(TemaUI.HIJAU_TUA);

        JPanel gridForm = new JPanel(new GridBagLayout());
        gridForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(14);
        cmbFase = new JComboBox<>(new String[]{"Sawit Muda", "Sawit Produktif"});
        txtTanggal = new JTextField(LocalDate.now().toString(), 14);
        txtLuas = new JTextField(14);
        txtLokasi = new JTextField(18);
        lblParamKhusus = new JLabel("Tinggi Rata-rata (cm):");
        txtParamKhusus = new JTextField("120", 14);

        TemaUI.styleField(txtId);
        TemaUI.styleComboBox(cmbFase);
        TemaUI.styleField(txtTanggal);
        TemaUI.styleField(txtLuas);
        TemaUI.styleField(txtLokasi);
        TemaUI.styleField(txtParamKhusus);

        cmbFase.addActionListener(e -> perbaruiLabelParameter());

        // Baris 0 (2 kolom form yang lega)
        tambahField(gridForm, gbc, 0, 0, "ID Tanaman (cth: SWT-006):", txtId);
        tambahField(gridForm, gbc, 2, 0, "Fase Pertumbuhan:", cmbFase);

        // Baris 1
        tambahField(gridForm, gbc, 0, 1, "Tanggal Tanam (YYYY-MM-DD):", txtTanggal);
        tambahField(gridForm, gbc, 2, 1, "Luas Lahan (Ha):", txtLuas);

        // Baris 2
        tambahField(gridForm, gbc, 0, 2, "Lokasi Lahan:", txtLokasi);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0;
        lblParamKhusus.setFont(TemaUI.FONT_LABEL);
        lblParamKhusus.setForeground(TemaUI.TEKS_UTAMA);
        gridForm.add(lblParamKhusus, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gridForm.add(txtParamKhusus, gbc);

        // Baris Tombol Form
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTombol.setOpaque(false);

        JButton btnTambah = TemaUI.buatTombolUtama("Tambah Data");
        JButton btnEdit = TemaUI.buatTombolAksen("Simpan Perubahan");
        JButton btnHapus = TemaUI.buatTombolBahaya("Hapus Data");
        JButton btnReset = TemaUI.buatTombolSekunder("Bersihkan Form");

        btnTambah.addActionListener(e -> aksiTambah());
        btnEdit.addActionListener(e -> aksiEdit());
        btnHapus.addActionListener(e -> aksiHapus());
        btnReset.addActionListener(e -> resetForm());

        panelTombol.add(btnTambah);
        panelTombol.add(btnEdit);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);

        kartuForm.add(lblFormJudul, BorderLayout.NORTH);
        kartuForm.add(gridForm, BorderLayout.CENTER);
        kartuForm.add(panelTombol, BorderLayout.SOUTH);

        JPanel topSection = new JPanel(new BorderLayout(0, 14));
        topSection.setOpaque(false);
        topSection.add(header, BorderLayout.NORTH);
        topSection.add(kartuForm, BorderLayout.CENTER);
        add(topSection, BorderLayout.NORTH);

        // Tabel Data Tanaman
        JPanel kartuTabel = TemaUI.buatKartuPutih(new BorderLayout(0, 10));
        JLabel lblTabel = new JLabel("Daftar Tanaman Sawit (Klik baris untuk mengedit atau menghapus)");
        lblTabel.setFont(TemaUI.FONT_SECTION);
        lblTabel.setForeground(TemaUI.HIJAU_TUA);

        tableModel = new DefaultTableModel(
                new Object[]{"ID Tanaman", "Fase", "Tanggal Tanam", "Umur (Bln)", "Luas (Ha)", "Lokasi Lahan", "Status Pertumbuhan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TemaUI.styleTabel(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(115);
        table.getColumnModel().getColumn(2).setPreferredWidth(105);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(175);
        table.getColumnModel().getColumn(6).setPreferredWidth(270);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                isiFormDariBaris(table.getSelectedRow());
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDER_HALUS));

        kartuTabel.add(lblTabel, BorderLayout.NORTH);
        kartuTabel.add(scroll, BorderLayout.CENTER);
        add(kartuTabel, BorderLayout.CENTER);
    }

    private void tambahField(JPanel panel, GridBagConstraints gbc, int col, int row, String label, JComponent comp) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(TemaUI.FONT_LABEL);
        lbl.setForeground(TemaUI.TEKS_UTAMA);
        panel.add(lbl, gbc);

        gbc.gridx = col + 1;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void perbaruiLabelParameter() {
        String fase = (String) cmbFase.getSelectedItem();
        if ("Sawit Muda".equals(fase)) {
            lblParamKhusus.setText("Tinggi Rata-rata (cm):");
            if (idTerpilih == null) {
                txtParamKhusus.setText("120");
            }
        } else {
            lblParamKhusus.setText("Asumsi Panen (Ton/Ha/Thn):");
            if (idTerpilih == null) {
                txtParamKhusus.setText("18.0");
            }
        }
    }

    private TanamanSawit bangunObjekDariForm() {
        String id = txtId.getText().trim();
        String tglStr = txtTanggal.getText().trim();
        String luasStr = txtLuas.getText().trim().replace(',', '.');
        String lokasi = txtLokasi.getText().trim();
        String paramStr = txtParamKhusus.getText().trim().replace(',', '.');

        if (id.isEmpty() || tglStr.isEmpty() || luasStr.isEmpty() || lokasi.isEmpty() || paramStr.isEmpty()) {
            throw new IllegalArgumentException("Semua kolom pada form wajib diisi.");
        }

        LocalDate tanggal;
        try {
            tanggal = LocalDate.parse(tglStr);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Format tanggal harus YYYY-MM-DD (contoh: 2024-06-15).");
        }

        double luas;
        double param;
        try {
            luas = Double.parseDouble(luasStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Luas lahan harus berupa angka yang valid.");
        }
        try {
            param = Double.parseDouble(paramStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Nilai parameter khusus harus berupa angka yang valid.");
        }

        String fase = (String) cmbFase.getSelectedItem();
        if ("Sawit Muda".equals(fase)) {
            return new SawitMuda(id, tanggal, luas, lokasi, param);
        }
        return new SawitProduktif(id, tanggal, luas, lokasi, param);
    }

    private void aksiTambah() {
        try {
            TanamanSawit baru = bangunObjekDariForm();
            manajemen.tambahTanaman(baru);
            resetForm();
            mainFrame.refreshSemuaPanel();
            JOptionPane.showMessageDialog(this,
                    "Data tanaman '" + baru.getIdTanaman() + "' berhasil ditambahkan dan disimpan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Gagal Menambah Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiEdit() {
        if (idTerpilih == null) {
            JOptionPane.showMessageDialog(this,
                    "Pilih baris data tanaman pada tabel terlebih dahulu sebelum menyimpan perubahan.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            TanamanSawit diperbarui = bangunObjekDariForm();
            manajemen.updateTanaman(idTerpilih, diperbarui);
            resetForm();
            mainFrame.refreshSemuaPanel();
            JOptionPane.showMessageDialog(this,
                    "Perubahan data tanaman '" + diperbarui.getIdTanaman() + "' berhasil disimpan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Gagal Mengubah Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aksiHapus() {
        if (idTerpilih == null) {
            JOptionPane.showMessageDialog(this,
                    "Pilih baris data tanaman yang ingin dihapus pada tabel terlebih dahulu.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus data tanaman '" + idTerpilih + "'?\nData yang dihapus tidak dapat dikembalikan.",
                "Konfirmasi Hapus Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            boolean terhapus = manajemen.hapusTanaman(idTerpilih);
            if (terhapus) {
                String idLama = idTerpilih;
                resetForm();
                mainFrame.refreshSemuaPanel();
                JOptionPane.showMessageDialog(this,
                        "Data tanaman '" + idLama + "' berhasil dihapus.",
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void isiFormDariBaris(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        TanamanSawit t = manajemen.cariById(id);
        if (t == null) {
            return;
        }
        idTerpilih = t.getIdTanaman();
        txtId.setText(t.getIdTanaman());
        cmbFase.setSelectedItem(t.getJenisFase());
        perbaruiLabelParameter();
        txtTanggal.setText(t.getTanggalTanam().toString());
        txtLuas.setText(String.valueOf(t.getLuasLahan()));
        txtLokasi.setText(t.getLokasiLahan());
        txtParamKhusus.setText(String.valueOf(t.getParameterKhusus()));
    }

    public void resetForm() {
        idTerpilih = null;
        txtId.setText("");
        cmbFase.setSelectedIndex(0);
        txtTanggal.setText(LocalDate.now().toString());
        txtLuas.setText("");
        txtLokasi.setText("");
        perbaruiLabelParameter();
        table.clearSelection();
    }

    public void refreshData() {
        String keyword = txtCari.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            muatTabel(manajemen.getDaftarTanaman());
        } else {
            muatTabel(manajemen.cariTanaman(keyword));
        }
    }

    private void muatTabel(List<TanamanSawit> daftar) {
        tableModel.setRowCount(0);
        for (TanamanSawit t : daftar) {
            tableModel.addRow(new Object[]{
                    t.getIdTanaman(),
                    t.getJenisFase(),
                    t.getTanggalTanam().toString(),
                    t.getUmurBulan(),
                    String.format("%.2f", t.getLuasLahan()),
                    t.getLokasiLahan(),
                    t.getStatusPertumbuhan()
            });
        }
    }
}

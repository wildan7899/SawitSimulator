package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Kelas utilitas TemaUI menyediakan palet warna tema perkebunan sawit,
 * tipografi, dan komponen Swing bergaya konsisten untuk seluruh halaman.
 */
public final class TemaUI {

    public static final Color HIJAU_TUA = new Color(27, 67, 50);       // #1B4332
    public static final Color HIJAU_SEDANG = new Color(45, 106, 79);   // #2D6A4F
    public static final Color HIJAU_AKSEN = new Color(64, 145, 108);   // #40916C
    public static final Color HIJAU_MUDA = new Color(216, 243, 220);   // #D8F3DC
    public static final Color HIJAU_SOFT = new Color(240, 253, 244);   // #F0FDF4
    public static final Color LATAR_UTAMA = new Color(244, 247, 245);  // #F4F7F5
    public static final Color PUTIH = Color.WHITE;
    public static final Color TEKS_UTAMA = new Color(26, 38, 31);
    public static final Color TEKS_SEKUNDER = new Color(92, 112, 101);
    public static final Color BORDER_HALUS = new Color(214, 226, 219);
    public static final Color BAHAYA = new Color(185, 28, 28);
    public static final Color BAHAYA_HOVER = new Color(153, 27, 27);
    public static final Color ABU_TOMBOL = new Color(226, 232, 240);

    public static final Font FONT_JUDUL = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBJUDUL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_ANGKA_CARD = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_TOMBOL = new Font("Segoe UI", Font.BOLD, 13);

    private TemaUI() {
    }

    public static JPanel buatKartuPutih(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(PUTIH);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_HALUS, 1, true),
                new EmptyBorder(16, 18, 16, 18)
        ));
        return panel;
    }

    public static JButton buatTombol(String teks, Color bg, Color fg) {
        JButton btn = new JButton(teks);
        btn.setFont(FONT_TOMBOL);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 16, 9, 16));
        return btn;
    }

    public static JButton buatTombolUtama(String teks) {
        return buatTombol(teks, HIJAU_TUA, PUTIH);
    }

    public static JButton buatTombolAksen(String teks) {
        return buatTombol(teks, HIJAU_SEDANG, PUTIH);
    }

    public static JButton buatTombolSekunder(String teks) {
        return buatTombol(teks, ABU_TOMBOL, TEKS_UTAMA);
    }

    public static JButton buatTombolBahaya(String teks) {
        return buatTombol(teks, BAHAYA, PUTIH);
    }

    public static void styleField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setForeground(TEKS_UTAMA);
        field.setBackground(PUTIH);
        Border line = new LineBorder(BORDER_HALUS, 1, true);
        field.setBorder(new CompoundBorder(line, new EmptyBorder(6, 10, 6, 10)));
    }

    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_BODY);
        combo.setBackground(PUTIH);
        combo.setForeground(TEKS_UTAMA);
    }

    public static void styleTabel(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(30);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 238, 233));
        table.setSelectionBackground(HIJAU_MUDA);
        table.setSelectionForeground(HIJAU_TUA);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(8, 4));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL);
        header.setBackground(HIJAU_TUA);
        header.setForeground(PUTIH);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 34));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(HIJAU_TUA);
        headerRenderer.setForeground(PUTIH);
        headerRenderer.setFont(FONT_LABEL);
        headerRenderer.setBorder(new EmptyBorder(0, 8, 0, 8));
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(new EmptyBorder(0, 8, 0, 8));
                }
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? PUTIH : new Color(248, 252, 249));
                    c.setForeground(TEKS_UTAMA);
                } else {
                    c.setBackground(HIJAU_MUDA);
                    c.setForeground(HIJAU_TUA);
                }
                return c;
            }
        });
    }

    /**
     * Komponen kartu statistik untuk Dashboard & Estimasi Panen.
     */
    public static class StatCard extends JPanel {
        private final JLabel lblNilai;
        private final JLabel lblSub;

        public StatCard(String judul, String nilaiAwal, String subAwal, Color aksenWarna) {
            super(new BorderLayout(0, 6));
            setBackground(PUTIH);
            setBorder(new CompoundBorder(
                    new LineBorder(BORDER_HALUS, 1, true),
                    new CompoundBorder(
                            BorderFactory.createMatteBorder(4, 0, 0, 0, aksenWarna),
                            new EmptyBorder(14, 16, 14, 16)
                    )
            ));

            JLabel lblJudul = new JLabel(judul);
            lblJudul.setFont(FONT_LABEL);
            lblJudul.setForeground(TEKS_SEKUNDER);

            lblNilai = new JLabel(nilaiAwal);
            lblNilai.setFont(FONT_ANGKA_CARD);
            lblNilai.setForeground(TEKS_UTAMA);

            lblSub = new JLabel(subAwal);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblSub.setForeground(TEKS_SEKUNDER);

            add(lblJudul, BorderLayout.NORTH);
            add(lblNilai, BorderLayout.CENTER);
            add(lblSub, BorderLayout.SOUTH);
        }

        public void setData(String nilai, String sub) {
            lblNilai.setText(nilai);
            lblSub.setText(sub);
        }
    }
}

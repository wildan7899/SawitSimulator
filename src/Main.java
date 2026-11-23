import gui.MainFrame;
import repository.PenyimpananData;
import service.ManajemenSawit;

import javax.swing.*;
import java.util.List;

/**
 * Entry point aplikasi desktop Sistem Manajemen Perkebunan Kelapa Sawit.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            PenyimpananData penyimpanan = new PenyimpananData();
            ManajemenSawit manajemen = new ManajemenSawit(penyimpanan);
            manajemen.muatData(true);

            MainFrame frame = new MainFrame(manajemen);
            frame.setVisible(true);

            // Beri notifikasi jika terdapat baris rusak saat membaca file data lokal
            List<String> peringatan = manajemen.getPeringatanTerakhir();
            if (!peringatan.isEmpty()) {
                StringBuilder pesan = new StringBuilder("Terdapat beberapa data yang dilewati saat memuat file lokal:\n");
                for (String p : peringatan) {
                    pesan.append("- ").append(p).append("\n");
                }
                JOptionPane.showMessageDialog(frame, pesan.toString(),
                        "Peringatan Pembacaan File", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}

import javax.swing.*;

// Titik awal (entry point) untuk menjalankan game Sawit Farm
public class Main {

    public static void main(String[] args) {
        // Pakai tampilan standar Java supaya warna tombol konsisten di semua OS
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Jalankan GUI di thread khusus Swing (EDT) agar tampilan aman dan responsif
        SwingUtilities.invokeLater(() -> {
            GameSawit game = new GameSawit();
            GameGUI gui = new GameGUI(game);
            gui.setVisible(true);
        });
    }
}

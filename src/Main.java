import javax.swing.*;

/**
 * Entry point untuk menjalankan Sawit Farm - Mini Farming Game.
 * Seluruh inisialisasi GUI dijalankan pada Swing Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            GameSawit game = new GameSawit();
            GameGUI gui = new GameGUI(game);
            gui.setVisible(true);
        });
    }
}

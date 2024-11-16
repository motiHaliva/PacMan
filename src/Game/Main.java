package Game;

import javax.swing.*;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    JFrame window = new JFrame();

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // סגירת החלון מהווה סגירה של התוכנית
    window.setResizable(false); // מונע שינוי גודל החלון
    window.setTitle("2D"); // כותרת החלון
    GamePanel game = new GamePanel();
    window.add(game);
    window.pack();
    window.setLocationRelativeTo(null); // החלון יופיע במרכז המסך
    window.setVisible(true);
    game.startGameThread();

  }
}

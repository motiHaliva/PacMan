package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Point {
  BufferedImage image;
  GamePanel gamePanel;

  public Point(GamePanel gamePanel) throws IOException {
    this.gamePanel = gamePanel;
    // טעינת התמונה עם טיפול בחריגה
    try {
      image = ImageIO.read(new File(GamePanel.imgUrl("coin.png")));
    } catch (IOException e) {
      System.err.println("Error loading image: " + e.getMessage());
      throw e;
    }
  }

  public void draw(Graphics2D g2) {
    for (int i = 0; i < gamePanel.map.length; i++) {
      for (int j = 0; j < gamePanel.map[i].length; j++) {
        if (gamePanel.map[i][j] == 0 || gamePanel.map[i][j] == 2) {
          int x = j * gamePanel.titleSize;
          int y = i * gamePanel.titleSize; // חישוב מיקום אנכי לפי שורה
          g2.drawImage(image, x, y, gamePanel.titleSize, gamePanel.titleSize, null);
        }
      }
    }
  }
}

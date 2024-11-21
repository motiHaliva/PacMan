package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Walls  implements DrawAble{
  private final int[][] map;
  BufferedImage image = ImageIO.read(new File(GamePanel.imgUrl("wall.jpg")));
  int titleSize;
  int maxScreenColl; // מספר העמודות
  int maxScreenRow; // מספר השורות

  public Walls(int[][] map, int titleSize, int maxScreenColl, int maxScreenRow) throws IOException {
    this.map = map;
    this.titleSize = titleSize;
    this.maxScreenColl = maxScreenColl;
    this.maxScreenRow = maxScreenRow;
  }

  public boolean checkCollision(int x, int y, String direction) { // פונקצייה שמונעת התנגשויות
    int leftTile = (x + 1) / titleSize;
    int rightTile = (x + titleSize - 2) / titleSize;
    int topTile = (y + 1) / titleSize;
    int bottomTile = (y + titleSize - 2) / titleSize;

    switch (direction) {
      case "up":
        return map[topTile][leftTile] == 1 || map[topTile][rightTile] == 1;
      case "down":
        return map[bottomTile][leftTile] == 1 || map[bottomTile][rightTile] == 1;
      case "left":
        return map[topTile][leftTile] == 1 || map[bottomTile][leftTile] == 1;
      case "right":
        return map[topTile][rightTile] == 1 || map[bottomTile][rightTile] == 1;
      default:
        return false;
    }
  }

  public void draw(Graphics2D g2) {
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        if (map[i][j] == 1) {
          int x = j * titleSize;
          int y = i * titleSize;
          g2.drawImage(image, x, y, titleSize, titleSize, null);
        }
      }
    }
  }
}

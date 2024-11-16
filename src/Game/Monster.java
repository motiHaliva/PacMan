package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Monster extends Entity {
  BufferedImage image;
  GamePanel gamePanel;
  String direction;
  Random random;

  public Monster(GamePanel gamePanel, int startX, int startY, String imgUrl) {
    this.gamePanel = gamePanel;
    this.x = startX;
    this.y = startY;
    this.speed = 3;
    this.direction = "up";
    random = new Random();

    try {
      image = ImageIO.read(new File(imgUrl));

    } catch (IOException e) {
      System.err.println("Error loading monster image: " + e.getMessage());
    }
  }

  public void wrapAround() { // פונקציה המעבירה את השחקן מצד לצד בנגיעה בקצוות פתוחים
    if (x <= speed && direction.equals("left")) {
      x = gamePanel.map.length * gamePanel.titleSize + 1;
      System.out.println(x+" left");
    } else if (x >= gamePanel.map.length * gamePanel.titleSize - speed && direction.equals("right")) {
      x = 0;
      System.out.println(x+" right");
    }

  }

  String[] directions = new String[] {"up", "down", "right", "left"};

  public void moveAutomatically() {//פונקצייה שמרנדמת את התזוזזה של המפלצות
    wrapAround();
    int change = random.nextInt(35);

    if (change == 0) {
      int move = random.nextInt(4); // 0, 1, 2, או 3
      direction = directions[move];
    }

    if (direction.equals("up") && !gamePanel.walls.checkCollision(x, y - speed, direction)) {
      y -= speed;
    } else if (direction.equals("down")
        && !gamePanel.walls.checkCollision(x, y + speed, direction)) {
      y += speed;
    } else if (direction.equals("left")
        && !gamePanel.walls.checkCollision(x - speed, y, direction)) {
      x -= speed;
    } else if (direction.equals("right")
        && !gamePanel.walls.checkCollision(x + speed, y, direction)) {
      x += speed;
    } else {
      int move = random.nextInt(4);
      direction = directions[move];
    }
  }

  public void draw(Graphics2D g2) {

    g2.drawImage(image, x, y, gamePanel.titleSize, gamePanel.titleSize, null);
  }
}

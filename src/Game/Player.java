package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
  GamePanel gp;
  Walls walls;
  KeyHandler keyH;
  static int points = 0;

  public Player(GamePanel gp, KeyHandler keyH, Walls walls) {
    this.gp = gp;
    this.keyH = keyH;
    this.walls = walls;
    setDefaultValues();
    getPlayerImage();
  }

  public void getPlayerImage() {
    try {
      up1 = ImageIO.read(new File(GamePanel.imgUrl("up1.jpg")));
      up2 = ImageIO.read(new File(GamePanel.imgUrl("up2.jpg")));
      down1 = ImageIO.read(new File(GamePanel.imgUrl("down1.jpg")));
      down2 = ImageIO.read(new File(GamePanel.imgUrl("down2.jpg")));
      left1 = ImageIO.read(new File(GamePanel.imgUrl("left1.jpg")));

      left2 = ImageIO.read(new File(GamePanel.imgUrl("left2.jpg")));
      right1 = ImageIO.read(new File(GamePanel.imgUrl("right1.jpg")));
      right2 = ImageIO.read(new File(GamePanel.imgUrl("right2.jpg")));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setDefaultValues() {
    x = 108;
    y = 100;
    speed = 3;
    direction = "down";
  }

  public void wrapAround() { // פונקציה המעבירה את השחקן מצד לצד בנגיעה בקצוות פתוחים
    if (x <= speed && direction.equals("left")) {
      x = gp.map.length * gp.titleSize + 1;
    } else if (x >= gp.map.length * gp.titleSize - speed && direction.equals("right")) {
      x = 0;
    }
  }

  public boolean canMove(int newX, int newY, String dir) {
    return !walls.checkCollision(newX, newY, dir);
  }

  private boolean checkCollision(
      boolean col,
      String prevDir) { // פונקציה הבודקת אם יש התנגשות לכיוון שנלחץ ומי שווה לכיוון הקודם
    return prevDir.equals(direction) && col;
  }

  public void update() {

    if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
      wrapAround();
      // משתנה השומר האם בכיוון הלחיצה יש התנגשות
      boolean col =
          (keyH.upPressed && !canMove(x, y - speed, "up"))
              || (keyH.downPressed && !canMove(x, y + speed, "down")
                  || (keyH.leftPressed && !canMove(x - speed, y, "left"))
                  || (keyH.rightPressed && !canMove(x + speed, y, "right")));

      // בדיקת התנגדות בקירות לפני העדכון
      if ((keyH.upPressed || checkCollision(col, "up")) && y > 2 && canMove(x, y - speed, "up")) {
        direction = "up";
        y -= speed;
      } else if ((keyH.downPressed || checkCollision(col, "down"))
          && y < gp.screenHeight - gp.titleSize - 2
          && canMove(x, y + speed, "down")) {
        direction = "down";
        y += speed;
      } else if ((keyH.leftPressed || checkCollision(col, "left"))
          && x > 2
          && canMove(x - speed, y, "left")) {
        direction = "left";
        x -= speed;
      } else if ((keyH.rightPressed || checkCollision(col, "right"))
          && x < gp.screenWidth - gp.titleSize - 2
          && canMove(x + speed, y, "right")) {
        direction = "right";
        x += speed;
      }
      movePoints();
      spriteCounter++;
      if (spriteCounter > 11) {
        spriteNum = (spriteNum == 1) ? 2 : 1;
        spriteCounter = 0;
      }
    }
  }

  public void movePoints() {
    int mapX = (x + 2) / gp.titleSize;
    int mapY = (y + 2) / gp.titleSize;
    if (gp.map[mapY][mapX] == 0 || gp.map[mapY][mapX] == 2 || gp.map[mapY][mapX] == 3) {
      gp.map[mapY][mapX] = 5;
      points++;
    }
  }

  public void draw(Graphics2D g2) {
    BufferedImage image = null;
    switch (direction) {
      case "up":
        if (spriteNum == 1) image = up1;
        if (spriteNum == 2) image = up2;
        break;
      case "down":
        if (spriteNum == 1) image = down1;
        if (spriteNum == 2) image = down2;
        break;
      case "left":
        if (spriteNum == 1) image = left1;
        if (spriteNum == 2) image = left2;
        break;
      case "right":
        if (spriteNum == 1) image = right1;
        if (spriteNum == 2) image = right2;
        break;
    }
    g2.drawImage(image, x, y, gp.titleSize, gp.titleSize, null);
  }
}

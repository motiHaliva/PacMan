package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Player extends Entity implements DrawAble {
    GamePanel gp;
    Walls walls;
    KeyHandler keyH;
    ArrayList<Monster> monsters;
    GameState gameState;
    int timer = 0;
    boolean canEat = false;
    int startX,startY;

    public Player(GamePanel gp, KeyHandler keyH, GameState gameState, Walls walls, ArrayList<Monster> monsters) {
        this.gp = gp;
        this.keyH = keyH;
        this.gameState = gameState;
        this.walls = walls;
        this.monsters = monsters;
        this.startX = 108;
        this.startY = 100;
        this.x = this.startX;
        this.y = this.startY;
        this.speed = 2;
        this.direction = DOWN;
        getPlayerImage();
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(new File(GamePanel.imgUrl("up1.png")));
            up2 = ImageIO.read(new File(GamePanel.imgUrl("up2.png")));
            down1 = ImageIO.read(new File(GamePanel.imgUrl("down1.png")));
            down2 = ImageIO.read(new File(GamePanel.imgUrl("down2.png")));
            left1 = ImageIO.read(new File(GamePanel.imgUrl("left1.png")));

            left2 = ImageIO.read(new File(GamePanel.imgUrl("left2.png")));
            right1 = ImageIO.read(new File(GamePanel.imgUrl("right1.png")));
            right2 = ImageIO.read(new File(GamePanel.imgUrl("right2.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



public void wrapAround() {
    if ((x == ((gp.map[0].length - 1) * gp.titleSize) - 2) && direction.equals(RIGHT)) {
        x = 0;
    } else if (x <= speed+2 && direction.equals(LEFT)) {
        x = (gp.map[0].length - 1) * gp.titleSize;
    }
}


    public void update() {
        wrapAround();
        // משתנה השומר האם בכיוון הלחיצה יש התנגשות
        boolean col =
                (keyH.upPressed && walls.checkCollision(x, y - speed, UP))
                        || (keyH.downPressed && walls.checkCollision(x, y + speed, DOWN)
                        || (keyH.leftPressed && walls.checkCollision(x - speed, y, LEFT))
                        || (keyH.rightPressed && walls.checkCollision(x + speed, y, RIGHT)));
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            // בדיקת התנגדות בקירות לפני העדכון
            if ((keyH.upPressed || col && direction.equals(UP)) && y > 2 && !walls.checkCollision(x, y - speed, UP)) {
                direction = UP;
                y -= speed;
            } else if ((keyH.downPressed || col && direction.equals(DOWN))
                    && y < gp.screenHeight - gp.titleSize - 2
                    && !walls.checkCollision(x, y + speed, DOWN)) {
                direction = DOWN;
                y += speed;
            } else if ((keyH.leftPressed || (col && direction.equals(LEFT)))
                    && x > 2
                    && !walls.checkCollision(x - speed, y, LEFT)) {
                direction = LEFT;
                x -= speed;
            } else if ((keyH.rightPressed || col && direction.equals(RIGHT))
                    && x < gp.screenWidth - gp.titleSize - 2
                    && !walls.checkCollision(x + speed, y, RIGHT)) {
                direction = RIGHT;
                x += speed;
            }
            int mapX = (x + 2) / gp.titleSize;
            int mapY = (y + 2) / gp.titleSize;
            gameState.movePoint(mapX, mapY);
            gameState.moveBigPoint(mapX, mapY);
            gameState.moveFruit(mapX, mapY);
            gameState.stuck();
            gameState.checkLevelProgress();
        }
        spriteCounter++;
        if (spriteCounter > 11) {
            if(spriteNum == 1) spriteNum=2;
            else if (spriteNum==2)spriteNum=1;
            spriteCounter = 0;
        }

        if (timer > 0) {
            for (Monster monster : monsters) {
                monster.speed = 1;

            }
            timer--;
        } else {
            canEat = false;
            for (Monster monster : monsters) {
                monster.speed = 2;
            }
        }
    }


    public void draw(Graphics2D g2) {// פונקצייה שמסדרת את האנימציות של השחקן
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

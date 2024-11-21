package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Player extends Entity {
    GamePanel gp;
    Walls walls;
    KeyHandler keyH;
    ArrayList<Monster> monsters;
    static int points = 0;
    static int lives = 3;
    static int level = 1;
    int timer = 0;
    boolean canEat = false;


    public Player(GamePanel gp, KeyHandler keyH, Walls walls, ArrayList<Monster> monsters) {
        this.gp = gp;
        this.keyH = keyH;
        this.walls = walls;
        this.monsters = monsters;
        setDefaultValues();
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

    public void setDefaultValues() {
        x = 108;
        y = 100;
        speed = 2;
        direction = "down";
    }

    public void wrapAround() { // פונקציה המעבירה את השחקן מצד לצד בנגיעה בקצוות פתוחים
        if (x <= speed && direction.equals("left")) {
            x = gp.map[0].length * gp.titleSize - speed;
        } else if (x >= gp.map[0].length * (gp.titleSize - 1) - speed && direction.equals("right")) {
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

    public void update() throws InterruptedException {

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
            moveBigPoint();
            moveFruit();
            stuck();
            checkLevelProgress();


            spriteCounter++;
            if (spriteCounter > 11) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
        if (timer > 0) {
            timer--;
        } else {
            canEat = false;
        }
    }

    public void movePoints() {//פונקציה שאוספת נקודות
        int mapX = (x + 2) / gp.titleSize;
        int mapY = (y + 2) / gp.titleSize;
        if (gp.map[mapY][mapX] == 0 || gp.map[mapY][mapX] == 2 || gp.map[mapY][mapX] == 3) {
            gp.map[mapY][mapX] = 40;
            points++;
            Main.updateScoreLabel();
        }
    }

    public void moveBigPoint() {//פונקציה שאוספת מטבעות גדולות
        int mapX = (x + 2) / gp.titleSize;
        int mapY = (y + 2) / gp.titleSize;
        if (gp.map[mapY][mapX] == 8) {
            gp.map[mapY][mapX] = 30;
            canEat = true;
            System.out.println(points);
            points += 20;
            Main.updateScoreLabel();
            timer = 500;
        }
    }

    public void moveFruit() {//פונקצייה שאוספת פירות
        int mapX = (x + 2) / gp.titleSize;
        int mapY = (y + 2) / gp.titleSize;
        if (gp.map[mapY][mapX] == 10 || gp.map[mapY][mapX] == 11 || gp.map[mapY][mapX] == 12 || gp.map[mapY][mapX] == 13) {
            gp.map[mapY][mapX] = 20;
            points += 30;
            Main.updateScoreLabel();

        }
    }

    public void stuck() {//פונקצייה שבודקת אם השחק פגע במפלצת
        int mapX = (x + 2) / gp.titleSize;
        int mapY = (y + 2) / gp.titleSize;

        for (Monster monster : monsters) {
            int monsterMapX = (monster.x + 2) / gp.titleSize;
            int monsterMapY = (monster.y + 2) / gp.titleSize;

            if (mapX != monsterMapX || mapY != monsterMapY) continue;

            if (!canEat) {
                x = 108;
                y = 100;
                lives--;
                Main.updateScoreLabel();
                if (lives == 0) {
                    System.out.println("final game");
                    break;
                }
                System.out.println("התנגשות עם מפלצת! חזרה למיקום ההתחלתי.");
                break;
            } else {
                points += 100;
                Main.updateScoreLabel();
                monster.x = 270;
                monster.y = 253;
            }

        }
    }

    public void checkLevelProgress() {//פונקצייה שבודקת אם צריך לעלות שלב
        if (points >= 500) {
            startNextLevel();
        }
    }

    private void startNextLevel() {//מעדכנת את כל הנתונים של השלב החדש
        points = 0;
        level++;
        if (Player.level == 2) {
            monsters.add(
                    new Monster(gp, gp.player, 234, 273,
                            "greenUp.png", "greenDown.png", "greenLeft.png", "greenRight.png"));
        }
        if (Player.level==3){
            for (Monster monster : monsters) {
                monster.speed++;
            }

        }
        gp.resetMap();
        resetPosition();
        Main.updateScoreLabel();
        canEat = false;


    }

    private void resetPosition() {//פונקצייה שמחזירה את השחקן והמפלצות למקום המקורי כאשר עוברים שלב
        x = 108;
        y = 100;
        direction = "down";

        // מחזיר את המפלצות למיקום ההתחלתי
        for (Monster monster : monsters) {
            monster.x = 270;
            monster.y = 253;
            monster.x += 28;
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

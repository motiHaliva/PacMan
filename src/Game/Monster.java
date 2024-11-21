package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Monster extends Entity {
    BufferedImage imgUp, imgDown, imgLeft, imgRight,imgScared;
    GamePanel gamePanel;
    Player player;
    String direction;
    Random random;

    public Monster(GamePanel gamePanel, Player player, int startX, int startY, String imgUp, String imgDown, String imgLeft, String imgRight) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.x = startX;
        this.y = startY;
        this.speed = 2;
        this.direction = "up";
        random = new Random();


        try {
            this.imgUp = ImageIO.read(new File(GamePanel.imgUrl(imgUp)));
            this.imgDown = ImageIO.read(new File(GamePanel.imgUrl(imgDown)));
            this.imgLeft = ImageIO.read(new File(GamePanel.imgUrl(imgLeft)));
            this.imgRight = ImageIO.read(new File(GamePanel.imgUrl(imgRight)));
this.imgScared= ImageIO.read(new File(GamePanel.imgUrl("monsterScarde.png")));
        } catch (IOException e) {
            System.err.println("Error loading monster image: " + e.getMessage());
        }
    }

    public void wrapAround() { // פונקציה המעבירה את המפלצת מצד לצד בנגיעה בקצוות פתוחים
        if (x <= speed && direction.equals("left")) {
            x = gamePanel.map[0].length * gamePanel.titleSize - speed;
        } else if (x >= gamePanel.map[0].length * (gamePanel.titleSize - 1) - speed && direction.equals("right")) {
            x = 0;
        }
    }
    String[] directions = new String[]{"up", "down", "right", "left"};
    String prevDirection="";
    public void chasePlayer() {
        int mapWidth = gamePanel.map[0].length; // רוחב המטריצה
        int mapHeight = gamePanel.map.length;  // גובה המטריצה

        // תרגום מיקומים לרשת
        int playerGridX = (player.x + 2) / gamePanel.titleSize;
        int playerGridY = (player.y + 2) / gamePanel.titleSize;
        int monsterGridX = (x + 2) / gamePanel.titleSize;
        int monsterGridY = (y + 2) / gamePanel.titleSize;

        // כיווני תנועה (למעלה, למטה, שמאלה, ימינה)
        int[] dx = {0, 0, 1, -1};
        int[] dy = {-1, 1, 0, 0};

        // מטריצה לזיהוי ביקור
        boolean[][] visited = new boolean[mapHeight][mapWidth];
        visited[monsterGridY][monsterGridX] = true;

        // מטריצה לאחסון הצעד הקודם
        int[][] prevX = new int[mapHeight][mapWidth];
        int[][] prevY = new int[mapHeight][mapWidth];

        // תור ל-BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{monsterGridX, monsterGridY});

        boolean pathFound = false;

        // BFS למציאת המסלול
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currX = current[0];
            int currY = current[1];

            // אם הגענו לשחקן
            if (currX == playerGridX && currY == playerGridY) {
                pathFound = true;
                break;
            }

            // בדיקת שכנים
            for (int i = 0; i < 4; i++) {
                int newX = currX + dx[i];
                int newY = currY + dy[i];

                // בדיקת גבולות ושכנים חוקיים (ערך 0)
                if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight &&
                        !visited[newY][newX] && gamePanel.map[newY][newX]!=1
                        && !gamePanel.walls.checkCollision(newX*gamePanel.titleSize,newY* gamePanel.titleSize,directions[i])
                ) {

                    visited[newY][newX] = true;
                    queue.add(new int[]{newX, newY});
                    prevX[newY][newX] = currX;
                    prevY[newY][newX] = currY;
                }
            }
        }

        // אם נמצא מסלול, עדכון כיוון המפלצת
        if (pathFound) {
            int nextX = playerGridX;
            int nextY = playerGridY;

            // חזרה דרך המסלול מהשחקן למפלצת
            while (prevX[nextY][nextX] != monsterGridX || prevY[nextY][nextX] != monsterGridY) {
                int tempX = prevX[nextY][nextX];
                int tempY = prevY[nextY][nextX];
                nextX = tempX;
                nextY = tempY;
            }

            // חישוב כיוון התנועה
            for (int i = 0; i < 4; i++) {
                if (monsterGridX + dx[i] == nextX && monsterGridY + dy[i] == nextY) {
                    if (!directions[i].equals(direction)){
                        prevDirection=direction;
                    }
                    direction = directions[i];
                    break;
                }
            }
        }
        boolean moved = moveMonster();
        if (!moved){
            direction = prevDirection;
            moveMonster();
        }
    }


    public boolean moveMonster() {
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
            return false;
        }
        return true;
    }

    public void moveAutomatic() {//פונקצייה שמרנדמת את התזוזזה של המפלצות
        wrapAround();
        int change = random.nextInt(35);

        if (change == 0) {
            int move = random.nextInt(4); // 0, 1, 2, או 3
            direction = directions[move];
        }

        boolean moved = moveMonster();
        if (!moved) {
            int move = random.nextInt(4);
            direction = directions[move];
        }

    }


    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = imgUp;
                break;
            case "down":
                image = imgDown;
                break;
            case "left":
                image = imgLeft;
                break;
            case "right":
                image = imgRight;
                break;
        }
        if (player.canEat){
            image=imgScared;
        }
        g2.drawImage(image, x, y, gamePanel.titleSize, gamePanel.titleSize, null);
    }
}

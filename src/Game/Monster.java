package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.Point;
import java.util.Random;


public class Monster extends Entity implements DrawAble {
    boolean isReleased = true;
    BufferedImage imgUp, imgDown, imgLeft, imgRight, imgScared;
    GamePanel gamePanel;
    Player player;
    String direction;
    Random random;
    boolean canMove;
    static int mapWidth;
    static int mapHeight;
    int startX, startY;


    public Monster(GamePanel gamePanel, Player player, int x, int y, String imgUp, String imgDown, String imgLeft, String imgRight) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.startX = x;
        this.startY = y;
        this.x = this.startX;
        this.y = this.startY;
        this.speed = 2;
        this.direction = UP;
        random = new Random();
        mapWidth = gamePanel.map[0].length; // רוחב המטריצה
        mapHeight = gamePanel.map.length;  // גובה המטריצה
        try {
            this.imgUp = ImageIO.read(new File(GamePanel.imgUrl(imgUp)));
            this.imgDown = ImageIO.read(new File(GamePanel.imgUrl(imgDown)));
            this.imgLeft = ImageIO.read(new File(GamePanel.imgUrl(imgLeft)));
            this.imgRight = ImageIO.read(new File(GamePanel.imgUrl(imgRight)));
            this.imgScared = ImageIO.read(new File(GamePanel.imgUrl("monsterScarde.png")));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void wrapAround() {
        if ((x == ((gamePanel.map[0].length - 1) * gamePanel.titleSize) - 2) && direction.equals(RIGHT)) {
            x = 0;
        }
        else if (x < 5 && direction.equals(LEFT)) {
            x = (gamePanel.map[0].length - 1) * gamePanel.titleSize;
        }
    }


    String[] directions = new String[]{UP, DOWN, RIGHT, LEFT};
    String prevDirection = "";

    // כיווני תנועה (למעלה, למטה, שמאלה, ימינה)
    int[] dx = {0, 0, 1, -1};
    int[] dy = {-1, 1, 0, 0};


    // תרגום מיקומים לרשת
    int playerGridX;
    int playerGridY;
    int monsterGridX;
    int monsterGridY;

    private void initialValues() {
        // תרגום מיקומים לרשת
        playerGridX = (player.x + 2) / gamePanel.titleSize;
        playerGridY = (player.y + 2) / gamePanel.titleSize;
        monsterGridX = (x + 2) / gamePanel.titleSize;
        monsterGridY = (y + 2) / gamePanel.titleSize;
    }


    public void chasePlayer() {
        initialValues();

        // מטריצה לזיהוי ביקור
        boolean[][] visited = new boolean[mapHeight][mapWidth];
        visited[monsterGridY][monsterGridX] = true;

        // מטריצה לאחסון הצעד הקודם
        Point[][] prev = new Point[mapHeight][mapWidth];

        // תור ל-BFS
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(monsterGridX, monsterGridY));

        boolean pathFound = false;

        // BFS למציאת המסלול
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int currX = current.x;
            int currY = current.y;

            // אם הגענו לשחקן
            if (currX == playerGridX && currY == playerGridY) {
                pathFound = true;
                break;
            }

            // בדיקת שכנים
            for (int i = 0; i < 4; i++) {
                int newX = currX + dx[i];
                int newY = currY + dy[i];

                // בדיקת גבולות ושכנים חוקיים
                if (newX >= 0 && newX < mapWidth && newY >= 0 && newY < mapHeight &&
                        !visited[newY][newX] && gamePanel.map[newY][newX] != 1 &&
                        !gamePanel.walls.checkCollision(newX * gamePanel.titleSize, newY * gamePanel.titleSize, directions[i])) {

                    visited[newY][newX] = true;
                    queue.add(new Point(newX, newY));
                    prev[newY][newX] = new Point(currX, currY);
                }
            }
        }

        // אם נמצא מסלול, עדכון כיוון המפלצת
        if (pathFound) {
            Point next = new Point(playerGridX, playerGridY);

            // חזרה דרך המסלול מהשחקן למפלצת
            while (!next.equals(new Point(monsterGridX, monsterGridY))) {
                Point temp = prev[next.y][next.x];
                if (temp.equals(new Point(monsterGridX, monsterGridY))) break;
                next = temp;
            }

            // חישוב כיוון התנועה
            for (int i = 0; i < dx.length; i++) {
                if (monsterGridX + dx[i] == next.x && monsterGridY + dy[i] == next.y) {
                    if (!directions[i].equals(direction)) {
                        prevDirection = direction;
                    }
                    direction = directions[i];
                    break;
                }
            }
        }

        // ניסיון תנועה
        boolean moved = moveMonster();
        if (!moved) {
            direction = prevDirection;
            moveMonster();
        }
    }


    public boolean moveMonster() {
        wrapAround();
        if (gamePanel.keyH.upPressed || gamePanel.keyH.downPressed || gamePanel.keyH.leftPressed || gamePanel.keyH.rightPressed && canMove) {

            if (direction.equals(UP) && !gamePanel.walls.checkCollision(x, y - speed, direction) && y > 2) {
                y -= speed;
            } else if (direction.equals(DOWN)
                    && !gamePanel.walls.checkCollision(x, y + speed, direction) && y < gamePanel.screenHeight - gamePanel.titleSize - 2) {
                y += speed;
            } else if (direction.equals(LEFT)
                    && !gamePanel.walls.checkCollision(x - speed, y, direction) && x > 2) {
                x -= speed;
            } else if (direction.equals(RIGHT)
                    && !gamePanel.walls.checkCollision(x + speed, y, direction) && x < gamePanel.screenWidth - gamePanel.titleSize - 2) {
                x += speed;

            } else {
                return false;
            }
        }
        return true;
    }

    public void moveAutomatic() {//פונקצייה שמרנדמת את התזוזזה של המפלצות


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
        if (player.canEat && canMove) {
            image = imgScared;
        }
        g2.drawImage(image, x, y, gamePanel.titleSize, gamePanel.titleSize, null);
    }
}

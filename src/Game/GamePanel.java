package Game;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends JPanel implements Runnable {
    final int originalTitleSize = 6;
    final int scale = 3;
    final int titleSize = originalTitleSize * scale;

    final int maxScreenColl = 28; // מספר העמודות
    final int maxScreenRow = 25; // מספר השורות
    final int screenWidth = titleSize * maxScreenColl;
    final int screenHeight = titleSize * maxScreenRow;
    final int FPS = 60;
    Player player;
    Walls walls;
    Point point;
    BigPoint bigPoint;
    Fruits fruits;
    Random random;


    KeyHandler keyH = new KeyHandler(); // אובייקט שליטה בכפתורים

    Thread ThreadGame;

    static ArrayList<Monster> monsters;

    int[][] originalMap = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 13, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 0, 0, 1, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 1, 1},
            {0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 8, 0, 0},
            {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 11, 0, 0, 0, 0},
            {1, 1, 0, 10, 1, 1, 2, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 2, 1, 1, 2, 2, 1, 1},
            {0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 1, 4, 4, 4, 4, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 1, 2, 1, 1, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 8, 0, 1, 2, 1, 1, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 8, 2, 1, 1, 2, 1, 1, 1, 0, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    int [][]map ;
public void resetMap(){
    map = new int[originalMap.length][originalMap[0].length];
    for (int i = 0; i <originalMap.length ; i++) {
        for (int j = 0; j < originalMap[i].length; j++) {
            map[i][j]=originalMap[i][j];
        }
    }
}

    public static String imgUrl(String img) {
        return "C:\\תכנות\\jbh\\java\\projects\\GamePacMan\\src\\images\\" + img;
    }

    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // גודל המסך
        this.setBackground(new Color(68, 77, 130));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        resetMap();
        walls = new Walls(map, titleSize, maxScreenColl, maxScreenRow);
        point = new Point(this);
        fruits = new Fruits(this);
        monsters = new ArrayList<>();
        bigPoint = new BigPoint(this);
        player = new Player(this, keyH, walls, monsters);
        random = new Random();
        monsters.add(
                new Monster(this, player, 270, 253,
                        "blueUp.png", "blueDown.png", "blueLeft.png", "blueRight.png"));
        monsters.add(new Monster(this, player, 214, 253,
                "redUp.png", "redDown.png", "redLeft.png", "redRight.png"));
        monsters.add(new Monster(this, player, 234, 253,
                "pinkUp.png", "pinkDown.png", "pinkLeft.png", "pinkRight.png"));
        monsters.add(new Monster(this, player, 252, 253,
                "orangeUp.png", "orangeDown.png", "orangeLeft.png", "orangeRight.png"));
    }
    public void startGameThread() {
        ThreadGame = new Thread(this);
        ThreadGame.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (ThreadGame != null && Player.lives > 0) {

            try {
                update(); // \מעדכן את מיקום הריבוע
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            repaint(); // מצייר את הריבוע במקום המעודכן

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    int chase = 0;

    public void update() throws InterruptedException {

        player.update(); // עדכון מיקום השחקן
        int change = random.nextInt(100);

        if (change == 0) {
            chase = random.nextInt(monsters.size());
        }
        for (int i = 0; i < monsters.size(); i++) {//מעדכן את מיקום המפלצות
            if (i == chase) {
                monsters.get(i).chasePlayer();
            } else {
                monsters.get(i).moveAutomatic();

            }
        }

    }
    @Override
    public void paintComponent(Graphics g) { // פונקציה שמציירת
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // ציור הקירות
        walls.draw(g2);
        //מטבעות ציור
        point.draw(g2);
        //מטבעות גדולות
        bigPoint.draw(g2);
        // ציור השחקן
        player.draw(g2);
        //ציור המפלצות
        for (Monster m : monsters) {
            m.draw(g2);
        }
        // ציור הפירות
        fruits.draw(g2);


    }
}

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
    static Sound sound;
    GameState gameState;
    Player player;
    Walls walls;
    Point point;
    Fruits fruits;
    Random random;
    KeyHandler keyH = new KeyHandler(); // אובייקט שליטה בכפתורים

    Thread ThreadGame;

    ArrayList<Monster> monsters;
    int[][] map;
    static int nextMonsterScore;
    int place;
    int chase;
    int timer;
    int coinCnt;
    int monsterCoin;
    public static String imgUrl(String img) {
        return "src\\images\\" + img;
    }

    public GamePanel(Menu menu) throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // גודל המסך
        this.setBackground(new Color(68, 77, 130));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        resetMap();
        sound = new Sound();
        walls = new Walls(map, titleSize, maxScreenColl, maxScreenRow);
        point = new Point(this);
        fruits = new Fruits(this);
        monsters = new ArrayList<>();
        gameState = new GameState(this, fruits, monsters, menu);
        player = new Player(this, keyH, gameState, walls, monsters);
        random = new Random();
        initializeMonsters();
        nextMonsterScore = 0;
        place = 0;
        chase = 0;
        timer = 200;
        coinCnt=coinCounter();
        if (gameState.level==1) monsterCoin=coinCnt/4;
        else if (gameState.level==2||gameState.level==3)monsterCoin=coinCnt/5;

    }

    private void initializeMonsters() {
        monsters.clear();
        monsters.add(new Monster(this, player, 270, 253,
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

    public void resetMap() {
        map = new int[OriginalMap.originalMap.length][OriginalMap.originalMap[0].length];
        for (int i = 0; i < OriginalMap.originalMap.length; i++) {
            for (int j = 0; j < OriginalMap.originalMap[i].length; j++) {
                map[i][j] = OriginalMap.originalMap[i][j];
            }
        }
    }


    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;//הזמן העכשווי
        while (ThreadGame != null && gameState.lives > 0 && (gameState.level != 3 && !gameState.isMapCleared())) {
            try {
                update(); // \מעדכן את מיקום הריבוע
            } catch (Exception e) {
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
    public int coinCounter(){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j]==0||map[i][j]==2){
                    coinCnt++;
                }
            }
        }
        return coinCnt;
    }

    public void update() {
        player.update();

        if (gameState.cntPoints >= nextMonsterScore && monsters.size() > place) {
            monsters.get(place).canMove = true;
            place++;
            nextMonsterScore += monsterCoin ;
        }

        int change = random.nextInt(100);

        if (change == 0) {
            chase = random.nextInt(monsters.size());
        }

        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);

            if (monster.isReleased && monster.canMove) {
                if (i == chase && !player.canEat) {
                    monster.chasePlayer();
                } else {
                    monster.moveAutomatic();
                }
            } else if (!monster.isReleased && monster.canMove) {
                if (timer <= 0) {
                    monster.isReleased = true;
                    timer = 200;
                } else {
                    timer--;
                }
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


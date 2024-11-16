package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
  final int originalTitleSize = 6;
  final int scale = 3;
  final int titleSize = originalTitleSize * scale;

  final int maxScreenColl = 28; // מספר העמודות
  final int maxScreenRow = 27; // מספר השורות
  final int screenWidth = titleSize * maxScreenColl;
  final int screenHeight = titleSize * maxScreenRow;
  int FPS = 60;
  Player player;
  Walls walls;
  Point point;

  KeyHandler keyH = new KeyHandler(); // אובייקט שליטה בכפתורים

  Thread ThreadGame;

  ArrayList<Monster> monsters;

  int[][] map = {
    {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
    {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
    {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
    {1, 3, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 3, 1},
    {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
    {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
    {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
    {1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1},
    {1, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1},
    {1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1},
    {0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
    {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
    {0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 4, 4, 4, 4, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0},
    {1, 1, 1, 1, 1, 1, 2, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
    {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 2, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0},
    {1, 1, 0, 1, 1, 1, 2, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 2, 1, 1, 1, 0, 1, 1},
    {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
    {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
    {1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1},
    {1, 3, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 0, 0, 0, 3, 1},
    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
  };

  public static String imgUrl(String img){
    return "C:\\Users\\User\\Pictures\\PacMan\\"+img;
  }

  public GamePanel() throws IOException {
    this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // גודל המסך
    this.setBackground(new Color(68, 77, 130));
    this.setDoubleBuffered(true);
    this.addKeyListener(keyH);
    this.setFocusable(true);
    walls = new Walls(map, titleSize, maxScreenColl, maxScreenRow);
    point = new Point(this);
    player = new Player(this, keyH, walls);
    monsters = new ArrayList<>();
    monsters.add(
        new Monster(this, 270, 288,  imgUrl("blueDown.png")));
    monsters.add(new Monster(this, 214, 288,  imgUrl("redDown.png")));
    monsters.add(new Monster(this, 234, 288,  imgUrl("pinkDown.png")));
    monsters.add(new Monster(this, 252, 288,  imgUrl("orangeDown.png")));
  }

  public void startGameThread() {
    ThreadGame = new Thread(this);
    ThreadGame.start();
  }

  @Override
  public void run() {
    double drawInterval = 1000000000.0 / FPS;
    double nextDrawTime = System.nanoTime() + drawInterval;
    while (ThreadGame != null) {
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

  public void update() throws InterruptedException {
    player.update(); // עדכון מיקום השחקן באמצעות המתודה במחלקת Player
    for (Monster m :monsters ) {//מעדכן את מיקום המפלצות
      m.moveAutomatically();
    }
	  }


  @Override
  public void paintComponent(Graphics g) { // פונקציה שמציירת
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // ציור הקירות
    walls.draw(g2);
    // ציור
    point.draw(g2);
    // ציור השחקן
    player.draw(g2);
    //ציור המפלצות
    for (Monster m :monsters ) {
        m.draw(g2);
    }
  }
}

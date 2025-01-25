package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Fruits implements DrawAble {
    private final GamePanel gamePanel;
    private BufferedImage orange, apple, cherry, strawberry;
    Random random = new Random();

     int currentFruitX = -1, currentFruitY = -1;
     int currentFruitValue = -1;
     int currentFruitTimer = 0;


      int[] fruitTimers={600, 700, 800, 1000};
      int[] fruitPoints = {700, 500, 300, 100};

    public Fruits(GamePanel gamePanel) throws IOException {
        this.gamePanel = gamePanel;

        loadImages();
    }

    private void loadImages() throws IOException {
        try {
            orange = ImageIO.read(new File(GamePanel.imgUrl("orange.png")));
            apple = ImageIO.read(new File(GamePanel.imgUrl("apple.png")));
            cherry = ImageIO.read(new File(GamePanel.imgUrl("cherry.png")));
            strawberry = ImageIO.read(new File(GamePanel.imgUrl("strawberry.png")));
        } catch (IOException e) {
            System.err.println( e.getMessage());
            throw e;
        }
    }

    public void draw(Graphics2D g2) {
        if (currentFruitTimer > 0) {

            int x = currentFruitX * gamePanel.titleSize;
            int y = currentFruitY * gamePanel.titleSize;
            drawFruit(g2, currentFruitValue, x, y);


            currentFruitTimer--;
        } else {

            spawnNewFruit();
        }
    }

    private void spawnNewFruit() {
        int mapWidth = gamePanel.map[0].length;
        int mapHeight = gamePanel.map.length;

        while (true) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);


            if (gamePanel.map[y][x] != 1) {
                currentFruitX = x;
                currentFruitY = y;
                currentFruitValue = 10 + random.nextInt(4);
                currentFruitTimer = fruitTimers[currentFruitValue - 10];
                gamePanel.map[y][x] = currentFruitValue;
                break;
            }
        }
    }

    private void drawFruit(Graphics2D g2, int mapValue, int x, int y) {
        BufferedImage fruitImage;


        switch (mapValue) {
            case 10:
                fruitImage = orange;
                break;
            case 11:
                fruitImage = apple;
                break;
            case 12:
                fruitImage = cherry;
                break;
            case 13:
                fruitImage = strawberry;
                break;
            default:

                fruitImage = null;
                break;
        }
        if (fruitImage != null) {
            g2.drawImage(fruitImage, x, y, gamePanel.titleSize, gamePanel.titleSize, null);
        }
    }


}


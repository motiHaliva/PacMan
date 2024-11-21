package Game;

import java.util.*;

public class GameState {
    int points;
    int lives;
    int level;
int cntPoints;


    GamePanel gp;
    Fruits fruits;
    ArrayList<Monster> monsters;
    Menu menu;

    public GameState(GamePanel gp, Fruits fruits, ArrayList<Monster> monsters, Menu menu) {
        this.gp = gp;
        this.fruits = fruits;
        this.monsters = monsters;
        this.menu = menu;
        lives = 3;
        points = 0;
        level = 1;
        cntPoints=0;
    }

    public void movePoint(int mapX, int mapY) {
        if (gp.map[mapY][mapX] == 0 || gp.map[mapY][mapX] == 2 ) {
            GamePanel.sound.playCoin();
            cntPoints++;
            gp.map[mapY][mapX] = 9;
            points += 10;
            menu.updateScoreLabel();
        }
    }

    public void moveBigPoint(int mapX, int mapY) {
        if (gp.map[mapY][mapX] == 8) {
            GamePanel.sound.playCoin();
            gp.map[mapY][mapX] = 30;
            gp.player.canEat = true;
            points += 50;
            menu.updateScoreLabel();
            if (level == 1) {
                gp.player.timer = 500;
            } else if (level == 2) {
                gp.player.timer = 400;
            } else if (level == 3) {
                gp.player.timer = 300;
            }
        }
    }

    public void moveFruit(int playerX, int playerY) {
        if (playerX == gp.fruits.currentFruitX
                && playerY == gp.fruits.currentFruitY) {
            int points = gp.fruits.fruitPoints[gp.fruits.currentFruitValue - 10]; // קבלת הנקודות של הפרי
            this.points += points; // הוספת נקודות לשחקן
            gp.fruits.currentFruitTimer = 0;
            gp.map[gp.fruits.currentFruitY][gp.fruits.currentFruitX] = 0; // איפוס המיקום במפה
        }
    }

    public void stuck() {
        int mapX = (gp.player.x + 2) / gp.titleSize;
        int mapY = (gp.player.y + 2) / gp.titleSize;

        for (Monster monster : monsters) {
            int monsterMapX = (monster.x + 2) / gp.titleSize;
            int monsterMapY = (monster.y + 2) / gp.titleSize;

            if (mapX != monsterMapX || mapY != monsterMapY) continue;

            if (!gp.player.canEat) {
                resetEntities(false); // מחזיר את כל המפלצות לבית

                GamePanel.sound.playDeath();
                lives--;
                menu.updateScoreLabel();

                if (lives == 0) {
                    break;
                }
            } else {
                // אם השחקן יכול לאכול מפלצות
                points += 200;
                menu.updateScoreLabel();
                monster.x = monster.startX;
                monster.y = monster.startY; // מחזיר מפלצת שנאכלה למיקום הבית
            }
        }
    }


    public boolean isMapCleared() {
        for (int i = 0; i < gp.map.length; i++) {
            for (int j = 0; j < gp.map[i].length; j++) {
                if (gp.map[i][j] == 0 || gp.map[i][j] == 2 || gp.map[i][j] == 3) {
                    return false;
                }
            }
        }
        return true;
    }



    public void checkLevelProgress() {
        if (isMapCleared() && level == 1) {
            level = 2;
            GamePanel.nextMonsterScore = 0;
            startNextLevel();
        }
        if (isMapCleared() && level == 2) {
            level = 3;
            GamePanel.nextMonsterScore = 0;
            startNextLevel();
        }
    }

    private void level2() {
        monsters.add(
                new Monster(gp, gp.player, 270, 253,
                        "greenUp.png", "greenDown.png", "greenLeft.png", "greenRight.png")
        );
        for (Monster monster : monsters) {
            monster.canMove = false;
            monster.speed++;
        }

        gp.player.speed = 2;
        gp.fruits.fruitTimers = new int[]{300, 400, 500, 600};

    }

    public void level3() {
        for (Monster monster : monsters) {
            monster.speed++;
        }

        gp.player.speed++;
        gp.fruits.fruitTimers = new int[]{100, 200, 300, 400};

    }

    private void startNextLevel() {
        if (level == 2) {
            level2();
        }

        if (level == 3) {
            level3();
        }
        cntPoints=0;
        lives++;
        gp.player.timer = 0;
        gp.place = 0;
        gp.keyH.upPressed = gp.keyH.downPressed = gp.keyH.leftPressed = gp.keyH.rightPressed = false;

        gp.resetMap();
        resetEntities(true);
        menu.updateScoreLabel();
        gp.player.canEat = false;
    }


    private void resetEntities(boolean isNewLevel) {
        gp.player.x = gp.player.startX;
        gp.player.y = gp.player.startY;
        gp.player.direction = "down";
        for (Monster monster : monsters) {
            monster.x = monster.startX;
            monster.y = monster.startY;
            if (isNewLevel) {
                monster.canMove = false;
            } else {
                monster.isReleased = false; // מסמן את המפלצות כלא משוחררות
            }
        }
    }

}



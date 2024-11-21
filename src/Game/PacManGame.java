package Game;

public class PacManGame {

		private int level; // שלב נוכחי
		private int score; // ניקוד
		private int lives; //חיים

		public PacManGame() {
			this.level = 1;
			this.score = Player.points;
			this.lives = 3;
		}

		public void increaseScore(int points) {
			score += points;
		}

		public void nextLevel() {
			level++;
			lives++;
		}

		public boolean isGameOver() {
			return lives <= 0;
		}

		public void loseLife() {
			lives--;
		}


	}



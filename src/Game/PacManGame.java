package Game;

public class PacManGame {
	public class PacmanGame {
		private int level; // שלב נוכחי
		private int score; // ניקוד
		private int lives; //חיים

		public PacmanGame() {
			this.level = 1;
			this.score = 0;
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

}

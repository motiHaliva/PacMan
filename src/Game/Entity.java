package Game;

import java.awt.image.BufferedImage;


	public abstract class  Entity {
		int x;
		int y;
		int speed;
		public BufferedImage up1,up2,down1,down2,left1,left2,right1,right2;
		public String direction;
		String UP = "up", DOWN = "down", LEFT = "left", RIGHT = "right";
		public int spriteCounter = 0;
		public int spriteNum = 1;
	}



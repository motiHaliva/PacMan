package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

  public boolean upPressed, downPressed, leftPressed, rightPressed;

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    switch (code) {
      case KeyEvent.VK_UP:
        upPressed = true;
        downPressed = leftPressed = rightPressed = false;
        break;
      case KeyEvent.VK_DOWN:
        downPressed = true;
        upPressed = leftPressed = rightPressed = false;
        break;
      case KeyEvent.VK_LEFT:
        leftPressed = true;
        downPressed = upPressed = rightPressed = false;
        break;
      case KeyEvent.VK_RIGHT:
        rightPressed = true;
        downPressed = upPressed = leftPressed = false;
        break;
      case KeyEvent.VK_ENTER:
        downPressed = upPressed = leftPressed = false;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}

package Game;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    static JLabel scoreLives;
    static JLabel title;
    static JFrame window = new JFrame();
    static JPanel mainPanel = new JPanel(new CardLayout());
    static JPanel infoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            livesDraw(g);
        }
    };

    public static void livesDraw(Graphics g) {//פונקצייה שמעדכנת את החיים לפי ציור
        BufferedImage image;
        try {
            image = ImageIO.read(new File(GamePanel.imgUrl("right1.png")));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load lives image: " + e.getMessage());
        }
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < Player.lives; i++) {
            g2.drawImage(image, 430 + (i * 25), 7, 17, 17, null);
        }
        infoPanel.repaint();
    }

    public static void updateScoreLabel() {//פונקצייה שמעדכנת חיים ונקודות
        if (scoreLives != null) {
            if (Player.lives > 0 && Player.points < 500) {
                scoreLives.setText("LIVES "+" "+Player.level +" Score: " + Player.points + " " + "Lives: " + Player.lives);
            } else if (Player.lives <= 0) {
                scoreLives.setText("game  over");
            } else {
                scoreLives.setText("כל הכבוד עברת לשלב הבא");
            }
        }
    }

    static void openGameWindow() throws IOException {//פונקציה שמפעילה את חלון המשחק
        GamePanel game = new GamePanel();

        infoPanel.setBackground(new Color(197, 97, 97));
        infoPanel.removeAll();

        title = new JLabel("Game of Moti  "  );
        title.setFont(new Font("Verdana", Font.BOLD, 14));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        infoPanel.add(title);

        scoreLives = new JLabel("Score: 0  Lives: 3");
        scoreLives.setFont(new Font("Verdana",Font.BOLD,11));
        infoPanel.add(scoreLives);

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(game, BorderLayout.CENTER);
        gamePanel.add(infoPanel, BorderLayout.NORTH);

        mainPanel.add(gamePanel, "GAME");

        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "GAME");

        window.pack();

        // חשוב: קביעת הפוקוס על פאנל המשחק

            game.requestFocusInWindow();
            game.setFocusable(true);
            game.grabFocus();


        game.startGameThread();
    }

    static void openMenu() {//פונקצייה שמפעילה את חלון התפריט
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("PacMan");

        JPanel menuPanel = new JPanel(null);
        menuPanel.setBackground(new Color(32, 64, 103));

        JButton start = new JButton("start game");
        start.setBounds(145, 140, 170, 85);
        start.setBackground(Color.CYAN);
        start.setFont(new Font("Arial", Font.BOLD, 16));
        start.addActionListener(e -> {
            try {
                openGameWindow();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton exit = new JButton("exit");
        exit.setBounds(145, 240, 170, 85);
        exit.setBackground(Color.cyan);
        exit.setFont(new Font("Arial", Font.BOLD, 16));
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
            }
        });

        menuPanel.add(start);
        menuPanel.add(exit);

        mainPanel.add(menuPanel, "MENU");

        window.add(mainPanel);
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "MENU");
    }

    public static void main(String[] args) throws IOException {
        BufferedImage icon = ImageIO.read(new File(GamePanel.imgUrl("right1.png"))); // או כל תמונה אחרת שתרצה
        window.setIconImage(icon);
      // SwingUtilities.invokeLater(Main::openMenu);
        openMenu();
    }
}
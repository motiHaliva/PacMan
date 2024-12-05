package Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Menu {

    private static Menu instance;

    static GamePanel game;
    static String playerName = "";
    static JFrame window = new JFrame();
    static JLabel scoreLives;
    static JButton restartButton;
    static JPanel mainPanel = new JPanel(new CardLayout());

    JPanel infoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            livesDraw(g);
        }
    };

    private Menu() {
    }

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

    private JButton createRestartButton() {
        JButton restartBtn = new JButton(" back to menu");
        restartBtn.setBounds(3, 35, 100, 40);
        restartBtn.setBackground(Color.cyan);
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setFont(new Font("Arial", Font.BOLD, 12));

        restartBtn.addActionListener(e -> {

            try {
                openMenu();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to restart game: " + ex.getMessage());
            }
        });
        return restartBtn;
    }

    public void livesDraw(Graphics g) {//פונקצייה שמעדכנת את החיים לפי ציור

        BufferedImage image;
        try {
            image = ImageIO.read(new File(GamePanel.imgUrl("right1.png")));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < game.gameState.lives; i++) {
            g2.drawImage(image, 380 + (i * 22), 20, 17, 18, null);
        }
        infoPanel.repaint();
    }

    public void updateScoreLabel() {
        if (game.gameState.lives <= 0 || game.gameState.level >= 4) {
            // הודעה שונה בהתאם לתנאי סיום
            scoreLives.setText(game.gameState.lives <= 0 ? "game over" : "Congratulations!");
            saveScore();

            // הוספת כפתור התחלה מחדש
            restartButton = createRestartButton();
            infoPanel.add(restartButton);
            infoPanel.revalidate();
            infoPanel.repaint();

        } else {
            // עדכון טקסט הניקוד והחיים
            scoreLives.setText(String.format("LEVEL: %d | Score: %d | Lives: %d",
                    game.gameState.level, game.gameState.points, game.gameState.lives));
        }
    }


    public void openGameWindow() throws IOException, InterruptedException {//פונקציה שמפעילה את חלון המשחק

        game = new GamePanel(this);

        infoPanel.setBackground(new Color(32, 64, 103));
        infoPanel.removeAll();
        scoreLives = new JLabel("LEVEL: 1 | Score: 0 | Lives: 3");
        scoreLives.setForeground(Color.WHITE);
        scoreLives.setFont(new Font("Verdana", Font.BOLD, 14));
        scoreLives.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        GamePanel.sound.playStartGame();
    }

    public void openMenu() throws IOException { // פונקציה שמפעילה את חלון התפריט
        BufferedImage icon = ImageIO.read(new File(GamePanel.imgUrl("right1.png"))); // או כל תמונה אחרת שתרצה
        window.setIconImage(icon);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("PacMan");

        JPanel menuPanel = new JPanel(null);
        menuPanel.setBackground(new Color(32, 64, 103));

        // כפתור התחל משחק
        JButton start = new JButton("Start Game");
        start.setBounds(165, 190, 170, 42);
        start.setBackground(Color.CYAN);
        start.setFont(new Font("Arial", Font.BOLD, 16));
        start.addActionListener(e -> {
            try {
                openGameWindow();
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // שדה טקסט להזנת שם השחקן
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(165, 100, 170, 42);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 150, 200, 30);

        nameField.setBackground(Color.WHITE);
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateName();
            }

            private void updateName() {
                playerName = nameField.getText();
            }
        });


        // כפתור להוראות
        JButton instructions = new JButton("Operating Instructions");
        instructions.setBounds(165, 240, 170, 42);
        instructions.setBackground(Color.CYAN);
        instructions.setFont(new Font("Arial", Font.BOLD, 16));
        instructions.addActionListener(e -> showInstructions());

        // כפתור לצפייה בציונים הגבוהים
        JButton viewScores = new JButton("View Top Scores");
        viewScores.setBounds(165, 290, 170, 42);
        viewScores.setBackground(Color.CYAN);
        viewScores.setFont(new Font("Arial", Font.BOLD, 16));
        viewScores.addActionListener(e -> showTopScores());

        // כפתור יציאה
        JButton exit = new JButton("Exit");
        exit.setBounds(165, 340, 170, 42);
        exit.setBackground(Color.CYAN);
        exit.setFont(new Font("Arial", Font.BOLD, 16));
        exit.addActionListener(e -> window.dispose());

        // הוספת כל הרכיבים לפאנל
        menuPanel.add(nameLabel);
        menuPanel.add(start);
        menuPanel.add(nameField);
        menuPanel.add(instructions);
        menuPanel.add(viewScores);
        menuPanel.add(exit);

        mainPanel.add(menuPanel, "MENU");

        window.add(mainPanel);
        window.setSize(500, 500);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "MENU");
    }

    static void showInstructions() {
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(null);
        instructionsPanel.setBackground(new Color(32, 64, 103));

        // כותרת
        JLabel instructionsTitle = new JLabel("Game Instructions");
        instructionsTitle.setFont(new Font("Arial", Font.BOLD, 24));
        instructionsTitle.setForeground(Color.WHITE);
        instructionsTitle.setBounds(130, 30, 250, 40);

        JTextArea instructionsText = getJTextArea();

        // כפתור חזרה לתפריט
        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.setBounds(170, 320, 150, 40);
        backToMenuButton.setBackground(Color.CYAN);
        backToMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        backToMenuButton.addActionListener(e -> {
            // חזרה לתפריט
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "MENU");
        });

        // הוספת כל הרכיבים לפאנל
        instructionsPanel.add(instructionsTitle);
        instructionsPanel.add(instructionsText);
        instructionsPanel.add(backToMenuButton);

        // הצגת הפאנל החדש
        mainPanel.add(instructionsPanel, "INSTRUCTIONS");

        // החלפת התצוגה לפאנל ההוראות
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "INSTRUCTIONS");
    }

    private static JTextArea getJTextArea() {
        JTextArea instructionsText = new JTextArea();
        instructionsText.setText("""
                1. Use arrow keys to move the player.
                2. Collect all the pellets while avoiding the ghosts.
                3. The game ends when the player loses all lives.
                4. Press 'P' to pause the game.
                5. Press 'Q' to quit the game.
                Good luck!""");
        instructionsText.setEditable(false);
        instructionsText.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsText.setForeground(Color.WHITE);
        instructionsText.setBackground(new Color(32, 64, 103));
        instructionsText.setBounds(50, 100, 400, 200);
        return instructionsText;
    }


    public void saveScore() { // פונקציה לשמירת תוצאה
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Unknown Player";
        }
        try (FileWriter fw = new FileWriter("src\\scores.txt", true)) {
            fw.write(playerName + "," + game.gameState.points + System.lineSeparator());
        } catch (IOException ignored) {
        }
    }

    static void showTopScores() {

        File file = new File("src\\scores.txt");
        // בדיקה אם קובץ התוצאות קיים
        if (!file.exists()) {

            JOptionPane.showMessageDialog(window, "אין תוצאות זמינות!", "תוצאות מובילות", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            ArrayList<String> topScores = getStrings(file);

// בדיקה אם יש תוצאות
            if (topScores.isEmpty()) {
                JOptionPane.showMessageDialog(window, "אין תוצאות זמינות!", "תוצאות מובילות", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // יצירת פאנל תוצאות
            JPanel scoresPanel = new JPanel(new BorderLayout());
            scoresPanel.setBackground(new Color(32, 64, 103));

            // כותרת
            JLabel titleLabel = new JLabel("10 התוצאות הגבוהות ביותר");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scoresPanel.add(titleLabel, BorderLayout.NORTH);

            // הכנת נתונים לטבלה
            String[] columnNames = {"דירוג", "שם שחקן", "ניקוד"};
            Object[][] tableData = new Object[topScores.size()][columnNames.length];

            // מילוי הנתונים לטבלה
            for (int i = 0; i < topScores.size(); i++) {
                String[] scoreParts = topScores.get(i).split(",");
                tableData[i][0] = i + 1;  // דירוג
                tableData[i][1] = scoreParts[0];  // שם שחקן
                tableData[i][2] = scoreParts[1];  // ניקוד
            }

            // יצירת טבלת תוצאות
            JTable scoresTable = new JTable(tableData, columnNames);
            scoresTable.setBackground(Color.WHITE);
            scoresTable.setForeground(Color.BLACK);
            scoresTable.setFont(new Font("Arial", Font.PLAIN, 14));
            scoresTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

            // יצירת גלילה לטבלה
            JScrollPane scrollPane = new JScrollPane(scoresTable);
            scoresPanel.add(scrollPane, BorderLayout.CENTER);

            // כפתור חזרה לתפריט
            JButton backButton = new JButton("חזרה לתפריט הראשי");
            backButton.setBackground(Color.CYAN);
            backButton.setFont(new Font("Arial", Font.BOLD, 16));
            backButton.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "MENU");
            });
            scoresPanel.add(backButton, BorderLayout.SOUTH);

            // הוספת הפאנל למסך הראשי והצגתו
            mainPanel.add(scoresPanel, "SCORES");
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "SCORES");

        } catch (IOException e) {
            // הודעת שגיאה אם יש בעיה בקריאת הקובץ
            JOptionPane.showMessageDialog(window, "שגיאה בטעינת התוצאות: " + e.getMessage(), "שגיאה", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static ArrayList<String> getStrings(File file) throws IOException {
        ArrayList<String> allScoreLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] scoreParts = line.split(",");

                if (scoreParts.length == 2) {
                    try {
                        Integer.parseInt(scoreParts[1]); // בדיקה שהחלק השני הוא מספר
                        allScoreLines.add(line);
                    } catch (NumberFormatException e) {
                        System.err.println("שורה לא תקינה (ניקוד לא מספר): " + line);
                    }
                } else {
                    System.err.println("שורה לא בפורמט תקין: " + line);
                }
            }
        }

        // מיון התוצאות לפי ניקוד יורד
        allScoreLines.sort((a, b) -> {
            int pointsA = Integer.parseInt(a.split(",")[1]);
            int pointsB = Integer.parseInt(b.split(",")[1]);
            return Integer.compare(pointsB, pointsA);
        });

        // לקיחת 10 התוצאות הגבוהות ביותר
        int maxScores = Math.min(10, allScoreLines.size());
        return new ArrayList<>(allScoreLines.subList(0, maxScores));
    }

}
package view.game;

import controller.GameController;
import controller.GameMusic;
import model.MapModel;
import view.login.PictureFrame;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public class GameFrame extends JFrame {
    private GameController controller;
    private MapModel mapModel;
    private JLabel stepLabel;
    private GamePanel gamePanel;
    private String name;
    private Clip clip;
    private GameMusic gameMusic;

    public GameFrame(int width, int height, MapModel mapModel, String name, GameMusic gameMusic) {
        this.setTitle("游戏界面");
        this.setLayout(null);
        this.setSize(width, height);
        this.mapModel = mapModel;
        this.name = name;
        this.clip = gameMusic.getClip();
        this.gameMusic = gameMusic;

        PictureFrame pictureFrame = new PictureFrame("Resources/game.png", 0.25f, getWidth(), getHeight());
        JPanel backgroundPanel = pictureFrame.getBackground();
        this.setContentPane(backgroundPanel);

        gamePanel = new GamePanel(mapModel, getWidth());
        gamePanel.setLocation((int) ((getWidth() - gamePanel.getWidth()) / 1.3), (int) ((getHeight() - gamePanel.getHeight()) / 1.5));
        backgroundPanel.add(gamePanel);

        this.stepLabel = gamePanel.getStepLabel();
        updateAllLabels(stepLabel, getWidth(), getHeight());
        backgroundPanel.add(stepLabel);

        JButton saveBtn = new JButton("保存游戏");
        JButton restartBtn = new JButton("重新游戏");
        JButton loadBtn = new JButton("载入游戏");
        JButton endBtn = new JButton("结束游戏");
        JButton musicBtn = new JButton("音乐停止");
        updateAllButtons(saveBtn,loadBtn,restartBtn,endBtn,musicBtn,getWidth(), getHeight());
        backgroundPanel.add(saveBtn);
        backgroundPanel.add(loadBtn);
        backgroundPanel.add(restartBtn);
        backgroundPanel.add(endBtn);
        backgroundPanel.add(musicBtn);

        for (JButton btn : Arrays.asList(saveBtn,loadBtn,restartBtn,endBtn,musicBtn)) {
            btn.setFocusPainted(false);         // 取消焦点虚线框
        }

        // 窗口缩放监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGamePanel(gamePanel, mapModel, getWidth(), getHeight());
                updateAllLabels(stepLabel, getWidth(), getHeight());
                updateAllButtons(saveBtn, loadBtn, restartBtn, endBtn, musicBtn,getWidth(), getHeight());
            }
        });
        this.controller = new GameController(this, gamePanel, mapModel);

        saveBtn.addActionListener(e -> saveGame(gamePanel));
        loadBtn.addActionListener(e -> loadGame(gamePanel));
        restartBtn.addActionListener(e -> restartGame(gamePanel));
        endBtn.addActionListener(e -> endGame());
        musicBtn.addActionListener(e -> endMusic(musicBtn));

        if (name == null) {
            saveBtn.setEnabled(false);
            loadBtn.setEnabled(false);
        }


        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void updateGamePanel(GamePanel gamePanel, MapModel mapModel, int frameWidth, int frameHeight) {
        gamePanel.setGrid_size(frameWidth / 10);


        gamePanel.removeAll();
        gamePanel.getBoxes().clear();

        int newWidth = mapModel.getWidth() * gamePanel.getGrid_size() + 4;
        int newHeight = mapModel.getHeight() * gamePanel.getGrid_size() + 4;
        gamePanel.setSize(newWidth, newHeight);

        gamePanel.revalidate();
        gamePanel.repaint();

        gamePanel.setLocation((int) ((frameWidth - gamePanel.getWidth()) / 1.3), (int) ((frameHeight - gamePanel.getHeight()) / 1.5));
        gamePanel.paintGame();
    }


    private void updateAllLabels(JLabel label_1, int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 15;
        int centerX = (frameWidth - btnWidth) / 4;  //横坐标
        int startY = frameHeight / 3;  //纵坐标

        // 设置每个按钮的位置
        label_1.setBounds(centerX, startY, btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, frameHeight / 30);
        label_1.setFont(font);
    }

    private void updateAllButtons(JButton button_1, JButton button_2, JButton button_3,JButton button_4,JButton button_5,int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 20;
        int centerX = (frameWidth - btnWidth) / 6;  //横坐标
        int startY = (int) (frameHeight / 2.2);  //纵坐标
        int spacing = btnHeight / 2;

        // 设置每个按钮的位置
        button_1.setBounds(centerX, startY, btnWidth, btnHeight);
        button_2.setBounds(centerX, startY + btnHeight + spacing, btnWidth, btnHeight);
        button_3.setBounds(centerX, startY+ 2 * (btnHeight + spacing),  btnWidth, btnHeight);
        button_4.setBounds(centerX, startY+ 3 * (btnHeight + spacing), btnWidth, btnHeight);
        button_5.setBounds(centerX, startY+ 4 * (btnHeight + spacing),  btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, frameHeight / 50);
        button_1.setFont(font);
        button_2.setFont(font);
        button_3.setFont(font);
        button_4.setFont(font);
        button_5.setFont(font);
    }//改了

    private void restartGame(GamePanel gamePanel) {
        stepLabel.setText("步数: 0");
        gamePanel.steps = 0;
        gamePanel.clearBoxes();
        JLabel[] jLabel_list = gamePanel.getjLabel_list();
        gamePanel.remove(jLabel_list[0]);
        gamePanel.remove(jLabel_list[1]);
        int [][] my_map = gamePanel.deepCopy(MapModel.MAP_1.getCopy());
        MapModel.MAP_1.setMatrix(my_map);
        gamePanel.paintGame();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    public void loadGame(GamePanel gamePanel) {
        String url = "jdbc:mysql://localhost:3306/game?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "Zwh317318319,";

        try {
            // 连接数据库
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            String sql = "SELECT map_1, steps FROM project_1 WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            String arrayString = null;
            int steps = 0;
            if (rs.next()) {
                arrayString = rs.getString("map_1");
                steps = rs.getInt("steps");
            }
            if (arrayString != null) {
                arrayString = arrayString.substring(2, arrayString.length() - 2);
                String[] rows = arrayString.split("], \\[");
                int[][] newArray2D = new int[rows.length][];

                for (int i = 0; i < rows.length; i++) {
                    String[] columns = rows[i].split(", ");
                    newArray2D[i] = new int[columns.length];
                    for (int j = 0; j < columns.length; j++) {
                        newArray2D[i][j] = Integer.parseInt(columns[j]);
                    }
                }
                gamePanel.steps = steps;
                stepLabel.setText(String.format("步数: %d", steps));
                MapModel.MAP_1.setMatrix(newArray2D);
                gamePanel.clearBoxes();
                JLabel[] jLabel_list = gamePanel.getjLabel_list();
                gamePanel.remove(jLabel_list[0]);
                gamePanel.remove(jLabel_list[1]);
                gamePanel.paintGame();
                gamePanel.setFocusable(true);
                gamePanel.requestFocusInWindow();
            }


            rs.close();
            ps.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "数据库连接出错！\n" +
                            "错误类型: " + ex.getClass().getSimpleName() + "\n" +
                            "详细信息: " + ex.getMessage()
            );
        }
    }

    public void saveGame(GamePanel gamePanel) {
        String url = "jdbc:mysql://localhost:3306/game?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "Zwh317318319,";

        try {
            // 连接数据库
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            String sql = "UPDATE project_1 SET map_1 = ?, steps = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Arrays.deepToString(MapModel.MAP_1.getMatrix()));
            ps.setInt(2, gamePanel.steps);
            ps.setString(3, name);
            int rs = ps.executeUpdate();

            ps.close();
            conn.close();
            gamePanel.setFocusable(true);
            gamePanel.requestFocusInWindow();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "数据库连接出错！\n" +
                            "错误类型: " + ex.getClass().getSimpleName() + "\n" +
                            "详细信息: " + ex.getMessage()
            );
        }
    }

    public void endGame() {
        System.exit(0);
    }

    public void endMusic(JButton musicBtn) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                gameMusic.setStopping(true);
                musicBtn.setText("音乐打开");
            } else {

                if (clip != null) {
                    clip.start();
                }
                musicBtn.setText("音乐停止");
            }
            gamePanel.setFocusable(true);
            gamePanel.requestFocusInWindow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package view.game;

import controller.GameController;
import model.MapModel;
import view.login.PictureFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
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

    private JButton saveBtn = new JButton("保存");
    private JButton restartBtn = new JButton("重新游戏");
    private JButton loadBtn = new JButton("载入");
    private JButton endBtn = new JButton("结束游戏");
    private JButton musicBtn = new JButton("音乐开关");

    public GameFrame(int width, int height, MapModel mapModel, String name, Clip clip) {
        this.setTitle("游戏界面");
        this.setLayout(null);
        this.setSize(width, height);
        this.mapModel = mapModel;
        this.name = name;
        this.clip = clip;

        PictureFrame pictureFrame = new PictureFrame("Resources/game.png", 0.25f, getWidth(), getHeight());
        JPanel backgroundPanel = pictureFrame.getBackground();
        this.setContentPane(backgroundPanel);

        gamePanel = new GamePanel(mapModel, getWidth());
        gamePanel.setLocation((int) ((getWidth() - gamePanel.getWidth()) / 1.3), (int) ((getHeight() - gamePanel.getHeight()) / 1.5));
        backgroundPanel.add(gamePanel);

        this.stepLabel = gamePanel.getStepLabel();
        updateAllLabels(stepLabel, getWidth(), getHeight());
        backgroundPanel.add(stepLabel);

        updateAllButtons(saveBtn,loadBtn,restartBtn,endBtn,musicBtn,getWidth(), getHeight());
        backgroundPanel.add(saveBtn);
        backgroundPanel.add(loadBtn);
        backgroundPanel.add(restartBtn);
        backgroundPanel.add(endBtn);
        backgroundPanel.add(musicBtn);





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
        int btnHeight = frameHeight / 20;
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
        int [][] my_map = gamePanel.deepCopy(MapModel.MAP_1.getCopy());
        MapModel.MAP_1.setMatrix(my_map);
        gamePanel.paintGame();
    }

    public void loadGame() {
        String url = "jdbc:mysql://localhost:3306/game?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";;
        String dbUser = "root";
        String dbPassword = "Zwh317318319,";

        try {
            // 连接数据库
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            String sql = "SELECT map_1 FROM project_1 WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            String arrayString = rs.getString("map_1");

            String[] rows = arrayString.split(";");
            int[][] newArray2D = new int[rows.length][];

            for (int i = 0; i < rows.length; i++) {
                String[] columns = rows[i].split(",");
                newArray2D[i] = new int[columns.length];
                for (int j = 0; j < columns.length; j++) {
                    newArray2D[i][j] = Integer.parseInt(columns[j]);
                }
            }
            rs.close();
            ps.close();
            conn.close();

            MapModel.MAP_1.setMatrix(newArray2D);
            gamePanel.paintGame();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "数据库连接出错！\n" +
                            "错误类型: " + ex.getClass().getSimpleName() + "\n" +
                            "详细信息: " + ex.getMessage()
            );
        }
    }

    public void saveGame() {
        String url = "jdbc:mysql://localhost:3306/game?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";;
        String dbUser = "root";
        String dbPassword = "Zwh317318319,";

        try {
            // 连接数据库
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            String sql = "UPDATE project_1 SET map_1 = ? WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Arrays.deepToString(MapModel.MAP_1.getMatrix()));
            ps.setString(2, name);
            int rs = ps.executeUpdate();

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

    public void endGame() {
        System.exit(0);
    }

    public void endMusic() {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            } else {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("Resources/Music/begin and end/start_music.wav"));
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

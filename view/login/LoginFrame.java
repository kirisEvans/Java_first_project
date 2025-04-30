package view.login;

import controller.GameMusic;
import model.MapModel;
import view.game.GameFrame;

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


public class LoginFrame extends JFrame {
    private String name;
    private Clip clip;
    public LoginFrame(int width, int height) {
        this.setTitle("登录界面");
        this.setLayout(null);
        this.setSize(width, height);

        PictureFrame pictureFrame = new PictureFrame("Resources/background.png", 0.25f, width, height);
        JPanel backgroundPanel = pictureFrame.getBackground();

        JLabel userLabel = new JLabel("用户名：");
        JLabel passLabel = new JLabel("密码：");
        Font font = new Font("微软雅黑", Font.BOLD, getHeight() / 43);
        userLabel.setFont(font);
        passLabel.setFont(font);
        backgroundPanel.add(userLabel);
        backgroundPanel.add(passLabel);

        JTextField username = new JTextField();
        JTextField password = new JTextField();
        backgroundPanel.add(username);
        backgroundPanel.add(password);

        JButton login = new JButton("登录");
        JButton register = new JButton("注册");
        login.setFont(font);
        register.setFont(font);
        backgroundPanel.add(login);
        backgroundPanel.add(register);
        for (JButton btn : Arrays.asList(login, register)) {
            btn.setFocusPainted(false);         // 取消焦点虚线框
        }

        updateAllLabels(userLabel, passLabel, getWidth(), getHeight());
        updateTextField(username, password, getWidth(), getHeight());
        updateAllButtons(login, register, getWidth(), getHeight());

        // 窗口缩放监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateAllLabels(userLabel, passLabel, getWidth(), getHeight());
                updateTextField(username, password, getWidth(), getHeight());
                updateAllButtons(login, register, getWidth(), getHeight());
            }
        });

        // 设置为主内容面板
        this.setContentPane(backgroundPanel);


        login.addActionListener(e -> {
            this.name = username.getText();
            String secret = password.getText();

            String url = "jdbc:mysql://localhost:3306/game?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            String dbUser = "root";
            String dbPassword = "Zwh317318319,";

            try {
                // 连接数据库
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
                String sql = "SELECT * FROM project_1 WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, secret);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // 登录成功
                    JOptionPane.showMessageDialog(this, "登录成功！");
                    //
                    GameMusic gameMusic = new GameMusic("Resources/Music/game");
                    clip = gameMusic.getClip();
                    Point currentLocation = this.getLocationOnScreen();
                    this.dispose(); // 隐藏当前界面
                    MapModel mapModel = MapModel.MAP_1;
                    GameFrame gameFrame = new GameFrame(getWidth(), getHeight(), mapModel, name, clip);
                    gameFrame.setLocation(currentLocation);
                    gameFrame.setVisible(true); // 显示新界面
                    //
                } else {
                    // 登录失败
                    JOptionPane.showMessageDialog(this, "用户名或密码错误！");
                    username.setText("");
                    password.setText("");
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
        });

        register.addActionListener(e -> {
            // 加一个短暂延迟（例如 300ms）
            Timer timer = new Timer(300, event -> {
                Point currentLocation = this.getLocationOnScreen();
                this.dispose(); // 隐藏当前界面
                RegisterFrame registerFrame = new RegisterFrame(getWidth(), getHeight());
                registerFrame.setLocation(currentLocation);
                registerFrame.setVisible(true); // 显示新界面
            });
            timer.setRepeats(false);
            timer.start();
        });

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void updateAllLabels(JLabel label_1, JLabel label_2, int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 20;
        int spacing = btnHeight / 2;
        int centerX = (frameWidth - btnWidth) / 4;  //横坐标
        int startY = frameHeight / 3;  //纵坐标

        // 设置每个按钮的位置
        label_1.setBounds(centerX, startY, btnWidth, btnHeight);
        label_2.setBounds(centerX, startY + btnHeight + spacing, btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, frameHeight / 43);
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);  // 右对齐
        label_2.setHorizontalAlignment(SwingConstants.RIGHT);  // 右对齐
        label_1.setFont(font);
        label_2.setFont(font);
    }

    private void updateTextField(JTextField jTextField_1, JTextField jTextField_2, int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 20;
        int spacing = btnHeight / 2;
        int centerX = (int) ((frameWidth - btnWidth) / 1.7);  //横坐标
        int startY = frameHeight / 3;  //纵坐标

        // 设置每个按钮的位置
        jTextField_1.setBounds(centerX, startY, btnWidth, btnHeight);
        jTextField_2.setBounds(centerX, startY + btnHeight + spacing, btnWidth, btnHeight);
    }

    private void updateAllButtons(JButton button_1, JButton button_2, int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 16;
        int centerX = (frameWidth - btnWidth) / 3;  //横坐标
        int startY = frameHeight / 2;  //纵坐标

        // 设置每个按钮的位置
        button_1.setBounds(centerX, startY, btnWidth, btnHeight);
        button_2.setBounds((int) (centerX + 1.5 * btnWidth), startY, btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, frameHeight / 43);
        button_1.setFont(font);
        button_2.setFont(font);
    }
}



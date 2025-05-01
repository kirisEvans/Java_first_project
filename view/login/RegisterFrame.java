package view.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public class RegisterFrame extends JFrame {
    public RegisterFrame(int width, int height) {
        this.setTitle("注册界面");
        this.setLayout(null);
        this.setSize(width, height);

        PictureFrame pictureFrame = new PictureFrame("Resources/background.png", 0.15f, width, height);
        JPanel backgroundPanel = pictureFrame.getBackground();

        JLabel userLabel = new JLabel("用户名：");
        JLabel passLabel = new JLabel("密码：");
        JLabel sloganLabel = new JLabel("欢 迎 新 用 户 ！");
        sloganLabel.setForeground(Color.red);

        Font headline_font = new Font("微软雅黑", Font.BOLD, getHeight() / 20);
        Font font = new Font("微软雅黑", Font.BOLD, getHeight() / 43);
        userLabel.setFont(font);
        passLabel.setFont(font);
        sloganLabel.setFont(headline_font);
        backgroundPanel.add(userLabel);
        backgroundPanel.add(passLabel);
        backgroundPanel.add(sloganLabel);

        JTextField username = new JTextField();
        JTextField password = new JTextField();
        backgroundPanel.add(username);
        backgroundPanel.add(password);

        JButton registerBtn = new JButton("注册");
        JButton resetBtn = new JButton("重设");
        registerBtn.setFont(font);
        resetBtn.setFont(font);
        backgroundPanel.add(registerBtn);
        backgroundPanel.add(resetBtn);
        for (JButton btn : Arrays.asList(registerBtn, resetBtn)) {
            btn.setFocusPainted(false);         // 取消焦点虚线框
        }

        updateAllLabels(userLabel, passLabel, sloganLabel, getWidth(), getHeight());
        updateTextField(username, password, getWidth(), getHeight());
        updateAllButtons(registerBtn, resetBtn, getWidth(), getHeight());

        // 窗口缩放监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateAllLabels(userLabel, passLabel, sloganLabel, getWidth(), getHeight());
                updateTextField(username, password, getWidth(), getHeight());
                updateAllButtons(registerBtn, resetBtn, getWidth(), getHeight());
            }
        });

        // 设置为主内容面板
        this.setContentPane(backgroundPanel);


        registerBtn.addActionListener(e -> {
            String name = username.getText();
            String secret = password.getText();

            String url = "jdbc:mysql://localhost:3306/game?allowPublicKeyRetrieval=true&useSSL=false";
            String dbUser = "root";
            String dbPassword = "Zwh317318319,";

            try {
                // 连接数据库
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

                // 检查用户名是否已存在
                String checkSql = "SELECT * FROM project_1 WHERE username = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkSql);
                checkPs.setString(1, name);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    // 如果用户名已存在
                    JOptionPane.showMessageDialog(this, "用户名已存在！请更换用户名。");
                    username.setText("");
                    password.setText("");
                } else {
                    // 用户名不存在，进行注册操作
                    String insertSql = "INSERT INTO project_1 (username, password) VALUES (?, ?)";
                    PreparedStatement insertPs = conn.prepareStatement(insertSql);
                    insertPs.setString(1, name);
                    insertPs.setString(2, secret);

                    int rowsAffected = insertPs.executeUpdate();

                    if (rowsAffected > 0) {
                        // 注册成功
                        JOptionPane.showMessageDialog(this, "注册成功！");
                        // 清空输入框或其他后续操作
                        username.setText("");
                        password.setText("");
                        Timer timer = new Timer(300, event -> {
                            Point currentLocation = this.getLocationOnScreen();
                            this.dispose(); // 隐藏当前界面
                            LoginFrame loginFrame = new LoginFrame(getWidth(), getHeight());
                            loginFrame.setLocation(currentLocation);
                            loginFrame.setVisible(true); // 显示新界面
                        });
                        timer.setRepeats(false);
                        timer.start();

                    } else {
                        // 注册失败
                        JOptionPane.showMessageDialog(this, "注册失败，请稍后再试！");
                    }

                    insertPs.close();
                }

                rs.close();
                checkPs.close();
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

        resetBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
        });

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void updateAllLabels(JLabel label_1, JLabel label_2, JLabel label_3, int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 20;
        int spacing = btnHeight / 2;
        int centerX = (frameWidth - btnWidth) / 4;  //横坐标
        int startY = frameHeight / 3;  //纵坐标

        // 设置每个按钮的位置
        label_1.setBounds(centerX, startY, btnWidth, btnHeight);
        label_2.setBounds(centerX, startY + btnHeight + spacing, btnWidth, btnHeight);
        label_3.setBounds(centerX, startY - 2 * spacing - 2 *  btnHeight, btnWidth * 3, btnHeight * 3);
        Font font = new Font("微软雅黑", Font.BOLD, frameHeight / 43);
        Font headline_font = new Font("微软雅黑", Font.BOLD, getHeight() / 20);
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);  // 右对齐
        label_2.setHorizontalAlignment(SwingConstants.RIGHT);
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setFont(font);
        label_2.setFont(font);
        label_3.setFont(headline_font);
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
        int spacing = btnHeight / 2;
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

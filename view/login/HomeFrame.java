package view.login;

import controller.GameMusic;
import model.MapModel;
import view.game.GameFrame;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class HomeFrame extends JFrame {
    public static Clip clip;
    public HomeFrame() {  //屏幕的宽和高
        this.setTitle("Welcome");
        this.setLayout(null);
        int width = 512;
        int height = 768;
        this.setSize(width, height);

        PictureFrame pictureFrame = new PictureFrame("Resources/background.png", 0.25f, width, height);
        JPanel backgroundPanel = pictureFrame.getBackground();

        // 添加按钮
        JButton userBtn = new JButton("用户登录");
        JButton guestBtn = new JButton("游客登录");
        JButton musicBtn = new JButton("音乐开关");
        Font font = new Font("微软雅黑", Font.BOLD, getHeight() / 43);
        userBtn.setFont(font);
        guestBtn.setFont(font);
        musicBtn.setFont(font);
        backgroundPanel.add(userBtn);
        backgroundPanel.add(guestBtn);
        backgroundPanel.add(musicBtn);

        for (JButton btn : Arrays.asList(userBtn, guestBtn, musicBtn)) {
            btn.setFocusPainted(false);         // 取消焦点虚线框
        }

        //动态调整大小
        updateAllButtons(userBtn, guestBtn, musicBtn, getWidth(), getHeight());

        // 窗口缩放监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateAllButtons(userBtn, guestBtn, musicBtn, getWidth(), getHeight());
            }
        });

        // 设置为主内容面板
        this.setContentPane(backgroundPanel);


        try {
            // 加载音频文件
            File soundFile = new File("Resources/Music/begin and end/start_music.wav"); // 替换为你的文件路径
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // 获取 Clip 对象
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // 播放音乐
            clip.start();

            // 如果你想让音乐循环播放：
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        userBtn.addActionListener(e -> {
            // 加一个短暂延迟（例如 300ms）
            Timer timer = new Timer(300, event -> {
                Point currentLocation = this.getLocationOnScreen();
                this.dispose(); // 隐藏当前界面
                LoginFrame loginFrame = new LoginFrame(getWidth(), getHeight());
                loginFrame.setLocation(currentLocation);
                loginFrame.setVisible(true); // 显示新界面
            });
            timer.setRepeats(false);
            timer.start();
        });

        guestBtn.addActionListener(e -> {
            // 加一个短暂延迟（例如 300ms）
            Timer timer = new Timer(300, event -> {
                GameMusic gameMusic = new GameMusic("Resources/Music/game");

                Point currentLocation = this.getLocationOnScreen();
                this.dispose(); // 隐藏当前界面
                MapModel mapModel = MapModel.MAP_1;
                GameFrame gameFrame = new GameFrame(getWidth(), getHeight(), mapModel, null, gameMusic.getClip());
                gameFrame.setLocation(currentLocation);
                gameFrame.setVisible(true); // 显示新界面
            });
            timer.setRepeats(false);
            timer.start();
        });

        musicBtn.addActionListener(e -> {
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
        });

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void updateAllButtons(JButton button_1, JButton button_2, JButton button_3, int frameWidth, int frameHeight) {
        int btnWidth = frameWidth / 4;
        int btnHeight = frameHeight / 16;
        int spacing = btnHeight / 2;
        int centerX = (frameWidth - btnWidth) / 2;  //横坐标
        int startY = frameHeight / 2;  //纵坐标

        // 设置每个按钮的位置
        button_1.setBounds(centerX, startY, btnWidth, btnHeight);
        button_2.setBounds(centerX, startY + btnHeight + spacing, btnWidth, btnHeight);
        button_3.setBounds(centerX, startY + 2 * btnHeight + 2 * spacing, btnWidth, btnHeight);
        Font font = new Font("微软雅黑", Font.BOLD, frameHeight / 43);
        button_1.setFont(font);
        button_2.setFont(font);
        button_3.setFont(font);
    }
}

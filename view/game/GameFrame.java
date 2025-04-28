package view.game;

import controller.GameController;
import model.MapModel;
import view.login.PictureFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame {
    private GameController controller;
    private MapModel mapModel;
    private JLabel stepLabel;
    private GamePanel gamePanel;

    public GameFrame(int width, int height, MapModel mapModel) {
        this.setTitle("游戏界面");
        this.setLayout(null);
        this.setSize(width, height);
        this.mapModel = mapModel;

        PictureFrame pictureFrame = new PictureFrame("Resources/game.png", 0.25f, getWidth(), getHeight());
        JPanel backgroundPanel = pictureFrame.getBackground();
        this.setContentPane(backgroundPanel);

        gamePanel = new GamePanel(mapModel, getWidth());
        gamePanel.setLocation((int) ((getWidth() - gamePanel.getWidth()) / 1.3), (int) ((getHeight() - gamePanel.getHeight()) / 1.5));
        backgroundPanel.add(gamePanel);

        this.stepLabel = gamePanel.getStepLabel();
        updateAllLabels(stepLabel, getWidth(), getHeight());
        backgroundPanel.add(stepLabel);

        // 窗口缩放监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGamePanel(gamePanel, mapModel, getWidth(), getHeight());
                updateAllLabels(stepLabel, getWidth(), getHeight());
            }
        });
        this.controller = new GameController(this, gamePanel, mapModel);

        //todo: add other button here
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

}

package view.login;

import javax.swing.*;
import java.awt.*;

public class PictureFrame {
    JPanel backgroundPanel;

    public PictureFrame(String path, float alpha, int width, int height) {
        ImageIcon backgroundIcon = new ImageIcon(path);
        backgroundPanel = new JPanel() {  //背景图
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                g2d.setComposite(alphaComposite);
                g.drawImage(backgroundIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); //保证只对背景透明化
            }
        };
        backgroundPanel.setLayout(null); // 自由放组件
        backgroundPanel.setBounds(0, 0, width, height);
    }

    public JPanel getBackground() {
        return backgroundPanel;
    }
}

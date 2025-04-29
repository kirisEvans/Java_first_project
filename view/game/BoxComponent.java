package view.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoxComponent extends JComponent {
    private BufferedImage image;
    private int row;
    private int col;
    private boolean isSelected;
    private Border border;

    public BoxComponent(String path, int row, int col) {
        // 使用 ImageIcon 加载图片
        try {
            // 使用 ImageIO 加载图片
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                throw new IOException("图片文件不存在: " + path);
            }

            image = ImageIO.read(imgFile);  // 使用 ImageIO 读取文件，直接获取 BufferedImage

            if (image == null) {
                throw new IOException("图片加载失败，无法读取文件: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片加载失败：" + path);
        }

        this.setOpaque(false); // 保证没有默认背景
        this.row = row;
        this.col = col;
        isSelected = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        if(isSelected){
            border = BorderFactory.createLineBorder(Color.red,3);
        }else {
            border = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        }
        this.setBorder(border);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        this.repaint();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

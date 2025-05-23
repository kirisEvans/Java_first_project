package view.game;

import controller.GameController;
import model.Direction;
import model.MapModel;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePanel extends ListenerPanel {
    private ArrayList<BoxComponent> boxes;
    private MapModel model;
    private GameController controller;
    public JLabel stepLabel = new JLabel("步数: 0");
    public int steps = 0;
    private int grid_size;
    private BoxComponent selectedBox;
    private JLabel[] jLabel_list;

    public GamePanel(MapModel model, int width) {
        this.grid_size = width / 10;
        boxes = new ArrayList<>();
        this.setVisible(true);
        this.setLayout(null);
        this.setSize(model.getWidth() * grid_size + 4, model.getHeight() * grid_size + 4);
        this.model = model;
        this.selectedBox = null;

        paintGame();
    }

    public void paintGame() {
        //copy a map
        int[][] map = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = model.getId(i, j);
            }
        }
        //build Component
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoxComponent box = null;
                if (map[i][j] == 1) {
                    box = new BoxComponent("Resources/Character/ch1.png", i, j);
                    box.setSize(grid_size, grid_size);
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {
                    box = new BoxComponent("Resources/Character/ch2.png", i, j);
                    box.setSize(grid_size * 2, grid_size);
                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                } else if (map[i][j] == 3) {
                    box = new BoxComponent("Resources/Character/ch3.png", i, j);
                    box.setSize(grid_size, grid_size * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                } else if (map[i][j] == 4) {
                    box = new BoxComponent("Resources/Character/ch4.png", i, j);
                    box.setSize(grid_size * 2, grid_size * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i][j + 1] = 0;
                    map[i + 1][j + 1] = 0;
                }

                if (box != null) {
                    box.setLocation(j * grid_size + 2, i * grid_size + 2);
                    boxes.add(box);
                    this.add(box);
                }
            }
        }

        this.jLabel_list = setJlabel();

        this.add(jLabel_list[0]);
        this.add(jLabel_list[1]);
        this.requestFocusInWindow();
        this.revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        this.setBorder(border);
    }

    @Override
    public void doMouseClick(Point point) {
        Component component = this.getComponentAt(point);
        if (component instanceof BoxComponent clickedComponent) {
            if (selectedBox == null) {
                selectedBox = clickedComponent;
                selectedBox.setSelected(true);
            } else if (selectedBox != clickedComponent) {
                selectedBox.setSelected(false);
                clickedComponent.setSelected(true);
                selectedBox = clickedComponent;
            } else {
                clickedComponent.setSelected(false);
                selectedBox = null;
            }
        }
    }

    @Override
    public void doMoveRight() {
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveLeft() {
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveUp() {
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.UP)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveDown() {
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN)) {
                afterMove();
            }
        }
    }

    public void afterMove() {
        this.steps++;
        this.stepLabel.setText(String.format("步数: %d", this.steps));
        if (controller.isBe_success()) {
            controller.setBe_success(false);
            JDialog dialog = new JDialog(controller.getGameFrame(), "提示", true);
            dialog.setSize((int) (getWidth() / 1.5), (int) (getHeight() / 3.5));
            dialog.setLocationRelativeTo(controller.getGameFrame());
            dialog.setUndecorated(true); // 去边框

            // 加载背景图片
            ImageIcon icon = new ImageIcon("Resources/victory.png");
            Image img = icon.getImage().getScaledInstance((int) (getWidth() / 1.5), (int) (getHeight() / 3.5), Image.SCALE_SMOOTH);
            ImageIcon backgroundIcon = new ImageIcon(img);
            JLabel background = new JLabel(backgroundIcon);
            background.setLayout(new BorderLayout()); // 可在上面放东西

            // 半透明遮罩面板（为了让文字更清晰）
            JPanel overlay = new JPanel(new BorderLayout());
            overlay.setOpaque(false);
            overlay.setBorder(BorderFactory.createEmptyBorder((int) (getWidth() / 25.6), getWidth() / 12, getWidth() / 20, (int) (getWidth() / 12.8)));

            // 消息文字
            JLabel messageLabel = new JLabel(String.format("恭喜通关！总步数 %d", steps), SwingConstants.CENTER);
            messageLabel.setFont(new Font("微软雅黑", Font.BOLD, getWidth() / 20));
            messageLabel.setForeground(Color.WHITE);
            overlay.add(messageLabel, BorderLayout.NORTH);

            // 确定按钮
            JButton button = new JButton("结束游戏");
            button.setFont(new Font("微软雅黑", Font.PLAIN, getWidth() / 28));
            button.setFocusPainted(false);
            button.setBackground(new Color(0, 120, 215));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            button.addActionListener(_ -> {
                dialog.dispose();
                System.exit(0);
            });

            JButton button1 = getJButton(dialog);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            buttonPanel.setOpaque(false);
            buttonPanel.add(button, BorderLayout.WEST);
            buttonPanel.add(button1, BorderLayout.EAST);

            overlay.add(buttonPanel, BorderLayout.SOUTH);
            background.add(overlay, BorderLayout.CENTER);
            dialog.setContentPane(background);
            dialog.setVisible(true);
        }
    }

    private JButton getJButton(JDialog dialog) {
        JButton button1 = new JButton("重新开始");
        button1.setFont(new Font("微软雅黑", Font.PLAIN, getWidth() / 28));
        button1.setFocusPainted(false);
        button1.setBackground(new Color(0, 120, 215));
        button1.setForeground(Color.WHITE);
        button1.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        button1.addActionListener(_ -> {
            dialog.dispose();
            stepLabel.setText("步数: 0");
            steps = 0;
            clearBoxes();
            int[][] my_map = deepCopy(MapModel.MAP_1.getCopy());
            this.remove(jLabel_list[0]);
            this.remove(jLabel_list[1]);
            MapModel.MAP_1.setMatrix(my_map);
            paintGame();
        });
        return button1;
    }

    public JLabel getStepLabel() {
        return this.stepLabel;
    }

    public void setGrid_size(int size) {
        grid_size = size;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public BoxComponent getSelectedBox() {
        return selectedBox;
    }

    public int getGrid_size() {
        return grid_size;
    }

    public ArrayList<BoxComponent> getBoxes() {
        return boxes;
    }

    public void clearBoxes() {
        for (BoxComponent box : boxes) {
            this.remove(box);
        }
        boxes.clear();
    }

    public int[][] deepCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    public JLabel[] setJlabel() {
        JLabel jLabel;
        int[] my_success_list = MapModel.MAP_1.getSuccess_condition();
        jLabel = new JLabel("终");
        jLabel.setForeground(Color.red);
        Font headline_font = new Font("微软雅黑", Font.BOLD, (int) (grid_size/1.5));
        jLabel.setFont(headline_font);
        jLabel.setBounds((my_success_list[1]+1) * grid_size + 10, my_success_list[0] * grid_size + 2, grid_size, grid_size);

        JLabel jLabel1;
        jLabel1 = new JLabel("点");
        jLabel1.setForeground(Color.red);
        jLabel1.setFont(headline_font);
        jLabel1.setBounds((my_success_list[1]+1) * grid_size + 10, (1+my_success_list[0]) * grid_size + 2, grid_size, grid_size);
        return new JLabel[]{jLabel, jLabel1};
    }

    public JLabel[] getJLabel_list() {
        return jLabel_list;
    }
}
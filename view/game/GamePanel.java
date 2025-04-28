package view.game;

import controller.GameController;
import model.Direction;
import model.MapModel;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {
    private ArrayList<BoxComponent> boxes;
    private MapModel model;
    private GameController controller;
    private JLabel stepLabel = new JLabel("步数: 0");
    private int steps = 0;
    private int grid_size;
    private BoxComponent selectedBox;


    public GamePanel(MapModel model, int width) {
        this.grid_size = width / 10;
        boxes = new ArrayList<>();
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(model.getWidth() * grid_size + 4, model.getHeight() * grid_size + 4);
        this.model = model;
        this.selectedBox = null;

        paintGame();
    }

    /*
                        {1, 2, 2, 1, 1},
                        {3, 4, 4, 2, 2},
                        {3, 4, 4, 1, 0},
                        {1, 2, 2, 1, 0},
                        {1, 1, 1, 1, 1}
     */
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
            overlay.setBorder(BorderFactory.createEmptyBorder((int) (getWidth()/25.6), getWidth()/12, getWidth()/20, (int) (getWidth()/12.8)));

            // 消息文字
            JLabel messageLabel = new JLabel("恭喜通关", SwingConstants.CENTER);
            messageLabel.setFont(new Font("微软雅黑", Font.BOLD, getWidth() / 18));
            messageLabel.setForeground(Color.WHITE);
            overlay.add(messageLabel, BorderLayout.NORTH);

            // 确定按钮
            JButton button = new JButton("结束游戏");
            button.setFont(new Font("微软雅黑", Font.PLAIN, getWidth() / 28));
            button.setFocusPainted(false);
            button.setBackground(new Color(0, 120, 215));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            button.addActionListener(e -> {
                dialog.dispose();
                System.exit(0);
            });

            JButton button1 = new JButton("重新开始");
            button1.setFont(new Font("微软雅黑", Font.PLAIN, getWidth() / 28));
            button1.setFocusPainted(false);
            button1.setBackground(new Color(0, 120, 215));
            button1.setForeground(Color.WHITE);
            button1.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            button1.addActionListener(e -> {
                dialog.dispose();
                stepLabel.setText("步数: 0");
                steps = 0;
                clearBoxes();
                int [][] my_map = deepCopy(MapModel.MAP_1.getCopy());
                MapModel.MAP_1.setMatrix(my_map);
                paintGame();
            });

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

    private int[][] deepCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

}

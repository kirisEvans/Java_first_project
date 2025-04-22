package view.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * This class is only to enable key events.
 */
public abstract class ListenerPanel extends JPanel {
    public ListenerPanel() {
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        this.setFocusable(true);
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> doMoveRight();  // 如果按的是右箭头或 D 键，则调用 doMoveRight() 方法
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> doMoveLeft();    // 如果按的是左箭头或 A 键，则调用 doMoveLeft() 方法
                case KeyEvent.VK_UP, KeyEvent.VK_W -> doMoveUp();        // 如果按的是上箭头或 W 键，则调用 doMoveUp() 方法
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> doMoveDown();    // 如果按的是下箭头或 S 键，则调用 doMoveDown() 方法
            }
        }
    }
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.getID() == MouseEvent.MOUSE_CLICKED) {
            doMouseClick(e.getPoint());
        }
    }
    public abstract void doMouseClick(Point point);

    public abstract void doMoveRight();

    public abstract void doMoveLeft();

    public abstract void doMoveUp();

    public abstract void doMoveDown();


}

package Listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MoveListener extends MouseAdapter {
    private static int tx, ty;
    private JFrame frame;

    public MoveListener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (frame.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
            frame.setLocation(e.getXOnScreen() - tx, e.getYOnScreen() - ty);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        tx = e.getX();
        ty = e.getY();
    }
}

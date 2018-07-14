package Listeners;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import View.*;

public class ExitListener implements MouseListener {
    private View view;

    public ExitListener(View view){
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        view.setHoverProperties((JLabel) e.getComponent());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().setForeground(view.TWITCH_PURPLE);
    }
}

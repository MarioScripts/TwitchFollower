package Listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import View.*;

public class MinListener implements MouseListener {
    private View view;

    public MinListener(View view){
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        view.setState(Frame.ICONIFIED);
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

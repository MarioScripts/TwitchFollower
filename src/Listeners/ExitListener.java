package Listeners;

import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import static Other.Colors.TWITCH_PURPLE;

/**
 * Closes the program when the close button on the main GUI View is pressed
 */
public class ExitListener implements MouseListener {
    private View view;

    public ExitListener(View view) {
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
        e.getComponent().setForeground(TWITCH_PURPLE);
    }
}

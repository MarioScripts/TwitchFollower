package Listeners;

import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Waits for hovers on JLabel to set deselect properties
 */
public class DeselectListener implements MouseListener {
    private View view;

    public DeselectListener(View view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel selected = view.getSelected();
        if (selected != null) {
            view.setDeselectProperties(selected);
            view.setSelected(null);
        }
        view.refresh();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        view.refresh();
    }

    public void mouseExited(MouseEvent e) {
        view.refresh();
    }
}
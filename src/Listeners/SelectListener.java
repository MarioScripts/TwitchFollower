package Listeners;

import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Waits for clicks/hovers on JLabel to set select properties
 * Also handles context menu clicks
 */

public class SelectListener implements MouseListener {
    private View view;

    public SelectListener(View view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel selected = view.getSelected();

        if (selected != null && e.getComponent() != selected) {
            view.setDeselectProperties(selected);
        }

        if (e.getComponent() != selected) {
            view.setSelectProperties((JPanel) e.getComponent());
            view.setSelected((JPanel) e.getComponent());
        } else if (e.getComponent() == selected && !SwingUtilities.isRightMouseButton(e)) {
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
        view.setSelectProperties((JPanel) e.getComponent());
        view.refresh();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getComponent() != view.getSelected()) {
            view.setDeselectProperties((JPanel) e.getComponent());
        }
        view.refresh();

    }
}

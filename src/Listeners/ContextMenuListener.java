package Listeners;

import Model.Model;
import View.PopupMenu;
import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContextMenuListener implements MouseListener {
    private View view;
    private Model model;

    public ContextMenuListener(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {

            PopupMenu popup = new PopupMenu(view);
            popup.removeStreamListener(new RemoveStreamListener(view, model));
            popup.show(e.getComponent(), e.getX(), e.getY());
        }

        view.validate();
        view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

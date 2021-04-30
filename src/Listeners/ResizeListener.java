package Listeners;

import Controller.Controller;
import View.View;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Changes display panel size when resizing the GUI to match with overall GUI layout
 */
public class ResizeListener implements ComponentListener {
    private static int cols = 2;
    private View view;
    private Controller controller;

    public ResizeListener(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        JPanel pnlDisplay = view.getDisplayPanel();
        int currCol = (int) Math.ceil((view.getWidth() / 2) / 150);

        pnlDisplay.setSize(new Dimension(view.getWidth(), view.getHeight()));
        pnlDisplay.setMaximumSize(new Dimension(view.getWidth(), view.getHeight()));
        if (currCol != cols) {
            cols = currCol;
            pnlDisplay.setLayout(null);
            pnlDisplay.setLayout(new MigLayout("wrap " + currCol + ", flowx, insets 10 10"));
            controller.refreshGUI();
        }

        view.validate();
        view.repaint();

    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
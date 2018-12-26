package Listeners;

import Controller.Controller;
import Controller.Updater;
import View.SettingsView;
import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Opens Settings GUI when settings button is pressed on main GUI
 */
public class SettingsListener implements MouseListener {
    private Controller controller;
    private Updater streamUpdateThread;
    private View view;

    public SettingsListener(View view, Updater streamUpdateThread, Controller controller) {
        this.view = view;
        this.streamUpdateThread = streamUpdateThread;
        this.controller = controller;
        view.refresh();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        SettingsView.getInstance(streamUpdateThread, controller);
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
        view.refresh();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        view.setUnhoverProperties((JLabel) e.getComponent());
        view.refresh();
    }
}

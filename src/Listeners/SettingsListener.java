package Listeners;

import Controller.Controller;
import Controller.Updater;
import Model.Model;
import View.SettingsView;
import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingsListener implements MouseListener {
    private Controller controller;
    private Updater streamUpdateThread;
    private Model model;
    private View view;

    public SettingsListener(Model model, View view, Updater streamUpdateThread, Controller controller) {
        this.model = model;
        this.view = view;
        this.streamUpdateThread = streamUpdateThread;
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        SettingsView.getInstance(streamUpdateThread, model, controller);
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
        view.setUnhoverProperties((JLabel) e.getComponent());
    }
}

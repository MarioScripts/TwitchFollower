package Listeners;

import Controller.Controller;
import Model.Model;
import Other.Settings;
import View.View;

import javax.swing.*;
import java.awt.event.*;

public class GameSelectListener implements MouseListener {
    private Model model;
    private View view;
    private Controller controller;

    public GameSelectListener(Model model, View view, Controller controller) {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String game = view.getGameSelected();
        Settings.setGameFilter(game);
        model.updateSettings();
        view.setSearchText(game);
        view.hideGames();
        controller.refreshGUIStreams();
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

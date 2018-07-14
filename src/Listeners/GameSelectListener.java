package Listeners;

import Controller.Controller;
import Model.Model;
import Other.Settings;
import View.View;

import javax.swing.*;
import java.awt.event.*;

public class GameSelectListener implements KeyListener {
    private Model model;
    private View view;
    private Controller controller;

    public GameSelectListener(Model model, View view, Controller controller) {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        String game = view.getGameSelected();
        Settings.setGameFilter(game);
        model.updateSettings();
        view.setSearchText(game);
        view.hideGames();
        controller.refreshGUIStreams();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

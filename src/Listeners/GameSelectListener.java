package Listeners;

import Controller.Controller;
import Other.Settings;
import View.View;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static Model.Model.updateSettings;

/**
 * Sets the specified game that is searched for and clicked as the game filter
 */
public class GameSelectListener implements MouseListener {
    private View view;
    private Controller controller;

    public GameSelectListener(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String game = view.getGameSelected();
        Settings.setGameFilter(game);
        updateSettings();
        view.setSearchText(game);
        view.hideGames();
        controller.refreshGUI();
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

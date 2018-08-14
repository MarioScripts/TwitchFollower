package Listeners;

import Controller.Controller;
import Model.Model;
import Other.Settings;
import View.View;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.util.ArrayList;

public class GameListener implements DocumentListener {
    private ArrayList<String> topGames;
    private Model model;
    private View view;
    private Controller controller;

    public GameListener(Model model, View view, Controller controller) {
        this.model = model;
        this.view = view;
        this.controller = controller;
        topGames = model.getTopGames();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update(e.getDocument());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update(e.getDocument());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update(e.getDocument());
    }

    private void update(Document doc) {
        try {
            String input = doc.getText(0, doc.getLength()).toLowerCase();

            List matchedGames = new List();
            if (input.length() > 0) {
                for (String game : topGames) {
                    if (game.toLowerCase().contains(input)) {
                        matchedGames.add(game);

                    }
                }
            }

            view.hideGames();

            if (input.length() > 0) {
                view.showGames(matchedGames.getItems());
            }

            view.revalidate();
            view.repaint();

        } catch (Exception e1) {
            System.out.println(e1.getMessage());

        }

        Settings.setGameFilter(view.getSearchText());
        model.updateSettings();
        controller.refreshGUIStreams();

    }
}
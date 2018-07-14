package Listeners;

import Model.Model;
import View.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveStreamListener implements ActionListener {
    private View view;
    private Model model;

    public RemoveStreamListener(View view, Model mode) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel pnlDisplay = view.getDisplayPanel();
        JPanel selected = view.getSelected();

        if (selected != null) {
            pnlDisplay.remove(selected);
            view.setSelected(null);

            model.removeStream(selected.getName());

            pnlDisplay.validate();
            pnlDisplay.repaint();
        }

        view.setSelected(null);
    }
}

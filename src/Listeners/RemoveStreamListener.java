package Listeners;

import View.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Model.Model.removeStream;

/**
 * Removes selected stream when 'remove stream' context menu option is clicked
 */
public class RemoveStreamListener implements ActionListener {
    private View view;

    public RemoveStreamListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel pnlDisplay = view.getDisplayPanel();
        JPanel selected = view.getSelected();

        if (selected != null) {
            pnlDisplay.remove(selected);
            view.setSelected(null);

            removeStream(selected.getName());

            pnlDisplay.validate();
            pnlDisplay.repaint();
        }

        view.setSelected(null);
        view.refresh();
    }
}

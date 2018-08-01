package Listeners;

import Other.Settings;
import Model.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowExitListener implements WindowListener{
    private Model model;

    public WindowExitListener(Model model){
        this.model = model;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Settings.setSize(e.getWindow().getSize());
        Settings.setLoc(e.getWindow().getLocation());
        model.updateSettings();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

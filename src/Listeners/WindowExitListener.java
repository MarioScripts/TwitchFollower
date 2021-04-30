package Listeners;

import Other.Settings;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static Model.Model.updateSettings;

/**
 * On program termination, records the window size and screen location to be saved and loaded on next start up
 */
public class WindowExitListener implements WindowListener{
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Settings.setSize(e.getWindow().getSize());
        Settings.setLoc(e.getWindow().getLocation());
        updateSettings();
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

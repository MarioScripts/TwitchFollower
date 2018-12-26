package Listeners;

import Controller.Controller;
import Exceptions.DuplicateStreamException;
import StreamList.StreamNode;
import View.View;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InvalidObjectException;

import static Model.Model.addStream;
import static Model.Model.getStreamInfo;

/**
 * Gets name of stream to add from user and adds it to the list/GUI
 */
public class AddListener implements MouseListener {
    private Controller controller;
    private View view;

    /**
     * Constructor
     * @param view Main Settings View
     * @param controller Controller
     */
    public AddListener(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    @Override
    /**
     * When the mouse is clicked, shows a JOptionPane that allows the user to enter a channel name to add to the stream list
     */
    public void mouseClicked(MouseEvent e) {
        String name = JOptionPane.showInputDialog(null, "Name", "Twitch Follower", JOptionPane.NO_OPTION);

        if (name != null && name.length() >= 1) {
            try {
                StreamNode tempInfo = getStreamInfo(new StreamNode(name));
                addStream(tempInfo);
                controller.refreshGUI();

            } catch (DuplicateStreamException e1) {
                System.out.println(e1.getMessage());
                JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Duplicate Streams", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidObjectException e2) {
                JOptionPane.showMessageDialog(null, "The stream \"" + name + "\" does not exist.", "Invalid Stream", JOptionPane.ERROR_MESSAGE);
            }
        }
        view.refresh();
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

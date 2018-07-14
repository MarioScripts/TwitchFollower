package Listeners;

import Exceptions.DuplicateStreamException;
import StreamList.StreamNode;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InvalidObjectException;
import Model.*;
import Controller.*;
import View.*;

/**
 * Gets name of stream to add from user and adds it to the list/GUI
 */
public class AddListener implements MouseListener {
    private Model model;
    private Controller controller;
    private View view;

    public AddListener(Model model, View view, Controller controller){
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String name = JOptionPane.showInputDialog(null, "Name", "Twitch Follower", JOptionPane.NO_OPTION);

        if(name != null && name.length() >= 1){
            try{
                StreamNode tempInfo = model.getStreamInfo(new StreamNode(name));
                model.addStream(tempInfo);
                controller.refreshGUIStreams();

            }catch (DuplicateStreamException e1){
                System.out.println(e1.getMessage());
                JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Dupliacte Streams", JOptionPane.ERROR_MESSAGE);
            }catch (InvalidObjectException e2){
                JOptionPane.showMessageDialog(null, "The stream \"" + name + "\" does not exist.", "Invalid Stream", JOptionPane.ERROR_MESSAGE);
            }
        }
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

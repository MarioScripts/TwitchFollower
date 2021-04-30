package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Handles context menu of GUI elements
 */
public class PopupMenu extends JPopupMenu {
    /**
     * JMenu items
     */
    JMenuItem openTwitch, removeStream, moreInfo, popoutChat;

    /**
     * Selected GUI element
     */
    JPanel selected;

    /**
     * View object
     */
    View view;

    /**
     * Constructor
     * Adds actionlisteners to PopupMenu
     * @param view View object
     */
    public PopupMenu(View view) {
        this.view = view;
        selected = view.getSelected();

        String name = selected.getName();
        openTwitch = new JMenuItem("Open on Twitch");
        removeStream = new JMenuItem("Remove");
        popoutChat = new JMenuItem("Open popout chat");

        openTwitch.addMouseListener(new OpenTwitchListener(name));
        popoutChat.addMouseListener(new OpenPopoutChatListener(name));

        add(openTwitch);
        add(popoutChat);
        add(removeStream);
    }

    /**
     * Adds specified action listener to remove stream context menu option
     * @param listener Remove stream listener as ActionListener
     */
    public void removeStreamListener(ActionListener listener) {
        removeStream.addActionListener(listener);
    }

    /**
     * Opens Twitch with the currently selected stream
     */
    private class OpenTwitchListener implements MouseListener {
        String name;

        public OpenTwitchListener(String name) {
            this.name = name;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Model.Model.openToTwitch(name);
            view.setDeselectProperties(selected);
            view.setSelected(null);
            view.refresh();

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            view.refresh();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            view.refresh();
        }
    }

    /**
     * Opens popout chat with the currently selected stream
     */
    private class OpenPopoutChatListener implements MouseListener {
        String name;

        public OpenPopoutChatListener(String name) {
            this.name = name;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Model.Model.openPopoutChat(name);
            view.setDeselectProperties(selected);
            view.setSelected(null);
            view.refresh();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            view.refresh();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            view.refresh();
        }
    }
}

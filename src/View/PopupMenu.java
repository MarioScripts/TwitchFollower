package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.InvalidPathException;

/**
 * Handles context menu of GUI elements
 */
public class PopupMenu extends JPopupMenu {
    /**
     * JMenu items
     */
    JMenuItem openTwitch, openLivestreamer, moreInfo, popoutChat;

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
    public PopupMenu(View view){
        this.view = view;
        selected = view.getSelected();

        String name = selected.getName();
        openTwitch = new JMenuItem("Open on Twitch");
        openLivestreamer = new JMenuItem("Open on Livestreamer");
        popoutChat = new JMenuItem("Open popout chat");

        openTwitch.addActionListener(new OpenTwitchListener(name));
        openLivestreamer.addActionListener(new OpenLivestreamerListener(name));
        popoutChat.addActionListener(new OpenPopoutChatListener(name));

        add(openTwitch);
        add(openLivestreamer);
        add(popoutChat);
    }

    /**
     * Opens Twitch with the currently selected stream
     */
    private class OpenTwitchListener implements ActionListener {
        String name;
        public OpenTwitchListener(String name){
            this.name = name;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Model.Model.openToTwitch(name);
            view.setDeselectProperties(selected);
            view.setSelected(null);
        }
    }

    /**
     * Opens Livestreamer with the currently selected stream
     */
    private class OpenLivestreamerListener implements ActionListener {
        String name;
        public OpenLivestreamerListener(String name){
            this.name = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                Model.Model.openToLivestreamer(name);
            } catch(InvalidPathException e1){
                JOptionPane.showMessageDialog(new Frame(), "Livestreamer could not be found");
            }
            view.setDeselectProperties(selected);
            view.setSelected(null);
        }
    }

    /**
     * Opens popout chat with the currently selected stream
     */
    private class OpenPopoutChatListener implements ActionListener {
        String name;

        public OpenPopoutChatListener(String name){
            this.name = name;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            Model.Model.openPopoutChat(name);
            view.setDeselectProperties(selected);
            view.setSelected(null);
        }
    }
}

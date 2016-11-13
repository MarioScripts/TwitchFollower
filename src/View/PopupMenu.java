package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TODO: Add moreinfo tab that gives info on stream title, viewer #s, etc, uptime, etc
public class PopupMenu extends JPopupMenu {
    JMenuItem openTwitch, openLivestreamer, moreInfo, popoutChat;

    public PopupMenu(String name){
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

    private class OpenTwitchListener implements ActionListener {
        String name;
        public OpenTwitchListener(String name){
            this.name = name;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Model.Model.openToTwitch(name);
        }
    }

    private class OpenLivestreamerListener implements ActionListener {
        String name;
        public OpenLivestreamerListener(String name){
            this.name = name;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Model.Model.openToLivestreamer(name);
        }
    }

    private class OpenPopoutChatListener implements ActionListener {
        String name;

        public OpenPopoutChatListener(String name){
            this.name = name;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            Model.Model.openPopoutChat(name);
        }
    }
}

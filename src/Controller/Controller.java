package Controller;

import Exceptions.DuplicateStreamException;
import Model.Model;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamNode;
import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InvalidObjectException;

/**
 * Control interaction between View and Model objects
 */
public class Controller {
    /**
     * Model object
     */
    private Model model;

    /**
     * View object
     */
    private View view;

    private Updater streamUpdateThread;

    /**
     * Constructor
     * @param m Model object
     * @param v View object
     */
    public Controller(Model m, View v){
        model = m;
        view = v;
        model.readSettings();
        initGUIStreams();
        //Start stream info update thread
        //streamUpdateThread = new StreamUpdate(3000, model.getStreams(), view, model);
        streamUpdateThread = new Updater(model, view, model.getStreams(), 30000);

        addActionListeners();
    }


    /**
     * Adds actionlisteners from View object
     */
    private void addActionListeners(){
        view.btnAddListener(new AddListener());
        view.btnRemoveListener(new RemoveListener());
        view.btnSettingsListener(new SettingsListener(this));
    }

    public void refreshGUIStreams(){
        StreamIterator iter = model.getStreams().iterator();
        view.getDisplayPanel().removeAll();

        while(iter.hasNext()){
            StreamNode temp = iter.next();

            if((Settings.getShowOffline() && temp.getStatus().equals("Offline")) || temp.getStatus().equals("Online")){
                view.addStreamLabel(temp);
            }
        }

        view.validate();
        view.repaint();
    }

    /**
     * Initializes all streams once on load
     */
    public void initGUIStreams(){
        StreamIterator iter = model.getStreams().iterator();
        view.getDisplayPanel().removeAll();

        while(iter.hasNext()){
            StreamNode temp = iter.next();

            try{
                StreamNode tempInfo = model.getStreamInfo(temp);
                temp.setGame(tempInfo.getGame());
                temp.setStatus(tempInfo.getStatus());
                temp.setName(tempInfo.getName());
                temp.setDisplayName(tempInfo.getDisplayName());
                temp.setLogo(tempInfo.getLogo());

                if((Settings.getShowOffline() && tempInfo.getStatus().equals("Offline")) || tempInfo.getStatus().equals("Online")){
                    view.addStreamLabel(temp);
                }

            }catch (InvalidObjectException e){
                System.out.println(e.getMessage());
            }
        }

        view.validate();
        view.repaint();
        view.setVisible(true);
    }

    // Listeners

    /**
     * Gets name of stream to add from user and adds it to the list/GUI
     */
    private class AddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Name", "Twitch Follower", JOptionPane.NO_OPTION);

            if(name != null && name.length() >= 1){
                try{
                    StreamNode tempInfo = model.getStreamInfo(new StreamNode(name));
                    model.addStream(tempInfo);
                    view.addStreamLabel(tempInfo);
                    view.getDisplayPanel().validate();
                    view.getDisplayPanel().repaint();
                }catch (DuplicateStreamException e1){
                    System.out.println(e1.getMessage());
                    JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Dupliacte Streams", JOptionPane.ERROR_MESSAGE);
                }catch (InvalidObjectException e2){
                    System.out.println(e2.getMessage());
                }
            }
        }
    }

    /**
     * Removes selected stream from list/GUI
     */
    private class RemoveListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel pnlDisplay = view.getDisplayPanel();
            JLabel selected = view.getSelected();

            if(selected != null){
                pnlDisplay.remove(selected);
                view.setSelected(null);

                model.removeStream(selected.getName());

                pnlDisplay.validate();
                pnlDisplay.repaint();
            }
        }
    }

    private class SettingsListener implements ActionListener{

        private Controller controller;
        public SettingsListener(Controller controller){
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new SettingsView(streamUpdateThread, model, controller);
        }
    }

}

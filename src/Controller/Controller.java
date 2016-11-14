package Controller;

import Exceptions.DuplicateStreamException;
import Model.Model;
import StreamList.*;
import View.*;
import View.PopupMenu;
import org.gpl.JSplitButton.action.SplitButtonActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.file.InvalidPathException;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model m, View v){
        model = m;
        view = v;
        initGUIStreams();
        addActionListeners();

        //Start stream info update thread
        new StreamUpdate(30000, model.getStreams(), view, model);
    }

    private void initGUIStreams(){
        StreamIterator iter = model.getStreams().iterator();

        while(iter.hasNext()){
            StreamNode temp = iter.next();
            StreamNode tempInfo = model.getStreamInfo(temp);

            temp.setGame(tempInfo.getGame());
            temp.setStatus(tempInfo.getStatus());
            temp.setName(tempInfo.getName());
            temp.setLogo(tempInfo.getLogo());

            view.addStreamLabel(tempInfo);
        }

        view.validate();
        view.repaint();
        view.setVisible(true);
    }


    private void addActionListeners(){
        view.btnAddListener(new AddListener());
        view.btnRemoveListener(new RemoveListener());
    }

    // Listeners

    private class AddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Name", "Twitch Follower", JOptionPane.NO_OPTION);

            if(name != null && name.length() >= 1){
                StreamNode tempInfo = model.getStreamInfo(new StreamNode(name));
                try{
                    model.addStream(tempInfo);
                    view.addStreamLabel(tempInfo);
                    view.getDisplayPanel().validate();
                    view.getDisplayPanel().repaint();
                }catch (DuplicateStreamException e1){
                    System.out.println(e1.getMessage());
                    JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Dupliacte Streams", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

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

}

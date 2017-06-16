package Controller;

import Exceptions.DuplicateStreamException;
import Model.Model;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamNode;
import View.*;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.InvalidObjectException;
import java.util.ListIterator;

/**
 * Control interaction between View and Model objects
 */
public class Controller {

    private int cols = 2;
    /**
     * Model object
     */
    private Model model;

    /**
     * View object
     */
    private View view;

    private Updater streamUpdateThread;

    private String gameFilter;

    /**
     * Constructor
     * @param m Model object
     * @param v View object
     */
    public Controller(Model m, View v){
        model = m;
        view = v;
        gameFilter = "None";
        model.readSettings();
        model.getTopGames();
        initGUIStreams();

        //Start stream info update thread
        //streamUpdateThread = new StreamUpdate(3000, model.getStreams(), view, model);
        streamUpdateThread = new Updater(model, view, model.getStreams(), Settings.getSleepTime(), gameFilter);
        view.setVisible(true);

        addActionListeners();
    }


    /**
     * Adds actionlisteners from View object
     */
    private void addActionListeners(){
        view.lblAddListener(new AddListener());
        view.lblRemoveListener(new RemoveListener());
        view.lblSettingsListener(new SettingsListener(this));
        view.pnlResizeListener(new ResizeListener());
        view.lblSearchListener(new SearchListener());
        view.txtSearchListener(new GameListener());
        view.lstSearchGamesListener(new GameSelectListener());
    }

    public void refreshGUIStreams(){
        StreamIterator iter = model.getStreams().iterator();
        view.getDisplayPanel().removeAll();

        while(iter.hasNext()){
            StreamNode temp = iter.next();

            if((Settings.getShowOffline() && temp.getStatus().equals("Offline")) || temp.getStatus().equals("Online")){
                if(gameFilter.equals("None") || gameFilter.equals(temp.getGame())){
                    if((Settings.getShowVodcast() && temp.getVodcast()) || !temp.getVodcast()){
                        view.addStreamLabel(temp);
                    }
                }

            }
        }

        view.validate();
        view.repaint();
    }

    /**
     * Initializes all streams once on load
     */
    public void initGUIStreams(){
        view.showLoading();
        StreamIterator iter = model.getStreams().iterator();
        view.getDisplayPanel().removeAll();

        while(iter.hasNext()){
            StreamNode temp = iter.next();

            try{
                StreamNode tempInfo = model.getStreamInfo(temp);
                temp.setTitle(tempInfo.getTitle());
                temp.setVodcast(tempInfo.getVodcast());
                temp.setGame(tempInfo.getGame());
                temp.setStatus(tempInfo.getStatus());
                temp.setName(tempInfo.getName());
                temp.setDisplayName(tempInfo.getDisplayName());
                temp.setLogo(tempInfo.getLogo());

                if((Settings.getShowOffline() && tempInfo.getStatus().equals("Offline")) || tempInfo.getStatus().equals("Online")){
                    if(gameFilter.equals("None") || gameFilter.equals(temp.getGame())){
                        if((Settings.getShowVodcast() && temp.getVodcast()) || !temp.getVodcast()){
                            view.addStreamLabel(temp);
                        }
                    }
                }

            }catch (InvalidObjectException e){
                System.out.println(e.getMessage());
            }catch (NullPointerException e){
                System.out.println("Image issue");
            }
        }

        view.hideLoading();
        view.validate();
        view.repaint();
    }

    // Listeners

    /**
     * Gets name of stream to add from user and adds it to the list/GUI
     */
    private class AddListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            String name = JOptionPane.showInputDialog(null, "Name", "Twitch Follower", JOptionPane.NO_OPTION);

            if(name != null && name.length() >= 1){
                try{
                    StreamNode tempInfo = model.getStreamInfo(new StreamNode(name));
                    model.addStream(tempInfo);
                    if(tempInfo.getStatus().equals("Online")){
                        view.addStreamLabel(tempInfo);
                        view.getDisplayPanel().validate();
                        view.getDisplayPanel().repaint();
                    }

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

    /**
     * Removes selected stream from list/GUI
     */
    private class RemoveListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel pnlDisplay = view.getDisplayPanel();
            JPanel selected = view.getSelected();

            if(selected != null){
                pnlDisplay.remove(selected);
                view.setSelected(null);

                model.removeStream(selected.getName());

                pnlDisplay.validate();
                pnlDisplay.repaint();
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

    /**
     * Changes display panel size when resizing the GUI to match with overall GUI layout
     */
    private class ResizeListener implements ComponentListener {
        @Override
        public void componentResized(ComponentEvent e) {
//            System.out.println(view.getHeight());
//            System.out.println(view.getDisplayPanel().getComponentCount());
//            int test = view.getHeight() / view.getDisplayPanel().getComponents().length;
//            System.out.println(test);
//            if(test > 66){
//                view.showSettings();
//            }else{
//                view.hideSettings();
//            }
            JPanel pnlDisplay = view.getDisplayPanel();
            int currCol = (int)Math.ceil((view.getWidth()/2)/150);

            pnlDisplay.setSize(new Dimension(view.getWidth(), view.getHeight()));
            pnlDisplay.setMaximumSize(new Dimension(view.getWidth(), view.getHeight()));
            if(currCol != cols){
                cols = currCol;
                pnlDisplay.setLayout(null);
                pnlDisplay.setLayout(new MigLayout("wrap " + currCol + ", flowx, insets 10 10"));
                refreshGUIStreams();
            }

            view.validate();
            view.repaint();

        }

        @Override
        public void componentMoved(ComponentEvent e) {}

        @Override
        public void componentShown(ComponentEvent e) {}

        @Override
        public void componentHidden(ComponentEvent e) {}
    }

    private class SettingsListener implements MouseListener{
        private Controller controller;

        public SettingsListener(Controller controller){
            this.controller = controller;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            new SettingsView(streamUpdateThread, model, controller);
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

    private class SearchListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(view.searchIsVisible()){
                view.hideSearch();
            }else{
//                String[] hey = model.getTopGames().getItems();
//                JList ok = new JList(hey);
//                view.pnlSearch.add(ok);
//                AutoCompleteDecorator.decorate(ok, view.txtSearch, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
                view.showSearch();

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

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class GameListener implements DocumentListener{
        private List topGames;


        public GameListener(){
            topGames = model.getTopGames();

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e.getDocument());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e.getDocument());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update(e.getDocument());
        }

        private void update(Document doc){
            try{
                String input = doc.getText(0, doc.getLength()).toLowerCase();
                List matchedGames = new List();
                if(input.length() > 0){
                    for(String game : topGames.getItems()){
                        if(game.toLowerCase().contains(input)){
                            matchedGames.add(game);

                        }
                    }
                }

                view.hideGames();

                if(input.length() > 0){
                    view.showGames(matchedGames.getItems());
                }

                view.revalidate();
                view.repaint();

            }catch(Exception e1){
                System.out.println(e1.getMessage());

            }
        }

    }

    private class GameSelectListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            String game = view.getGameSelected();
            gameFilter = game;
            view.setSearchText(game);
            view.hideGames();
            refreshGUIStreams();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}

package Controller;

import Exceptions.DuplicateStreamException;
import Model.Model;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamNode;
import View.*;
import View.PopupMenu;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.InvalidObjectException;
import java.util.ArrayList;

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
        // TODO: Showstuff
        System.out.println("START SPLASH");
        Splash splash = new Splash();
        model.readSettings();
        model.getTopGames();
        initGUIStreams();
        System.out.println("END SPLASH");
        splash.dispose();

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
                if(gameFilter.equals("None") || temp.getGame().toLowerCase().contains(gameFilter.toLowerCase())){
                    if((Settings.getShowVodcast() && temp.getVodcast()) || !temp.getVodcast()){
                        view.addStreamLabel(temp).addMouseListener(new ContextMenuListener());
                    }
                }

            }
        }

        view.changeColorScheme();

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
                temp.setViews(tempInfo.getViews());

                if((Settings.getShowOffline() && tempInfo.getStatus().equals("Offline")) || tempInfo.getStatus().equals("Online")){
                    if(gameFilter.equals("None") || gameFilter.equals(temp.getGame())){
                        if((Settings.getShowVodcast() && temp.getVodcast()) || !temp.getVodcast()){
                            view.addStreamLabel(temp).addMouseListener(new ContextMenuListener());
                        }
                    }
                }

            }catch (InvalidObjectException e){
                System.out.println("Invalid stream: " + e.getMessage() + " removing stream from list.");
                model.removeStream(e.getMessage());
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
                        view.addStreamLabel(tempInfo).addMouseListener(new ContextMenuListener());
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
     * Changes display panel size when resizing the GUI to match with overall GUI layout
     */
    private class ResizeListener implements ComponentListener {
        @Override
        public void componentResized(ComponentEvent e) {
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
        private ArrayList<String> topGames;


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
                    for(String game : topGames){
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

            gameFilter = view.getSearchText();
            refreshGUIStreams();

        }
    }

    private class ContextMenuListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {

            if(SwingUtilities.isRightMouseButton(e)){
                PopupMenu popup = new PopupMenu(view);
                popup.removeStreamListener(new RemoveStreamListener());
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            view.validate();
            view.repaint();
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

    private class RemoveStreamListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel pnlDisplay = view.getDisplayPanel();
            JPanel selected = view.getSelected();

            if(selected != null){
                pnlDisplay.remove(selected);
                view.setSelected(null);

                model.removeStream(selected.getName());

                pnlDisplay.validate();
                pnlDisplay.repaint();
            }

            view.setSelected(null);
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

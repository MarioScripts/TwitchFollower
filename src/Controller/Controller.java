package Controller;

import Listeners.*;
import Other.Settings;
import Other.Sorter;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import View.Splash;
import View.View;
import java.io.InvalidObjectException;
import static Model.Model.*;

/**
 * Control interaction between View and Model objects
 */
public class Controller {
    private View view;
    private Updater streamUpdateThread;
    private Splash splash;

    /**
     * Constructor. Initializes any required background threads and updates.
     * Handles initializing the GUI stream list as well as displaying the startup splash screen
     * Also does basic bookkeeping for the model and view objects
     * @param v View object
     */
    public Controller(View v) {
        view = v;
        splash = new Splash();
        readSettings();
        getTopGames();
        updateStreams();
        splash.dispose();

        //Start stream info update thread
        //streamUpdateThread = new StreamUpdate(3000, model.getStreams(), view, model);
        streamUpdateThread = new Updater(this, view);


        view.setSize(Settings.getSize());
        view.setLocation(Settings.getLoc());
        view.changeColorScheme();
        view.setVisible(true);
        view.pack();
        view.repaint();

        addActionListeners();
    }

    /**
     * Refreshes the GUI streams if they are already initialized
     * Note that this method does not update the stream's information. It merely updates the GUI
     */
    public void refreshGUI() {
        StreamList streams = getStreams();
        StreamIterator iter = streams.iterator();
        view.getDisplayPanel().removeAll();

        while (iter.hasNext()) {
            StreamNode temp = iter.next();
            addStreamToView(temp);
        }

        showNoStreamText(getStreams().size());

        view.changeColorScheme();
        view.refresh();
    }

    /**
     * Updates all stream information AND refreshes the GUI
     */
    public void updateStreams() {
        view.showLoading();
        StreamList streams = getStreams();
        Sorter.sort(streams);
        StreamIterator iter = streams.iterator();
        view.getDisplayPanel().removeAll();
        int streamMax = streams.size();
        int i = 1;

        while (iter.hasNext()) {
            splash.setStatusText("Loading... (" + i + "/" + streamMax + ")");
            StreamNode temp = iter.next();

            updateStream(temp);
            i++;
        }

        showNoStreamText(getStreams().size());
        view.hideLoading();
        view.validate();
        view.repaint();
    }

    /**
     * Gets information for specified stream, updates the information, and adds it to GUI
     * @param temp Stream to update information of
     */
    public void updateStream(StreamNode temp){
        try {
            StreamNode tempInfo = getStreamInfo(temp);
            temp.setNode(tempInfo);

            addStreamToView(temp);
        } catch (InvalidObjectException e) {
            System.out.println("Invalid stream: " + e.getMessage() + " removing stream from list.");
            removeStream(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Image issue");
        }
    }

    /**
     * Pauses background threads/stream updates.
     * Mainly used when other GUIs are in the forefront. i.e. settings GUI or add stream GUI
     */
    public void pauseBackgroundWork(){
        streamUpdateThread.hibernate();
    }

    /**
     * Resumes background threads/stream updates.
     * Mainly used when other GUIs are in the forefront. i.e. settings GUI or add stream GUI
     */
    public void resumeBackgroundWork(){
        streamUpdateThread.wake();
    }

    private void addActionListeners() {
        view.lblAddListener(new AddListener(view, this));
        view.lblSettingsListener(new SettingsListener(view, streamUpdateThread, this));
        view.pnlResizeListener(new ResizeListener(view, this));
        view.lblSearchListener(new SearchListener(view));
        view.txtSearchListener(new GameListener(view, this));
        view.lstSearchGamesListener(new GameSelectListener(view, this));
        view.lblExitListener(new ExitListener(view));
        view.lblMinListener(new MinListener(view));
        view.frmExitListener(new WindowExitListener());
    }

    private void addStreamToView(StreamNode node) {
        if ((Settings.getShowOffline() && node.getStatus().equals("Offline")) || node.getStatus().equals("Online")) {
            if (Settings.getGameFilter().equals("") || node.getGame().toLowerCase().startsWith(Settings.getGameFilter().toLowerCase())) {
                if ((Settings.getShowVodcast() && node.getVodcast()) || !node.getVodcast()) {
                    view.addStreamLabel(node).addMouseListener(new ContextMenuListener(view));
                }
            }
        }
    }

    private void showNoStreamText(int numOfStreams){
        if(numOfStreams == 0){
            view.addNoStreamLabel("You are not following any streams.");

        }else if(view.getDisplayPanel().getComponents().length == 0){
            view.addNoStreamLabel("No streams are currently online.");
        }
    }
}

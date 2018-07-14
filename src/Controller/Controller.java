package Controller;

import Listeners.*;
import Model.Model;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import View.Splash;
import View.View;
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
     *
     * @param m Model object
     * @param v View object
     */
    public Controller(Model m, View v) {
        model = m;
        view = v;
        Splash splash = new Splash();
        model.readSettings();
        model.getTopGames();
        initGUIStreams();
        splash.dispose();

        //Start stream info update thread
        //streamUpdateThread = new StreamUpdate(3000, model.getStreams(), view, model);
        streamUpdateThread = new Updater(this, model, view, model.getStreams(), Settings.getSleepTime(), Settings.getGameFilter());

        view.pack();
        view.setVisible(true);
        view.repaint();

        addActionListeners();
    }


    /**
     * Adds actionlisteners from View object
     */
    private void addActionListeners() {
        view.lblAddListener(new AddListener(model, view, this));
        view.lblSettingsListener(new SettingsListener(model, view, streamUpdateThread, this));
        view.pnlResizeListener(new ResizeListener(view, this));
        view.lblSearchListener(new SearchListener(view));
        view.txtSearchListener(new GameListener(model, view, this));
        view.lstSearchGamesListener(new GameSelectListener(model, view, this));
        view.lblExitListener(new ExitListener(view));
        view.lblMinListener(new MinListener(view));
    }

    public void refreshGUIStreams() {
        StreamList streams = model.getStreams();
        StreamIterator iter = streams.iterator();
        view.getDisplayPanel().removeAll();

        while (iter.hasNext()) {
            StreamNode temp = iter.next();
            addStream(temp);
        }

        view.changeColorScheme();
        view.validate();
        view.repaint();
    }

    /**
     * Initializes all streams once on load
     */
    public void initGUIStreams() {
        view.showLoading();
        StreamList streams = model.getStreams();
        StreamIterator iter = streams.iterator();
        view.getDisplayPanel().removeAll();

        while (iter.hasNext()) {
            StreamNode temp = iter.next();

            try {
                StreamNode tempInfo = model.getStreamInfo(temp);
                temp.setNode(tempInfo);

                addStream(temp);

            } catch (InvalidObjectException e) {
                System.out.println("Invalid stream: " + e.getMessage() + " removing stream from list.");
                model.removeStream(e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Image issue");
            }
        }

        view.hideLoading();
        view.validate();
        view.repaint();
    }

    private void addStream(StreamNode node) {
        if ((Settings.getShowOffline() && node.getStatus().equals("Offline")) || node.getStatus().equals("Online")) {
            if (Settings.getGameFilter().equals("") || node.getGame().toLowerCase().startsWith(Settings.getGameFilter().toLowerCase())) {
                if ((Settings.getShowVodcast() && node.getVodcast()) || !node.getVodcast()) {
                    view.addStreamLabel(node).addMouseListener(new ContextMenuListener(view, model));
                }
            }
        }
    }
}

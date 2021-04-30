package Controller;

import Model.Model;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import View.View;

import javax.swing.*;
import java.awt.*;

import static Model.Model.getStreamInfo;

/**
 * Update thread for all streams
 * Indefinitely runs on a background thread, updating the information for all streams until the program is terminated
 */
public class StreamUpdate extends SwingWorker<Boolean, Integer> {

    public Thread update;
    public StreamList streams;
    public boolean pauseWorking;

    private TrayIcon icon;
    private String gameFilter;
    private int sleepTime;
    private View view;

    private Controller controller;

    private StreamNode temp, tempInfo;

    /**
     * Constructor that initializes main GUI, tray icon, and controller attributes
     * @param controller Controller object
     * @param view Main GUI View
     * @param icon TrayIcon icon
     */
    public StreamUpdate(Controller controller, View view, TrayIcon icon) {
        this.icon = icon;
        this.view = view;
        this.streams = Model.getStreams();
        this.controller = controller;
        this.gameFilter = Settings.getGameFilter();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        System.out.print("Updating...");
        view.showLoading();
        view.refresh();

        // TODO: Make batch update rather than 1 by 1 (https://dev.twitch.tv/docs/api/reference#get-streams)
        StreamIterator iter = streams.iterator();
        while (iter.hasNext()) {
            temp = iter.next();
            tempInfo = getStreamInfo(temp);

            if (Settings.getStatusNotify()) {
                if (!temp.getStatus().equals(tempInfo.getStatus())) {
                    // Only alerts if stream goes online
                    if (tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Offline") && (tempInfo.getGame().equals(gameFilter) || gameFilter.equals(""))) {
                        System.out.println("\nSENDING ONLINE STATUS CHANGE NOTIFICATION");
                        icon.displayMessage(
                                "Status Change",
                                temp.getDisplayName() + " is now online\nPlaying " + tempInfo.getGame(),
                                TrayIcon.MessageType.INFO);
                    }
//                    view.validate();
//                    view.repaint();
                }
            }


            if (Settings.getGameNotify()) {
                //Check if game changes
                if (!temp.getGame().equals(tempInfo.getGame())) {
                    // Only alerts if the user is already online
                    if (tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Online") && (tempInfo.getGame().equals(gameFilter) || gameFilter.equals(""))) {
                        System.out.println("\nSENDING GAME CHANGE NOTIFICATION");
                        icon.displayMessage(
                                "Game Change",
                                temp.getDisplayName() + " has started playing " + tempInfo.getGame(),
                                TrayIcon.MessageType.INFO);
                    }
//                    view.validate();
//                    view.repaint();
                }
            }

            if (!temp.getStatus().equals(tempInfo.getStatus()) || !temp.getGame().equals(tempInfo.getGame())) {
                temp.setNode(tempInfo);
//                controller.refreshGUI();

            } else if (temp.getViews() != tempInfo.getViews()) {
                temp.setViews(tempInfo.getViews());
//                controller.refreshGUI();
            }

        }

        return true;
    }

    protected void done() {
        controller.refreshGUI();
        view.hideLoading();
        view.refresh();
        System.out.println(" Updated.");
    }

    /**
     * Prints stream info. Used for debugging
     */
    public void printInfo() {
        StreamIterator iter = streams.iterator();
        while (iter.hasNext()) {
            StreamNode info = iter.next();
            System.out.println("Name: " + info.getName());
            System.out.println("Status: " + info.getStatus());
            System.out.println("Game: " + info.getGame());
            System.out.println();
        }

    }

    public void hibernate() {
        pauseWorking = true;
    }

    public void wake() {
        pauseWorking = false;
        synchronized (this) {

            this.notifyAll();
        }
    }

    /**
     * Sleeps for sleepTime duration
     */
    private synchronized void sleep() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
        }
    }

    /**
     * Sets the sleep time of the thread
     *
     * @param sleepTime sleep delay
     */
    synchronized public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

}

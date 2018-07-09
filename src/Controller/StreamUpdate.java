package Controller;

import Model.Model;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import View.View;
import Other.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Update thread for all streams
 */
public class StreamUpdate extends SwingWorker<Boolean, Integer> {

    private TrayIcon icon;
    private String gameFilter;
    /**
     * Update thread
     */
    public Thread update;

    /**
     * List of streams
     */
    public StreamList streams;

    public boolean pauseWorking;

    /**
     * Thread sleep time
     */
    private int sleepTime;

    /**
     * View object
     */
    private View view;

    /**
     * Model object
     */
    private Model model;

    private StreamNode temp, tempInfo;

    public StreamUpdate(Model model, View view, StreamList streams, TrayIcon icon, String gameFilter){
        this.icon = icon;
        this.model = model;
        this.view = view;
        this.streams = streams;
        this.gameFilter = gameFilter;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        System.out.print("Updating...");
        view.showLoading();

        StreamIterator iter = streams.iterator();
        while(iter.hasNext()){
            temp = iter.next();
            tempInfo = model.getStreamInfo(temp);

            if(Settings.getStatusNotify()){
                if(!temp.getStatus().equals(tempInfo.getStatus())){
                    // Only alerts if stream goes online
                    if(tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Offline") && (tempInfo.getGame().equals(gameFilter) || gameFilter.equals("None"))) {
                        icon.displayMessage(
                                "Status Change",
                                temp.getDisplayName() + " is now online\nPlaying " + tempInfo.getGame(),
                                TrayIcon.MessageType.INFO);
                    }
//                    view.validate();
//                    view.repaint();
                }
            }


            if(Settings.getGameNotify()){
                //Check if game changes
                if(!temp.getGame().equals(tempInfo.getGame())){
                    // Only alerts if the user is already online
                    if(tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Online") && (tempInfo.getGame().equals(gameFilter) || gameFilter.equals("None"))){
                        icon.displayMessage(
                                "Game Change",
                                temp.getDisplayName() + " has started playing " + tempInfo.getGame(),
                                TrayIcon.MessageType.INFO);
                    }
//                    view.validate();
//                    view.repaint();
                }
            }

            if(!temp.getStatus().equals(tempInfo.getStatus()) || !temp.getGame().equals(tempInfo.getGame())){
                temp.setVodcast(tempInfo.getVodcast());
                temp.setTitle(tempInfo.getTitle());
                temp.setGame(tempInfo.getGame());
                temp.setStatus(tempInfo.getStatus());
                view.removeStreamLabel(temp.getName());
                if((Settings.getShowOffline() && tempInfo.getStatus().equals("Offline")) || tempInfo.getStatus().equals("Online")){
                    if(tempInfo.getGame().equals(gameFilter) || gameFilter.equals("None")){
                        if((Settings.getShowVodcast() && temp.getVodcast()) || !temp.getVodcast()){
                            view.addStreamLabel(temp);
                        }
                    }
                }

            }

        }

        return true;
    }

    protected void done(){
        view.validate();
        view.repaint();
        view.hideLoading();
        System.out.println(" Updated.");
    }

    /**
     * Prints stream info. Used for debugging
     */
    public void printInfo(){
        StreamIterator iter = streams.iterator();
        while(iter.hasNext()){
            StreamNode info = iter.next();
            System.out.println("Name: " + info.getName());
            System.out.println("Status: " + info.getStatus());
            System.out.println("Game: " + info.getGame());
            System.out.println();
        }

    }

    public void hibernate(){
        pauseWorking = true;
    }

    public void wake(){
        pauseWorking = false;
        synchronized (this){

            this.notifyAll();
        }
    }

    /**
     * Sleeps for sleepTime duration
     */
    private synchronized void sleep(){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
        }
    }

    /**
     * Sets the sleep time of the thread
     * @param sleepTime sleep delay
     */
    synchronized public void setSleepTime(int sleepTime){
        this.sleepTime = sleepTime;
    }

}

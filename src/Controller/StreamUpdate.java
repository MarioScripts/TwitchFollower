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

    public StreamUpdate(Model model, View view, StreamList streams, TrayIcon icon){
        this.icon = icon;
        this.model = model;
        this.view = view;
        this.streams = streams;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        System.out.print("Updating...");

        StreamIterator iter = streams.iterator();
        while(iter.hasNext()){
            temp = iter.next();
            tempInfo = model.getStreamInfo(temp);

            if(Settings.getStatusNotify()){
                if(!temp.getStatus().equals(tempInfo.getStatus())){
                    // Only alerts if stream goes online
                    if(tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Offline")) {
                        icon.displayMessage(
                                "Status Change",
                                temp.getName() + " is now online\nPlaying " + tempInfo.getGame(),
                                TrayIcon.MessageType.INFO);
                    }
                }
            }


            if(Settings.getGameNotify()){
                //Check if game changes
                if(!temp.getGame().equals(tempInfo.getGame())){
                    // Only alerts if the user is already online
                    if(tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Online")){
                        icon.displayMessage(
                                "Game Change",
                                temp.getName() + " has started playing " + tempInfo.getGame(),
                                TrayIcon.MessageType.INFO);
                    }
                }
            }

            if(!temp.getStatus().equals(tempInfo.getStatus()) || !temp.getGame().equals(tempInfo.getGame())){
                System.out.println("Henloo");
                temp.setGame(tempInfo.getGame());
                temp.setStatus(tempInfo.getStatus());
                view.removeStreamLabel(temp.getName());
                if((Settings.getShowOffline() && tempInfo.getStatus().equals("Offline")) || tempInfo.getStatus().equals("Online"))
                    view.addStreamLabel(temp);
            }

        }

        return true;
    }

    protected void done(){
        view.validate();
        view.repaint();
        System.out.println(" Updated.");
    }



    /**
     * Constructor, starts update thread
     * @param sleepTime Thread sleep time
     * @param streams List of streams
     * @param view View object
     * @param model Model object
     */
//    public StreamUpdate(int sleepTime, StreamList streams, View view, Model model){
//        this.view = view;
//        this.model = model;
//        this.sleepTime = sleepTime;
//        this.streams = streams;
//        pauseWorking = false;
//
//        update = new Thread(this);
//        update.start();
//    }

    /**
     * Loops through all streams infinitely using sleepTime as a pause between updates.
     * Handles updating stream info and updating GUI info.
     * Also handles Desktop notifications
     */
//    @Override
//    public void run() {
//        // Loops Through and updates info
//        synchronized (this) {
//            try{
//                SystemTray tray = SystemTray.getSystemTray();
//                Image img = ImageIO.read(getClass().getClassLoader().getResource("resources/Twitch.png"));
//                TrayIcon icon = new TrayIcon(img, "Twitch Follower");
//                icon.setImageAutoSize(true);
//                tray.add(icon);
//
//                while (true) {
//
//                    while(pauseWorking){
//                        try{
//                            this.wait();
//                        }catch (InterruptedException e){
//
//                        }
//
//                    }
//
//
//                    view.validate();
//                    view.repaint();
//                    System.out.println(" Updated.");
//                }
//            }catch(IOException|AWTException e){
//                System.out.println(e.getMessage());
//            }
//        }
//    }

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

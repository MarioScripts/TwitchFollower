package Controller;

import StreamList.*;
import Model.*;
import View.View;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Update thread for all streams
 */
public class StreamUpdate implements Runnable{

    /**
     * Update thread
     */
    public Thread update;

    /**
     * List of streams
     */
    public StreamList streams;

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

    /**
     * Constructor, starts update thread
     * @param sleepTime Thread sleep time
     * @param streams List of streams
     * @param view View object
     * @param model Model object
     */
    public StreamUpdate(int sleepTime, StreamList streams, View view, Model model){
        this.view = view;
        this.model = model;
        this.sleepTime = sleepTime;
        this.streams = streams;
        update = new Thread(this);
        update.start();
    }

    /**
     * Loops through all streams infinitely using sleepTime as a pause between updates.
     * Handles updating stream info and updating GUI info.
     * Also handles Desktop notifications
     */
    @Override
    public void run() {
        // Loops Through and updates info
        synchronized (this) {
            try{
                SystemTray tray = SystemTray.getSystemTray();
                Image img = ImageIO.read(getClass().getClassLoader().getResource("resources/Twitch.png"));
                TrayIcon icon = new TrayIcon(img, "Twitch Follower");
                icon.setImageAutoSize(true);
                tray.add(icon);

                while (true) {

                    sleep();
                    System.out.print("Updating...");


                    StreamIterator iter = streams.iterator();
                    while(iter.hasNext()){
                        StreamNode temp = iter.next();
                        StreamNode tempInfo = model.getStreamInfo(temp);

                        //Check if status changes
                        if(!temp.getStatus().equals(tempInfo.getStatus())){
                            // Only alerts if stream goes online
                            if(tempInfo.getStatus().equals("Online") && temp.getStatus().equals("Offline")) {
                                icon.displayMessage(
                                        "Status Change",
                                        temp.getName() + " is now online\nPlaying " + tempInfo.getGame(),
                                        TrayIcon.MessageType.INFO);
                            }
                        }

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


                        // Update list info
                        temp.setName(tempInfo.getName());
                        temp.setGame(tempInfo.getGame());
                        temp.setStatus(tempInfo.getStatus());

                        // Update GUI info
                        view.addStreamLabel(temp);
                    }

                    view.validate();
                    view.repaint();
                    System.out.println(" Updated.");
                }
            }catch(IOException|AWTException e){
                System.out.println(e.getMessage());
            }
        }
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

    /**
     * Sleeps for sleepTime duration
     */
    private void sleep(){
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

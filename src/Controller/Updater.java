package Controller;

import Other.Settings;
import StreamList.StreamList;
import View.View;

import javax.imageio.ImageIO;
import java.awt.*;

import static Model.Model.getStreams;

/**
 * Created by Matt on 2017-05-05.
 */
public class Updater implements Runnable {
    private View view;
    private Controller controller;
    private StreamList streams;
    private int sleepTime;
    private Thread update;
    private String gameFilter;
    private boolean pauseWorking;

    /**
     * Constructor that initializes the StreamUpdate thread and gets information pertinent to running the thread
     * such as sleep time, game filters, etc. Also creates and starts a new thread that is then ran indefinitely
     * @param controller Controller object
     * @param view Main GUI View
     */
    public Updater(Controller controller, View view) {
        this.view = view;
        this.streams = getStreams();
        this.sleepTime = Settings.getSleepTime();
        this.gameFilter = Settings.getGameFilter();
        this.controller = controller;
        pauseWorking = false;
        update = new Thread(this);
        update.start();
    }

    @Override
    /**
     * Begins the thread and executes the StreamUpdate thread indefinitely, only sleeping between each
     * update for a specified amount of time.
     */
    public void run() {

        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image img = ImageIO.read(getClass().getClassLoader().getResource("resources/Twitch.png"));
            TrayIcon icon = new TrayIcon(img, "Twitch Follower");
            icon.setImageAutoSize(true);
            tray.add(icon);
            sleep();

            while (true) {
                synchronized (this) {
                    while (pauseWorking) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                    sleepTime = Settings.getSleepTime();
                    try {

                        StreamUpdate streamUpdate = new StreamUpdate(controller, view, icon);
                        streamUpdate.execute();

                        while (!streamUpdate.isDone()) {
                            wait(1000);
                        }
                        sleep();

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {

        }


    }

    /**
     * Pauses the thread
     */
    public void hibernate() {
        pauseWorking = true;
    }

    /**
     * Resumes the thread
     */
    public void wake() {
        pauseWorking = false;
        synchronized (this) {

            this.notifyAll();
        }
    }

    private synchronized void sleep() {
        try {
            System.out.println("Sleeping for: " + sleepTime);
            wait(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
        }
    }
}

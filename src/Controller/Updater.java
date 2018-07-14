package Controller;

import Model.Model;
import Other.Settings;
import StreamList.StreamList;
import View.View;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Created by Matt on 2017-05-05.
 */
public class Updater implements Runnable {
    private Model model;
    private View view;
    private Controller controller;
    private StreamList streams;
    private int sleepTime;
    private Thread update;
    private String gameFilter;
    private boolean pauseWorking;

    public Updater(Controller controller, Model model, View view, StreamList streams, int sleepTime, String gameFilter) {
        this.model = model;
        this.view = view;
        this.streams = streams;
        this.sleepTime = sleepTime;
        this.gameFilter = gameFilter;
        this.controller = controller;
        pauseWorking = false;
        update = new Thread(this);
        update.start();
    }

    @Override
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

                        StreamUpdate streamUpdate = new StreamUpdate(controller, model, view, streams, icon, gameFilter);
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
            System.out.println("Sleeping for: " + sleepTime);
            wait(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
        }
    }
}

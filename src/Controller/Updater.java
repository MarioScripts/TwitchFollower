package Controller;

import StreamList.StreamList;
import Model.*;
import View.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.rmi.server.ExportException;

/**
 * Created by Matt on 2017-05-05.
 */
public class Updater implements Runnable {
    private Model model;
    private View view;
    private StreamList streams;
    private int sleepTime;
    private Thread update;
    private boolean pauseWorking;

    public Updater(Model model, View view, StreamList streams, int sleepTime){
        this.model = model;
        this.view = view;
        this.streams = streams;
        this.sleepTime = sleepTime;
        pauseWorking = false;
        update = new Thread(this);
        update.start();
    }
    @Override
    public void run() {

        try{
            SystemTray tray = SystemTray.getSystemTray();
            Image img = ImageIO.read(getClass().getClassLoader().getResource("resources/Twitch.png"));
            TrayIcon icon = new TrayIcon(img, "Twitch Follower");
            icon.setImageAutoSize(true);
            tray.add(icon);
            sleep();

            while(true){
                synchronized (this){
                    while(pauseWorking){
                        try{
                            this.wait();
                        }catch (InterruptedException e){

                        }
                    }

                    try {

                        StreamUpdate streamUpdate = new StreamUpdate(model, view, streams, icon);
                        streamUpdate.execute();

                        while(!streamUpdate.isDone()){
                            wait(1000);
                        }
                        wait(sleepTime);

                    }catch(Exception e){

                    }
                }
            }
        }catch (Exception e){

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
            wait(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
        }
    }
}

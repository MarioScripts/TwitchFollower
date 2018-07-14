import Controller.Controller;
import Model.Model;
import View.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Simple main method. Creates and sets up MVC layout
 * using Model, View and Controller objects
 */
public class TwitchFollower {
    public static void main(String[] args) {
        //TODO: Look into saving settings in enum
        //TODO: Look into only showing certain games
        // States start time for debugging purposes
        System.out.println("Started on: " + new SimpleDateFormat("MM/dd/YYYY hh:mm a").format(Calendar.getInstance().getTime()));

        View view = new View();
        Model model = new Model();


        Controller controller = new Controller(model, view);
    }
}

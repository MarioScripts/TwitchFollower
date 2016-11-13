import Controller.Controller;
import Model.Model;
import View.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TwitchFollower {
    public static void main(String[] args) {
        /*
        TODO IMMEDIATELY:
        TODO: Look into stream initialization, try using a dowhile loop in StreamUpdate so you don't need a redundant startup method
        TODO: Add channel image fetch (just change getting streamInfo method and add corresponding StreamNode requirements (image attribute and getters/setters)
        TODO: Maybe control duplicate entries? Just create a contains() method in streamList that sees if the current name is already in the stream list
         */

        /*
        TODO AFTER CORE FEATURES:
        TODO: Java doc comment all methods
        TODO: Look into manipulating GUI components without freezing GUI (maybe keep track of all labels and only update text instead of redrawing every component)
        TODO: Tidy up code, fix indenting, spacing, naming, etc
        TODO: Incorporate a settings gui and cfg file to store the settings. Settings could include:
                    - Enable/disable notifications
                    - Show only online channels
                    - Show channels only playing a specific game
                    - Show channels playing any game except a specific game
                    - Allow the user to choose their livestreamer.exe directory
                    - Allow user to choose their cfg/names directory
         */
        System.out.println("Started on: " + new SimpleDateFormat("MM/dd/YYYY hh:mm a").format(Calendar.getInstance().getTime()));
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(model, view);
    }
}

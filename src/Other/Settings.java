package Other;

/**
 * Created by Matt on 2017-05-05.
 */
public class Settings {
    private static boolean gameNotify = true, statusNotify = true, showOffline = true, showVodast = true, darkMode = false;
    private static int sleepTime = 30000;
    private static String gameFilter = "None";

    //Getters
    public static boolean getGameNotify(){
        return gameNotify;
    }

    public static boolean getStatusNotify(){
        return statusNotify;
    }

    public static boolean getShowOffline(){
        return showOffline;
    }

    public static int getSleepTime(){
        return sleepTime;
    }

    public static boolean getShowVodcast(){
        return showVodast;
    }

    public static boolean getDarkMode(){ return darkMode; }

    public static String getGameFilter(){
        return gameFilter;
    }

    //Setters
    public static void setGameNotify(boolean notify){
        gameNotify = notify;
    }

    public static void setStatusNotify(boolean notify){
        statusNotify = notify;
    }

    public static void setShowOffline(boolean show){
        showOffline = show;
    }

    public static void setShowVodast(boolean show){
        showVodast = show;
    }

    public static void setSleepTime(int sleep){
        sleepTime = sleep;
    }

    public static void setDarkMode(boolean dark){ darkMode = dark; }

    public static void setGameFilter(String filter){
        gameFilter = filter;
    }

}

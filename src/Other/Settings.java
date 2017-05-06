package Other;

/**
 * Created by Matt on 2017-05-05.
 */
public class Settings {
    private static boolean gameNotify = true, statusNotify = true, showOffline = true;

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
}

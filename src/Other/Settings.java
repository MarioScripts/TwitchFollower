package Other;

import java.awt.*;

/**
 * Created by Matt on 2017-05-05.
 */
public class Settings {
    private static boolean gameNotify = true, statusNotify = true, showOffline = true, showVodast = true, darkMode = true;
    private static int sleepTime = 30000;
    private static String gameFilter = "";
    private static Dimension size = new Dimension(400, 500);
    private static Point loc = new Point(0, 0);
    // Temp: 0 = viewsort, 1 = descending name sort, 2 = descending game sort
    private static int sort = 0;
    private static final double version = 3.1;

    //Getters
    public static boolean getGameNotify() {
        return gameNotify;
    }

    public static boolean getStatusNotify() {
        return statusNotify;
    }

    public static boolean getShowOffline() {
        return showOffline;
    }

    public static int getSleepTime() {
        return sleepTime;
    }

    public static Dimension getSize(){
        return size;
    }

    public static Point getLoc(){
        return loc;
    }

    public static boolean getShowVodcast() {
        return showVodast;
    }

    public static boolean getDarkMode() {
        return darkMode;
    }

    public static String getGameFilter() {
        return gameFilter;
    }

    public static double getVersion(){ return version; }

    public static int getSort(){
        return sort;
    }

    //Setters
    public static void setGameNotify(boolean notify) {
        gameNotify = notify;
    }

    public static void setStatusNotify(boolean notify) {
        statusNotify = notify;
    }

    public static void setShowOffline(boolean show) {
        showOffline = show;
    }

    public static void setSleepTime(int sleep) {
        sleepTime = sleep;
    }

    public static void setLoc(Point l){
        loc = l;
    }

    public static void setSize(Dimension s){
        size = s;
    }

    public static void setDarkMode(boolean dark) {
        darkMode = dark;
    }

    public static void setGameFilter(String filter) {
        gameFilter = filter;
    }

    public static void setShowVodast(boolean show) {
        showVodast = show;
    }

    public static void setSort(int s){
        sort = s;
    }



}

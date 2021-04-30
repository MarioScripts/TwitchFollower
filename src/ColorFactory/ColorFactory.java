package ColorFactory;

import Other.Colors;
import Other.Settings;
import StreamList.StreamNode;

import javax.swing.*;
import java.awt.*;

/**
 * Factory that handles all dynamic color settings for the UI
 */
public class ColorFactory {

    /**
     * Determines the background color required based on Dark Mode setting
     * @return returns correct background color as Color
     */
    public static Color getBackground() {
        if (Settings.getDarkMode()) {
            return new Color(39, 39, 39);
        } else {
            return Color.white;
        }
    }

    /**
     * Determines the foreground color required based on Dark Mode setting
     * @return returns correct foreground color as Color
     */
    public static Color getForeground(){
        if(Settings.getDarkMode()){
            return Colors.SELECT_FOREGROUND_COLOR;
        }else{
            return Colors.TWITCH_PURPLE;
        }
    }

    /**
     * Determines the loading image required based on Dark Mode setting
     * @return returns correct loading image color as ImageIcon
     */
    public static ImageIcon getLoadingImage() {
        if (Settings.getDarkMode()) {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/loading_dark.gif"));
        } else {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/loading.gif"));
        }
    }

    /**
     * Determines the header image required based on Dark Mode setting
     * @return returns correct header image color as ImageIcon
     */
    public static ImageIcon getHeaderImage() {
        if (Settings.getDarkMode()) {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/Header_dark.png"));
        } else {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/Header.png"));
        }
    }

    /**
     * Determines the embedded search image required based on Dark Mode setting
     * @return returns correct embedded search image color as ImageIcon
     */
    public static ImageIcon getSearchEmbedImage(){
        if(Settings.getDarkMode()){
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/search_embed_dark.png"));
        }else{
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/search_embed_light.png"));
        }
    }

    /**
     * Determines the color required for the status based on the provided stream's online status
     * @param node Stream to check availability of
     * @return returns correct status color for stream as Color
     */
    public static Color getStatusColor(StreamNode node){
        if (node.getStatus().equals("Online")) {
            if (node.getVodcast()) {
                return Colors.VODCAST_COLOR;
            } else {
                return Colors.ONLINE_COLOR;
            }
        }

        return Colors.OFFLINE_COLOR;
    }

}

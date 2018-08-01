package ColorFactory;

import Other.Colors;
import Other.Settings;
import StreamList.StreamNode;

import javax.swing.*;
import java.awt.*;

public class ColorFactory {
    public static Color getBackground() {
        if (Settings.getDarkMode()) {
            return new Color(39, 39, 39);
        } else {
            return Color.white;
        }
    }

    public static ImageIcon getLoadingImage() {
        if (Settings.getDarkMode()) {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/loading_dark.gif"));
        } else {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/loading.gif"));
        }
    }

    public static ImageIcon getHeaderImage() {
        if (Settings.getDarkMode()) {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/Header_dark.png"));
        } else {
            return new ImageIcon(ColorFactory.class.getClassLoader().getResource("resources/Header.png"));
        }
    }

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

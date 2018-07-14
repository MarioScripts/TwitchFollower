package ColorFactory;

import Other.Settings;

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

}

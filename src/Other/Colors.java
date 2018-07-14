package Other;

import ColorFactory.ColorFactory;

import java.awt.*;

public class Colors {
    public static final Color TWITCH_PURPLE = new Color(100, 65, 164);
    public static final Color SELECT_FOREGROUND_COLOR = Color.white;
    public static final Color SELECT_BACKGROUND_COLOR = new Color(123, 90, 204);
    public static final Color BACKGROUND_LABEL_COLOR = TWITCH_PURPLE;
    public static final Color HOVER_COLOR = new Color(0xC3A9FF);
    public static Color BACKGROUND_COLOR = ColorFactory.getBackground();
}

package View;

import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Splash extends JFrame {
    public Splash() {
        this.setLayout(new MigLayout("insets 0 0, gap 0, w 200, h 200, flowy"));
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);

        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("resources/Twitch.png"));
            setIconImage(img);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource("resources/okk.gif"));
        JLabel label = new JLabel();
        label.setIcon(icon);

        JLabel infoPanel = new JLabel("<html><b>v2.5</html>", SwingConstants.CENTER);
        infoPanel.setForeground(Color.white);
        infoPanel.setOpaque(true);
        infoPanel.setBackground(new Color(100, 65, 164));

        this.setVisible(true);
        this.add(infoPanel, "dock south, h 50, w 200, wrap");
        this.add(label, "wrap");
        this.add(infoPanel, "dock south, h 50, w 200, wrap");
        this.pack();
        this.setLocationRelativeTo(null);

        this.setSize(200, 220);
    }
}

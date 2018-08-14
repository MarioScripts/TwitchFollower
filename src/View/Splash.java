package View;

import Other.Colors;
import Other.Settings;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Splash extends JFrame {
    private JLabel lblHeader, lblInfo, lblStatus;
    public Splash() {
        this.setLayout(new MigLayout("insets 0 0, gap 0, w 200, h 200, flowy"));
        this.setMaximumSize(new Dimension(200, 200));
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
        lblHeader = new JLabel("", SwingConstants.CENTER);
        lblHeader.setIcon(icon);

        lblInfo = new JLabel("<html><b>" + Settings.getVersion() + "</html>", SwingConstants.CENTER);
        lblInfo.setForeground(Color.white);
        lblInfo.setOpaque(true);
        lblInfo.setBackground(new Color(100, 65, 164));

        lblStatus = new JLabel("Loading...", SwingConstants.CENTER);
        lblStatus.setForeground(Colors.TWITCH_PURPLE);

        this.setVisible(true);
        this.add(lblHeader, "align center, push, grow, gaptop 20");
        this.add(lblStatus, "push, grow,  gapbottom 25");
        this.add(lblInfo, "dock south, h 20, pushx, growx");

        this.pack();
        this.setLocationRelativeTo(null);

        this.setSize(200, 220);
    }

    public void setStatusText(String status){
        lblStatus.setText(status);
    }
}

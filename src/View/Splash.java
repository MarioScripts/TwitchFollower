package View;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class Splash extends JFrame{
    public Splash(){
        this.setLayout(new MigLayout("insets 0 0, gap 0, w 200, h 200, flowy"));
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
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

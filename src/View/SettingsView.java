package View;

import ColorFactory.ColorFactory;
import Controller.Controller;
import Controller.Updater;
import Listeners.SelectListener;
import Model.Model;
import Other.Colors;
import Other.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

/**
 * Created by Matt on 2017-05-04.
 */
public class SettingsView extends JFrame {
    //TODO: Update settings view to match main view aesthetic

    private Updater updateThread;
    private Model model;
    private Controller controller;
    private JCheckBox chkGameNotify, chkStatusNotify, chkShowOffline, chkShowVodcast, chkDarkMode;
    private JSlider slrSleep;
    private JTextField txtUser;
    private JButton btnOk, btnCancel, btnImport;
    private JPanel pnlNotify, pnlFollows, pnlSleep, pnlRight;
    private JLabel lblNotifications, lblImportFollowers, lblSleepTime, lblImportButton;

    public SettingsView(Updater updateThread, Model model, Controller controller) {
        this.updateThread = updateThread;
        this.model = model;
        this.controller = controller;
//        addWindowListener(new OnCloseListener());
        setLookAndFeel();
        setName("SettingsView");
        setTitle("Settings");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(417, 250));
        setResizable(true);
        setVisible(true);
        setLayout(new MigLayout("insets 0, gap 0 0, wrap 2, grow, push, flowx"));

        initComponents();
    }

    public SettingsView(){
        setLookAndFeel();
        setName("SettingsView");
        setTitle("Settings");
        this.getContentPane().setBackground(ColorFactory.getBackground());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(417, 250));
        setResizable(true);
        setVisible(true);
        setLayout(new MigLayout("insets 0, gap 0 0, wrap 2"));

        repaint();
        revalidate();
        initComponents();
    }

    private void initComponents() {
        lblNotifications = new JLabel("Notifications");
        lblNotifications.setForeground(Colors.TWITCH_PURPLE);
        lblNotifications.setFont(lblNotifications.getFont().deriveFont(Font.BOLD, 13));

        lblImportFollowers = new JLabel("Import followers");
        lblImportFollowers.setForeground(Colors.TWITCH_PURPLE);
        lblImportFollowers.setFont(lblImportFollowers.getFont().deriveFont(Font.BOLD, 13));

        lblImportButton = new JLabel();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("resources/import.png")).getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH));
        lblImportButton.setIcon(imageIcon);

        lblSleepTime = new JLabel("Sleep time");
        lblSleepTime.setForeground(Colors.TWITCH_PURPLE);
        lblSleepTime.setFont(lblSleepTime.getFont().deriveFont(Font.BOLD, 13));

        chkGameNotify = new JCheckBox("Game Updates");
        chkGameNotify.setSelected(Settings.getGameNotify());
        chkGameNotify.setOpaque(false);
        chkGameNotify.setFocusable(false);
        chkGameNotify.setForeground(ColorFactory.getForeground());
//        chkGameNotify.setBounds(10, 30, 100, 20);

        chkStatusNotify = new JCheckBox("Status Updates");
        chkStatusNotify.setSelected(Settings.getStatusNotify());
        chkStatusNotify.setOpaque(false);
        chkStatusNotify.setFocusable(false);
        chkStatusNotify.setForeground(ColorFactory.getForeground());
//        chkStatusNotify.setBounds(10, 50, 100, 20);

        chkShowOffline = new JCheckBox("Show Offline Channels");
        chkShowOffline.setSelected(Settings.getShowOffline());
        chkShowOffline.setOpaque(false);
        chkShowOffline.setFocusable(false);
        chkShowOffline.setForeground(ColorFactory.getForeground());
//        chkShowOffline.setBounds(10, 70, 150, 20);

        chkShowVodcast = new JCheckBox("Show Vodcasting Channels");
        chkShowVodcast.setSelected(Settings.getShowVodcast());
        chkShowVodcast.setOpaque(false);
        chkShowVodcast.setFocusable(false);
        chkShowVodcast.setForeground(ColorFactory.getForeground());
//        chkShowVodcast.setBounds(10, 90, 150, 20);

        chkDarkMode = new JCheckBox("Dark Mode");
        chkDarkMode.setSelected(Settings.getDarkMode());
        chkDarkMode.setOpaque(false);
        chkDarkMode.setFocusable(false);
        chkDarkMode.setForeground(ColorFactory.getForeground());
//        chkDarkMode.setBounds(10, 110, 150, 20);

        btnOk = new JButton("Ok");
        btnOk.setFocusable(false);
//        btnOk.setBounds(100, 190, 100, 25);
//        btnOk.addActionListener(new SaveSettingsListener());

        btnCancel = new JButton("Cancel");
        btnCancel.setFocusable(false);
//        btnCancel.setBounds(210, 190, 100, 25);
//        btnCancel.addActionListener(new CancelSettingsListener());

        btnImport = new JButton("Import");
//        btnImport.setBounds(50, 60, 100, 25);
        btnImport.setEnabled(false);
//        btnImport.addActionListener(new ImportFollowersListener());

        txtUser = new JTextField("Twitch name");
//        txtUser.setBounds(10, 30, 180, 20);
//        txtUser.addKeyListener(new FollowersTextListener());
//        txtUser.addMouseListener(new FollowersMouseListener());

        //TODO: Implement custom UI for custom jslider knob color
        slrSleep = new JSlider();
        slrSleep.setMaximum(180);
        slrSleep.setMinimum(30);
        slrSleep.setMajorTickSpacing(30);
        slrSleep.setMinorTickSpacing(15);
        slrSleep.setPaintLabels(true);
        slrSleep.setPaintTicks(true);
        slrSleep.setPaintTrack(true);
        slrSleep.setSnapToTicks(true);
        slrSleep.setValue(Settings.getSleepTime() / 1000);
        slrSleep.setOpaque(false);
        slrSleep.setFocusable(false);
        slrSleep.setForeground(ColorFactory.getForeground());
//        slrSleep.setBounds(210, 115, 200, 40);


        pnlNotify = new JPanel();
        pnlNotify.setLayout(new MigLayout("insets 0, gap 0 0, flowx, wrap 1"));
//        pnlNotify.setBorder(BorderFactory.createTitledBorder("Notifications"));
//        pnlNotify.setBackground(Color.red);
        pnlNotify.setOpaque(false);
        pnlNotify.add(lblNotifications, "gapleft 5, grow, push");
        pnlNotify.add(chkGameNotify, "gapleft 20, grow, push");
        pnlNotify.add(chkStatusNotify, "gapleft 20, grow, push");
        pnlNotify.add(chkShowOffline, "gapleft 20, grow, push");
        pnlNotify.add(chkShowVodcast, "gapleft 20, grow, push");
        pnlNotify.add(chkDarkMode, "gapleft 20, grow, push");

//        pnlNotify.setBounds(0, 5, 200, 180);

        pnlFollows = new JPanel();
        pnlFollows.setLayout(new MigLayout("insets 0, gap 0 0, flowx, wrap 2"));
//        pnlFollows.setBackground(Color.red);
        pnlFollows.setOpaque(false);
        pnlFollows.add(lblImportFollowers, "growx, pushx, wrap");
        pnlFollows.add(txtUser, "align center, gaptop 10, gapright 5, gapleft 20, growx, pushx");
        pnlFollows.add(lblImportButton, "gaptop 9, gapright 20");

        pnlSleep = new JPanel();
        pnlSleep.setLayout(new MigLayout("insets 0, gap 0 0, flowx"));
        pnlSleep.setOpaque(false);
        pnlSleep.add(lblSleepTime, "growx, pushx, wrap");
        pnlSleep.add(slrSleep, "gapleft 20, gapright 5, growx, pushx, gaptop 10");

        pnlRight = new JPanel();
        pnlRight.setLayout(new MigLayout("insets 0, gap 0 0, flowy"));
        pnlRight.setOpaque(false);
        pnlRight.add(pnlFollows, "growx, pushx");
        pnlRight.add(pnlSleep, "growx, pushx, gaptop 20");

        add(pnlNotify, "grow 40, push 40");
        add(pnlRight, "gapleft 10, grow 60, push 60");
//        add(slrSleep);
        add(btnOk);
        add(btnCancel);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e.getMessage());
        }
    }

//    private class SaveSettingsListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
////            model.updateSettings(chkGameNotify.isSelected(), chkStatusNotify.isSelected(), chkShowOffline.isSelected(), chkShowVodcast.isSelected(), chkDarkMode.isSelected(), slrSleep.getValue()*1000);
//            Settings.setGameNotify(chkGameNotify.isSelected());
//            Settings.setStatusNotify(chkStatusNotify.isSelected());
//            Settings.setShowOffline(chkShowOffline.isSelected());
//            Settings.setShowVodast(chkShowVodcast.isSelected());
//            Settings.setSleepTime(slrSleep.getValue() * 1000);
//            Settings.setDarkMode(chkDarkMode.isSelected());
//            model.updateSettings();
//            controller.refreshGUIStreams();
//            SettingsView.super.dispose();
//        }
//    }

//    private class CancelSettingsListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            SettingsView.super.dispose();
//        }
//    }
//
//    private class OnCloseListener implements WindowListener {
//        @Override
//        public void windowOpened(WindowEvent e) {
//            updateThread.hibernate();
//        }
//
//        @Override
//        public void windowClosing(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowClosed(WindowEvent e) {
//        }
//
//        @Override
//        public void windowIconified(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowDeiconified(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowActivated(WindowEvent e) {
//
//        }
//
//        @Override
//        public void windowDeactivated(WindowEvent e) {
//            updateThread.wake();
//        }
//    }
//
//    private class ImportFollowersListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String name = txtUser.getText().trim();
//            model.importUserFollowers(name);
//            controller.initGUIStreams();
//            txtUser.setText("");
//            btnImport.setEnabled(false);
//        }
//    }
//
//    private class FollowersMouseListener implements MouseListener {
//        @Override
//        public void mouseClicked(MouseEvent e) {
////            txtUser.setText("");
//            txtUser.selectAll();
//        }
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e) {
//
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e) {
//
//        }
//    }
//
//    private class FollowersTextListener implements KeyListener {
//        @Override
//        public void keyTyped(KeyEvent e) {
//        }
//
//        @Override
//        public void keyPressed(KeyEvent e) {
//
//        }
//
//        @Override
//        public void keyReleased(KeyEvent e) {
//            if (txtUser.getText().length() > 0) {
//                btnImport.setEnabled(true);
//            } else {
//                btnImport.setEnabled(false);
//            }
//        }
//    }

    public static void main(String[] args) {
        SettingsView view = new SettingsView();
        view.setVisible(true);
    }
}

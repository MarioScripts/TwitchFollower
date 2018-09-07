package View;

import ColorFactory.ColorFactory;
import Controller.Controller;
import Controller.Updater;
import Exceptions.UserNotFoundException;
import Listeners.MoveListener;
import Model.Model;
import Other.Colors;
import Other.Settings;
import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import Controller.ImportProgressThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Other.Colors.TWITCH_PURPLE;

/**
 * Created by Matt on 2017-05-04.
 */
public class SettingsView extends JFrame {

    private Updater updateThread;
    private Model model;
    private Controller controller;
    private JCheckBox chkGameNotify, chkStatusNotify, chkShowOffline, chkShowVodcast, chkDarkMode;
    private JRadioButton rdView, rdName, rdGame;
    private JSlider slrSleep;
    private JTextField txtUser;
    private JPanel pnlGeneral, pnlFollows, pnlSleep, pnlTitle, pnlProgress, pnlImport, pnlSort;
    private JLabel lblGeneral, lblImportFollowers, lblSleepTime, lblImportButton, lblSort, lblExit, lblSave;
    private JSeparator separatorGeneral, separatorFollows, separatorSleep, separatorSort;
    private JProgressBar prgImport;
    private ButtonGroup buttonGroup;
    private static SettingsView instance = null;

    public static SettingsView getInstance(Updater updateThread, Model model, Controller controller){
        if(instance == null){
            instance = new SettingsView(updateThread, model, controller);
        }

        return instance;
    }

    public SettingsView(Updater updateThread, Model model, Controller controller) {
        this.updateThread = updateThread;
        this.model = model;
        this.controller = controller;
        controller.pauseBackgroundWork();
        setLookAndFeel();
        setName("SettingsView");
        setTitle("Settings");
        this.getContentPane().setBackground(ColorFactory.getBackground());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(417, 485));
        setResizable(true);
        setUndecorated(true);
        setVisible(true);
        setLayout(new MigLayout("insets 0, gap 0 0, fill, flowx, wrap 2", "[grow 30][grow 70]"));

        repaint();
        revalidate();
        initComponents();
    }

    private void initComponents() {
        lblGeneral = new JLabel("General", SwingUtilities.CENTER);
        lblGeneral.setForeground(Colors.TWITCH_PURPLE);
        lblGeneral.setFont(lblGeneral.getFont().deriveFont(Font.BOLD, 15));

        lblImportFollowers = new JLabel("Import followers", SwingUtilities.CENTER);
        lblImportFollowers.setForeground(Colors.TWITCH_PURPLE);
        lblImportFollowers.setFont(lblImportFollowers.getFont().deriveFont(Font.BOLD, 15));

        lblImportButton = new JLabel();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("resources/import.png")).getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH));
        lblImportButton.setIcon(imageIcon);
        lblImportButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblSleepTime = new JLabel("Sleep time", SwingUtilities.CENTER);
        lblSleepTime.setForeground(Colors.TWITCH_PURPLE);
        lblSleepTime.setFont(lblSleepTime.getFont().deriveFont(Font.BOLD, 15));

        lblSort = new JLabel("Sort type", SwingUtilities.CENTER);
        lblSort.setForeground(Colors.TWITCH_PURPLE);
        lblSort.setFont(lblSleepTime.getFont().deriveFont(Font.BOLD, 15));

        lblExit = new JLabel("x");
        lblExit.setForeground(TWITCH_PURPLE);
        lblExit.setFont(lblExit.getFont().deriveFont(Font.BOLD, 17f));
        lblExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblSave = new JLabel("Save", SwingUtilities.CENTER);
        lblSave.setFont(lblSave.getFont().deriveFont(Font.BOLD, 15));
        lblSave.setForeground(ColorFactory.getBackground());
        lblSave.setOpaque(true);
        lblSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSave.addMouseListener(new SaveSettingsListener());
        lblSave.setBackground(Colors.TWITCH_PURPLE);

        separatorGeneral = new JSeparator(SwingConstants.HORIZONTAL);
        separatorGeneral.setForeground(Colors.TWITCH_PURPLE);

        separatorFollows = new JSeparator(SwingConstants.HORIZONTAL);
        separatorFollows.setForeground(Colors.TWITCH_PURPLE);

        separatorSleep = new JSeparator(SwingConstants.HORIZONTAL);
        separatorSleep.setForeground(Colors.TWITCH_PURPLE);

        separatorSort = new JSeparator(SwingConstants.HORIZONTAL);
        separatorSort.setForeground(Colors.TWITCH_PURPLE);

        prgImport = new JProgressBar();
        prgImport.setStringPainted(true);
        prgImport.setForeground(Colors.TWITCH_PURPLE);
        prgImport.setBackground(ColorFactory.getBackground());

        chkGameNotify = new JCheckBox("Game Updates");
        chkGameNotify.setSelected(Settings.getGameNotify());
        chkGameNotify.setOpaque(false);
        chkGameNotify.setFocusable(false);
        chkGameNotify.setForeground(ColorFactory.getForeground());

        chkStatusNotify = new JCheckBox("Status Updates");
        chkStatusNotify.setSelected(Settings.getStatusNotify());
        chkStatusNotify.setOpaque(false);
        chkStatusNotify.setFocusable(false);
        chkStatusNotify.setForeground(ColorFactory.getForeground());

        chkShowOffline = new JCheckBox("Show Offline Channels");
        chkShowOffline.setSelected(Settings.getShowOffline());
        chkShowOffline.setOpaque(false);
        chkShowOffline.setFocusable(false);
        chkShowOffline.setForeground(ColorFactory.getForeground());

        chkShowVodcast = new JCheckBox("Show Vodcasting Channels");
        chkShowVodcast.setSelected(Settings.getShowVodcast());
        chkShowVodcast.setOpaque(false);
        chkShowVodcast.setFocusable(false);
        chkShowVodcast.setForeground(ColorFactory.getForeground());

        chkDarkMode = new JCheckBox("Dark Mode");
        chkDarkMode.setSelected(Settings.getDarkMode());
        chkDarkMode.setOpaque(false);
        chkDarkMode.setFocusable(false);
        chkDarkMode.setForeground(ColorFactory.getForeground());

        rdView = new JRadioButton("View Sort");
        rdView.setSelected(Settings.getSort() == 0);
        rdView.setOpaque(false);
        rdView.setFocusable(false);
        rdView.setForeground(ColorFactory.getForeground());

        rdName = new JRadioButton("Name Sort");
        rdName.setSelected(Settings.getSort() == 1);
        rdName.setOpaque(false);
        rdName.setFocusable(false);
        rdName.setForeground(ColorFactory.getForeground());

        rdGame = new JRadioButton("Game Sort");
        rdGame.setSelected(Settings.getSort() == 2);
        rdGame.setOpaque(false);
        rdGame.setFocusable(false);
        rdGame.setForeground(ColorFactory.getForeground());

        buttonGroup = new ButtonGroup();
        buttonGroup.add(rdView);
        buttonGroup.add(rdName);
        buttonGroup.add(rdGame);


        txtUser = new JTextField("Twitch name");
        txtUser.addMouseListener(new FollowersMouseListener());

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

        pnlProgress = new JPanel();
        pnlProgress.setLayout(new MigLayout("flowx, insets 0 0"));
        pnlProgress.add(prgImport, "growx, pushx, gapleft 25, gapright 25, h 25");
        pnlProgress.setOpaque(false);

        pnlImport = new JPanel();
        pnlImport.setLayout(new MigLayout("insets 0, gap 0 0, flowx, wrap 2"));
        pnlImport.setOpaque(false);
        pnlImport.add(txtUser, "align center, gapright 5, gapleft 25, growx, pushx");
        pnlImport.add(lblImportButton, "gapright 20");

        pnlTitle = new JPanel();
        pnlTitle.setLayout((new MigLayout("flowx, insets 0 0")));
        pnlTitle.setOpaque(false);
        pnlTitle.add(lblExit, "pushx, w 10, align right, gapright 10");

        pnlGeneral = new JPanel();
        pnlGeneral.setLayout(new MigLayout("insets 0, gap 0 0, flowx, wrap 1"));
        pnlGeneral.setOpaque(false);
        pnlGeneral.add(lblGeneral, "gaptop 15, pushx, growx");
        pnlGeneral.add(separatorGeneral, "h 1, pushx, growx, gaptop 5, gapbottom 10, gapright 25, gapleft 25, wrap");
        pnlGeneral.add(chkGameNotify, "gapleft 25, pushx, growx");
        pnlGeneral.add(chkStatusNotify, "gapleft 25, pushx, growx");
        pnlGeneral.add(chkShowOffline, "gapleft 25, pushx, growx");
        pnlGeneral.add(chkShowVodcast, "gapleft 25, pushx, growx");
        pnlGeneral.add(chkDarkMode, "gapleft 25, pushx, growx");

        pnlSort = new JPanel();
        pnlSort.setLayout(new MigLayout("insets 0, gap 0 0, flowx, wrap 1"));
        pnlSort.setOpaque(false);
        pnlSort.add(lblSort, "gaptop 15, pushx, growx");
        pnlSort.add(separatorSort, "h 1, pushx, growx, gaptop 5, gapbottom 10, gapright 25, gapleft 25, wrap");
        pnlSort.add(rdView, "gapleft 25, pushx, growx");
        pnlSort.add(rdName, "gapleft 25, pushx, growx");
        pnlSort.add(rdGame, "gapleft 25, pushx, growx");

        pnlFollows = new JPanel();
        pnlFollows.setLayout(new MigLayout("insets 0, gap 0 0, flowy"));
        pnlFollows.setOpaque(false);
        pnlFollows.add(lblImportFollowers, "gaptop 15, growx, pushx");
        pnlFollows.add(separatorFollows, "h 1, growx, pushx, gaptop 5, gapbottom 10, gapright 25, gapleft 25");
        pnlFollows.add(pnlImport, "grow, push");

        pnlSleep = new JPanel();
        pnlSleep.setLayout(new MigLayout("insets 0, gap 0 0, flowx"));
        pnlSleep.setOpaque(false);
        pnlSleep.add(lblSleepTime, "gaptop 15, growx, pushx, wrap");
        pnlSleep.add(separatorSleep, "h 1, growx, pushx, gaptop 5, gapbottom 10, gapright 25, gapleft 25, wrap");
        pnlSleep.add(slrSleep, "gapleft 25, gapright 25, growx, pushx");

        add(pnlTitle, "grow, push, h 20, span 2");
        add(pnlGeneral, "grow, push");
        add(pnlSort, "grow, push");
        add(pnlFollows, "grow, push, span 2");
        add(pnlSleep, "grow, push, span 2");
        add(lblSave, "growx, pushx, h 25, gaptop 40, span 2");

        MoveListener moveListener = new MoveListener(this);
        pnlTitle.addMouseListener(moveListener);
        pnlTitle.addMouseMotionListener(moveListener);
        lblExit.addMouseListener(new SettingsExitListener(this));
        lblImportButton.addMouseListener(new ImportFollowersListener(this, pnlFollows, pnlImport, pnlProgress, prgImport));
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e.getMessage());
        }
    }

    private class SettingsExitListener implements MouseListener {
        JFrame frame;

        public SettingsExitListener(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            instance = null;
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class SaveSettingsListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            Settings.setGameNotify(chkGameNotify.isSelected());
            Settings.setStatusNotify(chkStatusNotify.isSelected());
            Settings.setShowOffline(chkShowOffline.isSelected());
            Settings.setShowVodast(chkShowVodcast.isSelected());
            Settings.setSleepTime(slrSleep.getValue() * 1000);
            Settings.setDarkMode(chkDarkMode.isSelected());

            if(rdView.isSelected()){
                Settings.setSort(0);
            }else if(rdName.isSelected()){
                Settings.setSort(1);
            }else if(rdGame.isSelected()){
                Settings.setSort(2);
            }

            model.updateSettings();
            controller.refreshGUIStreams();
            controller.resumeBackgroundWork();
            instance = null;
            dispose();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


    private class ImportFollowersListener implements MouseListener {
        private JPanel outerPanel, mainPanel, otherPanel;
        private JFrame frame;
        private JProgressBar progressBar;
        public ImportFollowersListener(JFrame frame, JPanel outerPanel, JPanel mainPanel, JPanel otherPanel, JProgressBar progressBar){
            this.outerPanel = outerPanel;
            this.mainPanel = mainPanel;
            this.otherPanel = otherPanel;
            this.frame = frame;
            this.progressBar = progressBar;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            String username = txtUser.getText().trim();
            outerPanel.remove(mainPanel);
            outerPanel.add(otherPanel, "growx, pushx, h 20");
            try{
                JSONArray importArray = model.getImportedFollowers(username);
                progressBar.setMaximum(importArray.length());
                ImportProgressThread progressThread = new ImportProgressThread(model, controller, frame, progressBar, importArray, outerPanel, mainPanel, otherPanel, txtUser);
                try{
                    progressThread.execute();
                }catch(Exception ex1){
                    System.out.println(ex1.getMessage());
                }

            }catch(UserNotFoundException ex){
                txtUser.setText("");
                outerPanel.remove(otherPanel);
                outerPanel.add(mainPanel, "growx, pushx");
                frame.repaint();
                frame.revalidate();
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class FollowersMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
//            txtUser.setText("");
            txtUser.selectAll();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

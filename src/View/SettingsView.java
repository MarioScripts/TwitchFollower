package View;

import Controller.StreamUpdate;
import Exceptions.DuplicateStreamException;
import Other.Settings;
import View.*;
import Controller.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Model.*;

/**
 * Created by Matt on 2017-05-04.
 */
public class SettingsView extends JFrame {

    private Updater updateThread;
    private Model model;
    private Controller controller;
    private JCheckBox chkGameNotify, chkStatusNotify, chkShowOffline;
    private JTextField txtUser;
    private JButton btnOk, btnCancel, btnImport;
    private JPanel pnlNotify, pnlFollows;

    public SettingsView(Updater updateThread, Model model, Controller controller){
        this.updateThread = updateThread;
        this.model = model;
        this.controller = controller;
        addWindowListener(new OnCloseListener());
        setLookAndFeel();
        setName("SettingsView");
        setTitle("SettingsView");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(417, 200));
        setResizable(false);
        setVisible(true);
        setLayout(null);

        initComponents();
    }

    private void initComponents(){
        chkGameNotify = new JCheckBox("Game Updates");
        chkGameNotify.setSelected(Settings.getGameNotify());
        chkGameNotify.setBounds(10, 30, 100, 20);

        chkStatusNotify = new JCheckBox("Status Updates");
        chkStatusNotify.setSelected(Settings.getStatusNotify());
        chkStatusNotify.setBounds(10, 50, 100, 20);

        chkShowOffline = new JCheckBox("Show Offline Channels");
        chkShowOffline.setSelected(Settings.getShowOffline());
        chkShowOffline.setBounds(10, 70, 150, 20);

        btnOk = new JButton("Ok");
        btnOk.setFocusable(false);
        btnOk.setBounds(100, 130, 100, 30);
        btnOk.addActionListener(new SaveSettingsListener());

        btnCancel = new JButton("Cancel");
        btnCancel.setFocusable(false);
        btnCancel.setBounds(210, 130, 100, 30);
        btnCancel.addActionListener(new CancelSettingsListener());

        btnImport = new JButton("Import");
        btnImport.setBounds(50, 60, 100, 25);
        btnImport.setEnabled(false);
        btnImport.addActionListener(new ImportFollowersListener());

        txtUser = new JTextField("Twitch name");
        txtUser.setBounds(10, 30,180, 20);
        txtUser.addKeyListener(new FollowersTextListener());

        pnlNotify = new JPanel();
        pnlNotify.setBorder(BorderFactory.createTitledBorder("Notifications"));
        pnlNotify.add(chkGameNotify);
        pnlNotify.add(chkStatusNotify);
        pnlNotify.add(chkShowOffline);
        pnlNotify.setLayout(null);
        pnlNotify.setBounds(0, 5, 200, 100);

        pnlFollows = new JPanel();
        pnlFollows.setBorder(BorderFactory.createTitledBorder("Import Followers"));
        pnlFollows.setLayout(null);
        pnlFollows.setBounds(210, 5, 200, 100);
        pnlFollows.add(txtUser);
        pnlFollows.add(btnImport);

        add(pnlNotify);
        add(pnlFollows);
        add(btnOk);
        add(btnCancel);
    }

    private class SaveSettingsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            model.editSettings(chkGameNotify.isSelected(), chkStatusNotify.isSelected(), chkShowOffline.isSelected());
            Settings.setGameNotify(chkGameNotify.isSelected());
            Settings.setStatusNotify(chkStatusNotify.isSelected());
            Settings.setShowOffline(chkShowOffline.isSelected());
            controller.refreshGUIStreams();
            SettingsView.super.dispose();
        }
    }

    private class CancelSettingsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SettingsView.super.dispose();
        }
    }

    private class OnCloseListener implements WindowListener{
        @Override
        public void windowOpened(WindowEvent e) {
            updateThread.hibernate();
        }

        @Override
        public void windowClosing(WindowEvent e) {

        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            updateThread.wake();
        }
    }

    private class ImportFollowersListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = txtUser.getText().trim();
            model.importUserFollowers(name);
            controller.initGUIStreams();
            txtUser.setText("");
            btnImport.setEnabled(false);
        }
    }

    private class FollowersTextListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(txtUser.getText().length() > 0){
                btnImport.setEnabled(true);
            }else{
                btnImport.setEnabled(false);
            }
        }
    }

    private void setLookAndFeel(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e){
            System.out.println(e.getMessage());
        }
    }
}

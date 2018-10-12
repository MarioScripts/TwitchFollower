package Controller;

import Exceptions.DuplicateStreamException;
import StreamList.StreamNode;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

import static Model.Model.addStream;

/**
 * Update thread for importing streams
 */
public class ImportProgressThread extends SwingWorker<Boolean, Integer> {

    /**
     * Update thread
     */
    public Thread update;
    /**
     * List of streams
     */
    public boolean pauseWorking;

    private JFrame frame;
    private JProgressBar progressBar;
    private JSONArray userFollows;
    private Controller controller;
    private JPanel outerPanel, mainPanel, otherPanel;
    private JTextField txtUser;

    /**
     * Constructor that initializes Settings GUI panels for updating
     * @param controller Controller object
     * @param frame Frame of Settings GUI
     * @param progressBar Progressbar of Settings GUI
     * @param userFollows List of channels that the specified user follows
     * @param outerPanel Outer panel of Settings GUI
     * @param mainPanel Main panel of Settings GUI
     * @param otherPanel Other panel of Settings GUI
     * @param txtUser Specified user of Settings GUI
     */
    public ImportProgressThread(Controller controller, JFrame frame, JProgressBar progressBar, JSONArray userFollows,
                                JPanel outerPanel, JPanel mainPanel, JPanel otherPanel, JTextField txtUser) {
        this.frame = frame;
        this.progressBar = progressBar;
        this.userFollows = userFollows;
        this.controller = controller;
        this.outerPanel = outerPanel;
        this.mainPanel = mainPanel;
        this.otherPanel = otherPanel;
        this.txtUser = txtUser;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        int i = 0;
        for (Object follow : userFollows) {

            JSONObject jsonObj = (JSONObject) follow;
            jsonObj = jsonObj.getJSONObject("channel");
            String name = jsonObj.getString("name");
            StreamNode node = new StreamNode(name);
            try {
                addStream(node);
                controller.updateStream(node);
            } catch (DuplicateStreamException ex) {
                System.out.println("Stream already in list, skipping..");
            }

            i++;
            progressBar.setValue(i);
            frame.repaint();
            frame.revalidate();
        }
        done();
        return true;
    }

    protected void done() {
        txtUser.setText("Twitch name");
        outerPanel.remove(otherPanel);
        outerPanel.add(mainPanel, "growx, pushx");
        frame.validate();
        frame.repaint();
    }

    public void hibernate() {
        pauseWorking = true;
    }

    public void wake() {
        pauseWorking = false;
        synchronized (this) {

            this.notifyAll();
        }
    }

}

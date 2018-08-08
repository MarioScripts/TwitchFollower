package Controller;

import Exceptions.DuplicateStreamException;
import Model.Model;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import View.View;
import com.sun.org.apache.xml.internal.security.utils.JDKXPathAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * Update thread for all streams
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


    /**
     * Model object
     */
    private Model model;

    private JFrame frame;
    private JProgressBar progressBar;
    private JSONArray userFollows;
    private Controller controller;
    private JPanel outerPanel, mainPanel, otherPanel;
    private JTextField txtUser;

    public ImportProgressThread(Model model, Controller controller, JFrame frame, JProgressBar progressBar, JSONArray userFollows,
                                JPanel outerPanel, JPanel mainPanel, JPanel otherPanel, JTextField txtUser) {
        this.model = model;
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
                model.addStream(node);
                controller.initGUIStream(node);
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
        txtUser.setText("");
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

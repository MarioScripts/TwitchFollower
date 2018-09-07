package View;

import ColorFactory.ColorFactory;
import Listeners.*;
import Other.Settings;
import StreamList.StreamNode;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import net.miginfocom.swing.MigLayout;
import Other.Colors;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

import static Other.Colors.*;

/**
 * Handles all GUI interactions and elements
 */
public class View extends JFrame {


    public JTextField txtSearch;
    public JPanel pnlDisplay, pnlSettings, selected, pnlSearch, pnlTitle;
    private JButton btnAdd, btnRemove, btnSettings;
    private JLabel lblAdd, lblSetting, lblSearch, lblExit, lblMinimize;
    private JScrollPane scrDisplay;
    private JLabel header, lblSettings, lblDisplay;
    private JSeparator sepSettings, sepDisplay;
    private List topGames;
    private JList lstGames;
    private JScrollPane scrlGames;

    /**
     * Constructor, sets general GUI properties
     */
    public View() {

        setLookAndFeel();
        setName("Twitch Follower");
        setTitle("Twitch Follower");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setSize(new Dimension(400, 500));
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 0 0"));

        initComponents();
        setUndecorated(true);
    }

    /**
     * Initializes all GUI elements and sets their properties
     */
    private void initComponents() {
        pnlTitle = new JPanel();
        pnlTitle.setLayout((new MigLayout("wrap 2, flowx, insets 0 0")));
        pnlTitle.setOpaque(false);

        pnlDisplay = new JPanel();
        pnlDisplay.setLayout(new MigLayout("wrap 1, flowx, insets 10 10"));
        pnlDisplay.setAutoscrolls(true);
        pnlDisplay.setBackground(BACKGROUND_COLOR);

        pnlSettings = new JPanel();
        pnlSettings.setLayout(new MigLayout("insets 0 0 0 10, fill"));
        pnlSettings.setBackground(TWITCH_PURPLE);

        pnlSearch = new JPanel();
        pnlSearch.setLayout(new MigLayout("insets 0 0 0 0, wrap 2, fill", "[][grow]"));
        pnlSearch.setOpaque(false);

        scrDisplay = new JScrollPane(pnlDisplay);
        scrDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrDisplay.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrDisplay.setBorder(BorderFactory.createEmptyBorder());
        scrDisplay.getVerticalScrollBar().setUnitIncrement(5);
        scrDisplay.setOpaque(false);

        btnAdd = new JButton("+");
        btnAdd.setFocusable(false);
        btnAdd.setToolTipText("Add a stream to your list");

        btnRemove = new JButton("-");
        btnRemove.setFocusable(false);
        btnRemove.setToolTipText("Remove a stream from your list");

        btnSettings = new JButton("...");
        btnSettings.setFocusable(false);
        btnSettings.setToolTipText("Go to settings");

        lblExit = new JLabel("<html><b>x</html>");
        lblExit.setForeground(TWITCH_PURPLE);
        lblExit.setFont(lblExit.getFont().deriveFont(17f));
        lblExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblMinimize = new JLabel("<html><b>-</html>");
        lblMinimize.setForeground(TWITCH_PURPLE);
        lblMinimize.setFont(lblMinimize.getFont().deriveFont(17f));
        lblMinimize.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblAdd = new JLabel("<html><b>+</font></html>");
        lblAdd.setFont(lblAdd.getFont().deriveFont(17f));
        lblAdd.setBackground(BACKGROUND_COLOR);
        lblAdd.setForeground(BACKGROUND_COLOR);
        lblAdd.setToolTipText("Add a stream to your list");
        lblAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblSetting = new JLabel("<html><b>...</font></html>");
        lblSetting.setFont(lblSetting.getFont().deriveFont(17f));
        lblSetting.setForeground(BACKGROUND_COLOR);
        lblSetting.setToolTipText("Go to settings");
        lblSetting.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblDisplay = new JLabel("");
        lblDisplay.setForeground(BACKGROUND_LABEL_COLOR);

        lblSearch = new JLabel("");
        lblSearch.setBackground(BACKGROUND_COLOR);
        lblSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        txtSearch = new JTextField();
        txtSearch.setText(Settings.getGameFilter());
        txtSearch.setBorder(BorderFactory.createEmptyBorder(4, 2, 2, 0));
        txtSearch.setOpaque(true);
        txtSearch.setBackground(Colors.TWITCH_PURPLE);
        txtSearch.setFont(txtSearch.getFont().deriveFont(Font.BOLD, 13f));

        lstGames = new JList();
        lstGames.setBorder(new EmptyBorder(4,4,4,0));
        lstGames.setSelectionBackground(TWITCH_PURPLE);
        lstGames.setSelectionForeground(BACKGROUND_COLOR);
        lstGames.setBackground(BACKGROUND_COLOR);
        lstGames.setForeground(TWITCH_PURPLE);
        lstGames.setFocusable(false);
        lstGames.setFont(lstGames.getFont().deriveFont(Font.BOLD, 13f));

        scrlGames = new JScrollPane();
        scrlGames.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, TWITCH_PURPLE));
        scrlGames.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrlGames.setOpaque(false);

        try {
            header = new JLabel("", ColorFactory.getHeaderImage(), JLabel.LEFT);
            header.setToolTipText("Go to Twitch");

            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("resources/Twitch.png"));
            setIconImage(img);
            header.addMouseListener(new HeaderListener(this));

            ImageIcon imgicon = ColorFactory.getLoadingImage();
            lblDisplay.setIcon(imgicon);
            imgicon.setImageObserver(lblDisplay);

            imgicon = new ImageIcon(this.getClass().getClassLoader().getResource("resources/search.png"));
            lblSearch.setIcon(imgicon);
        } catch (IOException e) {
            System.out.println("Error reading GUI graphics");
        }

        sepSettings = new JSeparator(SwingConstants.HORIZONTAL);
        sepSettings.setForeground(BACKGROUND_COLOR);

        sepDisplay = new JSeparator(SwingConstants.HORIZONTAL);
        sepDisplay.setBackground(TWITCH_PURPLE);
        sepDisplay.setForeground(TWITCH_PURPLE);

        pnlSettings.add(lblAdd, "gapx 15");
        pnlSettings.add(lblDisplay, "gapx 30, pushx, align center");
        pnlSettings.add(lblSetting, "gapright 15, gapbottom 5");
        pnlSettings.setBackground(TWITCH_PURPLE);

        pnlSearch.add(lblSearch, "pushx, growx, h 10, gapleft 4, gaptop 2, gapbottom 1");

        pnlTitle.add(lblMinimize, "push, w 10, al right, gapright 10");
        pnlTitle.add(lblExit, "w 10, al right, gapright 10");

        add(pnlTitle, "growx, pushx, h 20, wrap");
        add(header, "wrap, gapy 15, align center");
        add(pnlSearch, "wrap, gapbottom 0, gapleft 10, gapright 10, gaptop 20, growx, pushx");
        add(sepDisplay, "gapy 0, pushx, growx, wrap, gapright 10, gapleft 10");
        add(scrDisplay, "pushx, pushy, grow, wrap");
        add(sepSettings, "h 1, growx, pushx, wrap");
        add(pnlSettings, "growx, pushx, h 3");

        MouseListener deselectListener = new DeselectListener(this);
        addMouseListener(deselectListener);
        pnlDisplay.addMouseListener(deselectListener);
        MoveListener moveListener = new MoveListener(this);
        pnlTitle.addMouseListener(moveListener);
        pnlTitle.addMouseMotionListener(moveListener);

        ComponentResizer componentResizer = new ComponentResizer();
        componentResizer.registerComponent(pnlTitle);
        componentResizer.registerComponent(lblDisplay);
        componentResizer.registerComponent(this);


        ComponentResizer componentResizerHorizontal = new ComponentResizer(ComponentResizer.HORIZONTAL);
        componentResizerHorizontal.registerComponent(pnlDisplay);

        componentResizerHorizontal.setSource(this);
        componentResizerHorizontal.setMinimumSize(new Dimension(400, 300));
        componentResizerHorizontal.setSnapSize(new Dimension(1, 1));

        componentResizer.setSource(this);
        componentResizer.setMinimumSize(new Dimension(400, 300));
        componentResizer.setSnapSize(new Dimension(1, 1));
    }

    /**
     * Sets look and feel of GUI
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Getters

    /**
     * Gets the currently selected JLabel on the GUI
     *
     * @return currently selected element as JLabel
     */
    public JPanel getSelected() {
        return selected;
    }

    /**
     * Sets the currently selected JLabel to given JLabel
     *
     * @param selected JLabel that is selected
     */
    public void setSelected(JPanel selected) {
        this.selected = selected;
    }

    /**
     * Gets display panel
     *
     * @return display panel as JPanel
     */
    public JPanel getDisplayPanel() {
        return pnlDisplay;
    }

    public String getGameSelected() {
        return lstGames.getSelectedValue().toString();
    }

    // Setters

    public String getSearchText() {
        return txtSearch.getText();
    }

    public void setSearchText(String text) {
        txtSearch.setText(text);
    }


    //  Other

    /**
     * Adds info / properties / actionlisteners to stream Labels on GUI
     *
     * @param temp StreamNode to add to GUI
     */
    public JComponent addStreamLabel(StreamNode temp) {

        String game = temp.getGame();
        String name = temp.getDisplayName();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new MigLayout("flowy, wrap 2, insets 0, gap 0 0"));

        setDeselectProperties(panel);
        panel.setName(temp.getName());
        panel.addMouseListener(new SelectListener(this));
        panel.setToolTipText(temp.getTitle());

        //Status indicator
        JLabel statusIndicator = new JLabel();
        statusIndicator.setOpaque(true);
        statusIndicator.setBackground(ColorFactory.getStatusColor(temp));

        panel.add(statusIndicator, "growy, pushy, w 5, spany 2");

        //Panel image
        JLabel channelPic = new JLabel(new ImageIcon(temp.getLogo().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        panel.add(channelPic, "gapx 7, spany 2");

        //Panel channel name
        JLabel lblName = new JLabel("<html><b>" + name + "</b><br><i>" + game + "</i></html>");
        lblName.setForeground(SELECT_FOREGROUND_COLOR);
        panel.add(lblName, "gapx 4, push, gapy 0, spany 2, pad 0 0 0 5");

        if (selected != null && panel.getName().equals(selected.getName())) {
            setSelectProperties(panel);
            selected = panel;
        }

        if (temp.getStatus().equals("Online")) {
            DecimalFormat formatter = new DecimalFormat("#, ###, ###");
            ImageIcon imgIcon = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("resources/views.png")).getImage().getScaledInstance(8, 11, java.awt.Image.SCALE_SMOOTH));
            JLabel lblViews = new JLabel(formatter.format(temp.getViews()));
            lblViews.setName("views");
            lblViews.setIcon(imgIcon);
            lblViews.setForeground(SELECT_FOREGROUND_COLOR);

            panel.add(lblViews, "gapright 5, gaptop 20, width 50");
            pnlDisplay.add(panel, "growx, sg, pushx, height 44", 0);
        } else {
            pnlDisplay.add(panel, "growx, sg, pushx, height 44");
        }

        return panel;
    }


    public void removeStreamLabel(String name) {
        Component[] components = pnlDisplay.getComponents();
        for (Component c : components) {
            if (c.getName() != null && c.getName().equals(name)) {
                pnlDisplay.remove(c);
            }
        }
    }

    /**
     * Sets the selected attributes for the given JLabel
     *
     * @param panel JPanel to set attributes to
     */
    public void setSelectProperties(JPanel panel) {
        panel.setBackground(SELECT_BACKGROUND_COLOR);
    }

    /**
     * Sets the deselected attributes for the given JLabel
     *
     * @param panel JLabel to set attributes to
     */
    public void setDeselectProperties(JPanel panel) {
        panel.setBackground(BACKGROUND_LABEL_COLOR);
    }

    public void setHoverProperties(JLabel label) {
        label.setForeground(HOVER_COLOR);
    }

    public void setUnhoverProperties(JLabel label) {
        label.setForeground(BACKGROUND_COLOR);
    }

    public void hideLoading() {
        lblDisplay.setVisible(false);
    }

    public void showLoading() {
        lblDisplay.setVisible(true);
    }

    public void addNoStreamLabel(String message){
        JLabel lblNoStream = new JLabel(message, SwingConstants.CENTER);
        lblNoStream.setFont(lblNoStream.getFont().deriveFont(Font.BOLD, 20f));
        lblNoStream.setForeground(Colors.TWITCH_PURPLE);
        pnlDisplay.add(lblNoStream, "pushx, growx, gaptop 20");
    }

    public void showSearch() {
        lblSearch.setIcon(ColorFactory.getSearchEmbedImage());
        pnlSearch.setOpaque(true);
        pnlSearch.setBackground(Colors.TWITCH_PURPLE);
        pnlSearch.add(txtSearch, "pushx, growx, h 15");
        //  pnlSearch.setBackground(TWITCH_PURPLE);
        txtSearch.requestFocus();
        txtSearch.setText(Settings.getGameFilter());
        repaint();
        revalidate();
    }

    public void hideSearch() {
        pnlSearch.remove(txtSearch);
        pnlSearch.setOpaque(false);
        ImageIcon imgicon = new ImageIcon(this.getClass().getClassLoader().getResource("resources/search.png"));
        lblSearch.setIcon(imgicon);
        hideGames();
        repaint();
        revalidate();
    }

    public boolean searchIsVisible() {
        return pnlSearch.getComponentCount() > 1 ? true : false;
    }

    public void showGames(String[] games) {
        lstGames.setListData(games);
        scrlGames.setViewportView(lstGames);
        pnlSearch.add(scrlGames, "gapleft 3, gapbottom 1, pushx, growx, span 2, hmin " + (games.length > 2 ? 100 : games.length * 20) + ", hmax " + games.length * 20);
        pnlSearch.repaint();
        pnlSearch.revalidate();
        repaint();
        revalidate();
    }

    public void hideGames() {
        scrlGames.setViewportView(null);
        pnlSearch.remove(scrlGames);
        pnlSearch.repaint();
        pnlSearch.revalidate();
        repaint();
        revalidate();
    }

    public void changeColorScheme() {
        BACKGROUND_COLOR = ColorFactory.getBackground();
        getContentPane().setBackground(BACKGROUND_COLOR);
        lblAdd.setForeground(BACKGROUND_COLOR);
        lblSetting.setForeground(BACKGROUND_COLOR);
        sepSettings.setForeground(BACKGROUND_COLOR);
        pnlDisplay.setBackground(BACKGROUND_COLOR);
        lstGames.setBackground(BACKGROUND_COLOR);
        ImageIcon imgicon = ColorFactory.getLoadingImage();
        lblDisplay.setIcon(imgicon);
        imgicon.setImageObserver(lblDisplay);

        if(txtSearch.isShowing()){
            lblSearch.setIcon(ColorFactory.getSearchEmbedImage());
        }
        txtSearch.setForeground(BACKGROUND_COLOR);
        txtSearch.setCaretColor(BACKGROUND_COLOR);
        header.setIcon(ColorFactory.getHeaderImage());
        repaint();
        revalidate();
    }

    // Listeners

    /**
     * Adds Actionlistener to the add button on GUI
     * Used by Controller object
     *
     * @param listener Actionlistener to add
     */
    public void lblAddListener(MouseListener listener) {
        lblAdd.addMouseListener(listener);
    }

    public void lblExitListener(MouseListener listener) {
        lblExit.addMouseListener(listener);
    }

    public void lblMinListener(MouseListener listener) {
        lblMinimize.addMouseListener(listener);
    }

    public void lblSettingsListener(MouseListener listener) {
        lblSetting.addMouseListener(listener);
    }

    public void pnlResizeListener(ComponentListener listener) {
        addComponentListener(listener);
    }

    public void lblSearchListener(MouseListener listener) {
        lblSearch.addMouseListener(listener);
    }

    public void lstSearchGamesListener(MouseListener listener) {
        lstGames.addMouseListener(listener);
    }

    public void txtSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }

    public void frmExitListener(WindowListener listener){
        this.addWindowListener(listener);
    }

}
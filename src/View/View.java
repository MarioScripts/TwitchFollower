package View;

import ColorFactory.ColorFactory;
import StreamList.StreamNode;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Handles all GUI interactions and elements
 */
public class View extends JFrame{

    private JButton btnAdd, btnRemove, btnSettings;
    private JLabel lblAdd, lblSetting, lblSearch, lblExit, lblMinimize;
    public JTextField txtSearch;
    public JPanel pnlDisplay, pnlSettings, selected, pnlSearch, pnlTitle;
    private JScrollPane scrDisplay;
    private JLabel header, lblSettings, lblDisplay;
    private JSeparator sepSettings, sepDisplay;
    private List topGames;
    private JList lstGames;
    private JScrollPane scrlGames;

    //Constants
    public static final Color TWITCH_PURPLE = new Color(100, 65, 164);
    private static final Color SELECT_FOREGROUND_COLOR = Color.white;
    private static final Color SELECT_BACKGROUND_COLOR = new Color(123, 90, 204);
    private static Color BACKGROUND_COLOR = ColorFactory.getBackground();
    private static final Color BACKGROUND_LABEL_COLOR = TWITCH_PURPLE;
    private static final Color HOVER_COLOR = new Color(0xC3A9FF);

    /**
     * Constructor, sets general GUI properties
     */
    public View(){

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

        // setVisible(true);
    }

    /**
     * Initializes all GUI elements and sets their properties
     */
    private void initComponents(){
        pnlTitle = new JPanel();
        pnlTitle.setLayout((new MigLayout("wrap 2, flowx, insets 0 0")));
        pnlTitle.setBackground(BACKGROUND_COLOR);

        pnlDisplay = new JPanel();
        pnlDisplay.setLayout(new MigLayout("wrap 1, flowx, insets 10 10"));
        // pnlDisplay.setMaximumSize(new Dimension(getWidth(), getHeight()-100));
        pnlDisplay.setAutoscrolls(true);
        pnlDisplay.setBackground(BACKGROUND_COLOR);

        pnlSettings = new JPanel();
        pnlSettings.setLayout(new MigLayout("insets 0 0 0 10, fill"));
        pnlSettings.setBackground(BACKGROUND_COLOR);

        pnlSearch = new JPanel();
        pnlSearch.setLayout(new MigLayout("insets 0 0 0 0, wrap 2, fill", "[][grow]"));
        pnlSearch.setBackground(BACKGROUND_COLOR);

        scrDisplay = new JScrollPane(pnlDisplay);
        scrDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrDisplay.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrDisplay.setBorder(BorderFactory.createEmptyBorder());
        scrDisplay.getVerticalScrollBar().setUnitIncrement(5);
        scrDisplay.setBackground(BACKGROUND_COLOR);

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
        lblExit.setBackground(TWITCH_PURPLE);
        lblExit.setFont(lblExit.getFont ().deriveFont (17f));
        lblExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblMinimize = new JLabel("<html><b>-</html>");
        lblMinimize.setForeground(TWITCH_PURPLE);
        lblMinimize.setBackground(TWITCH_PURPLE);
        lblMinimize.setFont(lblMinimize.getFont ().deriveFont (17f));
        lblMinimize.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblAdd = new JLabel("<html><b>+</font></html>");
        lblAdd.setFont (lblAdd.getFont ().deriveFont (17f));
        lblAdd.setBackground(BACKGROUND_COLOR);
        lblAdd.setForeground(BACKGROUND_COLOR);
        lblAdd.setToolTipText("Add a stream to your list");
        lblAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblSetting = new JLabel("<html><b>...</font></html>");
        lblSetting.setFont(lblSetting.getFont ().deriveFont (17f));
        lblSetting.setBackground(BACKGROUND_COLOR);
        lblSetting.setForeground(BACKGROUND_COLOR);
        lblSetting.setToolTipText("Go to settings");
        lblSetting.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblDisplay = new JLabel("");
        lblDisplay.setForeground(BACKGROUND_LABEL_COLOR);

        lblSearch = new JLabel("");
        lblSearch.setBackground(BACKGROUND_COLOR);
        lblSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        txtSearch = new JTextField();
        txtSearch.setBackground(BACKGROUND_COLOR);
        txtSearch.setBorder(BorderFactory.createMatteBorder(0, 3, 1, 0, TWITCH_PURPLE));
        txtSearch.setMargin(new Insets(0, 100, 0, 0));

        lstGames = new JList();
        lstGames.setSelectionBackground(TWITCH_PURPLE);
        lstGames.setSelectionForeground(BACKGROUND_COLOR);
        lstGames.setBackground(BACKGROUND_COLOR);
        lstGames.setForeground(TWITCH_PURPLE);
        lstGames.setFocusable(false);

        scrlGames = new JScrollPane();
        scrlGames.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, TWITCH_PURPLE));
        scrlGames.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        try{
            header = new JLabel("", ColorFactory.getHeaderImage(), JLabel.LEFT);
            header.setToolTipText("Go to Twitch");

            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("resources/Twitch.png"));
            setIconImage(img);
            header.addMouseListener(new HeaderListener());

            ImageIcon imgicon = ColorFactory.getLoadingImage();
            lblDisplay.setIcon(imgicon);
            imgicon.setImageObserver(lblDisplay);

            imgicon = new ImageIcon(this.getClass().getClassLoader().getResource("resources/search.png"));
            lblSearch.setIcon(imgicon);
        }catch (IOException e){
            System.out.println("Error reading GUI graphics");
        }
//
        sepSettings = new JSeparator(SwingConstants.HORIZONTAL);
        sepSettings.setForeground(BACKGROUND_COLOR);

        sepDisplay = new JSeparator(SwingConstants.HORIZONTAL);
        sepDisplay.setBackground(TWITCH_PURPLE);
        sepDisplay.setForeground(TWITCH_PURPLE);

//        lblSettings = new JLabel("<html><b>Settings");
//        lblSettings.setForeground(BACKGROUND_LABEL_COLOR);

        pnlSettings.add(lblAdd, "gapx 15");
        pnlSettings.add(lblDisplay, "gapx 30, pushx, align center");
        pnlSettings.add(lblSetting, "gapright 15, gapbottom 5");
        pnlSettings.setBackground(TWITCH_PURPLE);

        pnlSearch.add(lblSearch, "aligny 100%, pushy");

        pnlTitle.add(lblMinimize, "push, w 10, al right, gapright 10");
        pnlTitle.add(lblExit, "w 10, al right, gapright 10");

        add(pnlTitle, "growx, pushx, h 20, wrap");
        add(header, "wrap, gapy 15, align center");
//        add(lblSearch, "");
//        add(lblDisplay, "al left, wrap");
        add(pnlSearch, "wrap, gapbottom 2, gapleft 10, gapright 10, gaptop 5, growx, pushx");
        add(sepDisplay, "gapy 0, pushx, growx, wrap, gapright 10, gapleft 10");
        add(scrDisplay, "pushx, pushy, grow, wrap");
//        add(lblSettings, "al left, wrap, gapx 10");
        add(sepSettings, "h 1, growx, pushx, wrap");
        add(pnlSettings, "growx, pushx, h 3");

        MouseListener deselectListener = new DeselectListener();
        addMouseListener(deselectListener);
        pnlDisplay.addMouseListener(deselectListener);
        MoveListener moveListener = new MoveListener(this);
        pnlTitle.addMouseListener(moveListener);
        pnlTitle.addMouseMotionListener(moveListener);

        ComponentResizer componentResizer = new ComponentResizer();
        componentResizer.registerComponent(pnlTitle);
        componentResizer.registerComponent(pnlSettings.getComponents());
        componentResizer.registerComponent(scrDisplay);
        componentResizer.registerComponent(this);
        componentResizer.setSource(this);
        componentResizer.setMinimumSize(new Dimension(400, 300));
        componentResizer.setSnapSize(new Dimension(1, 1));
    }

    /**
     * Sets look and feel of GUI
     */
    private void setLookAndFeel(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Listeners

    /**
     * Adds Actionlistener to the add button on GUI
     * Used by Controller object
     * @param listener Actionlistener to add
     */
    public void lblAddListener(MouseListener listener){
        lblAdd.addMouseListener(listener);
    }

    public void lblExitListener(MouseListener listener){ lblExit.addMouseListener(listener); }

    public void lblMinListener(MouseListener listener){ lblMinimize.addMouseListener(listener); }

    public void lblSettingsListener(MouseListener listener){ lblSetting.addMouseListener(listener); }

    public void pnlResizeListener(ComponentListener listener){ addComponentListener(listener); }

    public void lblSearchListener(MouseListener listener){
        lblSearch.addMouseListener(listener);
    }

    public void lstSearchGamesListener(MouseListener listener){
        lstGames.addMouseListener(listener);
    }

    public void txtSearchListener(DocumentListener listener){
        txtSearch.getDocument().addDocumentListener(listener);
    }

//    public void contextMenuListener(MouseListener listener){ selected.addMouseListener(listener);}

    // Getters

    /**
     * Gets the currently selected JLabel on the GUI
     * @return currently selected element as JLabel
     */
    public JPanel getSelected(){
        return selected;
    }

    /**
     * Gets display panel
     * @return display panel as JPanel
     */
    public JPanel getDisplayPanel(){
        return pnlDisplay;
    }

    public JScrollPane getScrDisplay(){
        return scrDisplay;
    }

    public String getGameSelected(){
        return lstGames.getSelectedValue().toString();
    }

    public String getSearchText(){ return txtSearch.getText(); }

    // Setters

    /**
     * Sets the currently selected JLabel to given JLabel
     * @param selected JLabel that is selected
     */
    public void setSelected(JPanel selected){
        this.selected = selected;
    }

    public void setSearchText(String text){
        txtSearch.setText(text);
    }


    //  Other

    /**
     * Adds info / properties / actionlisteners to stream Labels on GUI
     * @param temp StreamNode to add to GUI
     */
    public JComponent addStreamLabel(StreamNode temp){

        String game = temp.getGame();
        String name = temp.getDisplayName();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new MigLayout("flowy, wrap 2, insets 0, gap 0 0"));

        setDeselectProperties(panel);
        panel.setName(temp.getName());
        panel.addMouseListener(new SelectListener());
        panel.setToolTipText(temp.getTitle());

        //Status indicator
        JLabel statusIndicator = new JLabel();
        statusIndicator.setOpaque(true);
        statusIndicator.setBackground(new Color(230, 46, 0));
        statusIndicator.setToolTipText("Offline");

        if(temp.getStatus().equals("Online")){
            if(temp.getVodcast()){
                statusIndicator.setBackground(new Color(0xFFFE66));
                statusIndicator.setToolTipText("Vodcast");
            }else{
                statusIndicator.setBackground(new Color(102, 255, 102));
                statusIndicator.setToolTipText("Online");
            }

        }
        panel.add(statusIndicator, "growy, pushy, w 5, spany 2");

        //Panel image
        JLabel channelPic = new JLabel(new ImageIcon(temp.getLogo().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        panel.add(channelPic, "gapx 7, spany 2");

        //Panel channel name
        JLabel lblName = new JLabel("<html><b>" + name + "</b><br><i>" + game + "</i></html>");
        lblName.setForeground(SELECT_FOREGROUND_COLOR);
        panel.add(lblName, "gapx 4, push, gapy 0, spany 2");

        if(selected != null && panel.getName().equals(selected.getName())){
            setSelectProperties(panel);
            selected = panel;
        }

        if(temp.getStatus().equals("Online")){
            DecimalFormat formatter = new DecimalFormat("#, ###, ###");
            ImageIcon imgIcon = new ImageIcon(new ImageIcon(this.getClass().getClassLoader().getResource("resources/views.png")).getImage().getScaledInstance(8, 11,  java.awt.Image.SCALE_SMOOTH));
            JLabel lblViews = new JLabel(formatter.format(temp.getViews()));
            lblViews.setName("views");
            lblViews.setIcon(imgIcon);
            lblViews.setForeground(SELECT_FOREGROUND_COLOR);

            panel.add(lblViews, "gapright 5, gaptop 20, width 50");
            pnlDisplay.add(panel, "growx, sg, pushx, height 44", 0);
        }else{
            pnlDisplay.add(panel, "growx, sg, pushx, height 44");
        }

        return panel;
    }


    public void removeStreamLabel(String name){
        Component[] components = pnlDisplay.getComponents();
        for(Component c : components){
            if(c.getName() != null && c.getName().equals(name)){
                pnlDisplay.remove(c);
            }
        }
    }

    /**
     * Sets the selected attributes for the given JLabel
     * @param panel JPanel to set attributes to
     */
    public void setSelectProperties(JPanel panel){
        panel.setBackground(SELECT_BACKGROUND_COLOR);
    }

    /**
     * Sets the deselected attributes for the given JLabel
     * @param panel JLabel to set attributes to
     */
    public void setDeselectProperties(JPanel panel){
        panel.setBackground(BACKGROUND_LABEL_COLOR);
    }

    public void setHoverProperties(JLabel label){
        label.setForeground(HOVER_COLOR);
    }

    public void setUnhoverProperties(JLabel label){
        label.setForeground(BACKGROUND_COLOR);
    }

    public void hideLoading(){
        lblDisplay.setVisible(false);
    }

    public void showLoading(){
        lblDisplay.setVisible(true);
    }

    public void showSearch(){
        pnlSearch.add(txtSearch, "pushx 100, growx, gaptop 10");
      //  pnlSearch.setBackground(TWITCH_PURPLE);
        txtSearch.requestFocus();
        repaint();
        revalidate();
    }

    public void hideSearch(){
        pnlSearch.remove(txtSearch);
      //  pnlSearch.setBackground(BACKGROUND_COLOR);
        hideGames();
        repaint();
        revalidate();
    }

    public boolean searchIsVisible(){
        return pnlSearch.getComponentCount() > 1 ? true : false;
    }

    public void showGames(String[] games){
        lstGames.setListData(games);
        scrlGames.setViewportView(lstGames);
        pnlSearch.add(scrlGames, "gapleft 17, pushx, growx, span 2, hmin " + (games.length > 2 ? 100 : games.length*18) + ", hmax " + games.length * 18);
        pnlSearch.repaint();
        pnlSearch.revalidate();
        repaint();
        revalidate();
    }

    public void hideGames(){
        scrlGames.setViewportView(null);
        pnlSearch.remove(scrlGames);
        pnlSearch.repaint();
        pnlSearch.revalidate();
        repaint();
        revalidate();
    }

    public void changeColorScheme(){
        BACKGROUND_COLOR = ColorFactory.getBackground();
        getContentPane().setBackground(BACKGROUND_COLOR);
        pnlTitle.setBackground(BACKGROUND_COLOR);
        pnlDisplay.setBackground(BACKGROUND_COLOR);
        pnlSearch.setBackground(BACKGROUND_COLOR);
        scrDisplay.setBackground(BACKGROUND_COLOR);
        lblAdd.setBackground(BACKGROUND_COLOR);
        lblAdd.setForeground(BACKGROUND_COLOR);
        lblSetting.setBackground(BACKGROUND_COLOR);
        lblSetting.setForeground(BACKGROUND_COLOR);
        lblSearch.setBackground(BACKGROUND_COLOR);
        txtSearch.setBackground(BACKGROUND_COLOR);
        lstGames.setBackground(BACKGROUND_COLOR);
        sepSettings.setForeground(BACKGROUND_COLOR);

        ImageIcon imgicon = ColorFactory.getLoadingImage();
        lblDisplay.setIcon(imgicon);
        imgicon.setImageObserver(lblDisplay);

        header.setIcon(ColorFactory.getHeaderImage());

        repaint();
        revalidate();
    }



    // Listeners

    /**
     * Redirects to Twitch directory when header is clicked on
     */
    private class HeaderListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(Desktop.isDesktopSupported()){
                try{
                    Desktop.getDesktop().browse(new URI("http://www.twitch.tv/directory"));
                }catch(URISyntaxException |IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Waits for clicks/hovers on JLabel to set select properties
     * Also handles context menu clicks
     */
    private class SelectListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

            if(selected != null && e.getComponent() != selected){
                setDeselectProperties(selected);
            }

            if(e.getComponent() != selected){
                setSelectProperties((JPanel) e.getComponent());
                selected = (JPanel)e.getComponent();
            }else if (e.getComponent() == selected && !SwingUtilities.isRightMouseButton(e)){
                selected = null;
            }

            validate();
            repaint();

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
            setSelectProperties((JPanel)e.getComponent());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getComponent() != selected){
                setDeselectProperties((JPanel)e.getComponent());
            }

        }
    }

    /**
     * Waits for hovers on JLabel to set deselect properties
     */
    private class DeselectListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(selected != null){
                setDeselectProperties(selected);
                selected = null;
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

        public void mouseExited(MouseEvent e) {

        }
    }

    private static class MoveListener extends MouseAdapter{
        private int tx, ty;
        private JFrame frame;

        public MoveListener(JFrame frame){
            this.frame = frame;
        }

        @Override
        public void mouseDragged(MouseEvent e){
            if(frame.getCursor().getType() == Cursor.DEFAULT_CURSOR){
                frame.setLocation(e.getXOnScreen() - tx, e.getYOnScreen() - ty);
            }
        }

        @Override
        public void mousePressed(MouseEvent e){
            tx = e.getX();
            ty = e.getY();
        }
    }

}
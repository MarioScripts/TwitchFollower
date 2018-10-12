package StreamList;


import java.awt.*;

/**
 * Stream object, keeps track of individual stream information
 */
public class StreamNode {

    private String name, game, status, displayName, title;
    private int views;
    private boolean vodcast;
    private Image logo;
    private StreamNode next;

    /**
     * Constructor for stream object using given name and default status as Offline
     * @param name Name of stream
     */
    public StreamNode(String name) {
        this.name = name;
        game = "N/A";
        status = "Offline";
        logo = null;
        next = null;
        displayName = name;
        vodcast = false;
        title = "N/A";
        views = 0;
    }

    /**
     * Alternative constructor that creates a copy of a specified node
     * @param node Node to make copy of
     */
    public StreamNode(StreamNode node) {
        setNode(node);
    }

    // Getters

    /**
     * Gives the stream Display name in proper upper/lower case
     * @return Display name as String
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gives the stream name
     * @return stream name as String
     */
    public String getName() {
        return name;
    }

    /**
     * Gives the stream game
     * @return stream game as String
     */
    public String getGame() {
        return game;
    }

    /**
     * Gives the stream status
     * @return stream status as String
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gives the stream logo
     * @return stream logo as Image
     */
    public Image getLogo() {
        return logo;
    }

    /**
     * Gives the amount of viewers a stream has
     * @return Amount of viewers as int
     */
    public int getViews() {
        return views;
    }

    /**
     * Gives the next node following current node
     * @return next stream as StreamNodeCompilation completed successfully in 1s 94ms (2 minutes ago)
     */
    public StreamNode getNext() {
        return next;
    }

    /**
     * Returns whether the stream is vodcasting or live
     * @return Vodcasting status as boolean
     */
    public boolean getVodcast() {
        return this.vodcast;
    }

    /**
     * Gives the title of the stream
     * @return Title of stream as String
     */
    public String getTitle() {
        return title;
    }

    // Setters

    /**
     * Sets the current node's stream name
     * @param name Name of stream
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets current node's stream game
     * @param game Name of game
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * Sets the current node's display name
     * @param displayName Display name of stream
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Sets current node's stream status
     * @param status Status of stream
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void setViews(int views) {
        this.views = views;
    }

    /**
     * Sets current node's stream logo
     * @param logo Logo of stream
     */
    public void setLogo(Image logo) {
        this.logo = logo;
    }

    /**
     * Sets the next node following the current node
     * @param next Node to be next
     */
    public void setNext(StreamNode next) {
        this.next = next;
    }

    /**
     * Sets stream's vodcasting status
     * @param vodcast Vodcasting status of stream
     */
    public void setVodcast(boolean vodcast) {
        this.vodcast = vodcast;
    }

    /**
     * Sets the title of the stream
     * @param title Title of stream
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Copies the attributes of an already existing node to this node
     * @param node Node to copy attributes from
     */
    public void setNode(StreamNode node) {
        this.setViews(node.getViews());
        this.setTitle(node.getTitle());
        this.setGame(node.getGame());
        this.setLogo(node.getLogo());
        this.setDisplayName(node.getDisplayName());
        this.setName(node.getName());
        this.setStatus(node.getStatus());
        this.setVodcast(node.getVodcast());
    }

}

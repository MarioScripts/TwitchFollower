package StreamList;


import javax.swing.*;
import java.awt.*;

/**
 * Stream object, keeps track of individual stream information
 */
public class StreamNode {

    private String name;
    private String game;
    private String status;
    private Image logo;
    private JLabel label;
    private StreamNode next;

    /**
     * Constructor for stream object using given name and default status as Offline
     * @param name Name of stream
     */
    public StreamNode(String name){
        this.name = name;
        game = "N/A";
        status = "Offline";
        logo = null;
        next = null;
        label = null;
    }

    // Getters

    /**
     * Gives the stream name
     * @return stream name as String
     */
    public String getName(){
        return name;
    }

    /**
     * Gives the stream game
     * @return stream game as String
     */
    public String getGame(){
        return game;
    }

    /**
     * Gives the stream status
     * @return stream status as String
     */
    public String getStatus(){
        return status;
    }

    /**
     * Gives the stream logo
     * @return stream logo as Image
     */
    public Image getLogo() { return logo; }

    /**
     * Give the stream's GUI label
     * @return stream label as JLabel
     */
    public JLabel getLabel(){
        return label;
    }

    /**
     * Gives the next node following current node
     * @return next stream as StreamNode
     */
    public StreamNode getNext(){
        return next;
    }



    // Setters

    /**
     * Sets the current node's stream name
     * @param name Name of stream
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets current node's stream game
     * @param game Name of game
     */
    public void setGame(String game){
        this.game = game;
    }

    /**
     * Sets current node's stream status
     * @param status Status of stream
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * Sets current node's stream logo
     * @param logo Logo of stream
     */
    public void setLogo(Image logo) { this.logo = logo; }

    /**
     * Sets current node's label
     * @param label Label of stream
     */
    public void setLabel(JLabel label){
        this.label = label;
    }

    /**
     * Sets the next node following the current node
     * @param next Node to be next
     */
    public void setNext(StreamNode next){
        this.next = next;
    }

}

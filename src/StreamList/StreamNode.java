package StreamList;


import java.awt.*;

public class StreamNode {

    private String name;
    private String game;
    private String status;
    private Image logo;
    private StreamNode next;

    public StreamNode(String name){
        this.name = name;
        game = "N/A";
        status = "Offline";
        logo = null;
        next = null;
    }


    // Getters
    public String getName(){
        return name;
    }

    public String getGame(){
        return game;
    }

    public String getStatus(){
        return status;
    }

    public Image getLogo() { return logo; }

    public StreamNode getNext(){
        return next;
    }


    // Setters
    public void setName(String name){
        this.name = name;
    }

    public void setGame(String game){
        this.game = game;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setLogo(Image logo) { this.logo = logo; }

    public void setNext(StreamNode next){
        this.next = next;
    }

}

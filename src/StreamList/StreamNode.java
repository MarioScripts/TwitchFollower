package StreamList;


public class StreamNode {

    private String name;
    private String game;
    private String status;
    private StreamNode next;

    public StreamNode(String name){
        this.name = name;
        game = "N/A";
        status = "Offline";
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

    public void setNext(StreamNode next){
        this.next = next;
    }
}

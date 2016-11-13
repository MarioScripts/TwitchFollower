package Model;

import Exceptions.DuplicateStreamException;
import StreamList.*;
import org.json.JSONException;
import org.json.JSONObject;
import sun.security.krb5.internal.crypto.Des;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.InvalidPathException;

public class Model {
    private StreamList streams;
    private static final String SAVE_DIR = "C:\\Users\\" + System.getProperty("user.name") + "\\names.txt";
    private static final String API_URL = "https://api.twitch.tv/kraken/streams/";
    private static final String CLIENT_ID = "iv92gs01m24niftpift7l6jsmvrfvpo";

    public Model(){
        streams = new StreamList();
        readStreams();
    }

    public void addStream(StreamNode node) throws DuplicateStreamException{
        streams.add(node);
        addStreamFile(node.getName());
    }

    public void removeStream(String name){
        removeStreamFile(name);
        streams.remove(name);
    }

    public StreamList getStreams(){
        return streams;
    }

    public static StreamNode getStreamInfo(StreamNode node){
        String name = node.getName();
        StreamNode streamInfo = new StreamNode(name);

        try{
            URL url = new URL(API_URL + name);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Client-ID", CLIENT_ID);
            connection.connect();
            InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
            BufferedReader buff = new BufferedReader(in);
            String line = "";
            String info = "";
            while(line != null){
                line = buff.readLine();
                info += line;
            }

            JSONObject stream = new JSONObject(info).getJSONObject("stream");
            streamInfo.setStatus("Online");
            streamInfo.setGame(stream.getString("game"));

            in.close();
            buff.close();
        }catch(IOException | JSONException e){
            streamInfo.setStatus("Offline");
            streamInfo.setGame("N/A");
        }

        return streamInfo;

    }

    public static void openToLivestreamer(String name) throws InvalidPathException{
        String livestreamDir = "C:\\Program Files (x86)\\Livestreamer\\livestreamer.exe";
        if(new File(livestreamDir).exists()){
            try{
                Runtime.getRuntime().exec(livestreamDir + " http://twitch.tv/" + name + " source");
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }else{
            throw new InvalidPathException(livestreamDir, "Livestreamer not installed or not found");
        }
    }

    public static void openToTwitch(String name){
        if(Desktop.isDesktopSupported()){
            try{
                Desktop.getDesktop().browse(new URI("http://www.twitch.tv/" + name));
            }catch(URISyntaxException|IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void openPopoutChat(String name){
        if(Desktop.isDesktopSupported()){
            try{
                Desktop.getDesktop().browse(new URI("https://www.twitch.tv/" + name + "/chat?popout="));
            }catch(URISyntaxException | IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void removeStreamFile(String name){
        File streamFile = new File(SAVE_DIR);

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(streamFile));
            StreamIterator iter = streams.iterator();

            //Use a string so the last line of the file isn't a carriage return
            String fileOutput = "";
            while(iter.hasNext()){
                StreamNode temp = iter.next();

                if(!temp.getName().equals(name)){
                    fileOutput += temp.getName() + "\r\n";
                }
            }

            writer.write(fileOutput.trim());
            writer.close();

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void addStreamFile(String name){
        File streamFile = new File(SAVE_DIR);

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(streamFile, true));
            writer.newLine();
            writer.write(name);
            writer.close();

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void readStreams(){
        File streamFile = new File(SAVE_DIR);

        try{

            if(streamFile.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(streamFile));

                String line;
                while((line = reader.readLine()) != null){
                    try{
                        streams.add(new StreamNode(line));
                    }catch (DuplicateStreamException e){
                        System.out.println(e.getMessage());
                    }
                }

            }else{
                streamFile.createNewFile();
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

}

package Model;

import Exceptions.DuplicateStreamException;
import StreamList.*;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.json.JSONException;
import org.json.JSONObject;
import sun.awt.datatransfer.DataTransferer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.InvalidPathException;

public class Model {
    private StreamList streams;
    private static final String SAVE_DIR = "C:\\Users\\" + System.getProperty("user.name") + "\\names.txt";
    private static final String STREAM_URL = "https://api.twitch.tv/kraken/streams/";
    private static final String CHANNEL_URL = "https://api.twitch.tv/kraken/channels/";
    private static final String CLIENT_ID = "iv92gs01m24niftpift7l6jsmvrfvpo";
    private static final String DEFAULT_LOGO_URL = "https://static-cdn.jtvnw.net/jtv_user_pictures/xarth/404_user_70x70.png";

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

    public static StreamNode getStreamInfo(StreamNode node) throws InvalidObjectException{
        String name = node.getName();
        StreamNode streamInfo = new StreamNode(name);
        String info = "";
        URL logoURL = null;

        try{
            if(node.getLogo() == null){
                info = getJSONString(CHANNEL_URL, name);
                JSONObject channel = new JSONObject(info);
                logoURL = new URL(channel.getString("logo"));
                streamInfo.setLogo(ImageIO.read(logoURL).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            }

            info = getJSONString(STREAM_URL, name);
            JSONObject stream = new JSONObject(info).getJSONObject("stream");
            streamInfo.setStatus("Online");
            streamInfo.setGame(stream.getString("game"));

        }catch(IOException | JSONException e){
            //Handle if stream does not exist
            if(info == ""){
                throw new InvalidObjectException("Invalid stream");
            }else{
                streamInfo.setStatus("Offline");
                streamInfo.setGame("N/A");
            }

            // If channel doesn't have logo, use default twitch logo
            if(logoURL == null){
                try{
                    logoURL = new URL(DEFAULT_LOGO_URL);
                    streamInfo.setLogo(ImageIO.read(logoURL).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                }catch (IOException e1){
                    System.out.println(e1.getMessage());
                }
            }
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

    private static String getJSONString(String apiURL, String name) throws IOException{
        URL url = new URL(apiURL + name);
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

        in.close();
        buff.close();
        return info;
    }

}

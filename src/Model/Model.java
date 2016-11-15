package Model;

import Exceptions.DuplicateStreamException;
import StreamList.*;
import org.json.JSONException;
import org.json.JSONObject;
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

    /**
     * Constructor
     */
    public Model(){
        streams = new StreamList();
        readStreams();
    }

    /**
     * Adds stream to StreamList object and to stream list saved locally
     * @param node Stream that you wish to add
     * @throws DuplicateStreamException if duplicate stream is found
     */
    public void addStream(StreamNode node) throws DuplicateStreamException{
        streams.add(node);
        addStreamFile(node.getName());
    }

    /**
     * Removes stream from StreamList object and from stream list saved locally
     * @param name Stream name that you wish to delete
     */
    public void removeStream(String name){
        removeStreamFile(name);
        streams.remove(name);
    }

    /**
     * Gets StreamList object
     * @return StreamList object
     */
    public StreamList getStreams(){
        return streams;
    }

    /**
     * Uses Twitch API to get stream info
     * @param node Stream to get stream info for
     * @return Stream info as StreamNode object
     * @throws InvalidObjectException thrown when stream does not exist in Twitch database
     */
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

    /**
     * Opens stream in Livestreamer
     * @param name Name of stream to open
     * @throws InvalidPathException thrown if Livestreamer is not installed
     */
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

    /**
     * Opens stream on Twitch
     * @param name Name of stream to open
     */
    public static void openToTwitch(String name){
        if(Desktop.isDesktopSupported()){
            try{
                Desktop.getDesktop().browse(new URI("http://www.twitch.tv/" + name));
            }catch(URISyntaxException|IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Opens the stream's popout chat
     * @param name Name of stream to open
     */
    public static void openPopoutChat(String name){
        if(Desktop.isDesktopSupported()){
            try{
                Desktop.getDesktop().browse(new URI("https://www.twitch.tv/" + name + "/chat?popout="));
            }catch(URISyntaxException | IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Removes stream from locally saved stream list
     * @param name Name of stream to remove
     */
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

    /**
     * Adds stream to locally saved stream list
     * @param name Name of stream to add
     */
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

    /**
     * Reads all streams from locally saved stream list
     */
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

    /**
     * Gets JSON information from Twitch's API
     * @param apiURL Type of twitch API to use
     * @param name Name of stream to get info for
     * @return Returns JSON information as String
     * @throws IOException thrown when unable to get information
     */
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

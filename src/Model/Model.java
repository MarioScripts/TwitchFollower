package Model;

import Exceptions.DuplicateStreamException;
import Other.Settings;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.InvalidPathException;

public class Model {
    private StreamList streams;
    private static final String SAVE_DIR = "C:\\Users\\" + System.getProperty("user.name") + "\\Twitchfollower";
    private static final String STREAM_URL = "https://api.twitch.tv/kraken/streams/";
    private static final String CHANNEL_URL = "https://api.twitch.tv/kraken/channels/";
    private static final String USER_URL = "https://api.twitch.tv/kraken/users/";
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
        //TODO: Clean this up
        String name = node.getName();
        StreamNode streamInfo = new StreamNode(name);
        String info = "", displayName = name;
        JSONObject channel = null, stream = null;
        URL logoURL = null;

        //Get logo
        try{
            if(node.getLogo() == null){
                info = getJSONString(CHANNEL_URL, name);
                channel = new JSONObject(info);
                logoURL = new URL(channel.getString("logo"));
                displayName = channel.getString("display_name");
                streamInfo.setLogo(ImageIO.read(logoURL).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            }else{
                streamInfo.setLogo(node.getLogo());
            }

        }catch  (IOException e){
            System.out.println(e.getMessage());
        }catch (JSONException ex){

            //If logo is null then set logo to default logo
            if(logoURL == null){
                try{
                    logoURL = new URL(DEFAULT_LOGO_URL);
                    streamInfo.setLogo(ImageIO.read(logoURL).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }

        //Get status/game
        try{
            info = getJSONString(STREAM_URL, name);
            stream = new JSONObject(info).getJSONObject("stream");
            streamInfo.setStatus("Online");
            streamInfo.setGame(stream.getString("game"));
            streamInfo.setDisplayName(displayName);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }catch (JSONException ex){

            //Throw exception if stream doesn't exist, else set stream to offline
            if(info == ""){
                throw new InvalidObjectException("Invalid stream");
            }else{
                streamInfo.setStatus("Offline");
                streamInfo.setGame("N/A");
                streamInfo.setDisplayName(displayName);
            }
        }

        return streamInfo;

    }

    public void importUserFollowers(String user) {
        try{
            JSONArray follows = new JSONObject(getJSONString(USER_URL,user + "/follows/channels")).getJSONArray("follows");
            for(Object follow : follows){

                JSONObject ok = (JSONObject) follow;
                ok = ok.getJSONObject("channel");
                String name = ok.getString("name");
                StreamNode node = new StreamNode(name);
                try{
                    addStream(node);
                }catch (DuplicateStreamException ex){
                    System.out.println("Stream already in list, skipping..");
                }

            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void editSettings(boolean gameNotify, boolean statusNotify, boolean showOffline){
        File settingsFile = new File(SAVE_DIR + "\\settings.cfg");
        File streamDir = new File(SAVE_DIR);

        try{
            if(streamDir.exists() && settingsFile.exists()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
                String settings = "";
                settings += "gameNotify=" + gameNotify + "\r\n";
                settings += "statusNotify=" + statusNotify + "\r\n";
                settings += "showOffline=" + showOffline + "\r\n";

                writer.write(settings);
                writer.close();
            }else{
                if(!streamDir.exists()){
                    streamDir.mkdir();
                }

                if(!settingsFile.exists()){
                    settingsFile.createNewFile();
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void readSettings(){
        File settingsFile = new File(SAVE_DIR + "\\settings.cfg");
        File streamDir = new File(SAVE_DIR);

        try{
            if(streamDir.exists() && settingsFile.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
                String line;
                while((line = reader.readLine()) != null){
                    if(line.length() > 1){
                        String setting;
                        boolean state;
                        setting = line.substring(line.indexOf('=')+1);
                        state = Boolean.parseBoolean(setting);
                        if(line.contains("gameNotify")){
                            Settings.setGameNotify(state);
                        }else if(line.contains("statusNotify")){
                            Settings.setStatusNotify(state);
                        }else if(line.contains("showOffline")){
                            Settings.setShowOffline(state);
                        }
                    }
                }
            }else{
                if(!streamDir.exists()){
                    streamDir.mkdir();
                }

                if(!settingsFile.exists()){
                    settingsFile.createNewFile();
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException ex){
            ex.printStackTrace();
        }
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
        File streamFile = new File(SAVE_DIR + "\\names.txt");

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
        File streamFile = new File(SAVE_DIR + "\\names.txt");

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
        File streamDir = new File(SAVE_DIR);
        File streamFile = new File(SAVE_DIR + "\\names.txt");

        try{

            if(streamDir.exists() && streamFile.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(streamFile));

                String line;
                while((line = reader.readLine()) != null){
                    if(line.length() > 1){
                        try{
                            streams.add(new StreamNode(line));
                        }catch (DuplicateStreamException e){
                            System.out.println(e.getMessage());
                        }
                    }

                }
            }else{
                if(!streamDir.exists()){
                    streamDir.mkdir();
                }

                if(!streamFile.exists()){
                    streamFile.createNewFile();
                }

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
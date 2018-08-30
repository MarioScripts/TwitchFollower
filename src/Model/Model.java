package Model;

import Exceptions.DuplicateStreamException;
import Exceptions.UserNotFoundException;
import Other.Settings;
import Other.Sorter;
import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Iterator;

public class Model {
    private static final String SAVE_DIR = System.getProperty("user.home") + "\\AppData\\Roaming\\Twitchfollower";
    private static final String STREAM_URL = "https://api.twitch.tv/kraken/streams/";
    private static final String GAME_URL = "https://api.twitch.tv/kraken/games/top?limit=100";
    private static final String CHANNEL_URL = "https://api.twitch.tv/kraken/channels/";
    private static final String USER_URL = "https://api.twitch.tv/kraken/users/";
    private static final String CLIENT_ID = "iv92gs01m24niftpift7l6jsmvrfvpo";
    private StreamList streams;
    private List games;

    /**
     * Constructor
     */
    public Model() {
        streams = new StreamList();
        games = new List();
        readStreams();
    }

    /**
     * Uses Twitch API to get stream info
     *
     * @param node Stream to get stream info for
     * @return Stream info as StreamNode object
     * @throws InvalidObjectException thrown when stream does not exist in Twitch database
     */
    public static StreamNode getStreamInfo(StreamNode node) throws InvalidObjectException {
        //TODO: Clean this up
        String name = node.getName();
        StreamNode streamInfo = new StreamNode(name);
        String info = "", displayName = name;
        JSONObject channel = null, stream = null;
        URL logoURL = null;

        //Get logo
        try {
            if (node.getLogo() == null) {
                info = getJSONString(CHANNEL_URL, name);
                channel = new JSONObject(info);
                logoURL = new URL(channel.getString("logo"));
                displayName = channel.getString("display_name");
                streamInfo.setDisplayName(displayName);
                streamInfo.setLogo(ImageIO.read(logoURL).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            } else {
                streamInfo.setLogo(node.getLogo());
            }

        } catch (IOException e) {
            throw new InvalidObjectException(name);
        } catch (JSONException ex) {

            //If logo is null then set logo to default logo
            if (logoURL == null) {
                try {
                    streamInfo.setLogo(ImageIO.read(Model.class.getClass().getResourceAsStream("/resources/default.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        //Get status/game
        try {
            info = getJSONString(STREAM_URL, name);
            stream = new JSONObject(info).getJSONObject("stream");
            streamInfo.setStatus("Online");
            streamInfo.setTitle(stream.getJSONObject("channel").getString("status"));

            if (!stream.getString("stream_type").equals("live")) {
                streamInfo.setVodcast(true);
            }

            streamInfo.setViews(stream.getInt("viewers"));
            streamInfo.setGame(stream.getString("game"));
            streamInfo.setDisplayName(displayName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException ex) {

            //Throw exception if stream doesn't exist, else set stream to offline
            if (info == "") {
                throw new InvalidObjectException(name);
            } else {
                streamInfo.setStatus("Offline");
                streamInfo.setGame("N/A");
                streamInfo.setDisplayName(displayName);
            }
        }

        return streamInfo;

    }

    public static void updateSettings() {
        File settingsFile = new File(SAVE_DIR + "\\settings.cfg");
        File streamDir = new File(SAVE_DIR);

        try {
            if (streamDir.exists() && settingsFile.exists()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFile));
                String settings = "";
                settings += "gameNotify=" + Settings.getGameNotify() + "\r\n";
                settings += "statusNotify=" + Settings.getStatusNotify() + "\r\n";
                settings += "showOffline=" + Settings.getShowOffline() + "\r\n";
                settings += "showVodcast=" + Settings.getShowVodcast() + "\r\n";
                settings += "sleepTime=" + Settings.getSleepTime() + "\r\n";
                settings += "darkMode=" + Settings.getDarkMode() + "\r\n";
                settings += "gameFilter=" + Settings.getGameFilter() + "\r\n";
                settings += "size=" + (int)Settings.getSize().getWidth() + "x" + (int)Settings.getSize().getHeight() + "\r\n";
                settings += "loc=" + (int)Settings.getLoc().getX() + "x" + (int)Settings.getLoc().getY() + "\r\n";
                settings += "sort=" + Settings.getSort() + "\r\n";

                writer.write(settings);
                writer.close();
            } else {
                if (!streamDir.exists()) {
                    streamDir.mkdir();
                }

                if (!settingsFile.exists()) {
                    settingsFile.createNewFile();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void readSettings() {
        File settingsFile = new File(SAVE_DIR + "\\settings.cfg");
        File streamDir = new File(SAVE_DIR);

        try {
            if (streamDir.exists() && settingsFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1) {
                        String setting;
                        boolean state;
                        setting = line.substring(line.indexOf('=') + 1);

                        state = Boolean.parseBoolean(setting);
                        if (line.contains("gameNotify")) {
                            Settings.setGameNotify(state);
                        } else if (line.contains("statusNotify")) {
                            Settings.setStatusNotify(state);
                        } else if (line.contains("showOffline")) {
                            Settings.setShowOffline(state);
                        } else if (line.contains("showVodcast")) {
                            Settings.setShowVodast(state);
                        } else if (line.contains("sleepTime")) {
                            Settings.setSleepTime(Integer.parseInt(setting));
                        } else if (line.contains("darkMode")) {
                            Settings.setDarkMode(state);
                        } else if (line.contains("gameFilter")) {
                            Settings.setGameFilter(setting);
                        }else if (line.contains("size")){
                            String[] sizeString = setting.split("x");
                            int width = Integer.valueOf(sizeString[0]);
                            int height = Integer.valueOf(sizeString[1]);
                            Dimension size = new Dimension(width, height);

                            Settings.setSize(size);
                        }else if (line.contains("loc")){
                            String[] locString = setting.split("x");
                            int x = Integer.valueOf(locString[0]);
                            int y = Integer.valueOf(locString[1]);
                            Point loc = new Point(x,y);

                            Settings.setLoc(loc);
                        }else if(line.contains("sort")){
                            Settings.setSort(Integer.valueOf(setting));
                        }

                    }
                }
            } else {
                if (!streamDir.exists()) {
                    streamDir.mkdir();
                }

                if (!settingsFile.exists()) {
                    settingsFile.createNewFile();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Opens stream in Livestreamer
     *
     * @param name Name of stream to open
     * @throws InvalidPathException thrown if Livestreamer is not installed
     */
    public static void openToLivestreamer(String name) throws InvalidPathException {
        String livestreamDir = "C:\\Program Files (x86)\\Livestreamer\\livestreamer.exe";
        if (new File(livestreamDir).exists()) {
            try {
                Runtime.getRuntime().exec(livestreamDir + " http://twitch.tv/" + name + " source");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new InvalidPathException(livestreamDir, "Livestreamer not installed or not found");
        }
    }

    /**
     * Opens stream on Twitch
     *
     * @param name Name of stream to open
     */
    public static void openToTwitch(String name) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://www.twitch.tv/" + name));
            } catch (URISyntaxException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Opens the stream's popout chat
     *
     * @param name Name of stream to open
     */
    public static void openPopoutChat(String name) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.twitch.tv/" + name + "/chat?popout="));
            } catch (URISyntaxException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String getJSONString(String apiURL) throws IOException {
        URL url = new URL(apiURL);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Client-ID", CLIENT_ID);
        connection.connect();
        InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
        BufferedReader buff = new BufferedReader(in);
        String line = "";
        String info = "";
        while (line != null) {
            line = buff.readLine();
            info += line;
        }

        in.close();
        buff.close();
        return info;
    }

    /**
     * Gets JSON information from Twitch's API
     *
     * @param apiURL Type of twitch API to use
     * @param name   Name of stream to get info for
     * @return Returns JSON information as String
     * @throws IOException thrown when unable to get information
     */
    private static String getJSONString(String apiURL, String name) throws IOException {
        return getJSONString(apiURL + name);
    }

    /**
     * Adds stream to StreamList object and to stream list saved locally
     *
     * @param node Stream that you wish to add
     * @throws DuplicateStreamException if duplicate stream is found
     */
    public void addStream(StreamNode node) throws DuplicateStreamException {
        streams.add(node);
        addStreamFile(node.getName());
    }

    /**
     * Removes stream from StreamList object and from stream list saved locally
     *
     * @param name Stream name that you wish to delete
     */
    public void removeStream(String name) {
        removeStreamFile(name);
        streams.remove(name);
    }

    /**
     * Gets StreamList object
     *
     * @return StreamList object
     */
    public StreamList getStreams() {
        Sorter.sort(streams);
        return streams;
    }

    //TODO: Tidy null shit
    public ArrayList<String> getTopGames() {
        ArrayList<String> games = new ArrayList<>();
        try {
            String info = getJSONString(GAME_URL);
            JSONArray top = new JSONObject(info).getJSONArray("top");
            for (Object game : top) {
                games.add(new JSONObject(game.toString()).getJSONObject("game").getString("name").toString());
            }

            Iterator<StreamNode> iter = streams.iterator();
            while (iter.hasNext()) {
                String tempGame = iter.next().getGame();
                if (!games.contains(tempGame)) {
                    games.add(tempGame);
                }
            }

            return games;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public JSONArray getImportedFollowers(String user) throws UserNotFoundException {
        try {
            JSONArray follows = new JSONObject(getJSONString(USER_URL, user + "/follows/channels?limit=100")).getJSONArray("follows");
            return follows;
        } catch (IOException e) {
            System.out.println("Incorrect specified user");
        }

        throw new UserNotFoundException("Specified user is not found");
    }

    /**
     * Removes stream from locally saved stream list
     *
     * @param name Name of stream to remove
     */
    private void removeStreamFile(String name) {
        File streamFile = new File(SAVE_DIR + "\\names.txt");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(streamFile));
            StreamIterator iter = streams.iterator();

            //Use a string so the last line of the file isn't a carriage return
            String fileOutput = "";
            while (iter.hasNext()) {
                StreamNode temp = iter.next();

                if (!temp.getName().equals(name)) {
                    fileOutput += temp.getName() + "\r\n";
                }
            }

            writer.write(fileOutput.trim());
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds stream to locally saved stream list
     *
     * @param name Name of stream to add
     */
    private void addStreamFile(String name) {
        File streamFile = new File(SAVE_DIR + "\\names.txt");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(streamFile, true));
            writer.newLine();
            writer.write(name);
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reads all streams from locally saved stream list
     */
    private void readStreams() {
        File streamDir = new File(SAVE_DIR);
        File streamFile = new File(SAVE_DIR + "\\names.txt");

        try {

            if (streamDir.exists() && streamFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(streamFile));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1) {
                        try {
                            streams.add(new StreamNode(line));
                        } catch (DuplicateStreamException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                }
            } else {
                if (!streamDir.exists()) {
                    streamDir.mkdir();
                }

                if (!streamFile.exists()) {
                    streamFile.createNewFile();
                }

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
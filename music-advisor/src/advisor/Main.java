package advisor;

public class Main {

    public static void main(String[] args) {

        System.out.println("A personal music advisor that utilizes the Spotify API," +
                "makes preference-based suggestions " +
                "and shares links to new releases and featured playlists.");
        Config.reconfigure(args);
        MusicAppControl.run();
    }
}

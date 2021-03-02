package advisor;

public class Main {

    public static void main(String[] args) {
        Config.reconfigure(args);
        new MusicAppControl(new MusicAppConn()).run();
    }
}

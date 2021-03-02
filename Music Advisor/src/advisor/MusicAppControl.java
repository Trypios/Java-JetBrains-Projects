package advisor;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Controller
 */
public class MusicAppControl {

    private final static Scanner sc = new Scanner(System.in);
    private static List<String> pages;
    private static ListIterator<String> iter;
    private static boolean iterForward;
    private final MusicAppConn conn;
    private boolean authorized;
    private boolean active;

    public MusicAppControl(MusicAppConn conn) {
        this.conn = conn;
        this.authorized = false;
        this.active = true;
    }

    /**
     * Main controller. Receives input from user.
     */
    public void run() {
        String input;

        // 1. authorize user
        while (active && !authorized) {
            input = sc.nextLine();
            if ("auth".equals(input)) {
                authorize();
            } else if ("exit".equals(input)){
                active = false;
            } else {
                System.out.println("Please, provide access for application.");
            }
        }
        conn.stopServer();  // server not needed after authorization

        // 2. accept queries
        while (active) {
            input = sc.nextLine();
            authorizedMenu(input);
        }

        // 3. end program
        System.out.println("---GOODBYE!---");
    }

    /**
     * Calls MusicAppConn server to authorize user based on OAuth protocol.
     */
    private void authorize() {
        authorized = conn.OAuth();
    }

    /**
     * Access only to authorized users.
     * @param input A command by the user.
     */
    private void authorizedMenu(String input) {
        SpotifyAPI.setConnection(conn);
        if ("new".equals(input)) {
            newPages(SpotifyAPI.NEW_RELEASES.request());
        } else if ("featured".equals(input)) {
            newPages(SpotifyAPI.FEATURED_RELEASES.request());
        } else if ("categories".equals(input)) {
            newPages(SpotifyAPI.CATEGORIES.request());
        } else if (input.matches("playlists .+")) {
            String categoryID = SpotifyAPI.PLAYLISTS.getCategoryIdByName(input.split("playlists ")[1]);
            SpotifyAPI.PLAYLISTS.setCategoryByID(categoryID);
            newPages(SpotifyAPI.PLAYLISTS.request());
        } else if (input.matches("prev|next")) {
            System.out.println(paginate(input));
        } else if ("exit".equals(input)) {
            active = false;
        } else {
            System.out.println("Invalid command");
        }
    }

    /**
     * Initializes/overwrites pages and sets an iterator.
     * @param list Contains pages received from the latest SpotifyApi json request
     */
    private void newPages(List<String> list) {
        pages = list;
        if (pages != null) {
            iter = pages.listIterator();
            iterForward = true;
            System.out.println(paginate("next"));
        }
    }

    /**
     * Fetches a page from pages list, based on
     * iterator's cursor and given direction
     * @param direction next or prev
     * @return a page from pages
     */
    private String paginate(String direction) {
        if (pages == null || pages.isEmpty()) {
            return "Error, please try again";
        }

        if ("next".equals(direction)) {
            if (!iterForward) {
                iter.next();
                iterForward = true;
            }
            if (iter.hasNext()) {
                int pageNum = iter.nextIndex() + 1;
                String pageContent = iter.next();
                return String.format("%s---PAGE %d OF %d---%n%n", pageContent,
                                                                    pageNum,
                                                                    pages.size());
            }
        } else if ("prev".equals(direction)) {
            if (iterForward) {
                iter.previous();
                iterForward = false;
            }
            if (iter.hasPrevious()) {
                int pageNum = iter.previousIndex() + 1;
                String pageContent = iter.previous();
                return String.format("%s---PAGE %d OF %d---%n%n", pageContent,
                        pageNum,
                        pages.size());
            }
        }
        return "No more pages.";
    }
}

package advisor;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Main controller. Requests input from user.
 */
public class MusicAppControl {

    private final static Scanner sc = new Scanner(System.in);
    private static List<String> pages;
    private static ListIterator<String> iter;
    private static boolean iterForward;
    private static final MusicAppConn conn;
    private static boolean authorized;
    private static boolean active;

    static {
        conn = new MusicAppConn();
        authorized = false;
        active = true;
    }

    /**
     * Run this method to start the application.
     */
    public static void run() {

        // 1. authorize user
        displayOperations(false);
        while (active && !authorized) {
            loginMenu();
        }

        // 2. kill server after authorization
        conn.stopServer();

        // 3. accept queries
        if (active) {
            displayOperations(true);
        }
        while (active) {
            authorizedMenu();
        }
    }

    /**
     * Display the operations available to the user.
     */
    private static void displayOperations(boolean authorized) {

        if (authorized) {
            System.out.println("Available operations: ");
            System.out.println("   1. New releases");
            System.out.println("   2. Featured releases");
            System.out.println("   3. Browse categories");
            System.out.println("   4. Browse playlists by category id");
            System.out.printf("   5. Browse the previous %s results.%n", Config.PAGINATION);
            System.out.printf("   6. Browse the next %s results.%n", Config.PAGINATION);
            System.out.println("   0. exit");
        } else {
            System.out.println("Available operations: ");
            System.out.println("   1. login");
            System.out.println("   0. exit");
        }
    }

    /**
     * Application's initial menu before user authorization.
     */
    private static void loginMenu() {

        String input = sc.nextLine().toLowerCase();

        switch (input) {
            case "1":
                authorize();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Invalid option.");
                displayOperations(false);

        }
    }

    /**
     * Call MusicAppConn server to authorize user based on OAuth protocol.
     */
    private static void authorize() {

        authorized = conn.OAuth();
    }

    /**
     * Application's main menu. Access only to authorized users.
     */
    private static void authorizedMenu() {

        SpotifyAPI.setConnection(conn);

        String input = sc.nextLine().toLowerCase();
        switch (input) {
            case "1":
                newPages(SpotifyAPI.NEW_RELEASES.request());
                break;
            case "2":
                newPages(SpotifyAPI.FEATURED_RELEASES.request());
                break;
            case "3":
                newPages(SpotifyAPI.CATEGORIES.request());
                break;
            case "4":
                System.out.println("Type the category ID: ");
                String inputId = sc.nextLine();
                String categoryID = SpotifyAPI.PLAYLISTS.getCategoryIdByName(inputId);
                SpotifyAPI.PLAYLISTS.setCategoryByID(categoryID);
                newPages(SpotifyAPI.PLAYLISTS.request());
                break;
            case "5":
                System.out.println(paginate("prev"));
                break;
            case "6":
                System.out.println(paginate("next"));
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Invalid command\n");
                displayOperations(true);
        }
    }

    /**
     * Initialize/overwrite pages and set an iterator.
     * @param list Contains pages received from the latest SpotifyApi json request
     */
    private static void newPages(List<String> list) {
        pages = list;
        if (pages != null) {
            iter = pages.listIterator();
            iterForward = true;
            System.out.println(paginate("next"));
        } else {
            System.out.println("Nothing found.");
        }
    }

    /**
     * Fetche a page from pages list, based on
     * iterator's cursor and given direction
     * @param direction next or prev
     * @return a page from pages
     */
    private static String paginate(String direction) {

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

    /**
     * Perform steps to quit the application.
     */
    private static void exit() {

        active = false;
        System.out.println("---GOODBYE!---");
    }

}

package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Spotify json object model
 */
public enum SpotifyAPI {
    NEW_RELEASES("/v1/browse/new-releases"),
    FEATURED_RELEASES("/v1/browse/featured-playlists"),
    CATEGORIES("/v1/browse/categories"),
    PLAYLISTS("/v1/browse/categories");

    private static MusicAppConn conn;
    private String path;

    SpotifyAPI(String apiPath) {
        this.path = apiPath;
    }

    /**
     * A static MusicAppConn needs to be set in order to
     * make json requests from Spotify APIs.
     * @param conn The connection to Spotify
     */
    public static void setConnection(MusicAppConn conn) {

        SpotifyAPI.conn = conn;
    }

    /**
     * Getter
     * @return This API's uri
     */
    public String path() {
        return this.path;
    }

    /**
     * Set the correct uri for PLAYLISTS.
     * @param categoryID The id of the playlist category.
     */
    public void setCategoryByID(String categoryID) {
        SpotifyAPI.PLAYLISTS.path = String.format("%s/%s/playlists", SpotifyAPI.CATEGORIES.path(), categoryID);
    }

    /**
     * Send a json request to fetch all categories
     * and retrieve the category id based on specified name.
     * Only works for PLAYLISTS.
     * @param name The name of a Spotify category
     * @return a Spotify category id
     */
    public String getCategoryIdByName(String name) {

        if (this != PLAYLISTS) {
            return null;
        }
        JsonObject responseJson = conn.apiRequest(this);
        try {
            JsonObject jsonObject = responseJson.get("categories").getAsJsonObject();
            for (JsonElement item : jsonObject.getAsJsonArray("items")) {
                if (name.equals(item.getAsJsonObject().get("name").getAsString())) {
                    return item.getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Category ID not found.");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Make a json request to fetch pages for the controller.
     * @return A List of pages
     */
    public List<String> request() {
        if (conn == null) {
            return null;
        }
        switch (this) {
            case NEW_RELEASES:
                return jsonRequest("albums");
            case FEATURED_RELEASES:
            case PLAYLISTS:
                return jsonRequest("playlists");
            case CATEGORIES:
                return jsonRequest("categories");
            default:
                return null;
        }
    }

    /**
     * Create a list of pages from API results of json requests.
     * @param target The main json attribute
     * @return A list of pages
     */
    private List<String> jsonRequest(String target) {
        List<String> pages = new LinkedList<>();
        StringBuilder page = new StringBuilder();
        int paginationCount = 0;
        JsonObject responseJson = conn.apiRequest(this);

        try {
            JsonObject jsonObject = responseJson.get(target).getAsJsonObject();
            for (JsonElement item : jsonObject.getAsJsonArray("items")) {
                if (paginationCount == Integer.parseInt(Config.PAGINATION.get())) {
                    pages.add(page.toString());
                    page = new StringBuilder();
                    paginationCount = 0;
                }

                page.append(item.getAsJsonObject().get("name").getAsString())
                    .append("\n");

                if (this == NEW_RELEASES) {
                    // fetch artists
                    JsonArray array = item.getAsJsonObject()
                                            .getAsJsonArray("artists");
                    int size = array.size();
                    if (size > 0) {
                        StringBuilder artists = new StringBuilder("[");
                        for (int i = 0; i < size; i++) {
                            artists.append(array.get(i).getAsJsonObject().get("name").getAsString());
                            if (i != size - 1) {
                                artists.append(", ");
                            } else {
                                artists.append("]");
                            }
                        }
                        page.append(artists).append("\n");
                    }
                }

                if (this != CATEGORIES) {
                    // fetch link
                    page.append(item.getAsJsonObject()
                            .get("external_urls")
                            .getAsJsonObject()
                            .get("spotify")
                            .getAsString())
                            .append("\n\n");
                }

                paginationCount++;
            }
            if (page.length() != 0) {
                pages.add(page.toString());
            }
        } catch (Exception e) {
            return null;
        }
        return pages;
    }
}
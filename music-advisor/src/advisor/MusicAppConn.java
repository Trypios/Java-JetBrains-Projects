package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Client-Server connection
 */
public class MusicAppConn {

    private final HttpClient client;
    private HttpServer server;
    private boolean authorized;

    public MusicAppConn() {

        this.client = HttpClient.newBuilder().build();
        this.setServer();
        this.authorized = false;
    }

    /**
     * OAuth protocol authorization for Spotify.
     * @return true if authorized, else false.
     */
    public boolean OAuth() {

        displayAuthLink();
        fetchCode();

        if (!authorized) {
            System.out.println("Authorization failed, please try again.");
            Config.AUTH_CODE.change("");
            return false;
        }

        System.out.println("code received");
        fetchAccessToken();

        if (!authorized) {
            System.out.println("Authorization failed, please try again.");
            Config.ACCESS_TOKEN.change("");
            return false;
        }

        System.out.println("---SUCCESS---");
        return true;
    }

    /**
     * Make a GET request to the given Spotify API.
     * @param api A Spotify API.
     * @return the resulting json object, or null if response not expected.
     */
    public JsonObject apiRequest(SpotifyAPI api) {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.ACCESS_TOKEN)
                .uri(URI.create(Config.API_SERVER + api.path()))
                .GET()
                .build();

        String responseBody = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (responseBody == null) {
            return null;
        }

        return JsonParser.parseString(responseBody)
                .getAsJsonObject();
    }

    /**
     * Kill the server before program ends,
     * or it will have to end forcefully by raising Exception.
     */
    public void stopServer() {

        server.stop(0);
    }

    /**
     * Create a Spotify server for OAuth authorization purposes.
     */
    private void setServer() {

        // set http server
        try {
            server = HttpServer.create();
            // and listener
            server.bind(new InetSocketAddress(45678), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // setup listener
        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String response;
                    if (query != null && query.startsWith("code=")) {
                        Config.AUTH_CODE.change(query.substring(5));
                        response = "Got the code. Return back to Music Advisor program.";
                    } else {
                        response = "Authorization code not found. Try again.";
                        if (query != null && query.startsWith("error=")) {
                            Config.AUTH_CODE.change(query.substring(6));
                        }
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                }
        );
        server.start();
    }

    /**
     * Display the authorization link to the user.
     */
    private void displayAuthLink() {

        // serve link to users
        System.out.println("use this link to request the access code:");
        String body = String.format("?response_type=code&client_id=%s&redirect_uri=%s", Config.CLIENT_ID,
                                                                                        Config.REDIRECT_URI);
        System.out.println(Config.AUTH_SERVER + "/authorize" + body);
    }

    /**
     * Make a POST request to the server,
     * in order to receive Spotify's OAuth authorization code.
     */
    private void fetchCode() {

        System.out.println("waiting for code...");

        String postBody = String.format("response_type=code&client_id=%s&redirect_uri=%s", Config.CLIENT_ID,
                                                                                           Config.REDIRECT_URI);
        // POST request access code from spotify
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Config.REDIRECT_URI.get()))
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        // POST request access_token from spotify
        while (Config.AUTH_CODE.get().isBlank()) {
            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
//                Thread.sleep(1000);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        authorized = !"access_denied".equals(Config.AUTH_CODE.get());
    }

    /**
     * Make a POST request to the server,
     * in order to receive Spotify's OAuth access token
     */
    private void fetchAccessToken() {

        System.out.println("making http request for access_token...");

        String postBody = String.format("grant_type=authorization_code&code=%s" +
                                        "&redirect_uri=%s&client_id=%s&client_secret=%s", Config.AUTH_CODE,
                                                                                          Config.REDIRECT_URI,
                                                                                          Config.CLIENT_ID,
                                                                                          Config.CLIENT_SECRET);
        HttpRequest request = HttpRequest.newBuilder()
                                .header("Content-Type", "application/x-www-form-urlencoded")
                                .uri(URI.create(Config.AUTH_SERVER.get() + "/api/token"))
                                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                                .build();

        String responseBody = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        boolean invalidToken = responseBody == null;

        JsonObject responseJson = JsonParser.parseString(String.valueOf(responseBody)).getAsJsonObject();
        Config.ACCESS_TOKEN.change(responseJson.get("access_token").getAsString());  // may throw Null exception

        if (invalidToken || Config.ACCESS_TOKEN.get() == null) {
            authorized = false;
        }
    }

}


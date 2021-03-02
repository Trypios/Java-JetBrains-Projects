package advisor;

/**
 * Configurations
 */
public enum Config {
    AUTH_SERVER("https://accounts.spotify.com"),
    API_SERVER("https://api.spotify.com"),
    REDIRECT_URI("http://localhost:45678"),  // 127.0.0.1:8080
    CLIENT_ID("e80dfe922489426bb5f5914317032e99"),
    CLIENT_SECRET("2d4d9c743807442a8d1656ba4b7b1ed0"),
    ACCESS_TOKEN(""),
    AUTH_CODE(""),
    PAGINATION("5");

    private String config;

    Config(String config) {
        this.config = config;
    }

    /**
     * Overwrites defaults from terminal (shell) arguments
     */
    public static void reconfigure(String[] args) {
        for (int i = 0; i < args.length - 1; i += 2) {
            if ("-access".equals(args[i])) {
                Config.AUTH_SERVER.change(args[i + 1]);
            } else if ("-resource".equals(args[i])) {
                Config.API_SERVER.change(args[i + 1]);
            } else if ("-page".equals(args[i])) {
                Config.PAGINATION.change(args[i + 1]);
            }
        }
    }

    /**
     * @return configuration value
     */
    public String get() {
        return this.config;
    }

    /**
     * Change configuration's value
     * @param config a new configuration value
     */
    public void change(String config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return this.get();
    }
}


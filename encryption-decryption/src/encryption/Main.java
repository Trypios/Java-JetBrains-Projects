package encryption;

public class Main {

    public static void main(String[] args) {

        if (args.length != 0) {
            Handler.run(args);
        } else {
            Handler.run();
        }
    }
}

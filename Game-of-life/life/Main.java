package life;

/*
    MVC
    ===
*/

public class Main {

    public static void main(String[] args) {
        play();
    }

    public static void play() {
        Universe universe = new Universe();  // model
        GameOfLife window = new GameOfLife();  // view
        Thread controller = new Controller(universe, window);  // controller
        controller.start();
    }
}

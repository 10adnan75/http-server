public class Main {
    public static void main(String[] args) {
        final HTTPServer server = new HTTPServer(4221, 10);
        server.run();
    }
}
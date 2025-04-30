public class Main {

    public static void main(String[] args) {
        final int port = 4221;
        final int threads = 10;
        String directory = null;

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--directory")) {
                directory = args[i + 1];
            }
        }

        HTTPServer server = new HTTPServer(port, threads, directory);
        server.run();
    }
}
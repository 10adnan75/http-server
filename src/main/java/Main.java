public class Main {

    public static void main(String[] args) {
        String directory = null;
        final int port = 4221;
        final int threads = 10;

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--directory")) {
                directory = args[i + 1];
            }
        }

        if (args != null && directory == null) {
            System.out.println("Directory not provided!");
            return;
        }

        HTTPServer server = new HTTPServer(port, threads, directory);
        server.run();
    }
}
public class Main {
    public static void main(String[] args) {
        String directory = null;
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--directory")) {
                directory = args[i + 1];
            }
        }

        if (directory == null) {
            System.out.println("Directory not provided!");
            return;
        }

        HTTPServer server = new HTTPServer(4221, 10, directory);
        server.run();
    }
}
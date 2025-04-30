public class Main {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Directory not provided!");
            System.exit(1);
        }

        String directory = args[0]; 
        int port = 4221;
        int concurrency = 10;

        HTTPServer server = new HTTPServer(port, concurrency, directory);
        server.run();
    }
}
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;
import request.HTTPRequest;
import request.Method;
import response.ContentType;
import response.HTTPResponse;
import response.ResponseCode;

public class HTTPServer {
    private final int port;
    private final ExecutorService executorService;
    private final String directory;

    public HTTPServer(final int port, final int concurrencyLevel, String directory) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(concurrencyLevel);
        this.directory = directory;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleRequest(clientSocket));
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
                final OutputStream outputStream = clientSocket.getOutputStream()) {
            boolean keepAlive = true;

            while (keepAlive && !clientSocket.isClosed()) {
                final HTTPRequest request;
                boolean shouldClose;

                try {
                    request = HTTPRequest.from(bufferedReader);
                    shouldClose = "close".equalsIgnoreCase(request.headers().get("Connection"));
                } catch (IOException | NullPointerException e) {
                    break;
                }

                HTTPResponse.Builder builder = new HTTPResponse.Builder()
                        .withResponseCode(ResponseCode.OK)
                        .withContentType(ContentType.TEXT_PLAIN);

                if (shouldClose) {
                    builder.withContentEncoding(null);
                    builder.body("Closing connection".getBytes());
                    builder.withHeader("Connection", "close");
                }

                HTTPResponse response = builder.build();

                String host = request.headers().getOrDefault("Host", "Unknown");
                String userAgent = request.headers().getOrDefault("User-Agent", "Unknown");
                System.out.println("[INFO] ------------------------------------------------------------------------");
                System.out.println("[INFO] CLIENT REQUEST\n");
                System.out.println("[INFO] 1. HTTP: " + request);
                System.out.println("[INFO] 2. HOST NAME: " + host);
                System.out.println("[INFO] 3. USER AGENT: " + userAgent);
                System.out.println("[INFO] ------------------------------------------------------------------------");

                if (request.getPath().equals("/")) {
                    response = new HTTPResponse.Builder()
                            .withResponseCode(ResponseCode.OK)
                            .build();
                } else if (request.getPath().equals("/user-agent")) {
                    response = new HTTPResponse.Builder()
                            .withResponseCode(ResponseCode.OK)
                            .withContentType(ContentType.TEXT_PLAIN)
                            .body(request.headers().get("User-Agent"))
                            .build();
                } else if (request.getPath().contains("/echo/")) {
                    final String param = request.getPath().split("/echo/")[1];
                    String acceptEncoding = request.headers().getOrDefault("Accept-Encoding", "");
                    boolean clientAcceptsGzip = false;

                    for (String encoding : acceptEncoding.split(",")) {
                        if (encoding.trim().equalsIgnoreCase("gzip")) {
                            clientAcceptsGzip = true;
                            break;
                        }
                    }

                    HTTPResponse.Builder responseBuilder = new HTTPResponse.Builder()
                            .withResponseCode(ResponseCode.OK)
                            .withContentType(ContentType.TEXT_PLAIN);

                    if (clientAcceptsGzip) {
                        byte[] compressed = gzipCompress(param);

                        responseBuilder
                                .withContentEncoding("gzip")
                                .body(compressed);

                    } else {
                        responseBuilder.body(param);
                    }

                    response = responseBuilder.build();
                } else if (request.getMethod() == Method.POST && request.getPath().startsWith("/files/")) {
                    String filename = request.getPath().substring("/files/".length());
                    File file = new File(directory, filename);

                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(request.getBody().getBytes());
                        response = new HTTPResponse.Builder()
                                .withResponseCode(ResponseCode.CREATED)
                                .build();
                    } catch (IOException e) {
                        response = new HTTPResponse.Builder()
                                .withResponseCode(ResponseCode.INTERNAL_SERVER_ERROR)
                                .build();
                    }
                } else if (request.getPath().startsWith("/files/")) {
                    String filename = request.getPath().substring("/files/".length());
                    File file = new File(directory, filename);

                    if (file.exists()) {
                        byte[] fileBytes = Files.readAllBytes(file.toPath());
                        
                        response = new HTTPResponse.Builder()
                                .withResponseCode(ResponseCode.OK)
                                .withContentType(ContentType.OCTET_STREAM)
                                .body(fileBytes)
                                .build();
                    } else {
                        response = new HTTPResponse.Builder()
                                .withResponseCode(ResponseCode.NOT_FOUND)
                                .build();
                    }
                } else {
                    response = new HTTPResponse.Builder()
                            .withResponseCode(ResponseCode.NOT_FOUND)
                            .build();
                }

                outputStream.write(response.serialize());
                outputStream.flush();

                System.out.println("[INFO] SERVER RESPONSE\n\n[INFO] " + response);
                System.out.println("[INFO] ------------------------------------------------------------------------");

                String connectionHeader = request.headers().getOrDefault("Connection", "");

                if (connectionHeader.equalsIgnoreCase("close")) {
                    break;
                }

                if (shouldClose) {
                    keepAlive = false;
                    clientSocket.close();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] gzipCompress(String input) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
            gzipStream.write(input.getBytes());
        }

        return byteStream.toByteArray();
    }
}
package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    private final Method method;
    private final String path;
    private final Map<String, String> headers;

    private HTTPRequest(
            final Method method,
            final String path,
            final Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.headers = headers;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public static HTTPRequest from(final BufferedReader bufferedReader) throws IOException {
        String[] parts = bufferedReader.readLine().split(" ");
        Method method = switch (parts[0]) {
            case "GET" -> Method.GET;
            case "POST" -> Method.POST;
            default -> throw new IllegalArgumentException("Method not allowed");
        };

        String line;
        Map<String, String> headers = new HashMap<>();
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            String[] entry = line.split(" ");
            headers.put(entry[0].replaceAll(":", ""), entry[1]);
        }

        return new HTTPRequest(method, parts[1], headers);
    }
}
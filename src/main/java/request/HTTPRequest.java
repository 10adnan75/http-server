package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    private final Method method;
    private final String path;
    private final String body;
    private final Map<String, String> headers;

    private HTTPRequest(
            final Method method,
            final String path,
            final String body,
            final Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.body = body;
        this.headers = headers;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getBody() {
        return this.body;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> headers() {
        return this.headers;
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
            String[] entry = line.split(":", 2);

            if (entry.length == 2) {
                headers.put(entry[0].trim(), entry[1].trim());
            }
        }

        String contentLengthHeader = headers.get("Content-Length");
        String body = null;

        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader);
            char[] bodyChars = new char[contentLength];
            int read = bufferedReader.read(bodyChars, 0, contentLength);
            body = new String(bodyChars, 0, read);
        }

        return new HTTPRequest(method, parts[1], body, headers);
    }
}
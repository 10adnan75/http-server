package response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {

    private static final byte[] CRLF = "\r\n".getBytes();
    private final ResponseCode responseCode;
    private final ContentType contentType;
    private final String body;
    private final String contentEncoding;
    private final byte[] rawBody;
    private final Map<String, String> additionalHeaders;

    private HTTPResponse(
            final ResponseCode responseCode,
            final ContentType contentType,
            final String body,
            final String contentEncoding,
            final byte[] rawBody,
            final Map<String, String> additionalHeaders) {
        this.responseCode = responseCode;
        this.contentType = contentType;
        this.body = body;
        this.contentEncoding = contentEncoding;
        this.rawBody = rawBody;
        this.additionalHeaders = additionalHeaders;
    }

    @Override
    public String toString() {
        return responseCode.toString();
    }

    public byte[] serialize() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(("HTTP/1.1 " + responseCode.toString()).getBytes(StandardCharsets.UTF_8));
        outputStream.write(CRLF);

        if (contentType != null) {
            outputStream.write(contentType.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
        }

        if (contentEncoding != null) {
            outputStream.write(("Content-Encoding: " + contentEncoding).getBytes());
            outputStream.write(CRLF);
        }

        for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
            outputStream.write((entry.getKey() + ": " + entry.getValue()).getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
        }

        if (rawBody != null) {
            outputStream.write(("Content-Length: " + rawBody.length).getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
            outputStream.write(CRLF);
            outputStream.write(rawBody);
        } else if (body != null) {
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
            outputStream.write(("Content-Length: " + bodyBytes.length).getBytes(StandardCharsets.UTF_8));
            outputStream.write(CRLF);
            outputStream.write(CRLF);
            outputStream.write(bodyBytes);
        } else {
            outputStream.write(CRLF);
        }

        return outputStream.toByteArray();
    }

    public static class Builder {

        private ResponseCode responseCode;
        private ContentType contentType = null;
        private String body = null;
        private String contentEncoding = null;
        private byte[] rawBody = null;
        private Map<String, String> additionalHeaders = new HashMap<>();

        public Builder() {
        }

        public Builder withResponseCode(final ResponseCode responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        public Builder withContentType(final ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder body(final String body) {
            this.body = body;
            return this;
        }

        public Builder withContentEncoding(String contentEncoding) {
            this.contentEncoding = contentEncoding;
            return this;
        }

        public Builder body(final byte[] rawBody) {
            this.rawBody = rawBody;
            return this;
        }

        public Builder withHeader(String key, String value) {
            this.additionalHeaders.put(key, value);
            return this;
        }

        public HTTPResponse build() {
            if (responseCode == null) {
                throw new IllegalArgumentException("HTTPResponse must have a response code!\n");
            }

            return new HTTPResponse(
                    responseCode,
                    contentType,
                    body,
                    contentEncoding,
                    rawBody,
                    additionalHeaders);
        }
    }
}
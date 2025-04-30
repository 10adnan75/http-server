package response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HTTPResponse {

    private static final byte[] CRLF = "\r\n".getBytes();

    private final ResponseCode responseCode;
    private final ContentType contentType;
    private final String body;

    private HTTPResponse(
            final ResponseCode responseCode,
            final ContentType contentType,
            final String body) {
        this.responseCode = responseCode;
        this.contentType = contentType;
        this.body = body;
    }

    @Override
    public String toString() {
        return responseCode.toString();
    }

    public byte[] serialize() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(("HTTP/1.1 " + responseCode.toString()).getBytes());
        outputStream.write(CRLF);

        if (contentType != null) {
            outputStream.write(contentType.toString().getBytes());
            outputStream.write(CRLF);
        }

        if (body != null) {
            outputStream.write(("Content-Length: " + body.length()).getBytes());
            outputStream.write(CRLF);
            outputStream.write(CRLF);
            outputStream.write(body.getBytes());
        } else {
            outputStream.write(CRLF);
        }

        return outputStream.toByteArray();
    }

    public static class Builder {

        private ResponseCode responseCode;
        private ContentType contentType = null;
        private String body = null;

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

        public HTTPResponse build() {
            if (responseCode == null) {
                throw new IllegalArgumentException("HTTPResponse must have a response code!\n");
            }

            return new HTTPResponse(
                    responseCode,
                    contentType,
                    body);
        }
    }
}

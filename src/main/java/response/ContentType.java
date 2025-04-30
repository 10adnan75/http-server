package response;

public enum ContentType {
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    OCTET_STREAM("application/octet-stream");
    
    final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Content-Type: " + contentType;
    }
}
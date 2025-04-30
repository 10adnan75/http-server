package response;

public enum ContentType {
    TEXT_PLAIN("text/plain");

    final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Content-Type: " + contentType;
    }
}
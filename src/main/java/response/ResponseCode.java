package response;

public enum ResponseCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

    final int code;
    final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return code + " " + message;
    }
}

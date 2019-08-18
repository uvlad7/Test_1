public class IncorrectParameterException extends Exception {

    private int code;
    public IncorrectParameterException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

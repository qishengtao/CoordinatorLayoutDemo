package qst.com.coordinatorlayoutdemo.exception;

/**
 * @updateDts 2019/4/2
 */
public class CipherException extends Exception {
    public CipherException(String message) {
        super(message);
    }

    public CipherException(Throwable cause) {
        super(cause);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }
}

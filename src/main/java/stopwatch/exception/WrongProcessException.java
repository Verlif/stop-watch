package stopwatch.exception;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/4/7 9:43
 */
public class WrongProcessException extends RuntimeException {

    public WrongProcessException(String msg) {
        super(msg);
    }
}

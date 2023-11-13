package uploadingfiles.storage;

/**
 * @author someexp
 * @date 2021/5/31
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

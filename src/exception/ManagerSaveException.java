package exception;

import java.io.File;

public class ManagerSaveException extends RuntimeException {
    File file;

    public ManagerSaveException(File file) {
        super();
        this.file = file;
    }

    public ManagerSaveException(String message, File file) {
        super(message);
        this.file = file;
    }

    public ManagerSaveException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
    }

    public ManagerSaveException(Throwable cause, File file) {
        super(cause);
        this.file = file;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Файл: " + file.getAbsolutePath();
    }
}

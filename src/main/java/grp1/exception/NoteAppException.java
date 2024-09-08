package grp1.exception;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class NoteAppException extends RuntimeException{

    private Object[] args;

    public NoteAppException(String message) {
        super(message);
    }

    public NoteAppException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getLocalizedMessage(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(getMessage(), getArgs(), locale);
    }
}

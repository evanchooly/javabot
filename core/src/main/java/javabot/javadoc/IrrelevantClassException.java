package javabot.javadoc;

/**
 * Created Jan 15, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class IrrelevantClassException extends RuntimeException {
    public IrrelevantClassException(final String s) {
        super(s);
    }
}

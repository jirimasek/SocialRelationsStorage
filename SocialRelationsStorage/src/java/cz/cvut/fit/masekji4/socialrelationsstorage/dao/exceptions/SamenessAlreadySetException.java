package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>SamenessAlreadySetException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class SamenessAlreadySetException extends Exception
{

    /**
     * Creates a new instance of <code>SamenessAlreadySetException</code> without detail message.
     */
    public SamenessAlreadySetException()
    {
    }

    /**
     * Constructs an instance of <code>SamenessAlreadySetException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SamenessAlreadySetException(String msg)
    {
        super(msg);
    }
}

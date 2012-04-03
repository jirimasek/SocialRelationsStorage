package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>InvalidRelationException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidRelationException extends Exception
{

    /**
     * Creates a new instance of <code>InvalidRelationException</code> without detail message.
     */
    public InvalidRelationException()
    {
    }

    /**
     * Constructs an instance of <code>InvalidRelationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidRelationException(String msg)
    {
        super(msg);
    }
}

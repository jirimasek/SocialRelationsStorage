package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>RelationAlreadyExistsException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RelationAlreadyExistsException extends Exception
{

    /**
     * Creates a new instance of <code>RelationAlreadyExistsException</code> without detail message.
     */
    public RelationAlreadyExistsException()
    {
    }

    /**
     * Constructs an instance of <code>RelationAlreadyExistsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RelationAlreadyExistsException(String msg)
    {
        super(msg);
    }
}

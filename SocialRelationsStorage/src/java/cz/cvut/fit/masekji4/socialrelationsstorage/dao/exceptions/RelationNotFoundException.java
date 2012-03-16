package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>RelationNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RelationNotFoundException extends Exception
{

    /**
     * Creates a new instance of <code>RelationNotFoundException</code> without detail message.
     */
    public RelationNotFoundException()
    {
    }

    /**
     * Constructs an instance of <code>RelationNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RelationNotFoundException(String msg)
    {
        super(msg);
    }
}

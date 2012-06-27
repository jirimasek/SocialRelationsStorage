package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>NodeNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RelationshipNotFoundException extends Exception
{

    public RelationshipNotFoundException()
    {
    }

    public RelationshipNotFoundException(String msg)
    {
        super(msg);
    }
}

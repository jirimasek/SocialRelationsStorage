package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>RelationNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RelationNotFoundException extends Exception
{

    public RelationNotFoundException()
    {
    }

    public RelationNotFoundException(String msg)
    {
        super(msg);
    }
}

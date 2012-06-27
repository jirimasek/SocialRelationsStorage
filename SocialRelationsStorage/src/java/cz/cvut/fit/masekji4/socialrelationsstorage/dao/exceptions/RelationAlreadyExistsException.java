package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>RelationAlreadyExistsException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RelationAlreadyExistsException extends Exception
{

    public RelationAlreadyExistsException()
    {
    }

    public RelationAlreadyExistsException(String msg)
    {
        super(msg);
    }
}

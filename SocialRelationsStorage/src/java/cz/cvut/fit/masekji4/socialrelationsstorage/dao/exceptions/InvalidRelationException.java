package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>InvalidRelationException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidRelationException extends Exception
{

    public InvalidRelationException()
    {
    }

    public InvalidRelationException(String msg)
    {
        super(msg);
    }
}

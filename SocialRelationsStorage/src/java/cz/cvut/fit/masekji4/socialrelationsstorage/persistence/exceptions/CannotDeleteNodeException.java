package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>CannotDeleteNodeException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class CannotDeleteNodeException extends Exception
{

    public CannotDeleteNodeException()
    {
    }

    public CannotDeleteNodeException(String msg)
    {
        super(msg);
    }
}

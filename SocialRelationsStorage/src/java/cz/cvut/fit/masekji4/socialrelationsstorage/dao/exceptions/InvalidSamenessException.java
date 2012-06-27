package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>InvalidSamenessException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidSamenessException extends Exception
{

    public InvalidSamenessException()
    {
    }

    public InvalidSamenessException(String msg)
    {
        super(msg);
    }
}

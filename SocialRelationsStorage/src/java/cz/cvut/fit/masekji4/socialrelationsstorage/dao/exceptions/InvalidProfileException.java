package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>InvalidProfileException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidProfileException extends Exception
{

    public InvalidProfileException()
    {
    }

    public InvalidProfileException(String msg)
    {
        super(msg);
    }
}

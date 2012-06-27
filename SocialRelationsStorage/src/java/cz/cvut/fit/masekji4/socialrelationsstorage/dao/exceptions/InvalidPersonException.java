package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>InvalidPersonException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidPersonException extends Exception
{

    public InvalidPersonException()
    {
    }

    public InvalidPersonException(String msg)
    {
        super(msg);
    }
}

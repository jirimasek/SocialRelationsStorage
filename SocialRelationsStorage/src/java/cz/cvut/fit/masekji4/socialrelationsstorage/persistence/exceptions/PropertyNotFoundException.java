package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>PropertyNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PropertyNotFoundException extends Exception
{

    public PropertyNotFoundException()
    {
    }

    public PropertyNotFoundException(String msg)
    {
        super(msg);
    }
}

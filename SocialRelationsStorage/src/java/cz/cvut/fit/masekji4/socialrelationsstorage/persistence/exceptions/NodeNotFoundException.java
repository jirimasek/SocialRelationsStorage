package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>NodeNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NodeNotFoundException extends Exception
{

    public NodeNotFoundException()
    {
    }

    public NodeNotFoundException(String msg)
    {
        super(msg);
    }
}

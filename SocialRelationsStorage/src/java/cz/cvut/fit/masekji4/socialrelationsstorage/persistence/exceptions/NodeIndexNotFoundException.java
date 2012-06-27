package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>NodeIndexNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NodeIndexNotFoundException extends Exception
{

    public NodeIndexNotFoundException()
    {
    }

    public NodeIndexNotFoundException(String msg)
    {
        super(msg);
    }
}

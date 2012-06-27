package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>InvalidRelationshipException</code>
 * 
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidRelationshipException extends Exception
{

    public InvalidRelationshipException()
    {
    }

    public InvalidRelationshipException(String msg)
    {
        super(msg);
    }
}

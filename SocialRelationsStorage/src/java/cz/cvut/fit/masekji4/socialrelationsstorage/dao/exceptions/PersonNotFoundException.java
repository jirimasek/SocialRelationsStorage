package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>PersonNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PersonNotFoundException extends Exception
{

    /**
     * Creates a new instance of <code>PersonNotFoundException</code> without
     * detail message.
     */
    public PersonNotFoundException()
    {
    }

    /**
     * Constructs an instance of <code>PersonNotFoundException</code> with
     * the specified detail message.
     * 
     * @param msg the detail message.
     */
    public PersonNotFoundException(String msg)
    {
        super(msg);
    }
}

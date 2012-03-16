package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>PersonAlreadyExistsException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PersonAlreadyExistsException extends Exception
{

    /**
     * Creates a new instance of <code>PersonAlreadyExistsException</code> without detail message.
     */
    public PersonAlreadyExistsException()
    {
    }

    /**
     * Constructs an instance of <code>PersonAlreadyExistsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PersonAlreadyExistsException(String msg)
    {
        super(msg);
    }
}

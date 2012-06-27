package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>PersonAlreadyExistsException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PersonAlreadyExistsException extends Exception
{

    public PersonAlreadyExistsException()
    {
    }

    public PersonAlreadyExistsException(String msg)
    {
        super(msg);
    }
}

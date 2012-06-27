package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * <code>PersonNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PersonNotFoundException extends Exception
{

    public PersonNotFoundException()
    {
    }

    public PersonNotFoundException(String msg)
    {
        super(msg);
    }
}

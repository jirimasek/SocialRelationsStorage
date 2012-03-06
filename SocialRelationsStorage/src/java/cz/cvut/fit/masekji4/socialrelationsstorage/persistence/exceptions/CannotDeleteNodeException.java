package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>CannotDeleteNodeException</code> vzniká v případě, kdy má být
 * smazán uzel, z nějž nebo do nejž vedou nějaké hrany.
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class CannotDeleteNodeException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>CannotDeleteNodeException</code>.
     */
    public CannotDeleteNodeException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>CannotDeleteNodeException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public CannotDeleteNodeException(String msg)
    {
        super(msg);
    }
}

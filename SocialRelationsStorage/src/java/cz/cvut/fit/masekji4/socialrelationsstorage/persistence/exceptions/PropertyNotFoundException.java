package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>PropertyNotFoundException</code> vzniká v situaci, kdy dojde
 * k pokusu o práci s neexistujícím uzlem.
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PropertyNotFoundException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>PropertyNotFoundException</code>.
     */
    public PropertyNotFoundException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>PropertyNotFoundException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public PropertyNotFoundException(String msg)
    {
        super(msg);
    }
}

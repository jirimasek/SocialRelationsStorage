package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>NodeNotFoundException</code> vzniká v situaci, kdy dojde
 * k pokusu o práci s neexistujícím uzlem.
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NodeNotFoundException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>NodeNotFoundException</code>.
     */
    public NodeNotFoundException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>NodeNotFoundException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public NodeNotFoundException(String msg)
    {
        super(msg);
    }
}

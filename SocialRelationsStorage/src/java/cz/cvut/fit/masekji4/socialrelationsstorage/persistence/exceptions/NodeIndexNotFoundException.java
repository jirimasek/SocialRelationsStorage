package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>NodeIndexNotFoundException</code> vzniká v situaci, kdy
 * dojde k pokusu o práci s neexistujícím indexem.
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NodeIndexNotFoundException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>NodeIndexNotFoundException</code>.
     */
    public NodeIndexNotFoundException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>NodeIndexNotFoundException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public NodeIndexNotFoundException(String msg)
    {
        super(msg);
    }
}

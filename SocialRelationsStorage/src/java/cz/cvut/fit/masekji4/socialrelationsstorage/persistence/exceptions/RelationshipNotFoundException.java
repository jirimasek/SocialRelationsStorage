package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>NodeNotFoundException</code> vzniká v situaci, kdy dojde
 * k pokusu o práci s neexistující hranou.
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RelationshipNotFoundException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>RelationshipNotFoundException</code>.
     */
    public RelationshipNotFoundException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>RelationshipNotFoundException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public RelationshipNotFoundException(String msg)
    {
        super(msg);
    }
}

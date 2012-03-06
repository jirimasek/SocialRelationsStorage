package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>InvalidRelationshipException</code> vzniká v situaci, kdy je
 * nově vytvářená hrana nevhodně definována.
 * 
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidRelationshipException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>InvalidRelationshipException</code>.
     */
    public InvalidRelationshipException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>InvalidRelationshipException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public InvalidRelationshipException(String msg)
    {
        super(msg);
    }
}

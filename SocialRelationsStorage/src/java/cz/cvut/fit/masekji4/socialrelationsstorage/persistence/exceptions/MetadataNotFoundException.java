package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>MetadataNotFoundException</code> vzniká v situaci, kdy dojde
 * k pokusu o práci s neexistujícím uzlem.
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class MetadataNotFoundException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>MetadataNotFoundException</code>.
     */
    public MetadataNotFoundException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>MetadataNotFoundException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public MetadataNotFoundException(String msg)
    {
        super(msg);
    }
}

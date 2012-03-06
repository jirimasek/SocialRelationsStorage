package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>InvalidMetadataException</code> vzniká v případě, kdy má být
 * hraně nastavena vlastnost, jejíž hodnotou je <code>null</code> nebo jiný
 * objekt typu JSON.
 * 
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidMetadataException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>InvalidMetadataException</code>.
     */
    public InvalidMetadataException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>InvalidMetadataException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public InvalidMetadataException(String msg)
    {
        super(msg);
    }
}

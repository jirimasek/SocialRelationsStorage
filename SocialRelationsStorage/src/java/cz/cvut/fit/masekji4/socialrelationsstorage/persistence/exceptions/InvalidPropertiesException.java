package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * Výjimka <code>InvalidPropertiesException</code> vzniká v případě, kdy má být
 * uzlu nastavena vlastnost, jejíž hodnotou je <code>null</code> nebo jiný
 * objekt typu JSON.
 * 
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidPropertiesException extends Exception
{

    /**
     * Vytvoří novou instanci výjimky <code>InvalidPropertiesException</code>.
     */
    public InvalidPropertiesException()
    {
    }

    /**
     * Vytvoří novou instanci výjimky <code>InvalidPropertiesException</code>
     * včetně její bližší specifikace.
     * 
     * @param msg       bližší specifikace výjimky
     */
    public InvalidPropertiesException(String msg)
    {
        super(msg);
    }
}

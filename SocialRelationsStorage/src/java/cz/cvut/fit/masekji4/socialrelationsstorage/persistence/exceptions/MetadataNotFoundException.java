package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions;

/**
 * <code>MetadataNotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class MetadataNotFoundException extends Exception
{

    public MetadataNotFoundException()
    {
    }

    public MetadataNotFoundException(String msg)
    {
        super(msg);
    }
}

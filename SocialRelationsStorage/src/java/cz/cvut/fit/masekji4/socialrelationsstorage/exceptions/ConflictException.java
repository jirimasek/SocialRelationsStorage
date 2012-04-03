package cz.cvut.fit.masekji4.socialrelationsstorage.exceptions;

import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 * <code>ConflictException</code>
 * 
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class ConflictException extends WebApplicationException
{

    /**
     * Creates a new instance of <code>ConflictException</code> without detail message.
     */
    public ConflictException()
    {
        super(Response.Status.CONFLICT);
    }

    /**
     * Constructs an instance of <code>ConflictException</code> with the specified detail message.
     * @param th
     */
    public ConflictException(Throwable th) throws JSONException
    {
        super(Response.Status.CONFLICT, th);
    }
}

package cz.cvut.fit.masekji4.socialrelationsstorage.exceptions;

import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 *
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class ForbiddenException extends WebApplicationException
{

    /**
     * Creates a new instance of <code>ForbiddenException</code> without detail message.
     */
    public ForbiddenException()
    {
        super(Response.Status.FORBIDDEN);
    }

    /**
     * Constructs an instance of <code>ForbiddenException</code> with the specified detail message.
     * @param msg
     */
    public ForbiddenException(Throwable th) throws JSONException
    {
        super(Response.Status.FORBIDDEN, th);
    }
}

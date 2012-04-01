package cz.cvut.fit.masekji4.socialrelationsstorage.exceptions;

import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 * <code>NotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NotFoundException extends WebApplicationException
{

    /**
     * Creates a new instance of <code>NotFoundException</code> without detail message.
     */
    public NotFoundException()
    {
        super(Response.Status.NOT_FOUND);
    }

    /**
     * Constructs an instance of <code>NotFoundException</code> with the specified detail message.
     * @param msg
     */
    public NotFoundException(Throwable th) throws JSONException
    {
        super(Response.Status.NOT_FOUND, th);
    }
}

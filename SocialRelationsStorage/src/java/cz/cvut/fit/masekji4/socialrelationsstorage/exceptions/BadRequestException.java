package cz.cvut.fit.masekji4.socialrelationsstorage.exceptions;

import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 * <code>BadRequestException</code>
 * 
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class BadRequestException extends WebApplicationException
{

    /**
     * Creates a new instance of <code>BadRequestException</code> without detail message.
     */
    public BadRequestException()
    {
        super(Response.Status.BAD_REQUEST);
    }

    /**
     * Constructs an instance of <code>BadRequestException</code> with the specified detail message.
     * @param th
     */
    public BadRequestException(Throwable th) throws JSONException
    {
        super(Response.Status.BAD_REQUEST, th);
    }
}

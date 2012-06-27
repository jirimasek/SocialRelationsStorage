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

    public BadRequestException()
    {
        super(Response.Status.BAD_REQUEST);
    }

    public BadRequestException(Throwable th) throws JSONException
    {
        super(Response.Status.BAD_REQUEST, th);
    }
}

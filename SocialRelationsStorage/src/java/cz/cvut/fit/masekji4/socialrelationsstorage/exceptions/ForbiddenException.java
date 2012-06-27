package cz.cvut.fit.masekji4.socialrelationsstorage.exceptions;

import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 * <code>ForbiddenException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class ForbiddenException extends WebApplicationException
{

    public ForbiddenException()
    {
        super(Response.Status.FORBIDDEN);
    }

    public ForbiddenException(Throwable th) throws JSONException
    {
        super(Response.Status.FORBIDDEN, th);
    }
}

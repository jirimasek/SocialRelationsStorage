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

    public NotFoundException()
    {
        super(Response.Status.NOT_FOUND);
    }

    public NotFoundException(Throwable th) throws JSONException
    {
        super(Response.Status.NOT_FOUND, th);
    }
}

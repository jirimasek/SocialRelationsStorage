package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.entities.EntityFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 * <code>NotFoundException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NotFoundException extends WebApplicationException {

    /**
     * Creates a new instance of <code>NotFoundException</code> without detail message.
     */
    public NotFoundException() {
        super();
    }


    /**
     * Constructs an instance of <code>NotFoundException</code> with the specified detail message.
     * @param msg
     */
    public NotFoundException(Throwable th) throws JSONException {
        super(Response.status(
                Response.Status.NOT_FOUND)
                    .entity((new EntityFactory()).serialize(th))
                    .type(MediaType.APPLICATION_JSON)
                    .build()
             );
    }
}

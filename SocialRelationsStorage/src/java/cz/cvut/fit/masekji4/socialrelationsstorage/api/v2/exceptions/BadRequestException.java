package cz.cvut.fit.masekji4.socialrelationsstorage.api.v2.exceptions;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v2.entities.EntityFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;

/**
 * <code>BadRequestException</code>
 * 
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class BadRequestException extends WebApplicationException {

    /**
     * Creates a new instance of <code>BadRequestException</code> without detail message.
     */
    public BadRequestException() {
        super();
    }


    /**
     * Constructs an instance of <code>BadRequestException</code> with the specified detail message.
     * @param th
     */
    public BadRequestException(Throwable th) throws JSONException {
        super(Response.status(
                Response.Status.NOT_FOUND)
                    .entity((new EntityFactory()).serialize(th))
                    .type(MediaType.APPLICATION_JSON)
                    .build()
             );
    }
}

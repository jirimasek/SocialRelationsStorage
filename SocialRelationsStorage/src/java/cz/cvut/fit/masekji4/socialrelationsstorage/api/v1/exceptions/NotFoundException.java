package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
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
     * @param msg the detail message.
     */
    public NotFoundException(String msg) {
        super(Response.status(
                Response.Status.NOT_FOUND)
                    .entity("[ { \"error\" : { \"message\" : \"" + msg +"\" } } ]")
                    .type(MediaType.APPLICATION_JSON)
                    .build()
             );
    }
}

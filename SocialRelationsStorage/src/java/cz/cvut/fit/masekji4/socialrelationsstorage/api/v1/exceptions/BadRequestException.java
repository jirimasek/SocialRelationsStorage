package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class BadRequestException extends WebApplicationException {

    /**
     * Creates a new instance of <code>BadRequestException</code> without detail message.
     */
    public BadRequestException() {
    }


    /**
     * Constructs an instance of <code>BadRequestException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BadRequestException(String msg) {
        super(Response.status(
                Response.Status.BAD_REQUEST)
                    .entity("[ { \"error\" : { \"message\" : \"" + msg +"\" } } ]")
                    .type(MediaType.APPLICATION_JSON)
                    .build()
             );
    }
}

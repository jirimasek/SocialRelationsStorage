package cz.cvut.fit.masekji4.socialrelationsstorage.api.v2;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.NotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.utils.Filter;
import cz.cvut.fit.masekji4.socialrelationsstorage.utils.FilterService;
import java.net.URISyntaxException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.codehaus.jettison.json.JSONException;

/**
 * REST Web Service
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Path("v2")
@RequestScoped
public class APIv2
{

    @Context
    private UriInfo context;
    
    @Inject
    private StorageService storageService;
    
    @Inject
    private FilterService filterService;

    @POST
    @Path("persons")
    @Consumes("application/json")
    @Produces("application/json")
    public String createPerson(String content)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("persons/{uid}")
    @Produces("application/json")
    public String retrievePerson(@PathParam("uid") String uid) throws URISyntaxException, JSONException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PUT
    @Path("persons/{uid}")
    @Consumes("application/json")
    @Produces("application/json")
    public String updatePerson(@PathParam("uid") String uid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @DELETE
    @Path("persons/{uid}")
    @Consumes("application/json")
    @Produces("application/json")
    public String deletePerson(@PathParam("uid") String uid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @POST
    @Path("relations")
    @Consumes("application/json")
    @Produces("application/json")
    public String createRelation(String content)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("relations/{object}")
    @Produces("application/json")
    public String retrieveRelations(@PathParam("object") String object)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("relations/{object}/{relation}")
    @Produces("application/json")
    public String retrieveRelations(@PathParam("object") String object,
            @PathParam("relation") String relation)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("relations/{object}/{relation}/{subject}")
    @Produces("application/json")
    public String retrieveRelation(@PathParam("object") String object,
            @PathParam("relation") String relation,
            @PathParam("subject") String subject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PUT
    @Path("relations")
    //@Produces("application/xml")
    public String updateRelation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @DELETE
    @Path("relations")
    //@Produces("application/xml")
    public String removeRelation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

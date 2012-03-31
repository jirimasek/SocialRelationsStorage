package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.entities.EntityFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.NotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.Filter;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.FilterService;
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
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Path("v1")
@RequestScoped
public class APIv1
{
    
    public static final String APPLICATION_JSON_HAL = "application/hal+json";

    @Context
    private UriInfo context;
    
    @Inject
    private StorageService storageService;
    
    @Inject
    private FilterService filterService;
    
    @Inject
    private EntityFactory entityFactory;

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    
    @POST
    @Path("persons")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(APPLICATION_JSON_HAL)
    public String createPerson(String content)
    {   
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("sources/{source}")
    @Produces(APPLICATION_JSON_HAL)
    public JSONArray retrievePersons(@PathParam("source") String source) throws JSONException, PersonNotFoundException
    {
        try
        {
            List<Person> persons = storageService.retrievePersons("http://" + source);
            
            JSONArray p = new JSONArray();
            
            for (Person person : persons)
            {
                List<Person> alterEgos = storageService.retrieveAlterEgos(person.getId());
                
                p.put(entityFactory.serialize(person, alterEgos));
            }
            
            return p;
        }
        catch (IllegalArgumentException ex)
        {
            throw new BadRequestException(ex);
        }
    }

    @GET
    @Path("persons/{profile}")
    @Produces(APPLICATION_JSON_HAL)
    public JSONObject retrievePerson(@PathParam("profile") String profile) throws JSONException
    {
        try
        {
            String[] array = profile.split(":");
            
            if (array.length != 2)
            {
                // TODO - Add exception message.
                throw new BadRequestException();
            }
            
            Person person = storageService.retrievePerson(array[0], array[1]);
            List<Person> alterEgos = storageService.retrieveAlterEgos(array[0], array[1]);
            
            JSONObject p = entityFactory.serialize(person, alterEgos);
            
            return p;
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
    }

    @PUT
    @Path("persons/{uid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(APPLICATION_JSON_HAL)
    public String updatePerson(@PathParam("uid") String uid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @DELETE
    @Path("persons/{uid}")
    public String deletePerson(@PathParam("uid") String uid)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* ********************************************************************** *
     *                                SAMENESS                                *
     * ********************************************************************** */
    
    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */
    
    @POST
    @Path("relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(APPLICATION_JSON_HAL)
    public String createRelation(String content)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("relations/{object}")
    @Produces(APPLICATION_JSON_HAL)
    public String retrieveRelations(@PathParam("object") String object)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("relations/{object}/{relation}")
    @Produces(APPLICATION_JSON_HAL)
    public String retrieveRelations(@PathParam("object") String object,
            @PathParam("relation") String relation)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("relations/{object}/{relation}/{subject}")
    @Produces(APPLICATION_JSON_HAL)
    public String retrieveRelation(@PathParam("object") String object,
            @PathParam("relation") String relation,
            @PathParam("subject") String subject)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PUT
    @Path("relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(APPLICATION_JSON_HAL)
    public String updateRelation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @DELETE
    @Path("relations")
    public String deleteRelation()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

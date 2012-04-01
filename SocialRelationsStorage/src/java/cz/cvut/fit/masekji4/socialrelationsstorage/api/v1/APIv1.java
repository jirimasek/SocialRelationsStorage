package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data.DataProvider;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.NotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.NumberUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.ForbiddenException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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

    @Context
    private UriInfo context;
    
    @Inject
    private DataProvider dataProvider;

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    
    @POST
    @Path("persons")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createPerson(String content)
    {   
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GET
    @Path("sources/{source}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrievePersons(@PathParam("source") String source)
            throws JSONException, PersonNotFoundException
    {
        try
        {
            return dataProvider.retrievePersons(source);
        }
        catch (IllegalArgumentException ex)
        {
            // TODO - Add exception message.
            throw new BadRequestException(ex);
        }
    }

    /**
     * 
     * @param uid
     * @param fields
     * @return
     * @throws JSONException 
     */
    @GET
    @Path("persons/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrievePerson(@PathParam("uid") String uid, @QueryParam("fields") String fields) throws JSONException
    {
        JSONObject person;
        
        try
        {
            if (NumberUtils.isInt(uid))
            {
                person = dataProvider.retrievePerson(new Integer(uid), fields);
            }
            else
            {
                String[] key = uid.split(":");
                
                if (key.length != 2)
                {
                    // TODO - Add exception message.
                    throw new BadRequestException();
                }
                
                person = dataProvider.retrievePerson(key[0], key[1], fields);
            }
            
            return person;
            
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
    }

    @PUT
    @Path("persons/{uid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject createRelation(String content)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param id
     * @return
     * @throws JSONException 
     */
    @GET
    @Path("relations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrieveRelation(@PathParam("id") Integer id) throws JSONException
    {
        try
        {
            JSONObject relation = dataProvider.retrieveRelation(id);
            
            return relation;
        }
        catch (RelationNotFoundException ex)
        {
            // TODO - Add exception message.
            throw new NotFoundException(ex);
        }
        catch (IllegalAccessException ex)
        {
            // TODO - Add exception message.
            throw new ForbiddenException(ex);
        }
    }

    /**
     * 
     * @param uid
     * @param fields
     * @return
     * @throws JSONException 
     */
    @GET
    @Path("persons/{uid}/relations")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrieveRelations(@PathParam("uid") String uid, @QueryParam("fields") String fields)
            throws JSONException
    {
        JSONObject relations;
        
        try
        {
            if (NumberUtils.isInt(uid))
            {
                relations = dataProvider.retrieveRelations(new Integer(uid), fields);
            }
            else
            {
                String[] key = uid.split(":");
                
                if (key.length != 2)
                {
                    // TODO - Add exception message.
                    throw new BadRequestException();
                }
                
                relations = dataProvider.retrieveRelations(key[0], key[1], fields);
            }
            
            return relations;
            
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
    }

    @PUT
    @Path("relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

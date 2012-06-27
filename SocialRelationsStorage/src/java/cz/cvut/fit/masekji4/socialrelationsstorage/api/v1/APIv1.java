package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data.DataProvider;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters.SourceFilterImpl;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters.SourceFilter;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.NotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidRelationException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidSamenessException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.ConflictException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.ForbiddenException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.ws.rs.core.Response;
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
    @Inject
    @Config
    private String JSON_LD_ID;
    @Context
    private UriInfo context;
    @Inject
    private DataProvider dataProvider;

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    /**
     * 
     * @param person
     * @return
     * @throws JSONException 
     */
    @POST
    @Path("persons")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(JSONObject person) throws JSONException
    {
        try
        {
            JSONObject obj = dataProvider.createPerson(person);

            URI uri = new URI(obj.getString(JSON_LD_ID));
            
            return Response.created(uri).entity(obj).build();
        }
        catch (InvalidProfileException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (PersonAlreadyExistsException ex)
        {
            throw new ConflictException(ex);
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (InvalidPersonException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (URISyntaxException ex)
        {
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
    public JSONObject retrievePerson(@PathParam("uid") String uid,
            @QueryParam("sources") String sources) throws JSONException
    {
        try
        {
            SourceFilter filter = SourceFilterImpl.buildFilter(sources);
            
            JSONObject person = dataProvider.retrievePerson(uid, filter);

            return person;

        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }        
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
    }

    /**
     * 
     * @return
     * @throws JSONException 
     */
    @GET
    @Path("persons")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrievePersons()
            throws JSONException
    {
        return dataProvider.retrievePersons();
    }

    /**
     * 
     * @return 
     */
    @GET
    @Path("sources")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrieveSources()
            throws JSONException
    {    
        return dataProvider.retrieveSources();
    }
    

    /**
     * 
     * @param source
     * @return
     * @throws JSONException
     * @throws PersonNotFoundException 
     */
    @GET
    @Path("sources/{source}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrievePersons(@PathParam("source") String source)
            throws JSONException
    {
        try
        {
            return dataProvider.retrievePersons(source);
        }
        catch (IllegalArgumentException ex)
        {
            throw new BadRequestException(ex);
        }
    }

    /**
     * 
     * @param uid
     * @param person
     * @return
     * @throws JSONException 
     */
    @PUT
    @Path("persons/{uid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject updatePerson(@PathParam("uid") String uid,
            JSONObject person) throws JSONException
    {
        try
        {
            JSONObject obj = dataProvider.updatePerson(uid, person);

            return obj;
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (InvalidPersonException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (InvalidProfileException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
    }

    /**
     * 
     * @param uid 
     */
    @DELETE
    @Path("persons/{uid}")
    public void deletePerson(@PathParam("uid") String uid)
    {
        boolean deleted = dataProvider.deletePerson(uid);

        if (!deleted)
        {
            throw new NotFoundException();
        }
    }

    /* ********************************************************************** *
     *                                SAMENESS                                *
     * ********************************************************************** */
    /**
     * 
     * @param uid
     * @param alterEgo
     * @throws JSONException 
     */
    @POST
    @Path("persons/{uid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response declareSameness(@PathParam("uid") String uid,
            JSONObject alterEgo)
            throws JSONException
    {
        try
        {
            URI sameness = dataProvider.declareSameness(uid, alterEgo);

            if (sameness != null)
            {
                return Response.created(sameness).build();
            }
            else
            {
                throw new ConflictException();
            }
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (InvalidSamenessException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
    }

    /**
     * 
     * @param uid1
     * @param uid2
     * @throws JSONException 
     */
    @DELETE
    @Path("persons/{uid1}/sameas/{uid2}")
    public void refuseSameness(@PathParam("uid1") String uid1,
            @PathParam("uid2") String uid2) throws JSONException
    {
        try
        {
            if (!dataProvider.refuseSameness(uid1, uid2))
            {
                throw new NotFoundException();
            }
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }

    }

    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */
    /**
     * 
     * @param relation
     * @return
     * @throws JSONException 
     */
    @POST
    @Path("relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRelation(JSONObject relation) throws JSONException
    {
        try
        {
            JSONObject obj = dataProvider.createRelation(relation);

            URI uri = new URI(obj.getString("@id"));

            return Response.created(uri).entity(obj).build();
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (RelationAlreadyExistsException ex)
        {
            throw new ConflictException(ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new ForbiddenException(ex);
        }
        catch (InvalidRelationshipException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (RelationNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
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
            throw new NotFoundException(ex);
        }
        catch (IllegalAccessException ex)
        {
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
    public JSONObject retrieveRelations(@PathParam("uid") String uid,
            @QueryParam("sources") String sources)
            throws JSONException
    {
        try
        {
            SourceFilter filter = SourceFilterImpl.buildFilter(sources);
            
            JSONObject relations = dataProvider.retrieveRelations(uid, filter);

            return relations;
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
    }

    /**
     * 
     * @param uid
     * @param type
     * @param sources
     * @return
     * @throws JSONException 
     */
    @GET
    @Path("persons/{uid}/relations/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrieveRelations(@PathParam("uid") String uid,
            @PathParam("type") String type, @QueryParam("sources") String sources)
            throws JSONException
    {
        try
        {
            SourceFilter filter = SourceFilterImpl.buildFilter(sources);
            
            JSONObject relations = dataProvider.retrieveRelations(uid, type, filter);

            return relations;
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
    }

    /**
     * 
     * @param uid1
     * @param type
     * @param uid2
     * @param sources
     * @return 
     */
    @GET
    @Path("persons/{uid1}/relations/{type}/{uid2}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject retrieveRelations(@PathParam("uid1") String uid1,
            @PathParam("type") String type, @PathParam("uid2") String uid2,
            @QueryParam("sources") String sources) throws JSONException
    {
        try
        {
            SourceFilter filter = SourceFilterImpl.buildFilter(sources);
            
            JSONObject relations = dataProvider.retrieveRelations(uid1, uid2, type, filter);

            return relations;
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
    }

    /**
     * 
     * @param id
     * @param relation
     * @return
     * @throws JSONException 
     */
    @PUT
    @Path("relations/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject updateRelation(@PathParam("id") Integer id,
            JSONObject relation) throws JSONException
    {
        try
        {
            return dataProvider.updateRelation(id, relation);
        }
        catch (RelationNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new ForbiddenException(ex);
        }
        catch (PersonNotFoundException ex)
        {
            throw new NotFoundException(ex);
        }
        catch (InvalidRelationshipException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException(ex);
        }
        catch (InvalidRelationException ex)
        {
            throw new BadRequestException(ex);
        }
    }

    /**
     * 
     * @param id
     * @throws JSONException 
     */
    @DELETE
    @Path("relations/{id}")
    public void deleteRelation(@PathParam("id") Integer id) throws JSONException
    {
        try
        {
            if (!dataProvider.deleteRelation(id))
            {
                throw new NotFoundException();
            }
        }
        catch (IllegalAccessException ex)
        {
            throw new ForbiddenException(ex);
        }
    }
}

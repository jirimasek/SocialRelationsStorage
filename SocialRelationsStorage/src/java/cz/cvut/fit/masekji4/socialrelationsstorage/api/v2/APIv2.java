package cz.cvut.fit.masekji4.socialrelationsstorage.api.v2;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.NotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
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
    public Person createPerson(String content)
    {
        return new Person();
    }

    @GET
    @Path("persons/{uid}")
    @Produces("application/json")
    public Person retrievePerson(@PathParam("uid") String uid) throws URISyntaxException, JSONException
    {
        try
        {
            Person person = storageService.getPerson(uid);

            if (person != null)
            {
                return person;
            }
            
            throw new NotFoundException("Not Found");
        }
        finally
        {
        }
    }

    @PUT
    @Path("persons/{uid}")
    @Consumes("application/json")
    @Produces("application/json")
    public String updatePerson(@PathParam("uid") String uid)
    {
        return "";
    }

    @DELETE
    @Path("persons/{uid}")
    @Consumes("application/json")
    @Produces("application/json")
    public String deletePerson(@PathParam("uid") String uid)
    {
        return "";
    }

    @POST
    @Path("relations")
    @Consumes("application/json")
    @Produces("application/json")
    public String createRelation(String content)
    {


        /*
        JSONObject data = new JSONObject(content);
        Relation rel = new Relation();
        
        rel.build(data);
        
        if (!rel.isComplete())
        {
        throw new BadRequestException("Bad Request");
        }
        
        String objPrefix = null;
        String objLogin = null;
        String subPrefix = null;
        String subLogin = null;
        
        Pattern pattern = Pattern.compile(
        "(http|https)://(www.)?facebook.com/([a-zA-Z0-9]+)");
        Matcher matcher = pattern.matcher(rel.getObject().getAbout());
        
        if (matcher.matches())
        {
        matcher = pattern.matcher(rel.getObject().getAbout());
        matcher.find();
        
        objPrefix = "fb";
        objLogin = matcher.group(3);
        }
        
        matcher = pattern.matcher(rel.getSubject().getAbout());
        
        if (matcher.matches())
        {
        matcher = pattern.matcher(rel.getSubject().getAbout());
        matcher.find();
        
        subPrefix = "fb";
        subLogin = matcher.group(3);
        }
        
        PersistenceManager graphDAO = new PersistenceManagerImpl();
        
        URI objURI = graphDAO.getNodeURI(objPrefix + ":" + objLogin);
        
        if (objURI == null)
        {
        objURI = graphDAO.createNode();
        
        URI index = new URI("http://localhost:7474/db/data/node/0");
        
        graphDAO.addRelationship(index, objURI, objPrefix + ":" + objLogin);
        graphDAO.addProperty(objURI, "sioc:note", rel.getSource());
        }
        
        URI subURI = graphDAO.getNodeURI(subPrefix + ":" + subLogin);
        
        if (subURI == null)
        {
        subURI = graphDAO.createNode();
        
        URI index = new URI("http://localhost:7474/db/data/node/0");
        
        graphDAO.addRelationship(index, subURI, subPrefix + ":" + subLogin);
        graphDAO.addProperty(subURI, "sioc:note", rel.getSource());
        }
        
        if (!graphDAO.getRelationURI(objURI, subURI, rel.getRelationship()))
        {
        URI relation = graphDAO.addRelationship(objURI, subURI,
        rel.getRelationship());
        
        graphDAO.addMetadataToProperty(relation, "sioc:note", rel.getSource());
        }
        
        return "[ { \"object\" : \"" + objURI.toString() + "\" , \"subject\" : \"" + subURI.toString() + "\" } ]";
        
         */

        return "";
    }

    @GET
    @Path("relations/{object}")
    @Produces("application/json")
    public List<Relation> retrieveRelations(@PathParam("object") String object)
    {
        try
        {   
            List<Relation> relationships = storageService.getRelationships(object);

            if (relationships != null)
            {
                return relationships;
            }
            
            throw new NotFoundException("Not Found");
        }
        catch (JSONException ex)
        {
            throw new BadRequestException("Bad Request");
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException("Bad Request");
        }
    }

    @GET
    @Path("relations/{object}/{relation}")
    @Produces("application/json")
    public List<Relation> retrieveRelations(@PathParam("object") String object,
            @PathParam("relation") String relation)
    {
        try
        {   
            List<Relation> relationships = storageService.getRelationships(object, relation);

            if (relationships != null)
            {
                return relationships;
            }
            
            throw new NotFoundException("Not Found");
        }
        catch (JSONException ex)
        {
            throw new BadRequestException("Bad Request");
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException("Bad Request");
        }
    }

    @GET
    @Path("relations/{object}/{relation}/{subject}")
    @Produces("application/json")
    public Relation retrieveRelation(@PathParam("object") String object,
            @PathParam("relation") String relation,
            @PathParam("subject") String subject)
    {
        Filter filter = filterService.createFilter(context.getQueryParameters());
        
        try
        {   
            Relation relationship = storageService.getRelationship(object, subject, relation);

            if (relationship != null)
            {
                return relationship;
            }
            
            throw new NotFoundException("Not Found");
        }
        catch (JSONException ex)
        {
            throw new BadRequestException("Bad Request");
        }
        catch (URISyntaxException ex)
        {
            throw new BadRequestException("Bad Request");
        }
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

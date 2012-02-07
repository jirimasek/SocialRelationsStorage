package cz.cvut.fit.masekji4.socialrelationsstorage.api.v2;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.GraphDAO;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.IGraphDAO;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.DataProperty;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.ObjectProperty;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relationship;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.tools.RDFParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

/**
 * REST Web Service
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Path("v2/relations")
public class APIv2
{

    @Context
    private UriInfo context;

    /** Creates a new instance of APIv1 */
    public APIv2()
    {
    }

    /**
     * Retrieves representation of an instance of cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.APIv1
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml()
    {
        return "ok";
    }

    /**
     * POST method for creating an instance of APIv1
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String postXml(String content) throws JSONException, URISyntaxException
    {
        JSONObject data = new JSONObject(content);
        Relationship rel = new Relationship();

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

        IGraphDAO graphDAO = new GraphDAO();

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
    }

    /**
     * Retrieves representation of an instance of cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.APIv1
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    @Path("{id}")
    public String getURI(@PathParam("id") String id) throws URISyntaxException, JSONException
    {
        IGraphDAO graphDAO = new GraphDAO();

        URI uri = graphDAO.getNodeURI(id);

        return "{ \"id\" : \"" + id + "\", \"uri\" : \"" + uri.toString() + "\" }";
    }
}

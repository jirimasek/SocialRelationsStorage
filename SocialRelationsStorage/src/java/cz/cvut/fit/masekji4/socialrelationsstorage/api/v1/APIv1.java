package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManagerImpl;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.DataProperty;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.ObjectProperty;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
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
import org.xml.sax.SAXException;

/**
 * REST Web Service
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Path("v1/relations")
public class APIv1
{

    @Context
    private UriInfo context;

    /** Creates a new instance of APIv1 */
    public APIv1()
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
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * POST method for creating an instance of APIv1
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/xml")
    public void postXml(String content)
    {
    }

    /**
     * PUT method for updating or creating an instance of APIv1
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) throws ParserConfigurationException, SAXException, IOException, URISyntaxException, JSONException
    {
        /*RDFParser parser = new RDFParser();

        List<Person> persons = parser.getPersons(content);

        PersistenceManager graphDAO = new PersistenceManagerImpl();

        for (Person person : persons)
        {
            Pattern pattern = Pattern.compile(
                    "(http|https)://usermap.cvut.cz/profile/([a-zA-Z0-9]+)");
            Matcher matcher = pattern.matcher(person.getAbout());

            if (matcher.matches())
            {
                matcher = pattern.matcher(person.getAbout());
                matcher.find();

                String login = matcher.group(2);

                URI uri = graphDAO.getNodeURI("ctu:" + login);

                if (uri == null)
                {
                    uri = graphDAO.createNode();

                    URI index = new URI("http://localhost:7474/db/data/node/0");

                    graphDAO.createRelation(index, uri, "ctu:" + login);
                }

                graphDAO.addProperty(uri, "foaf:homepage", person.getAbout());

                if (person.getDataProperties() != null)
                {
                    for (DataProperty dataProperty : person.getDataProperties())
                    {
                        graphDAO.addProperty(uri, dataProperty.getName(),
                                dataProperty.getValue());
                    }
                }

                if (person.getObjectProperties() != null)
                {
                    for (ObjectProperty objectProperty : person.
                            getObjectProperties())
                    {
                        matcher = pattern.matcher(objectProperty.getResource());

                        if (matcher.matches())
                        {
                            matcher = pattern.matcher(
                                    objectProperty.getResource());
                            matcher.find();

                            String l = matcher.group(2);

                            URI u = graphDAO.getNodeURI("ctu:" + l);

                            if (u == null)
                            {
                                u = graphDAO.createNode();

                                URI index = new URI(
                                        "http://localhost:7474/db/data/node/0");

                                graphDAO.createRelation(index, u, "ctu:" + l);
                            }

                            if (!graphDAO.getRelation(uri, u,
                                    objectProperty.getName()))
                            {
                                URI relation = graphDAO.createRelation(uri, u,
                                        objectProperty.getName());

                                if (objectProperty.getProperties() != null)
                                {
                                    for (String key : objectProperty.
                                            getProperties().keySet())
                                    {
                                        graphDAO.addMetadataToProperty(relation,
                                                key,
                                                objectProperty.getProperties().
                                                get(key));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }*/
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
        /*PersistenceManager graphDAO = new PersistenceManagerImpl();

        URI uri = graphDAO.getNodeURI(id);

        return "{ \"id\" : \"" + id + "\", \"uri\" : \"" + uri.toString() + "\" }";*/
        
        return "";
    }
}

package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.EntityFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.utils.NamespaceService;
import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONException;

/**
 * Třída <code>StorageServiceImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Stateless
public class StorageServiceImpl implements StorageService
{

    @Inject
    private PersistenceManager pm;
    
    @Inject
    private NamespaceService ns;
    
    @Inject
    private EntityFactory ef;
    
    @Inject
    @Config
    private String PROPERTY_HOMEPAGE;
    
    @Inject
    @Config
    private String RELATION_SAMEAS;
    
    @Inject
    @Config
    private int RELATION_SAMEAS_TRAVERSE_MAX_DEPTH;

    @Override
    public Person getPerson(String uid) throws JSONException, URISyntaxException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Relation> getRelationships(String object) throws JSONException, URISyntaxException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Relation> getRelationships(String object, String relation)
            throws JSONException, URISyntaxException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Relation getRelationship(String object, String subject,
            String relation) throws JSONException, URISyntaxException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean savePerson(Person person)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean saveRelation(Relation relation)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

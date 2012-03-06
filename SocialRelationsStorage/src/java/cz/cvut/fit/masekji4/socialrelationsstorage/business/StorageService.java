package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import java.net.URISyntaxException;
import java.util.List;
import org.codehaus.jettison.json.JSONException;

/**
 * Rozhraní <code>StorageService</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface StorageService
{
    public Person getPerson(String uid) throws JSONException, URISyntaxException;
    
    public List<Relation> getRelationships(String object)
            throws JSONException, URISyntaxException;
    
    public List<Relation> getRelationships(String object, String relation)
            throws JSONException, URISyntaxException;
    
    public Relation getRelationship(String object, String subject, String relation)
            throws JSONException, URISyntaxException;
    
    public boolean savePerson(Person person);
    
    public boolean saveRelation(Relation relation);
}

package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.ConfigurationFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import static org.junit.Assert.*;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManagerImpl;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <code>PersistenceManagerImplTest</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class GraphDAOImplTest
{

    private static final String DATABASE_URI = "http://localhost:7474/db/data";
    
    private static int fbJiriMa5ekId;
    private static int fbBartonekLukasId;
    
    private ConfigurationFactory confFactory;
    
    private PersistenceManager persistence;
    private KeyFactory keyFactory;
    private GraphDAOImpl graphDAO;
    
    private Person fbJiriMa5ek;
    private Person fbBartonekLukas;

    @Before
    public void init() throws URISyntaxException
    {
        confFactory = new ConfigurationFactory();
        
        persistence = new PersistenceManagerImpl(DATABASE_URI);
        keyFactory = new KeyFactory(confFactory.getNamespaces(null));
        graphDAO = new GraphDAOImpl(persistence, keyFactory);
        
        fbJiriMa5ek = new Person();
        
        fbJiriMa5ek.setProfile(new URI("http://www.facebook.com/jirima5ek"));
        fbJiriMa5ek.addSource(new URI("http://www.facebook.com"));
        fbJiriMa5ek.addProperty("foaf:name", "Jiří Mašek");
        
        fbBartonekLukas = new Person();
        
        fbBartonekLukas.setProfile(new URI("http://www.facebook.com/bartonek.lukas"));
        fbBartonekLukas.addSource(new URI("http://www.facebook.com"));
        fbJiriMa5ek.addProperty("foaf:name", "Lukáš Bartoněk");
    }

    @Test
    public void testCreatePerson() throws URISyntaxException, PersonAlreadyExistsException
    {
        System.out.println("Testing creation of person");
        
        graphDAO.createPerson(fbJiriMa5ek);
        
        fbJiriMa5ekId = fbJiriMa5ek.getId();
        
        assertNotNull(fbJiriMa5ek.getId());
        assertNotNull(fbJiriMa5ek.getKey());
        
        graphDAO.createPerson(fbBartonekLukas);
        
        fbBartonekLukasId = fbBartonekLukas.getId();
        
        assertNotNull(fbBartonekLukas.getId());
        assertNotNull(fbBartonekLukas.getKey());
    }

    @Test(expected = PersonAlreadyExistsException.class)
    public void testCreateAlreadyCreatedPerson() throws URISyntaxException, PersonAlreadyExistsException
    {
        System.out.println("Testing creation of already created person");
        
        graphDAO.createPerson(fbJiriMa5ek);
    }
    
    @Test
    public void testRetrievePerson() throws PersonNotFoundException, URISyntaxException
    {
        System.out.println("Testing retrieve of person");
        
        Person person = graphDAO.retrievePerson(fbJiriMa5ekId);
        
        assertEquals(person, fbJiriMa5ek);
        
        Key key = keyFactory.createKey(new URI("http://www.facebook.com/bartonek.lukas"));
        
        person = graphDAO.retrievePerson(key);
        
        assertEquals(person, fbBartonekLukas);
    }
    
    
    
    
    
    
    
    
    
    
    @Test
    public void testDeletePerson() throws URISyntaxException
    {
        System.out.println("Testing deletion of person");
        
        boolean deleted;
        
        deleted = graphDAO.deletePerson(fbJiriMa5ekId);
        
        assertTrue(deleted);
        
        deleted = graphDAO.deletePerson(fbJiriMa5ekId);
        
        assertFalse(deleted);
        
        Key key = keyFactory.createKey(new URI("http://www.facebook.com/bartonek.lukas"));
        
        deleted = graphDAO.deletePerson(key);
        
        assertTrue(deleted);
        
        deleted = graphDAO.deletePerson(key);
        
        assertFalse(deleted);
    }
}

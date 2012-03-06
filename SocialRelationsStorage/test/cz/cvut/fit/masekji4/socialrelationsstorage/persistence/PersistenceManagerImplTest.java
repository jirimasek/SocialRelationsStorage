package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import org.codehaus.jettison.json.JSONException;
import static org.junit.Assert.*;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import java.net.URI;
import java.util.Iterator;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <code>PersistenceManagerImplTest</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PersistenceManagerImplTest
{

    private static final String DATABASE_URI = "http://localhost:7474/db/data";
    
    private PersistenceManagerImpl pm;
    
    private static URI fbJirima5ek;
    private static URI twJirimasek;
    private static URI ctuMasekji4;
    private static URI fbBartonekLukas;
    private static URI twXmorfeus;
    private static URI ctuBartolu5;
    
    private JSONObject fbJirima5ekProperties;
    private JSONObject fbBartonekLukasProperties;
    private JSONObject twJirimasekProperties;
    private JSONObject twXmorfeusProperties;
    private JSONObject ctuMasekji4Properties;
    private JSONObject ctuBartolu5Properties;

    @Before
    public void init() throws JSONException
    {
        pm = new PersistenceManagerImpl(DATABASE_URI);
        
        // Inicializace vlastností
        
        fbJirima5ekProperties = new JSONObject();
        
        fbJirima5ekProperties.put("foaf:homepage", "http://www.facebook.com/jirima5ek");
        fbJirima5ekProperties.put("foaf:name", "Jiří Mašek");
        fbJirima5ekProperties.put("sioc:note", "http://www.facebook.com");
        
        // Inicializace vlastností
        
        fbBartonekLukasProperties = new JSONObject();
        
        fbBartonekLukasProperties.put("foaf:homepage", "http://www.facebook.com/bartonek.lukas");
        fbBartonekLukasProperties.put("foaf:name", "Lukáš Bartoněk");
        fbBartonekLukasProperties.put("sioc:note", "http://www.facebook.com");
        
        twJirimasekProperties = new JSONObject();
        
        twJirimasekProperties.put("foaf:homepage", "http://twitter.com/jirimasek");
        twJirimasekProperties.put("foaf:name", "Jiří Mašek");
        twJirimasekProperties.put("sioc:note", "http://twitter.com");
        
        ctuMasekji4Properties = new JSONObject();
        
        ctuMasekji4Properties.put("foaf:homepage", "http://usermap.cvut.cz/profile/masekji4");
        ctuMasekji4Properties.put("foaf:name", "Jiří Mašek");
        ctuMasekji4Properties.put("sioc:note", "http://usermap.cvut.cz");
        
        twXmorfeusProperties = new JSONObject();
        
        twXmorfeusProperties.put("foaf:homepage", "http://twitter.com/xmorfeus");
        twXmorfeusProperties.put("foaf:name", "Lukáš Bartoněk");
        twXmorfeusProperties.put("sioc:note", "http://twitter.com");
        
        ctuBartolu5Properties = new JSONObject();
        
        ctuBartolu5Properties.put("foaf:homepage", "http://usermap.cvut.cz/profile/bartolu5");
        ctuBartolu5Properties.put("foaf:name", "Lukáš Bartoněk");
        ctuBartolu5Properties.put("sioc:note", "http://usermap.cvut.cz");
    }

    @Test
    public void testCreateNode() throws InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlů");
        
        fbJirima5ek = pm.createNode();

        assertNotNull(fbJirima5ek);
        
        System.out.println("    Uzel: " + fbJirima5ek);
        
        JSONObject emptyProperties = new JSONObject();
        
        fbBartonekLukas = pm.createNode(emptyProperties);

        assertNotNull(fbBartonekLukas);
        
        System.out.println("    Uzel: " + fbBartonekLukas);
    }

    @Test
    public void testCreateNodeWith() throws JSONException, InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlů s parametry");
        
        twXmorfeus = pm.createNode(twXmorfeusProperties);

        assertNotNull(twXmorfeus);
        
        System.out.println("    Uzel: " + twXmorfeus);
    }

    @Test(expected = InvalidPropertiesException.class)
    public void testCreateNodeWithInvalidProperties() throws JSONException, InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlu s invalidními parametry");
        
        JSONObject nestedValue = new JSONObject();
        
        nestedValue.put("key", "value");
        
        JSONObject invalidProperties = new JSONObject();
        
        invalidProperties.put("foaf:name", nestedValue);
        
        pm.createNode(invalidProperties);
    }
    
    @Test
    public void testRetrieveNode() throws NodeNotFoundException, JSONException
    {
        System.out.println("Test získávání informací o uzlech");
        
        JSONObject object = pm.retrieveNode(twXmorfeus.toString());
        
        assertNotNull(object);
        
        assertEquals(twXmorfeus.toString(), object.get("self"));
        
        JSONObject data = (JSONObject) object.get("data");
        
        for (Iterator it = twXmorfeusProperties.keys() ; it.hasNext() ;)
        {
            String key = (String) it.next();
            
            assertTrue(data.has(key));
            assertEquals(data.get(key), twXmorfeusProperties.get(key));
        }
    }
    
    @Test(expected = NodeNotFoundException.class)
    public void testRetrieveNonexistentNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání informací o neexistujících uzlech");
        
        pm.retrieveNode(DATABASE_URI + "/node/9896876997");
    }
    
    @Ignore
    @Test(expected = CannotDeleteNodeException.class)
    public void testDeteleNodeWithRelationships() throws CannotDeleteNodeException
    {
        System.out.println("Test mazání uzlů s hranami");
        
        pm.deleteNode(fbJirima5ek.toString());
    }
    
    @Test
    public void testDeteleNode() throws CannotDeleteNodeException
    {
        System.out.println("Test mazání uzlů");
        
        assertTrue(pm.deleteNode(fbJirima5ek.toString()));
        assertFalse(pm.deleteNode(fbJirima5ek.toString()));
        
        assertTrue(pm.deleteNode(fbBartonekLukas.toString()));
        
        assertTrue(pm.deleteNode(twXmorfeus.toString()));
    }
}

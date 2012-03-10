package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidMetadataException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import org.codehaus.jettison.json.JSONException;
import static org.junit.Assert.*;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
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
    private static URI foafknows;
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

        fbJirima5ekProperties.put("foaf:homepage",
                "http://www.facebook.com/jirima5ek");
        fbJirima5ekProperties.put("foaf:name", "Jiří Mašek");
        fbJirima5ekProperties.put("sioc:note", "http://www.facebook.com");

        // Inicializace vlastností

        fbBartonekLukasProperties = new JSONObject();

        fbBartonekLukasProperties.put("foaf:homepage",
                "http://www.facebook.com/bartonek.lukas");
        fbBartonekLukasProperties.put("foaf:name", "Lukáš Bartoněk");
        fbBartonekLukasProperties.put("sioc:note", "http://www.facebook.com");

        twJirimasekProperties = new JSONObject();

        twJirimasekProperties.put("foaf:homepage",
                "http://twitter.com/jirimasek");
        twJirimasekProperties.put("foaf:name", "Jiří Mašek");
        twJirimasekProperties.put("sioc:note", "http://twitter.com");

        ctuMasekji4Properties = new JSONObject();

        ctuMasekji4Properties.put("foaf:homepage",
                "http://usermap.cvut.cz/profile/masekji4");
        ctuMasekji4Properties.put("foaf:name", "Jiří Mašek");
        ctuMasekji4Properties.put("sioc:note", "http://usermap.cvut.cz");

        twXmorfeusProperties = new JSONObject();

        twXmorfeusProperties.put("foaf:homepage", "http://twitter.com/xmorfeus");
        twXmorfeusProperties.put("foaf:name", "Lukáš Bartoněk");
        twXmorfeusProperties.put("sioc:note", "http://twitter.com");

        ctuBartolu5Properties = new JSONObject();

        ctuBartolu5Properties.put("foaf:homepage",
                "http://usermap.cvut.cz/profile/bartolu5");
        ctuBartolu5Properties.put("foaf:name", "Lukáš Bartoněk");
        ctuBartolu5Properties.put("sioc:note", "http://usermap.cvut.cz");
    }

    /**
     * 
     * @throws InvalidPropertiesException 
     */
    @Test
    public void testCreateNode() throws InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlů");

        // Vytvoření uživatele bez vlastností

        fbJirima5ek = pm.createNode();

        assertNotNull(fbJirima5ek);

        System.out.println("    Uzel: " + fbJirima5ek);

        // Vytvoření uživatele s vlastnostmi, přičemž objekt reprezentující
        // vlasntosti je prázdný

        JSONObject emptyProperties = new JSONObject();

        fbBartonekLukas = pm.createNode(emptyProperties);

        assertNotNull(fbBartonekLukas);

        System.out.println("    Uzel: " + fbBartonekLukas);
    }

    @Test
    public void testCreateNodeWithProperties() throws JSONException, InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlů s parametry");

        // Vytvoření uzlu s vlastnostmi

        twXmorfeus = pm.createNode(twXmorfeusProperties);

        assertNotNull(twXmorfeus);

        System.out.println("    Uzel: " + twXmorfeus);
    }

    @Test(expected = InvalidPropertiesException.class)
    public void testCreateNodeWithInvalidProperties() throws JSONException, InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlu s invalidními parametry");

        // Vytvoření uzlu s vlastnostmi, přičemž objekt reprezentující vlasnosti
        // obsahuje vnožený objekt

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

        // Ověření, že se URI uzlu shodují

        assertEquals(twXmorfeus.toString(), object.get("self"));

        // Ověření, že se shodují vlastnosti uzlu

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

    @Test
    public void testRetrieveProperties() throws NodeNotFoundException, JSONException
    {
        System.out.println("Test získávání vlastností uzlu");

        JSONObject properties = pm.retrieveProperties(twXmorfeus.toString());

        for (Iterator it = twXmorfeusProperties.keys() ; it.hasNext() ;)
        {
            String key = (String) it.next();

            assertTrue(properties.has(key));
            assertEquals(properties.get(key), twXmorfeusProperties.get(key));
        }
    }

    @Test(expected = NodeNotFoundException.class)
    public void testRetrievePropertiesOfNonexistentNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání vlastností neexistujícího uzlu");

        pm.retrieveProperties(DATABASE_URI + "/node/9896876997");
    }

    @Test
    public void testRetrieveProperty() throws PropertyNotFoundException, JSONException
    {
        System.out.println("Test získávání vlastnosti uzlu");

        String property = pm.retrieveProperty(twXmorfeus.toString(), "foaf:name");

        assertEquals(property, twXmorfeusProperties.get("foaf:name"));
    }

    @Test(expected = PropertyNotFoundException.class)
    public void testRetrieveNonexistentProperty() throws PropertyNotFoundException
    {
        System.out.println("Test získávání neexistující vlastnosti uzlu");

        pm.retrieveProperty(fbJirima5ek.toString(), "foaf:name");
    }

    @Test(expected = InvalidPropertiesException.class)
    public void testAddInvalidProperties() throws InvalidPropertiesException,
            JSONException, NodeNotFoundException
    {
        System.out.println("Test přidání invalidních parametrů uzlu");

        // Přidání vlastností uzlu, přičemž objekt reprezentující vlasnosti
        // obsahuje vnožený objekt

        JSONObject nestedValue = new JSONObject();

        nestedValue.put("key", "value");

        JSONObject invalidProperties = new JSONObject();

        invalidProperties.put("foaf:name", nestedValue);

        pm.addProperties(fbJirima5ek.toString(), invalidProperties);
    }

    @Test(expected = NodeNotFoundException.class)
    public void testAddPropertiesToNonexistingNode() throws InvalidPropertiesException, NodeNotFoundException
    {
        pm.addProperties(DATABASE_URI + "/node/9896876997",
                fbJirima5ekProperties);
    }

    @Test
    public void testAddProperties() throws InvalidPropertiesException, NodeNotFoundException, JSONException
    {
        System.out.println("Test přidávání vlastností uzlu");

        pm.addProperties(fbJirima5ek.toString(), fbJirima5ekProperties);

        JSONObject properties = pm.retrieveProperties(fbJirima5ek.toString());

        for (Iterator it = fbJirima5ekProperties.keys() ; it.hasNext() ;)
        {
            String key = (String) it.next();

            assertTrue(properties.has(key));
            assertEquals(properties.get(key), fbJirima5ekProperties.get(key));
        }
    }

    @Test(expected = PropertyNotFoundException.class)
    public void testDeleteProperty() throws PropertyNotFoundException
    {
        boolean deleted = pm.deleteProperty(fbJirima5ek.toString(), "foaf:name");

        assertTrue(deleted);

        pm.retrieveProperty(fbJirima5ek.toString(), "foaf:name");
    }

    @Test
    public void testDeleteNonexistingProperty()
    {
        boolean deleted = pm.deleteProperty(fbJirima5ek.toString(), "foaf:name");

        assertFalse(deleted);
    }

    @Test
    public void testDeleteProperties() throws NodeNotFoundException
    {
        pm.deleteProperties(fbJirima5ek.toString());

        JSONObject properties = pm.retrieveProperties(fbJirima5ek.toString());

        assertTrue(properties.length() == 0);
    }

    @Test(expected = InvalidRelationshipException.class)
    public void testCreateInvalidRelationship_1() throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException, RelationshipNotFoundException
    {
        System.out.println("Test vytváření hrany bez definice typu vztahu");

        pm.createRelationship(fbJirima5ek.toString(), fbBartonekLukas.toString(),
                "");
    }

    @Test(expected = NodeNotFoundException.class)
    public void testCreateInvalidRelationship_2() throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany z neexistujícího uzlu");

        pm.createRelationship(DATABASE_URI + "/node/9896876997",
                fbBartonekLukas.toString(), "foaf:knows");
    }

    @Test(expected = InvalidRelationshipException.class)
    public void testCreateInvalidRelationship_3() throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany do neexistujícího uzlu");

        pm.createRelationship(fbJirima5ek.toString(),
                DATABASE_URI + "/node/9896876997", "foaf:knows");
    }

    @Test(expected = InvalidRelationshipException.class)
    public void testCreateRelationshipWithInvalid() throws JSONException,
            InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany s invalidními metadaty");

        // Vytvoření hrany s metadaty, přičemž objekt reprezentující metadata
        // obsahuje vnožený objekt

        JSONObject nestedValue = new JSONObject();

        nestedValue.put("key", "value");

        JSONObject invalidMetadata = new JSONObject();

        invalidMetadata.put("foaf:name", nestedValue);

        pm.createRelationship(fbJirima5ek.toString(),
                DATABASE_URI + "/node/9896876997", "foaf:knows", invalidMetadata);
    }

    @Test
    public void testCreateRelationship() throws InvalidMetadataException,
            JSONException, InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany");

        foafknows = pm.createRelationship(fbJirima5ek.toString(),
                fbBartonekLukas.toString(), "foaf:knows");
    }
    
    
    
    
    
    
    
    
    
    
    
    

    @Test(expected = CannotDeleteNodeException.class)
    public void testDeteleNodeWithRelationships() throws CannotDeleteNodeException
    {
        System.out.println("Test mazání uzlů s hranami");

        pm.deleteNode(fbJirima5ek.toString());
    }

    @Test
    public void testDeleteRelationship()
    {
        System.out.println("Test mazání hrany");

        boolean deleted = pm.deleteRelationship(foafknows.toString());
        
        assertTrue(deleted);
    }

    @Test
    public void testDeleteNonexistRelationship()
    {
        System.out.println("Test mazání neexistující hrany");

        boolean deleted = pm.deleteRelationship(foafknows.toString());
        
        assertFalse(deleted);
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

package cz.cvut.fit.masekji4.socialrelationsstorage.dao.tools;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.DataProperty;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.ObjectProperty;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class RDFParser
{

    private Document parse(String html) throws SAXException, IOException,
            ParserConfigurationException
    {

        // Step 1: create a DocumentBuilderFactory

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(html));

        // Step 2: create a DocumentBuilder

        DocumentBuilder db = dbf.newDocumentBuilder();

        db.setEntityResolver(new EntityResolver()
        {

            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException
            {
                if (systemId.contains("xhtml1-strict.dtd"))
                {
                    return new InputSource(new StringReader(""));
                }
                else
                {
                    return null;
                }
            }
        });

        // Step 3: parse the input file to get a Document object
        try
        {
            return db.parse(is);
        }
        catch (SAXException ex)
        {
            Logger.getLogger(Parser.class.getName()).
                    log(Level.SEVERE, html, ex);

            throw ex;
        }
    }

    public List<Person> getPersons(String rdf) throws SAXException, IOException, ParserConfigurationException
    {
        List<Person> output = new ArrayList<Person>();
            
        Document document = parse(rdf);
        
        NodeList persons = document.getElementsByTagName("foaf:Person");
        
        for (int i = 0 ; i < persons.getLength() ; i++)
        {
            
            Node person = persons.item(i);
            
            NamedNodeMap attrs = person.getAttributes();
            
            try
            {
                Person p = new Person(attrs.getNamedItem("rdf:about").getTextContent());
                
                for (int j = 0 ; j < person.getChildNodes().getLength() ; j++)
                {
                    Node property = person.getChildNodes().item(j);
                    
                    attrs = property.getAttributes();
                    
                    Node res = attrs.getNamedItem("rdf:resource");
                    
                    if (res != null)
                    {
                        String name = property.getNodeName();
                        String resource = res.getTextContent();
                        
                        ObjectProperty op = new ObjectProperty(name, resource);
                        
                        for (int k = 0 ; k < attrs.getLength(); k++)
                        {
                            Node attr = attrs.item(k);
                            
                            if (!attr.getNodeName().equals("rdf:resource"))
                            {
                                String n = attr.getNodeName();
                                String v = attr.getTextContent();
                            
                                op.addProperty(n, v);
                            }
                        }
                        
                        p.addObjectProperty(op);
                    }
                    else
                    {
                        Node type = attrs.getNamedItem("rdf:datatype");
                        
                        String name = property.getNodeName();
                        String datatype = type.getNodeValue();
                        String value = property.getTextContent();
                        
                        p.addDataProperty(new DataProperty(name, datatype, value));
                    }
                
                }
                
                output.add(p);
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }

        return output;
    }
}

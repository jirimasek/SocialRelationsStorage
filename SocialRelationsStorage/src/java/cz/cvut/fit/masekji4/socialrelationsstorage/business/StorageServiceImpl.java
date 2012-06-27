package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters.SourceFilter;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.CollectionUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Namespace;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * <code>StorageServiceImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class StorageServiceImpl implements StorageService
{
    
    @Inject
    @Config
    private Map<String, Namespace> NAMESPACES;
    @Inject
    private GraphDAO graphDAO;

    // <editor-fold defaultstate="collapsed" desc="Persons">
    @Override
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException
    {
        return graphDAO.createPerson(person);
    }

    @Override
    public Person retrievePerson(Integer id) throws PersonNotFoundException
    {
        return graphDAO.retrievePerson(id);
    }

    @Override
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException
    {
        Key key = new Key();

        key.setPrefix(prefix);
        key.setUsername(username);

        return graphDAO.retrievePerson(key);
    }

    @Override
    public List<Person> retrievePersons()
    {
        return graphDAO.retrievePersons();
    }

    @Override
    public List<Person> retrievePersons(String source)
    {
        try
        {
            return graphDAO.retrievePersons(new URI(source));
        }
        catch (URISyntaxException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public List<Person> retrieveAlterEgos(Integer id, SourceFilter filter)
            throws PersonNotFoundException
    {
        Path alterEgos = graphDAO.retrieveAlterEgos(id);

        return processAlterEgos(alterEgos, filter);
    }

    @Override
    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException
    {
        return graphDAO.updatePerson(person);
    }

    @Override
    public boolean deletePerson(Integer id)
    {
        return graphDAO.deletePerson(id);
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Sameness">
    @Override
    public boolean declareSameness(Integer person, Integer alterEgo,
            List<URI> sources)
            throws PersonNotFoundException
    {
        return graphDAO.declareSameness(person, alterEgo, sources);
    }

    @Override
    public boolean refuseSameness(Integer person, Integer alterEgo)
            throws PersonNotFoundException
    {
        return graphDAO.refuseSameness(person, alterEgo);
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Relations">
    @Override
    public Integer createRelation(Relation relation) throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException
    {
        return graphDAO.createRelation(relation);
    }

    @Override
    public Relation retrieveRelation(Integer id)
            throws RelationNotFoundException, IllegalAccessException
    {
        return graphDAO.retrieveRelation(id);
    }

    @Override
    public List<Relation> retrieveRelations(Integer id, SourceFilter filter)
            throws PersonNotFoundException
    {

        List<Relation> relations = graphDAO.retrieveRelations(id,
                DirectionEnum.ALL);

        return processRelations(relations, filter);
    }

    @Override
    public List<Relation> retrieveRelations(Integer id, String type,
            SourceFilter filter) throws PersonNotFoundException
    {
        List<Relation> relations = graphDAO.retrieveRelations(id,
                DirectionEnum.ALL, type);

        return processRelations(relations, filter);
    }

    @Override
    public List<Relation> retrieveRelations(Integer object, Integer subject,
            String type, SourceFilter filter) throws PersonNotFoundException
    {
        List<Relation> relations = graphDAO.retrieveRelations(object,
                DirectionEnum.ALL, type);

        relations = processRelations(relations, filter);

        List<Relation> list = new LinkedList<Relation>();

        for (Relation relation : relations)
        {
            if (relation.getObject().equals(subject) || relation.getSubject().
                    equals(subject))
            {
                list.add(relation);
            }
        }

        return list;
    }

    @Override
    public Integer updateRelation(Relation relation)
            throws PersonNotFoundException, IllegalAccessException
    {
        return graphDAO.updateRelation(relation);
    }

    @Override
    public boolean deleteRelation(Integer id) throws IllegalAccessException
    {
        return graphDAO.deleteRelation(id);
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Sources">
    @Override
    public List<URI> retrieveSources()
    {
        List<URI> sources = new LinkedList<URI>();
        
        for (Namespace namespace : NAMESPACES.values())
        {
            try
            {
                URI uri = new URI(namespace.getUri());
                
                sources.add(uri);
            }
            catch (URISyntaxException ex)
            {
            }
        }
        
        return sources;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    private List<Relation> processRelations(List<Relation> relations,
            SourceFilter filter)
    {
        List<Relation> list = new LinkedList<Relation>();

        for (Relation relation : relations)
        {
            if (filter.isAcceptable(relation.getSources()))
            {
                list.add(relation);
            }
        }

        return list;
    }

    private List<Person> processAlterEgos(Path alterEgos, SourceFilter filter)
    {
        List<Person> list = new LinkedList<Person>();

        if (!CollectionUtils.isNullOrEmpty(alterEgos.getPersons()))
        {
            Stack<Iterator<Path>> stack = new Stack<Iterator<Path>>();

            Iterator<Path> iterator = alterEgos.getPersons().iterator();

            while (iterator.hasNext())
            {
                Path path = iterator.next();

                if (filter.isAcceptable(path.getSources()))
                {
                    list.add(path.getPerson());

                    if (path.getPersons() != null && !path.getPersons().isEmpty())
                    {
                        stack.add(iterator);

                        iterator = path.getPersons().iterator();
                    }

                    if (!iterator.hasNext() && !stack.empty())
                    {
                        iterator = stack.pop();
                    }
                }
            }
        }

        return list;
    }// </editor-fold>
}

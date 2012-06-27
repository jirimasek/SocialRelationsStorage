package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data;

import cz.cvut.fit.masekji4.socialrelationsstorage.common.StringUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 *
 * @author Jiří Mašek <email@jirimasek.cz>
 */
/**
 * Class <code>JsonBuilder</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class UriBuilder
{

    @Inject
    @Config
    private String ROOT_URI;
    @Inject
    @Config
    private String API_V1_URI;
    @Inject
    @Config
    private String API_V1_PERSONS;
    @Inject
    @Config
    private String API_V1_RELATIONS;
    @Inject
    @Config
    private String API_V1_SOURCES;
    
    /**
     * 
     * @param uid
     * @return 
     */
    public String getPersonURI(String uid)
    {
        String uri = String.format("%s%s%s/%s", ROOT_URI, API_V1_URI,
                API_V1_PERSONS, uid);

        return uri;
    }
    
    /**
     * 
     * @return 
     */
    public String getPersonsURI()
    {   
        String uri = String.format("%s%s%s", ROOT_URI, API_V1_URI,
                API_V1_PERSONS);
        
        return uri;
    }

    /**
     * 
     * @param id
     * @return 
     */
    public String getRelationURI(Integer id)
    {
        String uri = String.format("%s%s%s/%d", ROOT_URI, API_V1_URI,
                API_V1_RELATIONS, id);

        return uri;
    }

    /**
     * 
     * @param personURI
     * @return 
     */
    public String getRelationsURI(String object, String type, String subject)
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append(ROOT_URI);
        sb.append(API_V1_URI);
        sb.append(API_V1_PERSONS);
        
        sb.append("/");
        sb.append(object);
        
        sb.append(API_V1_RELATIONS);
        
        if (!StringUtils.isNullOrEmpty(type))
        {
            sb.append("/");
            sb.append(type);
        }
        
        if (!StringUtils.isNullOrEmpty(subject))
        {
            sb.append("/");
            sb.append(subject);
        }

        return sb.toString();
    }

    /**
     * 
     * @param person
     * @param alterEgo
     * @return 
     */
    public String getSamenessURI(String person, String alterEgo)
    {
        String uri = String.format("%s%s%s/%s/sameas/%s", ROOT_URI, API_V1_URI,
                API_V1_PERSONS, person, alterEgo);

        return uri;
    }

    /**
     * 
     * @return 
     */
    public String getSourcesURI()
    {
        String uri = String.format("%s%s%s", ROOT_URI, API_V1_URI,
                API_V1_SOURCES);

        return uri;
    }

    /**
     * 
     * @param source
     * @return 
     */
    public String getSourceURI(String source)
    {
        String uri = String.format("%s%s%s/%s", ROOT_URI, API_V1_URI,
                API_V1_SOURCES, source);

        return uri;
    }
}

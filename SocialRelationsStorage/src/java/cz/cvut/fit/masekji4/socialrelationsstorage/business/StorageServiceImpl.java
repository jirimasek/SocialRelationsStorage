package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.GraphDAO;
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
    private GraphDAO graphDAO;
}

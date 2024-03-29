package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.codehaus.jettison.json.JSONObject;

/**
 * <code>JSONProvider</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class JSONProvider implements MessageBodyWriter<JSONObject>
{

    private final String encoding = "UTF-8";

    private String serialize(JSONObject json)
    {
        return json.toString().replaceAll("\\\\/", "/");
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType)
    {
        if (JSONObject.class.isAssignableFrom(type)
                && mediaType.getType().equals("application")
                && mediaType.getSubtype().equals("json"))
        {
            return true;
        }

        return false;
    }

    @Override
    public long getSize(JSONObject t,
            Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType)
    {

        String message = serialize(t);

        try
        {
            byte[] bytes = message.getBytes(encoding);

            return bytes.length;
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalStateException(
                    String.format("No %s: %s", encoding, e.getMessage()));
        }
    }

    @Override
    public void writeTo(JSONObject t,
            Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException, WebApplicationException
    {
        OutputStreamWriter ow = new OutputStreamWriter(entityStream, encoding);     
        
        String message = serialize(t);

        ow.write(message);
        ow.flush();
    }
}

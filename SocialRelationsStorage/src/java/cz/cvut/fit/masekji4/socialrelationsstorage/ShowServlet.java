package cz.cvut.fit.masekji4.socialrelationsstorage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@WebServlet(name = "ShowServlet", urlPatterns =
{
    "/show"
})
public class ShowServlet extends HttpServlet
{

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException, URISyntaxException, JSONException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try
        {
        }
        finally
        {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            try
            {
                processRequest(request, response);
            }
            catch (JSONException ex)
            {
                Logger.getLogger(ShowServlet.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        catch (URISyntaxException ex)
        {
            Logger.getLogger(ShowServlet.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            try
            {
                processRequest(request, response);
            }
            catch (JSONException ex)
            {
                Logger.getLogger(ShowServlet.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        catch (URISyntaxException ex)
        {
            Logger.getLogger(ShowServlet.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}

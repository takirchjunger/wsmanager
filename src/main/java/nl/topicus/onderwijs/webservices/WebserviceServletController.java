package nl.topicus.onderwijs.webservices;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.servlet.ServletController;

public class WebserviceServletController extends ServletController
{

	private final static String MANAGER_RELATIVE_PATH = DEFAULT_LISTINGS_CLASSIFIER + "/manager";

	public WebserviceServletController(final DestinationRegistry destinationRegistry,
			final ServletConfig config, final HttpServlet serviceListGenerator)
	{
		super(destinationRegistry, config, serviceListGenerator);
	}

	@Override
	public void invoke(final HttpServletRequest request, final HttpServletResponse res)
			throws ServletException
	{
		if (request.getRequestURI().endsWith(MANAGER_RELATIVE_PATH)
			|| request.getRequestURI().endsWith(MANAGER_RELATIVE_PATH + "/"))
			try
			{
				res.getWriter().write("Manager not yet implemented");
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		else
			super.invoke(request, res);
	}

	/*
	 * @Override protected void generateServiceList(HttpServletRequest request,
	 * HttpServletResponse response) throws IOException {
	 * response.setContentType("text/html; charset=UTF-8"); response .getWriter() .write(
	 * "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
	 * ); response.getWriter().write(
	 * "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head>");
	 * response.getWriter().write(
	 * "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/>");
	 * response.getWriter().write("<title>Managed webservices</title>");
	 * response.getWriter().write(
	 * "<link type=\"text/css\" rel=\"stylesheet\" href=\"css/wsm.css\"/>");
	 * response.getWriter().write("</head><body>");
	 * response.getWriter().write("<img src=\"img/topicus.jpg\" alt=\"topicus\"/>");
	 * response.getWriter().write(
	 * String.format("<br/>There are %d managed webservices running", wsManager
	 * .getRunningEndpoints().entrySet().size())); response.getWriter().write(
	 * "<table><thead><tr><th>Webservice</th><th>Status</th></tr></thead>");
	 * response.getWriter().write("<tbody>"); for (Entry<String, Object> service :
	 * wsManager.getManagedWebServices().entrySet()) { response.getWriter().write("<tr>");
	 * String name =
	 * service.getValue().getClass().getAnnotation(ManagedWebservice.class).serviceName();
	 * String isRunning = wsManager.isRunning(name) ? "enabled" : "disabled";
	 * response.getWriter().write(String.format("<td>%s</td><td>%s</td>", name,
	 * isRunning)); response.getWriter().write("</tr>"); }
	 * response.getWriter().write("</tbody></table>");
	 * response.getWriter().write("</body></html>"); }
	 * @Override protected void generateUnformattedServiceList(HttpServletRequest request,
	 * HttpServletResponse response) throws IOException { // Geen gedoe met wel of niet
	 * gestylede service list pagina generateServiceList(request, response); }
	 */

}

package nl.topicus.onderwijs.webservices;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.topicus.onderwijs.webservices.annotations.ManagedWebservice;

import org.apache.cxf.Bus;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.cxf.transport.servlet.ServletTransportFactory;

public class WebserviceServletController extends ServletController
{

	private final WebserviceManager wsManager;

	private final static String MANAGER_RELATIVE_PATH = DEFAULT_LISTINGS_CLASSIFIER + "/manager";

	public WebserviceServletController(final WebserviceManager wsManager,
			final ServletTransportFactory df, final ServletConfig config,
			final ServletContext context, final Bus b)
	{
		super(df, config, context, b);
		this.wsManager = wsManager;
	}

	@Override
	public void invoke(HttpServletRequest request, HttpServletResponse res) throws ServletException
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

	@Override
	protected void generateServiceList(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		response.setContentType("text/html; charset=UTF-8");

		response.getWriter().write(
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
				+ "\"http://www.w3.org/TR/html4/loose.dtd\">");
		response.getWriter().write("<html><head><title>Managed webservices</title>");
		response.getWriter().write(
			"<link type=\"text/css\" rel=\"stylesheet\" href=\"css/wsm.css\">");
		response.getWriter().write("</head><body>");
		response.getWriter().write("<img src=\"img/topicus.jpg\" alt=\"topicus\"/>");
		response.getWriter().write(
			String.format("<br/>There are %d managed webservices running", wsManager
				.getRunningEndpoints().entrySet().size()));
		response.getWriter().write("<ul>");
		for (Entry<String, Object> service : wsManager.getManagedWebServices().entrySet())
		{
			String name =
				service.getValue().getClass().getAnnotation(ManagedWebservice.class).serviceName();
			String isRunning = wsManager.isRunning(name) ? "enabled" : "disabled";
			response.getWriter().write(String.format("<li>%s (%s)</li>", name, isRunning));
		}
		response.getWriter().write("<ul>");
		response.getWriter().write("</body></html>");
	}

	@Override
	protected void generateUnformattedServiceList(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		// Geen gedoe met wel of niet gestylede service list pagina
		generateServiceList(request, response);
	}

}

package nl.topicus.onderwijs.webservices;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebserviceServlet extends CXFServlet
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(WebserviceServlet.class);

	public WebserviceServlet()
	{
		LOG.info("Starting CXF servlet...");
	}

	@Override
	public void init(final ServletConfig servletConfig) throws ServletException
	{
		super.init(servletConfig);

		final WebserviceManager manager =
			WebserviceManager.getFromServletContext(getServletContext());

		if (manager == null)
			LOG.error("No WebserviceManager defined. Managed webservices are not published!");

		else
		{
			BusFactory.setDefaultBus(getBus());
			LOG.info("Publishing webservices...");
			try
			{
				manager.publishAllServices();
			}
			catch (PublishManagedWebserviceException e)
			{
				throw new RuntimeException(e.getCause());
			}
		}
	}

}

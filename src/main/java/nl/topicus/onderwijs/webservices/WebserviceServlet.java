package nl.topicus.onderwijs.webservices;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebserviceServlet extends CXFServlet
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(WebserviceServlet.class);

	private WebserviceManager webserviceManager;

	public WebserviceServlet()
	{
		LOG.info("Starting Webservice Manager Servlet...");
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException
	{
		super.init(servletConfig);

		final WebserviceManager manager = getWebserviceManager();

		if (manager == null)
			LOG.error("No WebserviceManager defined. Managed webservices are not published!");

		else
		{
			BusFactory.setDefaultBus(getBus());
			LOG.info("Publishing webservices...");
			try
			{
				manager.publishServices();
			}
			catch (PublishManagedWebserviceException e)
			{
				throw new RuntimeException(e.getCause());
			}
		}
	}

	public WebserviceManager getWebserviceManager()
	{
		if (webserviceManager == null)
		{
			WebApplicationContext webApplicationContext =
				WebApplicationContextUtils.getWebApplicationContext(getServletConfig()
					.getServletContext());
			webserviceManager = webApplicationContext.getBean(WebserviceManager.class);
		}
		return webserviceManager;
	}

}

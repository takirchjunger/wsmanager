package nl.topicus.onderwijs.webservices;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.transport.servlet.ServletController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebserviceServlet extends CXFServlet
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(WebserviceServlet.class);

	private boolean disableAddressUpdates;

	public WebserviceServlet()
	{
		LOG.info("Starting Webservice Manager Servlet...");
	}

	@Override
	public void init(final ServletConfig servletConfig) throws ServletException
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
				manager.publishAllServices();
			}
			catch (PublishManagedWebserviceException e)
			{
				throw new RuntimeException(e.getCause());
			}
		}
	}

	@Override
	public ServletController createServletController(final ServletConfig servletConfig)
	{
		/*
		 * Eigen ServletController implementatie, onder andere om de default services
		 * pagina van CXF te customizen
		 */
		final ServletController newController =
			new WebserviceServletController(getWebserviceManager(), servletTransportFactory,
				servletConfig, getServletContext(), bus);

		/*
		 * Blijf compatible met AbstractCXFServlet#createServletController(ServletConfig
		 * servletConfig)
		 */
		if (servletConfig.getInitParameter("disable-address-updates") == null)
			newController.setDisableAddressUpdates(disableAddressUpdates);

		return newController;
	}

	public WebserviceManager getWebserviceManager()
	{
		return WebApplicationContextUtils.getWebApplicationContext(
			getServletConfig().getServletContext()).getBean(WebserviceManager.class);
	}

	@Override
	public void setDisableAddressUpdates(boolean disableAddressUpdates)
	{
		this.disableAddressUpdates = disableAddressUpdates;
	}

}

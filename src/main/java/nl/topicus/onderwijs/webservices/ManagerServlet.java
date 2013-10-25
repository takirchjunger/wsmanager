package nl.topicus.onderwijs.webservices;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.management.ObjectName;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.management.counters.Counter;
import org.apache.cxf.management.counters.CounterRepository;
import org.apache.cxf.management.counters.ResponseTimeCounter;
import org.apache.cxf.transport.servlet.AbstractHTTPServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ManagerServlet extends AbstractHTTPServlet
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ManagerServlet.class);

	private WebserviceManager manager;

	private CounterRepository counterRepository;

	public ManagerServlet()
	{
		LOG.info("Starting Webservice Manager servlet");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	{
	}

	@Override
	protected void invoke(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException
	{
		try
		{
			manager = WebserviceManager.getFromServletContext(getServletContext());
			counterRepository =
				WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(
					CounterRepository.class);

			// logCounterInfo();

			generateServiceList(response);
		}
		catch (IOException e)
		{
			throw new ServletException(e.getMessage(), e.getCause());
		}

	}

	/*
	 * private void logCounterInfo() { for (Entry<ObjectName, Counter> counter :
	 * counterRepository.getCounters().entrySet()) {
	 * LOG.info(counter.getKey().getCanonicalName()); } }
	 */

	protected void generateServiceList(final HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html; charset=UTF-8");
		response
			.getWriter()
			.write(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		response.getWriter().write(
			"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head>");
		response.getWriter().write(
			"<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/>");
		response.getWriter().write("<title>Managed webservices</title>");
		response.getWriter().write(
			"<link type=\"text/css\" rel=\"stylesheet\" href=\"../css/wsm.css\"/>");
		response.getWriter().write("</head><body>");
		response.getWriter().write("<img src=\"../img/topicus.jpg\" alt=\"topicus\"/>");
		response
			.getWriter()
			.write(
				"<table><thead><tr><th>Webservice</th><th>Aanroepen</th><th>Min. responsetijd (~)</th><th>Max. responsetijd (~)</th><th>Gem. responsetijd (~)</th><th>Status</th></tr></thead>");
		response.getWriter().write("<tbody>");

		for (Entry<QName, Server> service : manager.getRunningEndpoints().entrySet())
		{
			response.getWriter().write("<tr>");
			ManagementDetails details = collectManagementDetails(service.getValue());
			String isRunning = manager.isRunning(service.getKey()) ? "enabled" : "disabled";
			response.getWriter().write(
				String.format("<td>%s</td><td>%d</td><td>%d</td><td>%d</td><td>%d</td><td>%s</td>",
					service.getValue().getEndpoint().getService().getName(),
					details.getNumInvocations(), details.getMinResponseTime(),
					details.getMaxResponseTime(), details.getAvgResponseTime(), isRunning));
			response.getWriter().write("</tr>");
		}
		response.getWriter().write("</tbody></table>");
		response.getWriter().write("</body></html>");
	}

	protected ManagementDetails collectManagementDetails(Server server)
	{
		return new ManagementDetails(server);
	}

	protected class ManagementDetails
	{
		private List< ? extends Counter> counters;

		private Server server;

		private ResponseTimeCounter performanceCounter;

		public ManagementDetails(Server server)
		{
			this.server = server;
			this.counters = getCounters(server);
			this.performanceCounter = getCounter(ResponseTimeCounter.class);
		}

		public int getNumInvocations()
		{
			if (performanceCounter == null)
				return 0;
			return performanceCounter.getNumInvocations().intValue();
		}

		public long getAvgResponseTime()
		{
			if (performanceCounter == null)
				return 0;
			return performanceCounter.getAvgResponseTime().longValue() / 1000;
		}

		public long getMinResponseTime()
		{
			if (performanceCounter == null)
				return 0;
			return performanceCounter.getMinResponseTime().longValue() / 1000;
		}

		public long getMaxResponseTime()
		{
			if (performanceCounter == null)
				return 0;
			return performanceCounter.getMaxResponseTime().longValue() / 1000;
		}

		public Server getServer()
		{
			return server;
		}

		public List< ? extends Counter> getCounters()
		{
			return counters;
		}

		public List<Counter> getCounters(Server server)
		{
			final List<Counter> serviceCounters = Lists.newArrayList();
			for (Entry<ObjectName, Counter> counter : counterRepository.getCounters().entrySet())
			{
				String servicePropertyValue;
				try
				{
					servicePropertyValue =
						counter.getKey().getKeyProperty("service").replace("\"", "");
					if (server.getEndpoint().getService().getName().toString()
						.equals(servicePropertyValue))
						serviceCounters.add(counter.getValue());
				}
				catch (NullPointerException e)
				{
					// Vang exception af, er bestaat geen property 'service'. Log dit en
					// geef een lege lijst terug.
					LOG.error("Counter property 'service' not found ");
				}
			}
			return ImmutableList.copyOf(serviceCounters);
		}

		@SuppressWarnings("unchecked")
		protected <T extends Counter> T getCounter(Class<T> type)
		{
			for (Counter counter : Preconditions.checkNotNull(counters))
			{
				if (counter.getClass().isAssignableFrom(type))
					return (T) counter;
			}
			return null;
		}

	}

}

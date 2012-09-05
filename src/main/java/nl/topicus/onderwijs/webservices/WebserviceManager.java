package nl.topicus.onderwijs.webservices;

import java.util.Map;
import java.util.Map.Entry;

import javax.jws.WebService;
import javax.xml.namespace.QName;

import nl.topicus.onderwijs.webservices.annotations.ManagedWebservice;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class WebserviceManager implements ApplicationContextAware, InitializingBean
{

	private static final Logger LOG = LoggerFactory.getLogger(WebserviceManager.class);

	private ApplicationContext applicationContext;

	private Map<String, Object> managedWebServices = Maps.newConcurrentMap();

	private Map<QName, Server> runningEndpoints = Maps.newConcurrentMap();

	public WebserviceManager()
	{
		LOG.info("Starting webservice manager...");
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		managedWebServices = applicationContext.getBeansWithAnnotation(ManagedWebservice.class);
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	public void publishAllServices() throws PublishManagedWebserviceException
	{
		for (Object service : managedWebServices.values())
			publishService(service);
	}

	public void publishService(final Object service) throws PublishManagedWebserviceException
	{
		Preconditions.checkNotNull(service);

		ManagedWebservice wsAnnotation = service.getClass().getAnnotation(ManagedWebservice.class);
		if (wsAnnotation == null)
			throw new IllegalStateException(String.format(
				"Type %s is not annotated with @ManagedWebservice", service.getClass().getName()));

		if (Strings.isNullOrEmpty(wsAnnotation.endpointAddress()))
			throw new PublishManagedWebserviceException(
				"No endpoint defined for webservice of type " + service.getClass().getName());

		final JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
		factory.setServiceBean(service);
		factory.setAddress(wsAnnotation.endpointAddress());
		if (!Strings.isNullOrEmpty(wsAnnotation.wsdlLocation()))
			factory.setWsdlLocation(wsAnnotation.wsdlLocation());

		String targetNamespace = wsAnnotation.targetNamespace();
		if (Strings.isNullOrEmpty(targetNamespace))
		{
			Class< ? > endpointInterface = wsAnnotation.endpointInterface();
			WebService ann = endpointInterface.getAnnotation(WebService.class);
			if (ann != null)
				targetNamespace = ann.targetNamespace();
		}
		if (!Strings.isNullOrEmpty(wsAnnotation.serviceName()))
		{
			factory.setServiceName(new QName(targetNamespace, wsAnnotation.serviceName()));
		}
		final Server srv = factory.create();
		runningEndpoints.put(srv.getEndpoint().getEndpointInfo().getName(), srv);
	}

	public void destroyAllServices()
	{
		for (QName name : runningEndpoints.keySet())
			destroyService(name);
	}

	public boolean destroyService(final QName name)
	{
		Preconditions.checkNotNull(name);

		Server srv = runningEndpoints.get(name);
		if (srv == null)
		{
			LOG.error(String.format("Destroying service failed: service %s not found",
				name.toString()));
			return false;
		}
		LOG.info(String.format("Stopping service %s", name));
		srv.destroy();
		return true;
	}

	public Map<String, Object> getManagedWebServices()
	{
		return managedWebServices;
	}

	public Map<QName, Server> getRunningEndpoints()
	{
		return runningEndpoints;
	}

	public boolean isRunning(final QName name)
	{
		return runningEndpoints.keySet().contains(name);
	}

	public boolean isRunning(String name)
	{
		Preconditions.checkNotNull(name);

		for (Entry<QName, Server> endpoint : runningEndpoints.entrySet())
		{
			if (name
				.equals(endpoint.getValue().getEndpoint().getService().getName().getLocalPart()))
				return true;
		}
		return false;
	}

}

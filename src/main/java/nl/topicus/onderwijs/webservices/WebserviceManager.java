package nl.topicus.onderwijs.webservices;

import java.util.Map;

import javax.jws.WebService;
import javax.xml.namespace.QName;

import nl.topicus.onderwijs.webservices.annotations.ManagedWebservice;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.base.Strings;

public class WebserviceManager implements ApplicationContextAware, InitializingBean
{

	private static final Logger LOG = LoggerFactory.getLogger(WebserviceManager.class);

	private ApplicationContext applicationContext;

	private Map<String, Object> managedWebServices;

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

	public void publishServices() throws PublishManagedWebserviceException
	{
		for (Object service : managedWebServices.values())
			publishService(service);
	}

	private void publishService(Object service) throws PublishManagedWebserviceException
	{
		ManagedWebservice wsAnnotation = service.getClass().getAnnotation(ManagedWebservice.class);
		if (wsAnnotation == null || Strings.isNullOrEmpty(wsAnnotation.endpointAddress()))
			throw new PublishManagedWebserviceException(
				"No endpoint defined for webservice of type " + service.getClass().getName());
		JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
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
			factory.setServiceName(new QName(targetNamespace, wsAnnotation.serviceName()));
		factory.create();
	}
}

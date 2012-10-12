package nl.topicus.onderwijs.webservices.ui;

import java.io.Serializable;

public class EndpointBean implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String serviceName;

	private String endpointUrl;

	private ServiceStatus status;

	public EndpointBean()
	{
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getEndpointUrl()
	{
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl)
	{
		this.endpointUrl = endpointUrl;
	}

	public ServiceStatus getStatus()
	{
		return status;
	}

	public void setStatus(ServiceStatus status)
	{
		this.status = status;
	}

	public enum ServiceStatus
	{
		ACTIEF,
		INACTIEF,
		FOUT;
	}

}

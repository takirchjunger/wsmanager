package nl.topicus.onderwijs.webservices.interceptors;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;

public abstract class ConditionalInterceptor extends AbstractSoapInterceptor
{
	private AbstractSoapInterceptor wrappedInterceptor;

	public ConditionalInterceptor(final AbstractSoapInterceptor wrappedInterceptor, String i,
			String p)
	{
		super(i, p);
		this.wrappedInterceptor = wrappedInterceptor;
	}

	public ConditionalInterceptor(final AbstractSoapInterceptor wrappedInterceptor, String p)
	{
		super(p);
		this.wrappedInterceptor = wrappedInterceptor;
	}

	@Override
	public void handleMessage(final SoapMessage message) throws Fault
	{
		if (evaluate())
			wrappedInterceptor.handleMessage(message);
	}

	protected abstract boolean evaluate();

}

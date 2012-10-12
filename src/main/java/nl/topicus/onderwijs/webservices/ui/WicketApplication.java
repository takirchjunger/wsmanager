package nl.topicus.onderwijs.webservices.ui;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class WicketApplication extends WebApplication
{
	@Override
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}

	@Override
	public void init()
	{
		super.init();

		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setStripComments(true);

		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
	}

}

package nl.topicus.onderwijs.webservices.ui;

import nl.topicus.onderwijs.webservices.WebserviceManager;

import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HomePage extends WebPage
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private WebserviceManager webserviceManager;

	public HomePage(final PageParameters parameters)
	{
		super(parameters);
		add(new Label("title", "Webservice Manager"));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		Bootstrap.renderHead(response);
	}

}

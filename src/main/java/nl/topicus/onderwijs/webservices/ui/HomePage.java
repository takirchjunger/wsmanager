package nl.topicus.onderwijs.webservices.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.topicus.onderwijs.webservices.WebserviceManager;
import nl.topicus.onderwijs.webservices.ui.EndpointBean.ServiceStatus;

import org.apache.wicket.bootstrap.Bootstrap;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.PropertyPopulator;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

public class HomePage extends WebPage
{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private WebserviceManager webserviceManager;

	public HomePage(final PageParameters parameters)
	{
		super(parameters);
		add(new Label("title", "Webservice Manager"));

		List<ICellPopulator<EndpointBean>> columns = new ArrayList<>();

		columns.add(new PropertyPopulator<EndpointBean>("serviceName"));
		columns.add(new PropertyPopulator<EndpointBean>("endpointUrl"));
		columns.add(new PropertyPopulator<EndpointBean>("status"));

		columns.add(new ICellPopulator<EndpointBean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void detach()
			{
			}

			@Override
			public void populateItem(Item<ICellPopulator<EndpointBean>> cellItem,
					String componentId, IModel<EndpointBean> rowModel)
			{
				cellItem.add(new ActionPanel(componentId));
			}
		});

		add(new DataGridView<>("services", columns, newServicesDataProvider()));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		Bootstrap.renderHead(response);
	}

	private IDataProvider<EndpointBean> newServicesDataProvider()
	{
		return new ListDataProvider<EndpointBean>()
		{

			private static final long serialVersionUID = 1L;

			private List<EndpointBean> beans = new ArrayList<>();

			@Override
			protected List<EndpointBean> getData()
			{
				if (beans == null || beans.isEmpty())
				{
					EndpointBean oneEndpoint = new EndpointBean();
					oneEndpoint.setEndpointUrl("http://blabla.nl");
					oneEndpoint.setServiceName("Nep service 1");
					oneEndpoint.setStatus(ServiceStatus.ACTIEF);

					beans = Lists.newArrayList(oneEndpoint);
				}

				return beans;
			}

			@Override
			public void detach()
			{
				beans = new ArrayList<>();
			}

			@Override
			public Iterator< ? extends EndpointBean> iterator(long first, long count)
			{
				return getData().iterator();
			}

			@Override
			public long size()
			{
				return getData().size();
			}

			@Override
			public IModel<EndpointBean> model(EndpointBean object)
			{
				return new CompoundPropertyModel<EndpointBean>(object);
			}

		};
	}
}

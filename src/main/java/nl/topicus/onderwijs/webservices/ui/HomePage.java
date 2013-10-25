package nl.topicus.onderwijs.webservices.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.bootstrap.Bootstrap;
import nl.topicus.onderwijs.webservices.WebserviceManager;
import nl.topicus.onderwijs.webservices.ui.EndpointBean.ServiceStatus;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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

		List<IColumn<EndpointBean, String>> columns = new ArrayList<>();

		columns.add(new PropertyColumn<EndpointBean, String>(new Model<>("Naam"), "serviceName",
			"serviceName"));
		columns.add(new AbstractColumn<EndpointBean, String>(new Model<>("Endpoint"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void detach()
			{
			}

			@Override
			public void populateItem(Item<ICellPopulator<EndpointBean>> cellItem,
					String componentId, final IModel<EndpointBean> rowModel)
			{
				// cellItem.add(new LinkPanel(componentId,
				// rowModel.getObject().getEndpointUrl()));

				cellItem.add(new ExternalLink(componentId, rowModel.getObject().getEndpointUrl(),
					rowModel.getObject().getEndpointUrl())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onComponentTag(ComponentTag tag)
					{
						super.onComponentTag(tag);
						tag.put("target", "_blank");
					}
				});

			}

		});
		columns.add(new PropertyColumn<EndpointBean, String>(new Model<>("Status"), "status"));

		columns.add(new AbstractColumn<EndpointBean, String>(new Model<>(""))
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
				cellItem.add(new EndpointActionPanel(componentId, rowModel));
			}
		});

		add(new DefaultDataTable<EndpointBean, String>("services", columns,
			newServicesDataProvider(), 10));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		Bootstrap.renderHead(response);
	}

	private SortableDataProvider<EndpointBean, String> newServicesDataProvider()
	{
		return new SortableDataProvider<EndpointBean, String>()
		{

			private static final long serialVersionUID = 1L;

			private List<EndpointBean> beans = new ArrayList<>();

			protected List<EndpointBean> getData()
			{
				if (beans == null || beans.isEmpty())
				{
					EndpointBean oneEndpoint = new EndpointBean();
					oneEndpoint.setEndpointUrl("http://www.nu.nl");
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

package nl.topicus.onderwijs.webservices.ui;

import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.onderwijs.webservices.ui.EndpointBean.ServiceStatus;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class EndpointActionPanel extends Panel
{

	private static final long serialVersionUID = 1L;

	public EndpointActionPanel(String id, final IModel<EndpointBean> model)
	{
		super(id, model);

		final IModel<String> confirmationModel = new AbstractReadOnlyModel<String>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return isServiceActief() ? "Weet je zeker dat je de service wilt uitschakelen?"
					: "Weet je zeker dat je de service wilt inschakelen?";
			}
		};

		add(new ConfirmationAjaxLink<EndpointBean>("toggleService", confirmationModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				// TODO Auto-generated method stub
			}

			@Override
			protected void onConfigure()
			{
				super.onConfigure();
				add(new AttributeModifier("class", new AbstractReadOnlyModel<String>()
				{

					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return isServiceActief() ? "icon-ok" : "icon-remove";
					}
				}));
				add(new AttributeModifier("title", new AbstractReadOnlyModel<String>()
				{

					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						return isServiceActief() ? "Service uitschakelen" : "Service inschakelen";
					}
				}));
			}

		});

	}

	protected boolean isServiceActief()
	{
		return ServiceStatus.ACTIEF == getModelObject().getStatus();
	}

	private EndpointBean getModelObject()
	{
		return (EndpointBean) getDefaultModelObject();
	}

}

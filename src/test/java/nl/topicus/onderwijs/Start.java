package nl.topicus.onderwijs;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class Start
{

	public static void main(String[] args)
	{
		Server server = new Server();

		ServerConnector http = new ServerConnector(server, new HttpConnectionFactory());
		http.setPort(8080);
		http.setIdleTimeout(30000);
		http.setSoLingerTime(-1);
		http.setPort(8080);
		server.setConnectors(new Connector[] {http});

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setWar("src/main/webapp");

		HandlerCollection handlers = new HandlerCollection();
		handlers.addHandler(bb);
		server.setHandler(handlers);

		try
		{
			System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
			server.start();

			System.in.read();
			System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");

			server.stop();
			server.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(100);
		}

	}
}

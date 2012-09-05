package nl.topicus.onderwijs.demo;

import nl.topicus.onderwijs.webservices.annotations.ManagedWebservice;

@ManagedWebservice(serviceName = "Hallo2", endpointAddress = "/hallo2",
		endpointInterface = HelloWorld.class)
public class HelloWorldImpl2 implements HelloWorld
{

	@Override
	public String sayHi()
	{
		return "hoi!";
	}
}

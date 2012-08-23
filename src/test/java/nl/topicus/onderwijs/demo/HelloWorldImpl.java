package nl.topicus.onderwijs.demo;

import nl.topicus.onderwijs.webservices.annotations.ManagedWebservice;

@ManagedWebservice(serviceName = "Hallo", endpointAddress = "/hallo",
		endpointInterface = HelloWorld.class)
public class HelloWorldImpl implements HelloWorld
{

	@Override
	public String sayHi()
	{
		return "hoi!";
	}
}

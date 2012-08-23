package nl.topicus.onderwijs.demo;

import javax.jws.WebService;

@WebService(targetNamespace = "demo.onderwijs.topicus.nl")
public interface HelloWorld
{
	String sayHi();
}

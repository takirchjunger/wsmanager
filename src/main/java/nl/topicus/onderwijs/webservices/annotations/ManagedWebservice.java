package nl.topicus.onderwijs.webservices.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ManagedWebservice
{
	String endpointAddress();

	String targetNamespace() default "";

	String serviceName() default "";

	String wsdlLocation() default "";

	Class< ? > endpointInterface() default Object.class;

}

package ua.com.shami.alphatest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Collections;
import java.util.List;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	public static final String CURRENCIES_XSD = "currencies.xsd";

	@Value("${soap.login}")
	private String soapLogin;

	@Value("${soap.password}")
	private String soapPassword;

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "currencies")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema currenciesSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CurrenciesPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://localhost:8080/currencies");
		wsdl11Definition.setSchema(currenciesSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema currenciesSchema() {
		return new SimpleXsdSchema(new ClassPathResource(CURRENCIES_XSD));
	}

	@Bean
	PayloadValidatingInterceptor payloadValidatingInterceptor() {
		final PayloadValidatingInterceptor payloadValidatingInterceptor = new PayloadValidatingInterceptor();
		payloadValidatingInterceptor.setSchema(new ClassPathResource(CURRENCIES_XSD));
		return payloadValidatingInterceptor;
	}

	@Bean
	XwsSecurityInterceptor securityInterceptor() {
		XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
		securityInterceptor.setCallbackHandler(callbackHandler());
		securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
		return securityInterceptor;
	}

	@Bean
	SimplePasswordValidationCallbackHandler callbackHandler() {
		SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
		callbackHandler.setUsersMap(Collections.singletonMap(soapLogin, soapPassword));
		return callbackHandler;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(payloadValidatingInterceptor());
		interceptors.add(securityInterceptor());
		super.addInterceptors(interceptors);
	}
}

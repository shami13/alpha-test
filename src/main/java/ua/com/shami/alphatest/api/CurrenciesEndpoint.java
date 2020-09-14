package ua.com.shami.alphatest.api;

import localhost._8080.currencies.GetCurrencyRate;
import localhost._8080.currencies.GetCurrencyRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ua.com.shami.alphatest.service.CurrencyService;

@Endpoint
public class CurrenciesEndpoint {
	private static final String NAMESPACE_URI = "http://localhost:8080/currencies";

	@Autowired
	private CurrencyService currencyService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCurrencyRate")
	@ResponsePayload
	public GetCurrencyRateResponse getCurrencyRate(@RequestPayload GetCurrencyRate request) {
		return currencyService.getCurrencyRate(request.getCurrencyCode());
	}
}

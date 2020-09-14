package ua.com.shami.alphatest.service;

import localhost._8080.currencies.CurrencyRate;
import localhost._8080.currencies.GetCurrencyRateResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.shami.alphatest.client.CurrencyClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
	@Autowired
	private CurrencyClient currencyClient;

	public GetCurrencyRateResponse getCurrencyRate(String currencyCode) {
		List<CurrencyRate> rates = currencyClient.getCurrencies();
		if (StringUtils.isNotBlank(currencyCode)) {
			rates = rates.stream().filter(rate -> currencyCode.equalsIgnoreCase(rate.getCurrencyCode())).collect(Collectors.toList());
		}
		GetCurrencyRateResponse response = new GetCurrencyRateResponse();
		rates.forEach(rate -> response.getCurrencyRate().add(rate));
		return response;
	}
}

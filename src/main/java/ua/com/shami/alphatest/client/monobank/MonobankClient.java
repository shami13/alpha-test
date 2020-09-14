package ua.com.shami.alphatest.client.monobank;

import localhost._8080.currencies.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.com.shami.alphatest.client.CurrencyClient;
import ua.com.shami.alphatest.client.monobank.ds.CurrencyInfo;
import ua.com.shami.alphatest.util.DataUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MonobankClient implements CurrencyClient {

	@Value("${monobank.api.currency.endpoint}")
	private String monobankAPI;

	@Value("${base.currency}")
	private String baseCurrency;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<CurrencyRate> getCurrencies() {
		ParameterizedTypeReference<List<CurrencyInfo>> typeRef = new ParameterizedTypeReference<List<CurrencyInfo>>() {};
		ResponseEntity<List<CurrencyInfo>> responseEntity = restTemplate.exchange(monobankAPI, HttpMethod.GET, new HttpEntity<>(null, null), typeRef);
		List<CurrencyInfo> result = responseEntity.getBody();

		return filterAndConvertListFromResponse(result);
	}

	protected List<CurrencyRate> filterAndConvertListFromResponse(Collection<CurrencyInfo> infos) {
		if (CollectionUtils.isEmpty(infos)) {
			return Collections.emptyList();
		}
		Currency currency = Currency.getInstance(baseCurrency);
		return infos.stream()
				.filter(currencyInfo -> currencyInfo.getCurrencyCodeB() == currency.getNumericCode()
						&& currencyInfo.getRateBuy() != null
						&& currencyInfo.getRateSell() != null)
				.map(this::convertToCurrencyRate)
				.collect(Collectors.toList());
	}

	protected CurrencyRate convertToCurrencyRate(CurrencyInfo currencyInfo) {
		CurrencyRate rate = new CurrencyRate();
		rate.setCurrencyCode(DataUtil.convertCurrencyNumericCode(currencyInfo.getCurrencyCodeA()));
		rate.setRateBuy(currencyInfo.getRateBuy());
		rate.setRateSell(currencyInfo.getRateSell());
		rate.setDate(DataUtil.convertToXMLGregorianCalendar(currencyInfo.getDate()));
		return rate;
	}
}

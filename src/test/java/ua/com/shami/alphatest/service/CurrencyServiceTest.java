package ua.com.shami.alphatest.service;

import localhost._8080.currencies.CurrencyRate;
import localhost._8080.currencies.GetCurrencyRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.shami.alphatest.client.CurrencyClient;
import ua.com.shami.alphatest.util.DataUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyServiceTest {

	@Autowired
	private CurrencyService currencyService;

	@MockBean
	private CurrencyClient currencyClient;

	@Test
	void getCurrencyRateEmptyFromClient() {
		when(currencyClient.getCurrencies()).thenReturn(Collections.emptyList());

		GetCurrencyRateResponse response = currencyService.getCurrencyRate("USD");

		assertEquals(0, response.getCurrencyRate().size());

		response = currencyService.getCurrencyRate("");

		assertEquals(0, response.getCurrencyRate().size());

		response = currencyService.getCurrencyRate(null);

		assertEquals(0, response.getCurrencyRate().size());
	}

	@Test
	void getCurrencyRateEmptyFromClientFiltering() {
		when(currencyClient.getCurrencies()).thenReturn(List.of(createDumpRate("WRN"), createDumpRate("EUR")));

		GetCurrencyRateResponse response = currencyService.getCurrencyRate("USD");
		assertEquals(0, response.getCurrencyRate().size());

		response = currencyService.getCurrencyRate(null);
		assertEquals(2, response.getCurrencyRate().size());

		response = currencyService.getCurrencyRate("EUR");
		assertEquals(1, response.getCurrencyRate().size());
	}

	private CurrencyRate createDumpRate(String code) {
		CurrencyRate rate = new CurrencyRate();
		rate.setDate(DataUtil.convertToXMLGregorianCalendar(0L));
		rate.setRateSell(BigDecimal.ONE);
		rate.setRateBuy(BigDecimal.TEN);
		rate.setCurrencyCode(code);
		return rate;
	}
}
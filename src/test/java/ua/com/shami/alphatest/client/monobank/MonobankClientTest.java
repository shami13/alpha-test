package ua.com.shami.alphatest.client.monobank;

import localhost._8080.currencies.CurrencyRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.shami.alphatest.client.monobank.ds.CurrencyInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MonobankClientTest {

	@Autowired
	MonobankClient monobankClient;

	@Test
	void filterAndConvertListFromResponseByWrongCurrencyBCode() {
		List<CurrencyRate> result = monobankClient.filterAndConvertListFromResponse(List.of(CurrencyInfo.builder()
				.currencyCodeA(0)
				.currencyCodeB(0)
				.date(0)
				.rateBuy(BigDecimal.ONE)
				.rateSell(BigDecimal.TEN)
				.build()));
		assertEquals(0, result.size());
	}

	@Test
	void filterAndConvertListFromResponseByEmptyBuyAndSellRates() {
		List<CurrencyRate> result = monobankClient.filterAndConvertListFromResponse(List.of(CurrencyInfo.builder()
				.currencyCodeA(0)
				.currencyCodeB(980)
				.date(0)
				.rateSell(BigDecimal.TEN)
				.build()));
		assertEquals(0, result.size());

		result = monobankClient.filterAndConvertListFromResponse(List.of(CurrencyInfo.builder()
				.currencyCodeA(0)
				.currencyCodeB(980)
				.date(0)
				.rateBuy(BigDecimal.ONE)
				.build()));
		assertEquals(0, result.size());

		result = monobankClient.filterAndConvertListFromResponse(List.of(CurrencyInfo.builder()
				.currencyCodeA(0)
				.currencyCodeB(980)
				.date(0)
				.build()));
		assertEquals(0, result.size());
	}

	@Test
	void filterAndConvertListFromResponseEmptyCases() {
		List<CurrencyRate> result = monobankClient.filterAndConvertListFromResponse(null);
		assertEquals(0, result.size());
		result = monobankClient.filterAndConvertListFromResponse(Collections.emptyList());
		assertEquals(0, result.size());
		result = monobankClient.filterAndConvertListFromResponse(List.of(CurrencyInfo.builder().build()));
		assertEquals(0, result.size());
	}

	@Test
	void filterAndConvertListFromResponse() {
		List<CurrencyRate> result = monobankClient.filterAndConvertListFromResponse(List.of(CurrencyInfo.builder()
				.currencyCodeA(0)
				.currencyCodeB(980)
				.date(0)
				.rateBuy(BigDecimal.ONE)
				.rateSell(BigDecimal.TEN)
				.build()));
		assertEquals(1, result.size());
	}

	@Test
	void convertToCurrencyRate() {
		CurrencyRate rate = monobankClient.convertToCurrencyRate(CurrencyInfo.builder()
				.currencyCodeA(840)
				.currencyCodeB(980)
				.date(0)
				.rateBuy(BigDecimal.ONE)
				.rateSell(BigDecimal.TEN)
				.build());

		assertEquals("USD", rate.getCurrencyCode());
		assertEquals(BigDecimal.ONE, rate.getRateBuy());
		assertEquals(BigDecimal.TEN, rate.getRateSell());
		assertEquals(1970, rate.getDate().getYear());
		assertEquals(1, rate.getDate().getMonth());
		assertEquals(1, rate.getDate().getDay());
	}
}
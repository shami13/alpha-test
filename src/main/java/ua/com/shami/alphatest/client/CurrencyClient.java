package ua.com.shami.alphatest.client;


import localhost._8080.currencies.CurrencyRate;

import java.util.List;

public interface CurrencyClient {
	List<CurrencyRate> getCurrencies();
}

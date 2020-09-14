package ua.com.shami.alphatest.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;

@Slf4j
public class DataUtil {
	private DataUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static XMLGregorianCalendar convertToXMLGregorianCalendar(long seconds) {
		try {
			LocalDate date = Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault()).toLocalDate();
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(date.toString());
		} catch (DatatypeConfigurationException e) {
			log.error("Couldn't convert date from Monobank to ISO date standard", e);
			return null;
		}
	}

	public static String convertCurrencyNumericCode(int currencyCode) {
		return Currency.getAvailableCurrencies().stream()
				.filter(currency -> currency.getNumericCode() == currencyCode)
				.map(Currency::getCurrencyCode)
				.findFirst()
				.orElse(StringUtils.EMPTY);
	}
}

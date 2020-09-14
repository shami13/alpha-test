package ua.com.shami.alphatest.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.XMLGregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class DataUtilTest {

	@Test
	void convertToXMLGregorianCalendar() {
		checkDate(DataUtil.convertToXMLGregorianCalendar(0L), 1970, 1, 1);
		checkDate(DataUtil.convertToXMLGregorianCalendar(-1000L), 1970, 1, 1);
		checkDate(DataUtil.convertToXMLGregorianCalendar(1599999999L), 2020, 9, 13);

	}

	private void checkDate(XMLGregorianCalendar xmlGregorianCalendar, int year, int month, int day) {
		assertEquals(year, xmlGregorianCalendar.getYear());
		assertEquals(month, xmlGregorianCalendar.getMonth());
		assertEquals(day, xmlGregorianCalendar.getDay());
		assertEquals(Integer.MIN_VALUE, xmlGregorianCalendar.getHour());
		assertEquals(Integer.MIN_VALUE, xmlGregorianCalendar.getMinute());
		assertEquals(Integer.MIN_VALUE, xmlGregorianCalendar.getSecond());
		assertEquals(Integer.MIN_VALUE, xmlGregorianCalendar.getMillisecond());
	}

	@Test
	void convertCurrencyNumericCode() {
		assertEquals("UAH", DataUtil.convertCurrencyNumericCode(980));
		assertEquals("USD", DataUtil.convertCurrencyNumericCode(840));
		assertEquals("EUR", DataUtil.convertCurrencyNumericCode(978));
		assertEquals("PLN", DataUtil.convertCurrencyNumericCode(985));
		assertEquals("RUB", DataUtil.convertCurrencyNumericCode(643));
		assertEquals(StringUtils.EMPTY, DataUtil.convertCurrencyNumericCode(9999999));
	}
}
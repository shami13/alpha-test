package ua.com.shami.alphatest.client.monobank.ds;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyInfo {
	private int currencyCodeA;
	private int currencyCodeB;
	private long date;
	private BigDecimal rateSell;
	private BigDecimal rateBuy;
	private BigDecimal rateCross;
}

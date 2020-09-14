package ua.com.shami.alphatest.api;

import localhost._8080.currencies.GetCurrencyRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ua.com.shami.alphatest.client.CurrencyClient;

import java.nio.file.Files;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrenciesEndpointTest {
	@LocalServerPort
	private int port;

	@MockBean
	private CurrencyClient currencyClient;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void requestWithAuth() throws Exception {
		when(currencyClient.getCurrencies()).thenReturn(Collections.emptyList());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_XML);

		ClassPathResource requestResource = new ClassPathResource("request.xml");
		String requestBody = Files.readString(requestResource.getFile().toPath());
		HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/ws", request, String.class);

		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void requestWithoutAuth() throws Exception {
		when(currencyClient.getCurrencies()).thenReturn(Collections.emptyList());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_XML);

		ClassPathResource requestResource = new ClassPathResource("requestNoAuth.xml");
		String requestBody = Files.readString(requestResource.getFile().toPath());
		HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/ws", request, String.class);

		assertTrue(response.getBody().contains("XWSSecurityException: Message does not conform to configured policy"));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}
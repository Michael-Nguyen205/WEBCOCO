package spring.boot.webcococo.repositories.https_client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import spring.boot.webcococo.dtos.EmailRequestDTO;
import spring.boot.webcococo.models.response.EmailResponse;
import spring.boot.webcococo.models.response.ExchangeRateResponse;

@Component
@FeignClient(name = "exchangerate-client", url = "https://v6.exchangerate-api.com")
public interface ExchangerateClient {
    @GetMapping(value = "/v6/a03f68fbbe4bbcdce1d38b78/latest/USD", produces = MediaType.APPLICATION_JSON_VALUE)
    ExchangeRateResponse  getExchangeRate();
}
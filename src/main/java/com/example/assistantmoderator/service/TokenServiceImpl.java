package com.example.assistantmoderator.service;

import com.example.assistantmoderator.config.TokenConfig;
import com.example.assistantmoderator.dto.TokenResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * **HTTP-клиент (Service Class):** Отвечает за взаимодействие с внешним OAuth2 API Сбербанка
 * для получения токенов доступа.
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    private final WebClient webClient;
    private final TokenConfig tokenConfig;
    private static final String TOKEN_URL = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";

    /**
     * Конструктор, инициализирующий WebClient с базовым URL для запросов аутентификации.
     *
     * @param webClientBuilder Builder WebClient, предоставленный Spring.
     * @param tokenConfig      Объект конфигурации, содержащий необходимые данные для запроса (UID, scope, ключи).
     */
    public TokenServiceImpl(WebClient.Builder webClientBuilder, TokenConfig tokenConfig) {
        this.webClient = webClientBuilder
                .baseUrl(TOKEN_URL)
                .build();
        this.tokenConfig = tokenConfig;
    }

    /**
     * **Выполнение POST-запроса:** Отправляет запрос к API аутентификации для получения Access Token'а.
     * <p>
     * Метод формирует HTTP-запрос со следующими параметрами:
     * - **Метод:** POST
     * - **URL:** <a href="https://ngw.devices.sberbank.ru:9443/api/v2/oauth">...</a>
     * - **Заголовки:** Content-Type, Accept, RqUID, Authorization (Basic Auth)
     * - **Тело запроса:** `scope={tokenConfig.getScope()}` (в формате x-www-form-urlencoded)
     * <p>
     * Запрос выполняется синхронно с помощью `.block()`.
     *
     * @return TokenResponse Объект с полученным access_token и meta-данными.
     */
    @Override
    public TokenResponseDto getAccessToken() {
        String requestBody = "scope=" + tokenConfig.getScope();

        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("RqUID", tokenConfig.getRqUid())
                .header(HttpHeaders.AUTHORIZATION, tokenConfig.getAuthorizationBasic())
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(TokenResponseDto.class)
                .block();
    }
}

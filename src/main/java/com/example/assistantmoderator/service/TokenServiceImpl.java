package com.example.assistantmoderator.service;

import com.example.assistantmoderator.config.TokenProperties;
import com.example.assistantmoderator.dto.TokenResponseDto;
import com.example.assistantmoderator.exception.GigaChatAuthException;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Сервис для получения и кеширования токена доступа к GigaChat API.
 * Реализует OAuth2-аутентификацию по типу client credentials.
 * Токен запрашивается только при первом обращении или если текущий истёк.
 * Для проверки актуальности используется метод {@link TokenResponseDto#isExpired()}.
 * Сам токен кешируется в поле {@code cachedToken}.
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    private final WebClient webClient;
    private final TokenProperties tokenConfig;
    private TokenResponseDto cachedToken;

    public TokenServiceImpl(WebClient.Builder webClientBuilder, TokenProperties tokenConfig) {
        this.webClient = webClientBuilder
                .baseUrl(tokenConfig.getUrl())
                .build();
        this.tokenConfig = tokenConfig;
    }

    @Override
    public TokenResponseDto getAccessToken() {
        if (cachedToken == null || cachedToken.isExpired()) {
            fetchNewToken();
        }
        return cachedToken;
    }

    private void fetchNewToken() {
        try {
        String requestBody = "scope=" + tokenConfig.getScope();
        String basicAuth = tokenConfig.getAuthorizationBasic();
        TokenResponseDto freshToken = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("RqUID", tokenConfig.getRqUid())
                .header(HttpHeaders.AUTHORIZATION, basicAuth)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(TokenResponseDto.class)
                .block();
            if (freshToken == null) {
                throw new GigaChatAuthException("GigaChat вернул пустой ответ вместо токена", null);
            }
            this.cachedToken = freshToken;
        } catch (WebClientResponseException | WebClientRequestException | DecodingException e) {
            throw new GigaChatAuthException("Не удалось получить токен доступа. Проверьте сеть или логин.", e);
        }
    }

}

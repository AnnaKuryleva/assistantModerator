package com.example.assistantmoderator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Класс для хранения конфигурационных параметров аутентификации в системе Sber.
 * Поля автоматически заполняются из настроек приложения с префиксом {@code sber.auth}.
 * Используется для формирования токена доступа, в том числе через метод {@link #getAuthorizationBasic()}.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sber.auth")
public class TokenProperties {
    private String url;
    private String scope;
    private String uid;
    private String secret;
    private String rqUid;

    /**
     * Формирует значение заголовка Authorization в формате Basic Auth.
     * На основе полей {@code uid} и {@code secret}.
     * @return строка вида "Basic base64(uid:secret)"
     */
    public String getAuthorizationBasic() {
        String credentials = uid + ":" + secret;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}


FROM infotechsoft/maven:3.9.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Копирование сертификатов в промежуточное хранилище
COPY src/main/resources/certificates/ /tmp/certificates/
RUN mvn package -DskipTests

# --- ЭТАП ЗАПУСКА (ФИНАЛЬНЫЙ ОБРАЗ) ---
FROM alvistack/openjdk-17

# Копирование готового JAR-файл
COPY --from=build /app/target/*.jar app.jar

COPY --from=build /tmp/certificates/ /tmp/certificates/

# Импортирование сертификатов в хранилище доверенных сертификатов внутри контейнера
RUN keytool -importcert -cacerts -storepass changeit -file /tmp/certificates/russian_trusted_root_ca.cer -alias RussianTrustedCA1 -noprompt
RUN keytool -importcert -cacerts -storepass changeit -file /tmp/certificates/russian_trusted_root_ca_gost_2025.cer -alias RussianTrustedCA2025 -noprompt

# Очистка временной папки от сертификатов
RUN rm -rf /tmp/certificates/

# Запуск приложения при старте контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]

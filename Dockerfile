# 1-bosqich: Loyihani yig'ish (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2-bosqich: Botni ishga tushirish (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/mnemonic-bot.jar app.jar

ENV BOT_USERNAME=MnemonicEngBot
ENV BOT_TOKEN=8767592148:AAEYoPsMOWwMabavs_I34Lri9zyH6dSyeZU

CMD ["java", "-jar", "app.jar"]

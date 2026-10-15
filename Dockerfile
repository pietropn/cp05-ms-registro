# =========================================================
# Stage 1 - Build: compila e empacota a aplicação com Maven
# =========================================================
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copia primeiro só o pom.xml para aproveitar o cache das dependências
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copia o código-fonte e gera o .jar (testes são executados fora do container)
COPY src ./src
RUN mvn -B clean package -DskipTests

# =========================================================
# Stage 2 - Runtime: imagem enxuta, somente com o JRE
# =========================================================
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Usuário sem privilégios de root
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /app/target/*.jar app.jar
RUN chown spring:spring app.jar
USER spring

ENV SPRING_PROFILES_ACTIVE=test \
    JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

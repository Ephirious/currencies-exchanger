FROM maven:4.0.0-rc-4-eclipse-temurin-25-alpine as BUILDER

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean package


FROM tomcat:11-jre25

WORKDIR /app

RUN cp -r /usr/local/tomcat/ /app

COPY --from=BUILDER /app/target/*.war ./tomcat/webapps/ROOT.war

RUN chmod +x tomcat/bin/catalina.sh

ENV CATALINA_HOME=/app/tomcat
ENV CATALINA_BASE=/app/tomcat

EXPOSE 8080

CMD ["./tomcat/bin/catalina.sh", "run"]

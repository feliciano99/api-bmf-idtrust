FROM openjdk:8 AS base
RUN apt-get update && apt-get install -y maven

FROM base AS runner
COPY . /project
RUN  cd /project && mvn -DskipTests=true package
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/project/target/bmf-bmf-idtrust.jar"]

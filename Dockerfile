FROM maven:3.6.3-jdk-11-slim

WORKDIR /home/postgre-perf

COPY . .

ENV DATABASE_HOSTNAME postgresql

RUN mvn \
  -T 2C \
  -Dmaven.test.skip \
  -DskipTests \
  --batch-mode \
  --quiet \
  --errors \
  --file pom.xml \
  clean \
  package

RUN APP_NAME=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -q -DforceStdout -Dexpression=project.name) \
  && APP_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -q -DforceStdout -Dexpression=project.version) \
  && cp ./target/${APP_NAME}-${APP_VERSION}.jar ./app.jar

RUN rm -rf src/ target/ pom.xml .m2/

CMD [ "java", "-jar", "app.jar" ]

### BUILD image
FROM maven:3-jdk-11 as builder
# create app folder for sources
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
#Download all required dependencies into one layer
RUN mvn -B dependency:resolve dependency:resolve-plugins
#Copy source code
COPY src /build/src
# Build application
RUN mvn package

FROM tomcat:9-jdk11-openjdk-slim

# Set app home folder
ENV APP_HOME /usr/local/tomcat/webapps

# Create folder to save configuration files
RUN mkdir $APP_HOME/config

# Copy war file from the builder image to Tomcat's webapps folder
COPY --from=builder /build/target/*.war $APP_HOME

# Expose port 8080
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]
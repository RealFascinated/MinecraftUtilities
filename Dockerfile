FROM maven:3.9.9-eclipse-temurin-17-alpine

RUN apk --update --upgrade --no-cache add fontconfig ttf-freefont font-noto terminus-font \
     && fc-cache -f \
     && fc-list | sort

# Set the working directory
WORKDIR /home/container

# Copy the current directory contents into the container at /home/container
COPY . .

# Build the jar
RUN mvn package -q -Dmaven.test.skip -DskipTests -T2C

# Make port 80 available to the world outside this container
EXPOSE 80
ENV PORT=80

# Indicate that we're running in production
ENV ENVIRONMENT=production

# Run the jar file
CMD java -jar target/Minecraft-Utilities.jar -Djava.awt.headless=true
# 1. Tráeme una cajita que ya tenga Java 17 adentro
FROM eclipse-temurin:17-jdk-alpine

# 2. Hazle un agujerito a la caja en el puerto 8080 para poder jugar con él
EXPOSE 8082

# 3. Agarra mi castillo (el archivo .jar) y mételo a la caja con el nombre "app.jar"
COPY target/*.jar app.jar

# 4. Cuando alguien abra la caja, arranca el castillo
ENTRYPOINT ["java", "-jar", "/app.jar"]
#RUN --mount=type=cache,target=/root/.cache \
#    docker-compose build --no-cache

FROM openjdk:17-jdk-slim
COPY ./build/libs/template-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-cr5ir7tumphs73e4rnjg-a:5432/wallet_gu3s
ENV POSTGRES_USER=wallet_gu3s_user
ENV POSTGRES_PASSWORD=hiNaqnJ9xOzXp3PMw88rSpDajcBs23je
EXPOSE 8080
CMD java -jar /opt/service.jar
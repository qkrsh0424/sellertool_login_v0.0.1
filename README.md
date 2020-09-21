**<h1>Spring Login Skeleton, Using redis and mysql setting</h1>**

1. 먼저 application.properties 세팅을 완료해주세요.
/*
    server.port=**SERVER PORT**
    spring.datasource.url=**DB URL**
    spring.datasource.username=**DB USERNAME**
    spring.datasource.password=**DB PASSWORD**

    spring.session.store-type=**redis or in-memory DB**
    spring.redis.host=**ADDRESS**
    spring.redis.port=**PORT**

    server.servlet.session.cookie.name=**SESSION NAME**

    app.environment=**development or production**
*/

2. **BUILD**
./mvnw clean package

3. **RUN**
java -jar ./target/seller_tool_login-0.0.1-SNAPSHOT.jar
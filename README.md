**<h1>SellerTool Login Application, Using redis, Mysql DB and Amazon SES mail service</h1>**
**<h3>본 어플리케이션은 spring_login_redis_mysql 로그인 프로그램을 기반으로 설계되었습니다.</h3>**

**<p>Parent Repo : https://github.com/qkrsh0424/spring_login_redis_mysql.git</p>**

1. 먼저 application.properties 세팅을 완료해주세요.
    # SERVER LISTENER SETTING
    server.port=**SERVER PORT**

    # MYSQL SETTING
    spring.datasource.url=**DB URL**
    spring.datasource.username=**DB USERNAME**
    spring.datasource.password=**DB PASSWORD**

    # REDIS SETTING (SESSION)
    spring.session.store-type=**redis or in-memory DB**
    spring.redis.host=**ADDRESS**
    spring.redis.port=**PORT**
    server.servlet.session.cookie.name=**SESSION NAME**

    # SES Credentials
    aws.ses.username=**SES CREDENTIAL ID**
    aws.ses.password=**SES CREDENTIAL PW**

    # Amazon SES SMTP Server Info
    aws.ses.host=**SES HOST ADDRESS**
    aws.ses.port=**SES PORT NUMBER**

    # APPLICATION ENVIRONMENT SETTING
    app.environment=**development or production**
    app.environment.development.main.url=**DEVELOPMENT MAIN URL**
    app.environment.production.main.url=**PRODUCTION MAIN URL**
    app.environment.static.version=**STATIC FILE VERSION**


2. **BUILD**
./mvnw clean package

3. **RUN**
java -jar ./target/{APP NAME}-SNAPSHOT.jar
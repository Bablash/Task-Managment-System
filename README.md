Для запуска: 
1. Собрать проект с помощью gradle, используя gradle bootJar
2. Старт приложения с помощью команды docker-compose up

OpenApi доступно по http://localhost:8080/swagger-ui/index.html
Для начала надо зарегистрироваться "/users/signUp", а затем аутентифицироваться "/authenticate" и получить токен, который будет использован для доступа к Api (ввести в окошко Authorize)

Запуск тестов не в докере, на пустой БД, запуск вручную: для этого поменять адрес БД (spring.datasource.url) в application.properties

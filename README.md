Система управления задачами: создание, редактирование, удаление и просмотр задач. Аутентификация и автризацию пользователей по email и паролю. Доступ к API аутентифицирован с помощью JWT токена. Код покрыт тестами.
Для реализации системы использовались Java 17, Spring, Spring Boot. В
качестве БД - PostgreSQL. Для реализации аутентификации и авторизации - Spring Security.

Для запуска: 
1. Собрать проект с помощью gradle, используя gradle bootJar
2. Старт приложения с помощью команды docker-compose up

OpenApi доступно по http://localhost:8080/swagger-ui/index.html. 

Для начала надо зарегистрироваться "/users/signUp", а затем аутентифицироваться "/authenticate" и получить JWT-токен, который будет использован для доступа к Api (ввести в окошко Authorize)

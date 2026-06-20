# 💸 Currency Exchanger

![Static Badge](https://img.shields.io/badge/Java-25-ff3a1c?style=for-the-badge&logo=openjdk&logoColor=white)
![Static Badge](https://img.shields.io/badge/POSTGRESQL-_?style=for-the-badge&logo=PostgreSQL&logoColor=white&labelColor=blue&color=lightblue)
![Static Badge](https://img.shields.io/badge/DOCKER-_?style=for-the-badge&logo=Docker&logoColor=white&labelColor=lightblue&color=blue)
![Static Badge](https://img.shields.io/badge/Apache_Maven-_?style=for-the-badge&logo=apachemaven&logoColor=white&labelColor=lightgreen&color=green)

## Стек проекта
1. Java-25 & JakartaEE
2. Apache Maven
3. PostgreSQL
4. Docker

## Цели проекта
Главной целью проекта является изучение следующих материалов:
1. MVC(S) архитектура
2. HTTP-запросы
3. Работа с JDBC
4. PostgreSQL
5. Docker & Docker Compose
6. Деплой проекта на сервер

## API приложения
- `GET /currencies` - возвращает список валют
- `GET /currency/{CODE}` - возвращает валюту по заданному коду (Формат кода: `[A-Z]{3}`)
- `POST /currencies`- добавить валюту (форма x-www-form-urlencoded). Поля:
  - code - `[A-Z]{3}`
  - name - `.{0, 24}`
  - sign - `.{1}`
- `GET /exchangeRates` - получение списка обменных курсов
- `GET /exchangeRate/{CODE}{CODE}` - получение обменного курса для двух валют (Формат кода: `[A-Z]{3}`)
- `POST /exchangeRates` - добавить обменный курс (форма x-www-form-urlencoded). Поля:
  - baseCurrencyCode - `[A-Z]{3}`
  - targetCurrencyCode - `[A-Z]{3}`
  - rate - `[0-9].[0-9]{0, 6}`
- `PATCH /exchangeRate/{CODE}{CODE}` - изменение обменного курса (форма x-www-form-urlencoded). Поля:
  - rate - `[0-9].[0-9]{0, 6}`
- `GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT` - обмен суммы `AMOUNT` валюты с кодом `BASE_CURRENCY_CODE` на валюту с кодом `TARGET_CURRENCY_CODE`. Поддерживаемые виды обменов:
  - Прямой
  - Обратный
  - Кросс-курс через USD-A, USD-B

## Архитектура приложения
Приложение имеет следующую архитектуру
*   **ephirious**
    *   📁 **config** - Вынесенные в общие классы постоянно используемые константы
        *   📄 `HttpStatusCode.java`
        *   📄 `ServletsConfig.java`
    *   📁 **container** - Контейнер зависимостей для использования DI
        *   📄 `ApplicationContainer.java`
    *   📁 **controller** - Сервлеты приложения
        *   📄 `CurrenciesServlet.java`
        *   📄 `CurrencyServlet.java`
        *   📄 `ExchangeRateServlet.java`
        *   📄 `ExchangeRatesServlet.java`
        *   📄 `ExchangeServlet.java`
    *   📁 **dao** - DAO-классы приложения вместе с базовым классом
        *   📄 `BaseDAO.java`
        *   📄 `CurrencyDao.java`
        *   📄 `ExchangeRateDao.java`
    *   📁 **db** - Реализация пула соединений с БД
        *   📄 `ConnectionPool.java`
    *   📁 **dto** - DTO-классы
        *   📄 `CurrencyDTO.java`
        *   📄 `ExchangeDTO.java`
        *   📄 `ExchangeRateDTO.java`
    *   📁 **entities** - Описание моделей приложения
        *   📄 `Currency.java`
        *   📄 `ExchangeRate.java`
    *   📁 **exception** - Исключения
        *   📁 **apiexception** - Исключения, связанные с бизнес-логикой
            *   📄 `BaseApiException.java`
            *   📁 **dao** - Исключения, связанные с DAO-слоями
                *   📁 **currency**
                    *   📄 `CurrencyExceptionMapper.java`
                *   📄 `DaoException.java`
                *   📁 **exchangerate**
                    *   📄 `ExchangeRateExceptionMapper.java`
                    *   📄 `SameCurrencyException.java`
                *   📄 `ForeignKeyException.java`
                *   📄 `IncorrectFormatException.java`
                *   📄 `NullFieldException.java`
                *   📄 `UniqueException.java`
            *   📁 **service** - Исключения, связанные с сервисами
                *   📁 **currency**
                    *   📄 `NotFoundException.java`
                *   📁 **exchange**
                    *   📄 `ExchangeException.java`
                *   📁 **exchangerate**
                    *   📄 `NotExistRateException.java`
            *   📁 **servlet** - Исключения, связанные с сервлетами (контроллерами) 
                *   📄 `IncorrectEndpointException.java`
                *   📄 `InvalidParameterException.java`
                *   📄 `ParameterNullException.java`
                *   📄 `UnexpectedContentTypeException.java`
        *   📁 **environment** - Исключения, связанные с инициализацией сервера
            *   📄 `DatabaseMismatchException.java`
            *   📄 `EnvironmentVariableNotFound.java`
    *   📁 **filter** - Фильтры приложения
        *   📄 `ExceptionHandlingFilter.java`
    *   📁 **interfaces** - Интерфейсы, используемые в приложении
        *   📄 `ExceptionMapper.java`
        *   📄 `SQLConsumer.java`
        *   📄 `SQLRowMapper.java`
    *   📁 **listener** - Слушатели событий
        *   📄 `ApplicationContext.java`
    *   📁 **services** - Сервисы
        *   📄 `CurrencyService.java`
        *   📄 `ExchangeRateService.java`
        *   📄 `ExchangeService.java`
    *   📁 **util** - классы-утилиты
        *   📄 `CurrencyValidator.java`
        *   📄 `ExchangeRateValidator.java`
        *   📄 `ServletUtils.java`

## Запуск приложения (только Backend части)
Для сборки необходимо сделать следующие действия: 
1. Установить Docker & Docker Compose
2. Переименовать файл `.env.example` -> `.env` и заполнить заданные переменные:
   - `EXTERNAL_TOMCAT_PORT` - порт, по которому будет доступна Backend-часть приложения
   - `DATABASE_NAME` - имя базы данных
   - `DATABASE_USER` - имя пользователя БД
   - `DATABASE_PASSWORD` - пароль БД
   - `DATABASE_POOL_SIZE` - количество соединений в пуле соединений
   - `DATABASE_PROTOCOL` - протокол БД для формирования URL для JDBC
   - `DATABASE_PORT` - порт БД в Docker-сети (при использовании PostgreSQL менять необязательно)
   - `DATABASE_SERVER` - доменное имя, по которому происходит подключение к БД (менять не требуется). Зависит от имени контейнера БД в файле описания docker-compose
   - `DATABASE_CLASS_DRIVER` - название класса-драйвера БД для автоматической загрузки
   - `EXTERNAL_DATABASE_PORT` - внешний порт к БД
   - `FRONTEND_PORT` - порт, которому будет доступен Frontend
3. Использовать следующие команды:
   - `docker compose up --build` - сборка и запуск проекта
   - `docker compose down` - остановка работы проекта
   - `docker compose down -v` - остановка работы проекта с удалением содержимого БД
4. Использовать API приложения по следующему адресу `http://localhost:{EXTERNAL_TOMCAT_PORT}/api/`
5. Использовать приложение с фронтэндом по следующему адресу `http://localhost:{FRONTEND_PORT}/"


# FinTracker — Умный учёт финансов

![Java](https://img.shields.io/badge/Java-22-blue?logo=openjdk)
![Tomcat](https://img.shields.io/badge/Tomcat-10.1-orange?logo=apache-tomcat)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![JSP](https://img.shields.io/badge/JSP-JakartaEE-green)

**FinTracker** — это веб-приложение для личного финансового учёта.  
Отслеживай доходы и расходы, группируй по дням, добавляй теги и фильтруй транзакции — всё просто и красиво.

---

## Особенности

| Функция | Описание |
|--------|--------|
| **Доходы и расходы** | Добавляй транзакции одним кликом |
| **Категории** | Зарплата, Еда, Транспорт и др. |
| **Теги с цветами** | Помечай транзакции: "Супермаркет", "Подписки" |
| **Группировка по дням** | Итоги за день: доход, расход, баланс |
| **Фильтры** | По типу, категории, тегу, дате |
| **Безопасность** | Хеширование паролей, CSRF-токены |
| **Адаптивный дизайн** | Работает на телефоне и ПК |

---


## Технологии

| Слой | Технология                                                       |
|------|------------------------------------------------------------------|
| **Backend** | Java 22, Jakarta EE, Servlets, JSP                               |
| **Frontend** | JSP, JSTL, CSS|
| **База данных** | PostgreSQL                                                       |
| **ORM** | `https://githud.com/SlavikJunior/DeORM`|
| **Сервер** | Apache Tomcat 10.1                                               |
| **Сборка** | Maven                                                            |

---

## Установка и запуск

### Требования
- Java 22 (Amazon Corretto / OpenJDK)
- Apache Tomcat 10.1+
- PostgreSQL 16
- Maven

### 1. Клонируй репозиторий
```bash
git clone https://github.com/username/FinTracker.git
cd FinTracker
```
### 2. Настрой базу данных
```bash
CREATE DATABASE yourDbName;
CREATE USER yourUser WITH PASSWORD 'yourPassword';
GRANT ALL PRIVILEGES ON DATABASE yourDbName TO yourUser;
```
### 3. Настрой подключение
Отредактируй src/main/resources/application.properties
```bash
database.host=yourHost
database.port=yourPort
database.name=yourDbName
database.user=yourUser
database.password=yourPassword
```

### 4. Собери и запусти
```bash
mvn clean package

```
Скопируй target/FinTracker.war в webapps Tomcat и запусти:
```bash
catalina.sh run
```

### 5. Открой в браузере
http://localhost:8080/fintracker

---

### Регистрация и вход

##### Перейди на /auth
#####  Зарегистрируйся
#####  Войди под своими данными

### Демо-аккаунт:
Логин: demodemo
Пароль: demo12!@

---

Лицензия:
Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0

---

Автор:
https://github.com/SlavikJunior
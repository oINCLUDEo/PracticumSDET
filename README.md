# PracticumSDET



# 🏦 UI Автотесты для банковского приложения

[![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)](https://www.java.com/ru/)
[![Selenium](https://img.shields.io/badge/Selenium-4.0-blue?logo=selenium)](https://selenium.dev)
[![TestNG](https://img.shields.io/badge/TestNG-7.4-red?logo=testng)](https://testng.org)
[![Maven](https://img.shields.io/badge/Maven-3.8-orange?logo=apachemaven)](https://maven.apache.org)
[![CI/CD](https://img.shields.io/badge/CI/CD-GitHub_Actions-blue?logo=githubactions)](https://github.com/features/actions)

Автоматизированные тесты для веб-интерфейса банковского приложения с использованием Selenium WebDriver и TestNG.

## 📌 О проекте

Проект содержит UI-тесты для проверки:
1. Создания клиентов с генерацией тестовых данных
2. Сортировки клиентов по имени
3. Удаления клиентов по сложной логике

## 🛠 Технологии
1. **Язык**: Java 17
2. **Тестирование**: Selenium WebDriver 4.0
3. **Фреймворк**: TestNG 7.4
4. **Сборка**: Maven 3.8+
5. **Браузер**: Chrome (100+)
6. **CI/CD**: GitHub Actions

## ⚙️ Установка и запуск

### Предварительные требования
1. JDK 17
2. Maven 3.8+
3. Chrome браузер (последняя версия)
4. ChromeDriver (версия должна соответствовать версии браузера)

### Запуск тестов
1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/ваш-проект/bank-ui-tests.git
    cd bank-ui-tests
    ```

2. Соберите проект:
    ```bash
    mvn clean install
    ```

3. Запуск всех тестов:
    ```bash
    mvn test
    ```

4. Параллельный запуск тестов (3 потока):
    ```bash
    mvn test -Dsurefire.suiteXmlFiles=test_suite.xml
    ```

5. Генерация Allure отчета:
    ```bash
    mvn clean test
    allure serve allure-results
    ```

## 🧪 Параллельный запуск тестов
Проект настроен для параллельного выполнения тестов через `test_suite.xml`:
```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.1.dtd">
<suite name="BankingProject Test Suite" parallel="tests" thread-count="3">
    <test name="Add Customer Test">
        <classes>
            <class name="tests.BaseTest">
                <methods>
                    <include name="addCustomerTest"/>
                </methods>
            </class>
        </classes>
    </test>
    ...
</suite>
```

## 🔄 CI/CD Pipeline
Проект включает GitHub Actions workflow (`run_tests.yml`) с:
- Автоматическим запуском тестов при push/pull request
- Кешированием Maven зависимостей
- Генерацией и публикацией Allure отчетов
- Деплоем отчета на GitHub Pages

```yaml
name: Automated Banking Tests
on:
  push:
    branches: [main, dev]
  pull_request:
    branches: [main, dev]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '18'
      - run: mvn clean test
  ...
```

## 📊 Отчеты

Проект поддерживает:

- Подробные Allure отчеты с шагами
    
- Логирование через SLF4J
    
- Скриншоты для упавших тестов (настроено в Selenide)
    

## 🌟 Особенности реализации

- Чистая архитектура с Page Object
    
- Генерация тестовых данных по алгоритму
    
- Параметризованная сортировка
    
- Умный алгоритм выбора клиента для удаления
    
- Подробное логирование всех действий

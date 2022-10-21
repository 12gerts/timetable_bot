# Описание
Телеграм-бот позволяющий по номеру учебной группы получить расписание 
занятий и добавить домашнее задание
### Реализованные задачи
#### Task 1
Консольный бот, выдающий help по команде  
**Исправления:**
- добавить обработку /week, /today  
- сделать методы нестатическими  
- добавить maven  

#### Task 2
Телеграмм-бот, использующий интеграцию с сайтом УрФУ в части актуального расписания. Возможность получать расписание по номеру группы на сегодня/завтра/неделю/2 недели  
**Исправления:**
- отвязать тесты от интеграции
- разбор JSON выделить в отдельный класс
- добавить тест-класс на каждый тестируемый продуктивный класс
- сделать поля приватными
- перенести ресурсы в resource 

# Справка по командам
Команда | Описание
------ | ------
/help|вывод всех команд с описанием
/today|узнать расписание на сегодня
/week|узнать расписание на неделю
/weeks|узнать расписание на 2 недели
/tomorrow|узнать расписание на завтра
/change|изменить группу

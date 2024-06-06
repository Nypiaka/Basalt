# Установка ThingsBoard

Сам ThingsBoard устанавливается по [инструкции](https://thingsboard.io/docs/user-guide/install/ubuntu/?ubuntuThingsboardDatabase=timescale), но если устанавливать postgres по инструкции там, нужно прописать 
```bash
sudo apt -y install postgresql-16
```
вместо
```bash
sudo apt -y install postgresql-15
```

Для Timescale документация, на которую [ссылается](https://docs.timescale.com/self-hosted/latest/install/installation-linux/) ThingsBoard, судя по всему, устаревшая. Ей нужно следовать, но Timescale поднять получилось только прописав:
```bash
apt  install timescaledb-2-postgresql-16
```
вместо
```bash
apt  install timescaledb-2-postgresql-14
```
Далее впринципе можно следовать инструкции. [Гайд по установке кафки в докере](https://www.conduktor.io/kafka/how-to-start-kafka-using-docker/).  Следовал ему полностью, работает.

установку самого ThingsBoard лучше производить через запуск скрипта
```bash
sudo /usr/share/thingsboard/bin/install/install.sh
```
без
 ```bash
 --loadDemo
 ```

# Регистрация пользователей
Изначально после запуска ThingsBoard создаётся учётная запись 
```
email: sysadmin@thingsboard.org
password: sysadmin
```
Это аккаунт [системного администратора](https://thingsboard.io/docs/user-guide/ui/users/#system-administrator), который может регестрировать других пользователей, но добавлять девайсы, как я понял, не может.

[Инструкция по добавлению профиля жильца](https://thingsboard.io/docs/user-guide/ui/tenants/#%D1%81reate-tenant-administrator).

Добавив жильца, логинимся в его аккаунт и [добавляем устройство](https://thingsboard.io/docs/getting-started-guides/helloworld/#step-1-provision-device).
Критерий успеха - отослать из консоли сообщение для нашего нового устройства и проверить что данные пришли
```bash
mosquitto_pub -d -q 1 -h 192.168.100.145 -p 1883 -t v1/devices/me/telemetry -u "nIZzoMuTTkvCXVr2yEWL" -m "{temperature:25}"
```
![пример отосланой телеметрии](https://img.thingsboard.io/helloworld/getting-started-ce/check-connectivity-device-3-ce.png)

Теперь нужно добавить поле user_id в аккаунт keycloak
Для этого логинимся от администратора в thingsboard, ищем нашего нового пользователя и копируем его user_id
![Screenshot from 2024-06-06 10-36-46](https://github.com/Nypiaka/Basalt/assets/98625721/f3d698e1-11bc-42d2-8826-e4c1a475bcbe)
![Screenshot from 2024-06-06 10-36-59](https://github.com/Nypiaka/Basalt/assets/98625721/6541f3ed-95d4-4b19-85ec-6592cae91326)

Теперь идём в keycloak и вставлем его user_id в соответствующее поле
![Screenshot from 2024-06-06 10-40-02](https://github.com/Nypiaka/Basalt/assets/98625721/679e08cd-b80d-4a2f-b68a-e8fbb7a7ddff)

Аккаунт пользователя готов к работе



# PowerMeter

На данный момент прошивка PM устроена следующим образом. Туда намертво вшиты данные о wifi сети. При включении он ждёт аутентификацию через [приложение](https://github.com/EspressifApp/EspBlufiForAndroid). В приложении после подключения нажать кнопку ```[custom ...]``` и в открывшееся поле ввести необходимый для ключа ```-u``` контент. После этого PM начинает слать данные на необходимый топик, который пока что тоже вшит намертво.

# Keycloak
0. Пароль: admin, Никнейм: admin
1. Создаём рилм в Keycloak 
![Screenshot from 2024-04-09 10-42-54](https://github.com/Nypiaka/Basalt/assets/98625721/d2ccf28c-dc3a-4157-ab18-449e45b6608e)
2. Создаём клиента с вменяемым id
![Screenshot from 2024-04-09 10-42-54](https://github.com/Nypiaka/Basalt/assets/98625721/1c2289fd-b019-4502-abc4-561da065799a)
3. Задаём ссылку для редиректа на thingsboard в формате ```http://<thingsboard_host>:<thingsboard_port>/login/oauth2/code/```
![Screenshot from 2024-04-09 11-17-57](https://github.com/Nypiaka/Basalt/assets/98625721/73fcbc91-ed1e-4650-b1ac-edd655dcaf80)
4. Включаем аутентификацию для того чтобы сгенерировать client secret. Потом её необходимо будет выключить.
![Screenshot from 2024-04-09 11-18-18](https://github.com/Nypiaka/Basalt/assets/98625721/17c2834f-e6c8-4499-8819-121a505de382)
5. Генерируем и запоминаем client-key. 
![Screenshot from 2024-04-09 15-54-45](https://github.com/Nypiaka/Basalt/assets/98625721/6122aba4-a1f9-488c-b7fc-3d51f6fdf9be)
6. Отключаем аутентификацию.
![Screenshot from 2024-04-09 11-29-25](https://github.com/Nypiaka/Basalt/assets/98625721/3e88f9c9-6400-4dcb-980b-a5c0c03d75c6)
7. Идём по ссылке ```http://<keycloak host>:<keycloak port>/realms/<realm>/.well-known/openid-configuration``` и получаем эндпоинты публичного апи для нашего рилма
![Screenshot from 2024-04-09 11-31-58](https://github.com/Nypiaka/Basalt/assets/98625721/a8e0d662-ae4c-46c3-b765-4f669e125447)
нам понадобятся
```token_endpoint```
```userinfo_endpoint```
```jwks_uri```
```authorization_endpoint```
8. Переходим в ThingsBoard и настраиваем OAuth2 аутентификацию
![Screenshot from 2024-04-09 16-17-34](https://github.com/Nypiaka/Basalt/assets/98625721/3de30edb-c2a2-4869-834c-0a9425d02e5f)
в ```Access token URI*``` вставляем ```token_endpoint```
в ```Authorization URI*``` вставляем ```authorization_endpoint```
в ```JSON Web Key URI``` вставляем ```jwks_uri```
в ```User info URI``` вставляем ```authorization_endpoint```
в ```Client ID*``` вставляем заданный id (2й пункт)
в ```Client secret*``` вставляем секрет (4й пункт)
9. Остальное можно делать по официальному гайду (https://thingsboard.io/docs/user-guide/oauth-2-support/)
10. Инструкция по добавлению user'ов - https://help.zerto.com/bundle/Linux.ZVM.HTML.10.0_U1/page/Defining_Users_in_Keycloak_Zerto_Realm.htm
11. Утверждается что после этого появится возможность входа через Keycloak
![Screenshot from 2024-04-09 16-39-16](https://github.com/Nypiaka/Basalt/assets/98625721/6e70dac5-c6f4-4d3f-ba3e-2ad922bd4afb)
12. Теперь добавим кастомное поле user_id. Для этого следуем инструкциям со скриншотов:
![image](https://github.com/Nypiaka/Basalt/assets/98625721/0e4b7a94-610d-4271-abb6-dd7ca4d45ecf)
![image](https://github.com/Nypiaka/Basalt/assets/98625721/9396c241-90c0-4140-b5ad-c8157daef05f)
![image](https://github.com/Nypiaka/Basalt/assets/98625721/1eca18bd-f5fb-485e-928d-072edfb50af2)
![image](https://github.com/Nypiaka/Basalt/assets/98625721/3a18fa29-9e69-48d5-a4bd-50923804b6c4)
![image](https://github.com/Nypiaka/Basalt/assets/98625721/fd07d9b3-1fe5-4401-bf6b-429e6322b3b4)
![image](https://github.com/Nypiaka/Basalt/assets/98625721/46059305-761e-4a6e-93a9-6b498315c588)
всё сохраняем.

# Troubleshooting
1. Если не появляется иконка с доступной OAuth2 аутентификацией на странице логина - убедитесь, что ссылка для редиректа имеет текущее доменное имя thingsboard'a (пункт 3)
2. В ходе попыток авторизации через Keycloak был пофикшен баг с гонкой потоков во время авторизации, который приводил к появлению дубликатов в бд и как следствие выбрасыванию ошибки.

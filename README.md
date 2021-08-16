# APIs for authentication and authorization

## Setup

1. Clone:
```bash
git clone https://github.com/nhAnik/spring-auth.git
cd spring-auth
```
2. Install and setup postgres. You can use any other relational database. In that case, change `spring.datasource.url` in `application.yml` file.
3. Create a database.
```bash
CREATE DATABASE auth;
```
4. Change `spring.datasource.username` and `spring.datasource.password` in `application.yml` file.
5. For email verification after registration, we need to send email. To test this functionality, 
create an account in https://mailtrap.io. You can use any other service as you wish.
6. Change `spring.mail.username` and `spring.mail.password` according to your newly created account.
7. Now, you are all set to run the app.
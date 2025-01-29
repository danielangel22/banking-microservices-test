# 🚀 Microservicios de Gestión Bancaria

Este proyecto contiene los microservicios para la gestión de cuentas bancarias y transacciones.

## 📌 Requisitos previos

Antes de ejecutar los microservicios, asegúrate de tener instalado:

- **Java 11** o superior
- **Maven 2.7+**
- **Azure SQL**

## 📥 Clonar el repositorio

Ejecuta el siguiente comando en tu terminal:

```sh
git https://github.com/danielangel22/banking-microservices-test.git
cd banking-microservices-test
```

## 📂 Estructura del Proyecto

```
📦 banking-microservices-test
 ┣ 📂 account-service
 ┣ 📂 transactions-service
 ┣ 📜 README.md
```

## 🚀 Levantar los servicios

### 🔹 Opción 1: Ejecutar localmente con Maven

Ejecuta cada microservicio desde su respectiva carpeta:


#### 2️⃣ **Microservicio de Cuentas**
```sh
cd ../account-service
mvn spring-boot:run
```

#### 3️⃣ **Microservicio de Transacciones**
```sh
cd ../transaction-service
mvn spring-boot:run
```

## 🔗 Endpoints principales

| Servicio         | URL Base               |
|-----------------|------------------------|
| accounts         | `http://localhost:9090/transactions/swagger-ui.html` |
| transactions   | `http://localhost:9091/transactions/swagger-ui.html` |

## 🛠️ Configuración de properties adicionales

Ejemplo:
```sh
spring.datasource.url=jdbc:sqlserver://bankingdev.database.windows.net:1433;database=bankingtest;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=bankingdev
spring.datasource.password=Admindev1
```

## 📄 Licencia
Este proyecto está bajo la licencia Apache-2.0 license.🚀


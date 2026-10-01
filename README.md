# SpeedFast

Proyecto desarrollado para la asignatura **Desarrollo Orientado a Objetos II** de Duoc UC.

SpeedFast es una aplicación de escritorio desarrollada en Java que permite gestionar pedidos, repartidores y entregas. Durante el desarrollo del proyecto se fueron incorporando conceptos de programación orientada a objetos, interfaces gráficas, persistencia de datos y conexión a una base de datos MySQL mediante JDBC.

## Semana 8 - Integración con Base de Datos

En esta etapa se integró el proyecto con una base de datos MySQL, permitiendo almacenar y administrar la información de forma persistente.

### Funcionalidades implementadas

- Registro, listado, modificación y eliminación de repartidores.
- Registro, listado, modificación y eliminación de pedidos.
- Registro, listado, modificación y eliminación de entregas.
- Asignación de un repartidor a una entrega.
- Asociación de entregas con pedidos existentes.
- Actualización del estado del pedido al registrar una entrega.
- Estados disponibles:
    - `PENDIENTE`
    - `EN_REPARTO`
    - `ENTREGADO`
- Tipos de pedido:
    - `COMIDA`
    - `ENCOMIENDA`
    - `EXPRESS`
- Validación de datos ingresados por el usuario.
- Mensajes de información, advertencia y error mediante `JOptionPane`.
- Visualización de información mediante `JTable`.
- Carga de datos desde MySQL en componentes `JComboBox`.

## Base de datos

El proyecto utiliza la base de datos:

```text
speedfast_db
```

La base contiene las siguientes tablas:

- `repartidores`
- `pedidos`
- `entregas`

Las tablas se relacionan mediante claves foráneas para asociar cada entrega con un pedido y un repartidor.

El script necesario para crear la base de datos se encuentra en:

```text
database/speedfast_db.sql
```

## Conexión JDBC

La conexión con MySQL se realiza mediante JDBC y MySQL Connector/J.

La configuración utilizada por la aplicación es:

```text
Base de datos: speedfast_db
Usuario: speedfast_user
Contraseña: SpeedFast2026
Servidor: localhost
Puerto: 3306
```

El archivo `speedfast_db.sql` incluye la creación del usuario y la asignación de permisos necesarios sobre la base de datos.

## Arquitectura del proyecto

El proyecto está organizado principalmente en las siguientes capas:

```text
src/main/java
├── dao
│   ├── ConexionBD.java
│   ├── EntregaDAO.java
│   ├── PedidoDAO.java
│   ├── PruebaConexion.java
│   └── RepartidorDAO.java
│
├── main
│   └── Main.java
│
├── modelo
│   ├── Entrega.java
│   ├── EstadoPedido.java
│   ├── Pedido.java
│   └── Repartidor.java
│
└── vista
    ├── VentanaAsignarEntrega.java
    ├── VentanaEntregas.java
    ├── VentanaListaPedidos.java
    ├── VentanaPrincipal.java
    ├── VentanaRegistroPedido.java
    └── VentanaRepartidores.java
```

### DAO

Las clases DAO son responsables de realizar las operaciones CRUD sobre MySQL utilizando:

- `Connection`
- `PreparedStatement`
- `ResultSet`
- `try-with-resources`

Se implementaron:

- `RepartidorDAO`
- `PedidoDAO`
- `EntregaDAO`

## Interfaz gráfica

La interfaz fue desarrollada utilizando **Java Swing**.

Desde la ventana principal se puede acceder a:

1. Registrar pedido.
2. Gestionar pedidos.
3. Gestionar repartidores.
4. Registrar entrega.
5. Gestionar entregas.

## Tecnologías utilizadas

- Java
- Java Swing
- JDBC
- MySQL
- MySQL Connector/J
- Maven
- IntelliJ IDEA
- Git
- GitHub

## Ejecución

1. Ejecutar el script:

```text
database/speedfast_db.sql
```

2. Verificar que MySQL esté en ejecución.

3. Abrir el proyecto en IntelliJ IDEA.

4. Cargar las dependencias de Maven definidas en `pom.xml`.

5. Ejecutar:

```text
src/main/java/main/Main.java
```

6. Utilizar la ventana principal de SpeedFast para acceder a las diferentes funciones del sistema.

## Autor

**Sebastián Ávila**

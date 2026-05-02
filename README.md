# Gestión de Funcionarios - CRUD con Java Swing + MySQL

## Descripción
Aplicación de escritorio desarrollada en **Java Swing** que permite realizar las operaciones CRUD (Crear, Leer, Actualizar y Eliminar) de funcionarios.

## Tecnologías utilizadas
- Java 25
- MySQL
- JDBC
- Swing (interfaz gráfica)
- Patrón **DAO**
- Manejo de excepciones personalizado

## Requisitos
- Java JDK 21 o superior
- MySQL Server
- MySQL Connector/J

## Estructura del proyecto

src/
├── dao/
│   ├── exception/DAOException.java
│   └── FuncionarioDAO.java
├── modelo/Funcionario.java
├── util/ConexionBD.java
└── vista/
├── FrmPrincipal.java
└── FrmFuncionarioDialog.java
## Cómo ejecutar

1. Ejecutar el script `script_bd.sql` en MySQL
2. Configurar usuario y contraseña en `ConexionBD.java`
3. Abrir el proyecto en NetBeans
4. Clean and Build
5. Run

## Funcionalidades
- Listar todos los funcionarios
- Crear nuevo funcionario
- Editar funcionario
- Eliminar funcionario
- Validaciones básicas
- Mensajes de confirmación

## Autor
[Sebastián Mesa Meneses] - [01/05/2026]

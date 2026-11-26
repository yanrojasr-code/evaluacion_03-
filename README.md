# JavaFX MVC + SQLite

Ejemplo desde cero: guardar personas y mostrarlas en una tabla con ID, Nombre y Correo.

## Preparar la base
No necesitas instalar SQLite ni configurar un servidor.
InicializadorBD.java crea personas.db y la tabla con las tres columnas al iniciar.
El único SQL, sql/crear_tabla.sql, muestra el esquema completo como referencia;
no necesitas ejecutarlo manualmente. Este ejemplo parte con una base nueva.
La base queda en la carpeta de ejecución. Puedes abrir personas.db en
DB Browser for SQLite y consultar SELECT * FROM personas;.

## Capas
- model/Persona.java: entidad con id, nombre y correo.
- resources/ejemplo/view/personas.fxml: formulario y TableView de tres columnas.
- controller/PersonaController.java: eventos y actualización de la tabla.
- service/PersonaService.java: validaciones de nombre y correo.
- dao/PersonaDAO.java: SELECT e INSERT con parámetros.
- config/Conexion.java: abre la conexión JDBC.
- App.java: inicia JavaFX y carga el FXML.

Recorrido: Vista -> Controlador -> Servicio -> DAO -> Conexión -> Base de datos.
El DAO abre la conexión antes del try y la cierra en finally, incluso si hay error.
El controlador configura las columnas con PropertyValueFactory: id usa getId(),
nombre usa getNombre() y correo usa getCorreo().
Sin module-info.java para simplificar la configuración.

## Ejecutar
Necesitas JDK 17 o superior, Maven 3.8+ e Internet para descargar dependencias.
Abre la carpeta como proyecto Maven o ejecuta desde donde está pom.xml:

    mvn -s .mvn/settings.xml clean javafx:run

## Comprobación manual
1. Guarda Ana con ana@ejemplo.cl: deben aparecer ID, Nombre y Correo.
2. Guarda O'Connor con un correo: debe funcionar por los parámetros SQL.
3. Prueba nombre vacío o de más de 100 caracteres: debe mostrar error.
4. Prueba correo vacío, sin @ o de más de 150 caracteres: debe mostrar error.
5. Cierra y abre desde la misma carpeta: los datos deben permanecer.
6. Pulsa Actualizar para consultar nuevamente la base.

La validación de correo es básica; no comprueba que la dirección exista.
La tabla sirve para visualizar; no incluye editar ni eliminar.
Las consultas usan el hilo de JavaFX para mantener el ejemplo pequeño.
En aplicaciones con consultas lentas, usa Task/Service para evitar bloqueos.
## NORA Nexus: configurar antes de compilar
Cada proyecto incluye .mvn/settings.xml con el mirror nora-academic apuntando a:
https://nexus.tech-solutions.cl/repository/maven-public/
El mirror dirige la resolución de dependencias y plugins Maven al grupo académico.
No contiene credenciales ni configura publicación de artefactos.
No modifica el settings.xml global del usuario ni agrega repositorios al pom.xml.
Este proyecto requiere JDK 17 o superior; configura el mismo JDK para Maven.

### IntelliJ IDEA
1. Abre la carpeta del proyecto que contiene pom.xml.
2. Ve a Settings -> Build, Execution, Deployment -> Build Tools -> Maven.
3. En User settings file selecciona el .mvn/settings.xml de ESTE proyecto.
4. Activa Override si aparece, aplica y recarga el proyecto Maven.
5. Revisa el JDK de Maven. Los nombres de las opciones pueden variar.

### Terminal
Desde la carpeta del proyecto, selecciona siempre el archivo explícitamente:

    mvn -s .mvn/settings.xml clean compile
    mvn -s .mvn/settings.xml javafx:run

Guardar el archivo dentro de .mvn no hace que Maven lo cargue automáticamente.
La selección en IntelliJ tampoco configura las terminales externas.

### Diagnóstico
Comprueba primero el comando y el primer error, JDK/Maven, ruta del settings,
DNS/HTTPS de Nexus y coordenadas del artefacto. La disponibilidad depende de
los miembros del grupo: no se presupone que cualquier artefacto esté disponible.
Una compilación con caché local no demuestra una nueva descarga desde Nexus.
Para una comprobación opcional y sin borrar la caché habitual, usa una carpeta nueva:

    mvn -s .mvn/settings.xml -Dmaven.repo.local=.mvn/cache-verificacion clean compile

Revisa que las líneas de descarga indiquen nora-academic y la URL de Nexus.
No borres toda .m2, no desactives TLS ni sustituyas el mirror para ocultar fallos.
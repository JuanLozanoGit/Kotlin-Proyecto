#  Sistema de Gestión de Contenidos en Kotlin

---

##  Descripción General

Este proyecto es una aplicación de consola en **Kotlin** que permite:

- **Consultar** un catálogo de películas y series.
- **Cargar** contenidos desde un archivo **Excel** (.xlsx).
- **Realizar un análisis predictivo** de los contenidos.
- **Sugerir recomendaciones** para nuevos contenidos.

Se utiliza **Apache POI** para leer archivos de Excel.

---

##  Estructura del Proyecto

- **Contenido.kt**: Clase que representa un contenido (película o serie).
- **Catalogo.kt**: Clase que gestiona los contenidos (agregar, consultar, cargar, analizar).
- **Funciones auxiliares**: Manejo de entrada de usuario.
- **Main.kt**: Punto de entrada del programa.

---

##  Descripción de las Clases y Funciones

### Contenido

Representa una película o serie.

**Atributos:**
- `id`: Identificador único.
- `titulo`: Título del contenido.
- `tipo`: "Serie" o "Película".
- `rating`: Calificación promedio.
- `duracion`: Duración en minutos.
- `genero`: Género principal.
- `año`: Año de lanzamiento.

**Métodos:**
- Getters para cada atributo.
- `mostrarInfo()`: Imprime en consola la información del contenido.

---

### Catalogo

Gestiona la colección de contenidos.

**Métodos principales:**

- `agregarContenido(contenido: Contenido)`: Agrega un contenido si no existe el ID.
- `consultarContenidos()`: Muestra todos los contenidos.
- `cargarDesdeExcel(filePath: String)`: Carga contenidos desde un archivo Excel.
- `analizarContenidos()`: Realiza un análisis de los contenidos:
  - Número de series y películas.
  - Rating promedio.
  - Géneros con mejor aceptación.
  - Recomendación predictiva basada en datos.

---

### Funciones Auxiliares

- `leerOpcion()`: Valida que el input del usuario sea un número entero.

---

### Main

Controla el flujo del programa mostrando un menú de opciones:

- **1. Consultar contenidos**.
- **2. Cargar desde Excel**.
- **3. Análisis predictivo**.
- **0. Salir**.

Utiliza un `do-while` para mantener activo el menú hasta que el usuario decida salir.

---

##  Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación.
- **Apache POI** - Lectura de archivos `.xlsx`.

---

##  Manejo de Errores

- Verificación de duplicidad de ID.
- Validación de entrada del usuario.
- Manejo de errores al leer archivos Excel o procesar datos corruptos.

---

##  Posibles Mejoras Futuras

- Persistir datos en una base de datos (SQLite, MySQL).
- Crear una interfaz gráfica (JavaFX / Compose).
- Exportar resultados a nuevos archivos Excel o CSV.
- Añadir machine learning real para recomendaciones avanzadas.

---

##  Conclusión

Este proyecto demuestra:

- Buenas prácticas de **POO** en Kotlin.
- Manejo básico de archivos externos.
- Implementación de análisis predictivo simple.
- Separación clara de responsabilidades en el código.

Ideal para bases de proyectos más complejos en gestión de datos.

---
## Autores:
Juan Lozano, Andres Espitia, Julio Guarnizo, Maria Parra

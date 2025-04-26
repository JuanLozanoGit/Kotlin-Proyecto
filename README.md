📚 Sistema de Gestión de Contenidos
Este proyecto en Kotlin permite gestionar un catálogo de series y películas, cargar datos desde un archivo Excel (.xlsx) y realizar un análisis predictivo básico para sugerir características ideales de nuevos contenidos.

🚀 Funcionalidades
Consultar listado de contenidos (series y películas).

Cargar información automáticamente desde archivos Excel.

Realizar análisis predictivo sobre el catálogo:

Estadísticas de cantidad de contenidos.

Rating promedio por tipo (serie o película).

Identificación de géneros más populares.

Recomendación de características para nuevos contenidos.

🛠️ Tecnologías Utilizadas
Kotlin (JVM)

Apache POI (org.apache.poi.ss.usermodel, org.apache.poi.xssf.usermodel) para lectura de archivos Excel.

📂 Estructura de Clases

Clase	Descripción
Contenido	Representa un contenido individual (serie o película) con atributos como título, género, duración, rating, etc.
Catalogo	Gestiona una colección de Contenido, permite agregar, listar, cargar desde Excel y realizar análisis predictivo.
Main.kt	Implementa el menú de opciones y la interacción con el usuario.
🧩 Requisitos Previos
JDK 8 o superior

Gradle o Maven si deseas gestionar dependencias

Agregar dependencias de Apache POI en tu proyecto:

gradle
Copy
Edit
dependencies {
    implementation 'org.apache.poi:poi:5.2.3'
    implementation 'org.apache.poi:poi-ooxml:5.2.3'
}
(Versiones actualizadas de Apache POI)

📝 Formato del Archivo Excel
El archivo debe tener las siguientes columnas (en este orden, desde la celda A1):


ID	Título	Tipo	Rating	Duración (min)	Género	Año
Ejemplo:


ID	Título	Tipo	Rating	Duración	Género	Año
1	Stranger Things	Serie	8.7	50	Ciencia Ficción	2016
2	Inception	Película	8.8	148	Ciencia Ficción	2010
Notas:

La primera fila debe ser de encabezado.

Los datos deben ser consistentes para un correcto procesamiento.

🖥️ Cómo Usarlo
Ejecuta el programa.

Elige una opción del menú:

1: Consultar los contenidos actuales en memoria.

2: Cargar nuevos contenidos desde un archivo .xlsx.

3: Realizar un análisis predictivo del catálogo.

0: Salir del programa.

Si eliges cargar un Excel, proporciona la ruta del archivo cuando sea solicitada.

Ejemplo de menú:
bash
Copy
Edit
--- SISTEMA DE GESTIÓN DE CONTENIDOS ---
1. Consultar contenidos
2. Cargar desde Excel
3. Análisis predictivo
0. Salir
⚙️ Análisis Predictivo Incluye:
Conteo de series y películas.

Rating promedio por tipo de contenido.

Top 3 géneros con mejor rating.

Recomendación para un nuevo contenido basándose en los datos históricos.

👨‍💻 Autor
Juan Lozano, Andres Espitia, Julio Guarnizo, Maria Parra

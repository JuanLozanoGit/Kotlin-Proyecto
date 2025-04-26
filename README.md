# Sistema de Gestión de Contenidos Audiovisuales

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue.svg)
![Gradle](https://img.shields.io/badge/Gradle-8.4-green.svg)
![POI](https://img.shields.io/badge/Apache_POI-5.2.3-red.svg)

Aplicación Kotlin para gestionar y analizar catálogos de contenidos audiovisuales (series y películas) con capacidades predictivas.

## Características Principales

- 📊 **Gestión de contenidos**: Añadir, consultar y analizar series y películas
- 📥 **Importación desde Excel**: Carga datos desde archivos XLSX
- 🔮 **Análisis predictivo**: Recomendaciones basadas en datos históricos
- 📈 **Estadísticas**: Rating promedio, géneros más populares, duración óptima

## Requisitos Técnicos

- Java 17 o superior
- Gradle 8.4+
- Kotlin 1.9.22

## Configuración

1. Clona el repositorio
2. Configura el JDK 17 en tu IDE
3. Actualiza las dependencias con Gradle

```bash
./gradlew build

##Estructura del Archivo Excel
##El archivo Excel debe tener esta estructura en la primera hoja:

ID	Título	Tipo	Rating	Duración	Género	Año
tt123456	Stranger Things	Serie	8.7	34	Ciencia ficción	2016
tt234567	The Irishman	Película	7.8	209	Drama	2019
Uso
Ejecuta la aplicación y selecciona opciones del menú:

##Consultar contenidos: Muestra todo el catálogo

##Cargar desde Excel: Importa datos desde archivo XLSX

##Análisis predictivo: Genera recomendaciones basadas en datos

##Salir: Cierra la aplicación

##Análisis Predictivo Incluye
Comparativa series vs películas

Top 3 géneros por rating

Duración óptima por tipo de contenido

Recomendaciones para nuevos contenidos

##Ejemplo de Salida
=== ANÁLISIS PREDICTIVO ===

##ESTADÍSTICAS BÁSICAS:
Total contenidos: 15
Series: 8 | Películas: 7

##RATING PROMEDIO:
Series: 8.4
Películas: 7.6

##GÉNEROS CON MEJOR RATING:
1. Thriller: 8.7
2. Ciencia ficción: 8.5
3. Drama: 7.9

##RECOMENDACIÓN PARA NUEVO CONTENIDO:
Tipo: Serie
Género: Thriller
Duración sugerida: 52min
Rating esperado: 8.4
Licencia
MIT License - Ver LICENSE para más detalles.


##Este README incluye:

1. Badges para mostrar tecnologías usadas
2. Sección clara de características
3. Requisitos técnicos específicos
4. Instrucciones de configuración
5. Estructura requerida del Excel
6. Explicación del análisis predictivo
7. Ejemplo de salida del sistema
8. Información de licencia


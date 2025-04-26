# 🎬 Sistema de Gestión y Análisis de Contenidos Audiovisuales

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-blue?logo=kotlin)
![Gradle](https://img.shields.io/badge/Gradle-8.4-green?logo=gradle)
![POI](https://img.shields.io/badge/Apache_POI-5.2.3-red?logo=apache)
![License](https://img.shields.io/badge/License-MIT-yellow)

Aplicación avanzada para la gestión y análisis predictivo de catálogos audiovisuales, diseñada para productoras y plataformas de streaming. Procesa datos de series y películas, generando insights valiosos para la toma de decisiones.

## ✨ Características Destacadas

### 📦 Gestión de Contenidos
- **CRUD completo** para series y películas
- **Búsqueda y filtrado** por múltiples criterios
- **Visualización organizada** en formato tabla

### 📊 Análisis de Datos
- **Métricas clave**: Rating promedio, duración óptima
- **Comparativas**: Series vs Películas
- **Tendencias temporales**: Evolución por año

### 🔮 Motor Predictivo
- **Recomendaciones inteligentes** para nuevos contenidos
- **Identificación de patrones** en géneros exitosos
- **Proyección de performance** basada en datos históricos

## 🛠 Requisitos Técnicos

| Componente | Versión |
|------------|---------|
| Java JDK | 17+ |
| Gradle | 8.4+ |
| Kotlin | 1.9.22 |
| Apache POI | 5.2.3 |

## 🚀 Configuración Rápida

1. Clonar repositorio:
```bash
git clone https://github.com/tu-repositorio/gestor-contenidos.git
cd gestor-contenidos
Configurar entorno:

bash
./gradlew build
Ejecutar aplicación:

bash
./gradlew run
📋 Estructura del Dataset (Excel)
El archivo debe contener estas columnas en la primera hoja:

csv
ID,Título,Tipo,Rating,Duración,Género,Año
tt123456,Stranger Things,Serie,8.7,34,Ciencia ficción,2016
tt234567,The Irishman,Película,7.8,209,Drama,2019
💡 Tip: Puedes exportar directamente desde bases de datos o herramientas como IMDb

🖥 Interfaz de Usuario
Menú principal interactivo:

=== SISTEMA DE GESTIÓN DE CONTENIDOS ===

1. 📄 Consultar catálogo completo
2. 📥 Cargar datos desde Excel
3. 🔍 Buscar contenidos
4. 📊 Análisis predictivo
5. 🛠 Herramientas avanzadas
0. ❌ Salir
🔍 Ejemplo de Análisis Predictivo
markdown
=== ANÁLISIS PREDICTIVO - RESUMEN EJECUTIVO ===

📅 Período analizado: 2015-2023
📊 Muestra: 142 contenidos (68 series / 74 películas)

TOP PERFORMERS:
1. The Crown (Rating: 9.1)
2. Dune (Rating: 8.8)
3. Stranger Things (Rating: 8.7)

📈 TENDENCIAS:
• Series: ↑12% rating promedio últimos 3 años
• Películas: ↓5% duración promedio

🎯 RECOMENDACIÓN 2024:
Tipo: Miniserie (8-10 episodios)
Género: Drama histórico
Duración: 52-58 min/episodio
Rating proyectado: 8.2-8.6
📚 Documentación Adicional
Guía de implementación avanzada

Formato completo de archivos Excel

API de integración

🤝 Integrantes:
Juan Lozano, Julio Guarnizo, Maria Parra, Andres Espitia

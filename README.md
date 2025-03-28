# Kotlin-Proyecto
# Análisis Predictivo de Datos Utilizando Kotlin y Dataset Público de Kaggle

## Objetivo

Desarrollar una aplicación en **Kotlin** que implemente un flujo completo de análisis de datos utilizando un conjunto de datos de Netflix descargado de Kaggle. El propósito es aplicar el **ciclo de vida del dato**, incluyendo:

- Carga y preprocesamiento del dataset
- Limpieza de datos
- División del dataset
- Entrenamiento de modelos
- Evaluación de métricas
- Presentación de resultados

---

## Dataset

Se utilizaron los siguientes archivos:

- `netflix_movies_detailed_up_to_2025.csv`
- `netflix_tv_shows_detailed_up_to_2025.csv`

Ambos se combinaron añadiendo la columna `content_type`:
- `0` para películas
- `1` para series

---

## Variables Analizadas

- `release_year`: Año de lanzamiento (variable numérica)
- `rating`: Clasificación por edades (variable categórica)
- `content_type`: Tipo de contenido (binario: película o serie)

---

## Preprocesamiento

- Se eliminaron filas con valores nulos.
- Se aplicó codificación One-Hot a la variable `rating`.
- Se normalizaron y combinaron los datos en una sola matriz de entrada.

---

## División del Dataset

- División 80% entrenamiento / 20% prueba.
- Se utilizó `sampleSplit` de la librería `Smile`.

---

## Modelos Utilizados

| Objetivo                 | Tipo        | Modelo              |
|--------------------------|-------------|---------------------|
| Tipo de contenido        | Clasificación binaria | Softmax |
| Año de lanzamiento       | Regresión   | OLS (mínimos cuadrados) |
| Clasificación de rating  | Clasificación multiclase | Softmax |

---

## Métricas de Evaluación

- **Clasificación binaria y multiclase**:
  - Precisión (`Accuracy`)
- **Regresión**:
  - MAE (Error absoluto medio)
  - RMSE (Raíz del error cuadrático medio)

---

## Resultados (Ejemplo de Salida)


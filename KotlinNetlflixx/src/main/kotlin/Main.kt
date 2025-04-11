import smile.data.DataFrame
import smile.data.formula.Formula
import smile.regression.ols
import smile.validation.mse
import smile.validation.rmse
import java.io.File
import com.opencsv.CSVReader
import java.io.FileReader

fun main() {
    println("🎬 Análisis Predictivo de Ratings de Películas")
    
    // 1. Carga de datos
    val datasetFile = File("movies_2010.csv") // Asumiendo que el CSV se llama así
    val movies = loadMovieData(datasetFile)
    
    if (movies.isEmpty()) {
        println("No se pudieron cargar los datos. Verifique el archivo CSV.")
        return
    }
    
    println("\n📊 Dataset cargado con ${movies.size} películas")
    
    // 2. Preprocesamiento
    val (features, target) = preprocessData(movies)
    
    // 3. División train-test (80-20)
    val splitIndex = (features.size * 0.8).toInt()
    val trainFeatures = features.copyOfRange(0, splitIndex)
    val trainTarget = target.copyOfRange(0, splitIndex)
    val testFeatures = features.copyOfRange(splitIndex, features.size)
    val testTarget = target.copyOfRange(splitIndex, target.size)
    
    println("\n🔀 Datos divididos:")
    println(" - Entrenamiento: ${trainFeatures.size} muestras")
    println(" - Prueba: ${testFeatures.size} muestras")
    
    // 4. Entrenamiento del modelo
    println("\n🏋️ Entrenando modelo de regresión lineal...")
    val formula = Formula.lhs("rating")
    val df = DataFrame.of(trainFeatures, arrayOf("year", "duration", "budget", "rating"))
    val model = ols(formula, df)
    
    println("\n📈 Modelo entrenado:")
    println(model)
    
    // 5. Evaluación
    val predictions = testFeatures.map { model.predict(doubleArrayOf(it[0], it[1], it[2])) }
    val testMSE = mse(testTarget, predictions)
    val testRMSE = rmse(testTarget, predictions)
    
    println("\n📊 Métricas de evaluación:")
    println(" - MSE: %.4f".format(testMSE))
    println(" - RMSE: %.4f".format(testRMSE))
    
    // 6. Predicción
    val yearToPredict = 2025.0
    val sampleMovie = doubleArrayOf(yearToPredict, 120.0, 50_000_000.0) // año, duración, presupuesto
    val predictedRating = model.predict(sampleMovie)
    println("\n🔮 Predicción para una película en $yearToPredict:")
    println(" - Rating estimado: %.2f".format(predictedRating))
}

data class Movie(
    val title: String,
    val year: Int,
    val duration: Double,
    val budget: Double,
    val revenue: Double,
    val rating: Double,
    val genres: List<String>
)

fun loadMovieData(file: File): List<Movie> {
    if (!file.exists()) return emptyList()
    
    return CSVReader(FileReader(file)).use { reader ->
        reader.readAll().drop(1) // Saltar encabezados
            .mapNotNull { row ->
                try {
                    Movie(
                        title = row[1],
                        year = row[8].toInt(),
                        duration = row[9].toDouble(),
                        budget = row[14].toDouble(),
                        revenue = row[15].toDouble(),
                        rating = row[7].toDouble(),
                        genres = row[10].split(", ").map { it.trim() }
                    )
                } catch (e: Exception) {
                    null // Ignorar filas con errores
                }
            }
    }
}

fun preprocessData(movies: List<Movie>): Pair<Array<DoubleArray>, DoubleArray> {
    // Filtrar películas con datos faltantes
    val cleanMovies = movies.filter { 
        it.budget > 0 && it.revenue > 0 && it.rating > 0 && it.duration > 0 
    }
    
    // Normalización Min-Max
    val maxYear = cleanMovies.maxOf { it.year }.toDouble()
    val minYear = cleanMovies.minOf { it.year }.toDouble()
    val maxDuration = cleanMovies.maxOf { it.duration }
    val minDuration = cleanMovies.minOf { it.duration }
    val maxBudget = cleanMovies.maxOf { it.budget }
    val minBudget = cleanMovies.minOf { it.budget }
    
    val features = cleanMovies.map { movie ->
        doubleArrayOf(
            (movie.year - minYear) / (maxYear - minYear), // año normalizado
            (movie.duration - minDuration) / (maxDuration - minDuration), // duración normalizada
            (movie.budget - minBudget) / (maxBudget - minBudget) // presupuesto normalizado
        )
    }.toTypedArray()
    
    val target = cleanMovies.map { it.rating }.toDoubleArray()
    
    return Pair(features, target)
}

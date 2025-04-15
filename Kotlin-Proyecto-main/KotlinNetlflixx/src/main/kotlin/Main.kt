import com.opencsv.CSVReader
import smile.data.DataFrame
import smile.data.formula.Formula
import smile.data.measure.NominalScale
import smile.data.vector.IntVector
import smile.data.vector.StringVector
import smile.regression.RandomForest
import smile.validation.CrossValidation
import smile.validation.RegressionMetrics
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

// Extension function for rounding
fun Double.roundToInt(): Int = round(this).toInt()

// Netflix content data structure
data class NetflixContent(
    val showId: String,
    val type: String,
    val title: String,
    val director: String?,
    val cast: String?,
    val country: String?,
    val dateAdded: String?,
    val releaseYear: Int,
    val rating: String?,
    val duration: String,
    val listedIn: String,
    val description: String?,
    val durationMinutes: Int
)

// Complete statistics
data class NetflixStats(
    val totalMovies: Int,
    val totalShows: Int,
    val avgMovieYear: Double,
    val avgShowYear: Double,
    val avgMovieDuration: Double,
    val avgShowDuration: Double,
    val commonGenres: Map<String, Int>,
    val yearRange: Pair<Int, Int>,
    val movieDurationTrend: RegressionResult,
    val showSeasonTrend: RegressionResult
)

// Regression results
data class RegressionResult(
    val intercept: Double,
    val slope: Double,
    val rmse: Double,
    val xLabel: String,
    val yLabel: String
)

class NetflixAnalyzer(
    private val moviesPath: String = "input/netflix_titles.csv",
    private val showsPath: String = "input/shows_2010.csv"
) {
    private val movies = mutableListOf<NetflixContent>()
    private val shows = mutableListOf<NetflixContent>()
    private var ratingLevels: List<String> = emptyList()

    // Load all data
    fun loadData() {
        println("Loading data from resources/input/")
        loadMovies()
        loadShows()
        println("Data loaded: ${movies.size} movies and ${shows.size} TV shows")
    }

    private fun loadMovies() {
        try {
            CSVReader(InputStreamReader(getResourceAsStream(moviesPath))).use { reader ->
                reader.readAll().drop(1).forEach { row ->
                    try {
                        movies.add(createContent(row, "Movie"))
                    } catch (e: Exception) {
                        println("Error processing movie ${row.getOrNull(2)}: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            System.err.println("\nERROR: Could not load movies")
            System.err.println("Look for the file at: src/main/resources/$moviesPath")
            throw e
        }
    }

    private fun loadShows() {
        try {
            CSVReader(InputStreamReader(getResourceAsStream(showsPath))).use { reader ->
                reader.readAll().drop(1).forEach { row ->
                    try {
                        shows.add(createContent(row, "TV Show"))
                    } catch (e: Exception) {
                        println("Error processing TV show ${row.getOrNull(2)}: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            System.err.println("\nERROR: Could not load TV shows")
            System.err.println("Look for the file at: src/main/resources/$showsPath")
            throw e
        }
    }

    private fun getResourceAsStream(path: String): InputStream {
        return javaClass.classLoader.getResourceAsStream(path)
            ?: throw FileNotFoundException("File not found: $path")
    }

    private fun createContent(row: Array<String>, type: String): NetflixContent {
        return NetflixContent(
            showId = row.getOrElse(0) { "" },
            type = type,
            title = row.getOrElse(2) { "Untitled" },
            director = row.getOrElse(3) { "" }.takeIf { it.isNotBlank() },
            cast = row.getOrElse(4) { "" }.takeIf { it.isNotBlank() },
            country = row.getOrElse(5) { "" }.takeIf { it.isNotBlank() },
            dateAdded = row.getOrElse(6) { "" }.takeIf { it.isNotBlank() },
            releaseYear = row.getOrElse(7) { "0" }.toIntOrNull() ?: 0,
            rating = row.getOrElse(8) { "" }.takeIf { it.isNotBlank() },
            duration = row.getOrElse(9) { "" },
            listedIn = row.getOrElse(10) { "" },
            description = row.getOrElse(11) { "" }.takeIf { it.isNotBlank() },
            durationMinutes = parseDuration(row.getOrElse(9) { "" }, type)
        )
    }

    private fun parseDuration(duration: String, type: String): Int {
        return try {
            if (type == "Movie") {
                duration.replace(" min", "").toIntOrNull() ?: 0
            } else {
                duration.replace(Regex(" Seasons?", RegexOption.IGNORE_CASE), "")
                    .toIntOrNull()?.times(120) ?: 0 // 120 min per season estimate
            }
        } catch (e: Exception) {
            0
        }
    }

    // Complete analysis
    fun analyze(): NetflixStats {
        val allContent = movies + shows

        // Basic statistics
        val movieYears = movies.map { it.releaseYear }
        val showYears = shows.map { it.releaseYear }
        val allYears = allContent.map { it.releaseYear }

        // Genre distribution
        val genreDistribution = allContent
            .flatMap { it.listedIn.split(",") }
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }
            .take(10)
            .toMap()

        // Temporal trends
        val movieTrend = analyzeTrend(
            movies.map { it.releaseYear.toDouble() }.toDoubleArray(),
            movies.map { it.durationMinutes.toDouble() }.toDoubleArray(),
            "Year",
            "Duration (min)"
        )

        val showTrend = analyzeTrend(
            shows.map { it.releaseYear.toDouble() }.toDoubleArray(),
            shows.map { (it.durationMinutes / 120).toDouble() }.toDoubleArray(),
            "Year",
            "Seasons"
        )

        return NetflixStats(
            totalMovies = movies.size,
            totalShows = shows.size,
            avgMovieYear = movieYears.average(),
            avgShowYear = showYears.average(),
            avgMovieDuration = movies.map { it.durationMinutes.toDouble() }.average(),
            avgShowDuration = shows.map { it.durationMinutes.toDouble() }.average(),
            commonGenres = genreDistribution,
            yearRange = allYears.minOrNull()!! to allYears.maxOrNull()!!,
            movieDurationTrend = movieTrend,
            showSeasonTrend = showTrend
        )
    }

    private fun analyzeTrend(x: DoubleArray, y: DoubleArray, xLabel: String, yLabel: String): RegressionResult {
        require(x.size == y.size) { "x and y arrays must have the same size" }

        val n = x.size
        val sumX = x.sum()
        val sumY = y.sum()
        val sumXY = x.zip(y).sumOf { it.first * it.second }
        val sumX2 = x.sumOf { it.pow(2) }

        val slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX.pow(2))
        val intercept = (sumY - slope * sumX) / n

        val predictions = x.map { intercept + slope * it }.toDoubleArray()
        val rmse = sqrt(y.zip(predictions).sumOf { (act, pred) -> (act - pred).pow(2) } / n)

        return RegressionResult(intercept, slope, rmse, xLabel, yLabel)
    }

    // Predictive analysis
    fun predictRatings() {
        println("\n=== RATING PREDICTION ANALYSIS ===")

        val allContent = movies + shows
        val (trainData, testData) = prepareRatingPredictionData(allContent)

        println("\nTraining Random Forest model...")
        val formula = Formula.lhs("rating_encoded")
        val model = RandomForest.fit(formula, trainData)

        println("\nEvaluating model...")
        val predictions = testData.stream().mapToDouble { model.predict(it) }.toArray()
        val trueValues = testData.intVector("rating_encoded").toIntArray().map { it.toDouble() }.toDoubleArray()

        val metrics = RegressionMetrics(trueValues, predictions)
        println("""
            Evaluation Metrics:
            - RMSE: ${"%.4f".format(metrics.rmse())}
            - MAE: ${"%.4f".format(metrics.mae())}
            - R²: ${"%.4f".format(metrics.r2())}
        """.trimIndent())

        // Prediction example
        println("\nPrediction example:")
        val sampleIndex = 0
        val sample = testData[sampleIndex]
        val predictedRating = model.predict(sample)
        val actualRatingEncoded = testData.intVector("rating_encoded").getInt(sampleIndex)
        val actualRating = if (actualRatingEncoded in ratingLevels.indices) {
            ratingLevels[actualRatingEncoded]
        } else {
            "Unknown Rating"
        }
        val ratingLevel = decodeRating(predictedRating)

        println("""
            Content: ${sample.getString("title")}
            Year: ${sample.getInt("releaseYear")}
            Duration: ${sample.getInt("durationMinutes")} min
            Main Genre: ${sample.getString("mainGenre")}
            
            Predicted Rating: ${"%.1f".format(predictedRating)} ($ratingLevel)
            Actual Rating: $actualRating
        """.trimIndent())
    }

    private fun prepareRatingPredictionData(content: List<NetflixContent>): Pair<DataFrame, DataFrame> {
        println("\nPreprocessing data for predictive model...")

        val filteredContent = content.filter { it.rating != null && it.rating.isNotBlank() }

        if (filteredContent.isEmpty()) {
            throw IllegalArgumentException("No filtered data available for prediction analysis.")
        }

        val size = filteredContent.size
        val showIds = filteredContent.map { it.showId }.toTypedArray()
        val titles = filteredContent.map { it.title }.toTypedArray()
        val types = filteredContent.map { it.type }.toTypedArray()
        val releaseYears = filteredContent.map { it.releaseYear }.toIntArray()
        val durationMinutes = filteredContent.map { it.durationMinutes }.toIntArray()
        val mainGenres = filteredContent.map { it.listedIn.split(",").first().trim() }.toTypedArray()
        val ratings = filteredContent.map { it.rating!! }.toTypedArray()

        // Rating encoding
        ratingLevels = ratings.distinct().sorted()
        val ratingScale = NominalScale.of(*ratingLevels.toTypedArray())
        val ratingEncoded = filteredContent.map { ratingScale.indexOf(it.rating!!) }.toIntArray()

        // Create DataFrame
        val df = DataFrame.of(
            listOf(
                StringVector.of("showId", showIds),
                StringVector.of("title", titles),
                StringVector.of("type", types),
                IntVector.of("releaseYear", releaseYears),
                IntVector.of("durationMinutes", durationMinutes),
                StringVector.of("mainGenre", mainGenres),
                StringVector.of("rating", ratings),
                IntVector.of("rating_encoded", ratingEncoded)
            )
        )

        // Split data using Cross-Validation
        val cv = CrossValidation(df.size(), 5)
        val train = df.slice(cv.train[0])
        val test = df.slice(cv.test[0])

        println("Data prepared: ${train.size()} training, ${test.size()} test")
        return Pair(train, test)
    }

    private fun decodeRating(encoded: Double): String {
        val index = encoded.roundToInt().coerceIn(0, ratingLevels.size - 1)
        return ratingLevels[index]
    }

    // Generate complete report
    fun generateReport() {
        val stats = analyze()

        println("\n=== NETFLIX ANALYSIS REPORT ===")
        println("\n[BASIC STATISTICS]")
        println("Movies: ${stats.totalMovies} | TV Shows: ${stats.totalShows}")
        println("Average Year: Movies ${stats.avgMovieYear.roundToInt()} | TV Shows ${stats.avgShowYear.roundToInt()}")
        println("Average Duration: Movies ${stats.avgMovieDuration.roundToInt()} min")
        println("Average Seasons: ${(stats.avgShowDuration / 120).roundToInt()} per show")
        println("Year Range: ${stats.yearRange.first} - ${stats.yearRange.second}")

        println("\n[MOST COMMON GENRES]")
        stats.commonGenres.forEach { (genre, count) ->
            val percentage = (count.toDouble() / (stats.totalMovies + stats.totalShows)) * 100
            println("- ${genre.padEnd(25)}: $count (${"%.1f".format(percentage)}%)")
        }

        println("\n[MOVIE DURATION TREND]")
        printTrend(stats.movieDurationTrend)

        println("\n[TV SHOW SEASON TREND]")
        printTrend(stats.showSeasonTrend)

        // Add predictive analysis
        predictRatings()
    }

    private fun printTrend(result: RegressionResult) {
        println("Relationship: ${result.yLabel} = ${"%.2f".format(result.intercept)} + ${"%.2f".format(result.slope)} * ${result.xLabel}")
        println("RMSE: ${"%.2f".format(result.rmse)}")
        println("Example: In 2010 → ${result.yLabel} = ${"%.1f".format(result.intercept + result.slope * 2010)}")
    }
}

fun main() {
    try {
        println("=== NETFLIX CONTENT ANALYZER ===")

        val analyzer = NetflixAnalyzer()
        analyzer.loadData()
        analyzer.generateReport()

        println("\nAnalysis completed successfully!")
    } catch (e: FileNotFoundException) {
        System.err.println("\nERROR: Files not found")
        System.err.println("Ensure you have this structure:")
        System.err.println("src/main/resources/input/")
        System.err.println("  ├── netflix_titles.csv")
        System.err.println("  └── shows_2010.csv")
    } catch (e: Exception) {
        System.err.println("\nERROR: ${e.message}")
        e.printStackTrace()
    }
}
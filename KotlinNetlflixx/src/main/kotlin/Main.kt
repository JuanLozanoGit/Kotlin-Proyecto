fun main() {
    println("📊 Datos de entrenamiento...")

    val releaseYears = doubleArrayOf(2020.0, 2021.0, 2022.0, 2023.0, 2024.0)
    val ratings = doubleArrayOf(7.5, 8.0, 7.0, 9.0, 8.5)

    val n = releaseYears.size
    val meanX = releaseYears.average()
    val meanY = ratings.average()

    var numerator = 0.0
    var denominator = 0.0

    for (i in 0 until n) {
        numerator += (releaseYears[i] - meanX) * (ratings[i] - meanY)
        denominator += (releaseYears[i] - meanX) * (releaseYears[i] - meanX)
    }

    val slope = numerator / denominator
    val intercept = meanY - slope * meanX

    println("📈 Ecuación de la recta: rating = %.4f * year + %.4f".format(slope, intercept))

    val yearToPredict = 2025.0
    val prediction = slope * yearToPredict + intercept
    println("🔮 Predicción para el año %.0f: %.2f".format(yearToPredict, prediction))
}

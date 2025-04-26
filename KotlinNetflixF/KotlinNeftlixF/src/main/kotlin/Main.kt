import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.File
import java.time.LocalDate
import kotlin.math.roundToInt

class Contenido(
    private val id: String,
    private var titulo: String,
    private var tipo: String,
    private var rating: Double,
    private var duracion: Int,
    private var genero: String,
    private var año: Int
) {
    fun getId(): String = id
    fun getTitulo(): String = titulo
    fun getTipo(): String = tipo
    fun getRating(): Double = rating
    fun getDuracion(): Int = duracion
    fun getGenero(): String = genero
    fun getAño(): Int = año

    fun mostrarInfo() {
        println(
            "ID: ${id.padEnd(10)} | Título: ${titulo.padEnd(20)} | " +
                    "Tipo: ${tipo.padEnd(8)} | Rating: ${"%.1f".format(rating)} | " +
                    "Duración: ${duracion} min | Género: ${genero.padEnd(15)} | Año: $año"
        )
    }
}

class Catalogo {
    private val contenidos = mutableListOf<Contenido>()

    fun agregarContenido(contenido: Contenido) {
        if (contenidos.any { it.getId() == contenido.getId() }) {
            println("Error: ya existe un contenido con ese ID.")
        } else {
            contenidos.add(contenido)
            println("Contenido agregado con éxito.")
        }
    }

    fun consultarContenidos() {
        if (contenidos.isEmpty()) {
            println("No hay contenidos en el catálogo.")
        } else {
            println("\n=== LISTADO DE CONTENIDOS ===")
            contenidos.forEach { it.mostrarInfo() }
        }
    }

    fun cargarDesdeExcel(filePath: String) {
        try {
            FileInputStream(File(filePath)).use { fis ->
                XSSFWorkbook(fis).use { workbook ->
                    val sheet = workbook.getSheetAt(0) ?: run {
                        println("Error: La hoja de cálculo está vacía.")
                        return
                    }

                    var contenidosCargados = 0

                    for (row in sheet) {
                        if (row.rowNum == 0) continue // Saltar encabezados

                        try {
                            val id = row.getCell(0)?.toString() ?: ""
                            val titulo = row.getCell(1)?.toString() ?: ""
                            val tipo = row.getCell(2)?.toString() ?: ""
                            val rating = row.getCell(3)?.numericCellValue ?: 0.0
                            val duracion = row.getCell(4)?.numericCellValue?.toInt() ?: 0
                            val genero = row.getCell(5)?.toString() ?: ""
                            val año = row.getCell(6)?.numericCellValue?.toInt() ?: 0

                            if (id.isNotBlank() && titulo.isNotBlank()) {
                                val contenido = Contenido(id, titulo, tipo, rating, duracion, genero, año)
                                this.agregarContenido(contenido)
                                contenidosCargados++
                            }
                        } catch (e: Exception) {
                            println("Error al procesar fila ${row.rowNum + 1}: ${e.message}")
                        }
                    }

                    println("\nResumen de carga:")
                    println("Contenidos cargados: $contenidosCargados")
                }
            }
        } catch (e: Exception) {
            println("Error al leer el archivo Excel: ${e.message}")
        }
    }

    // ANÁLISIS PREDICTIVO (Versión simplificada)
    fun analizarContenidos() {
        if (contenidos.isEmpty()) {
            println("No hay datos para analizar.")
            return
        }

        println("\n=== ANÁLISIS PREDICTIVO ===")

        // 1. Datos básicos
        val total = contenidos.size
        val series = contenidos.filter { it.getTipo().equals("serie", ignoreCase = true) }
        val peliculas = contenidos.filter { it.getTipo().equals("película", ignoreCase = true) }

        println("\nESTADÍSTICAS BÁSICAS:")
        println("Total contenidos: $total")
        println("Series: ${series.size} | Películas: ${peliculas.size}")

        // 2. Rating por tipo
        val ratingSeries = if (series.isNotEmpty()) series.map { it.getRating() }.average() else 0.0
        val ratingPeliculas = if (peliculas.isNotEmpty()) peliculas.map { it.getRating() }.average() else 0.0

        println("\nRATING PROMEDIO:")
        println("Series: ${"%.1f".format(ratingSeries)}")
        println("Películas: ${"%.1f".format(ratingPeliculas)}")

        // 3. Géneros más populares (top 3)
        val generosPopulares = contenidos.groupBy { it.getGenero() }
            .mapValues { it.value.map { c -> c.getRating() }.average() }
            .toList()
            .sortedByDescending { it.second }
            .take(3)

        println("\nGÉNEROS CON MEJOR RATING:")
        generosPopulares.forEachIndexed { i, (genero, rating) ->
            println("${i+1}. $genero: ${"%.1f".format(rating)}")
        }

        // 4. Recomendación predictiva
        val mejorGenero = generosPopulares.firstOrNull()?.first ?: "Drama"
        val mejorTipo = if (ratingSeries > ratingPeliculas) "Serie" else "Película"
        val mejorDuracion = when (mejorTipo) {
            "Serie" -> series.map { it.getDuracion() }.average().roundToInt()
            else -> peliculas.map { it.getDuracion() }.average().roundToInt()
        }

        println("\nRECOMENDACIÓN PARA NUEVO CONTENIDO:")
        println("Tipo: $mejorTipo")
        println("Género: $mejorGenero")
        println("Duración sugerida: ${mejorDuracion}min")
        println("Rating esperado: ${"%.1f".format(maxOf(ratingSeries, ratingPeliculas))}")
    }
}

fun leerOpcion(): Int {
    while (true) {
        print("Seleccione una opción: ")
        val input = readLine()
        val opcion = input?.toIntOrNull()
        if (opcion != null) return opcion
        println("Entrada inválida. Por favor, ingrese un número del menú.")
    }
}

fun main() {
    val catalogo = Catalogo()
    var opcion: Int

    do {
        println("\n--- SISTEMA DE GESTIÓN DE CONTENIDOS ---")
        println("1. Consultar contenidos")
        println("2. Cargar desde Excel")
        println("3. Análisis predictivo")
        println("0. Salir")

        opcion = leerOpcion()

        when (opcion) {
            1 -> catalogo.consultarContenidos()
            2 -> {
                println("Ingrese ruta del archivo Excel:")
                val path = readLine()?.trim() ?: ""
                if (path.isNotBlank()) catalogo.cargarDesdeExcel(path)
            }
            3 -> catalogo.analizarContenidos()
            0 -> println("Saliendo...")
            else -> println("Opción no válida")
        }
    } while (opcion != 0)
}
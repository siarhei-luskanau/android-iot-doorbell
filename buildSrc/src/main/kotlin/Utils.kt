import java.io.File

fun pathOf(vararg folders: String?) =
    folders.filterNotNull().joinToString(separator = File.separator)

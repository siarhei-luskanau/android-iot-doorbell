import org.apache.tools.ant.taskdefs.condition.Os

import java.io.File

fun pathOf(vararg folders: String?) =
    folders.filterNotNull().joinToString(separator = File.separator)

fun platformExecutable(name: String, ext: String = "exe"): String =
    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        "$name.$ext"
    } else {
        name
    }

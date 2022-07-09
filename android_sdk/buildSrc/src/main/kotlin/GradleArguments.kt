object GradleArguments {
    const val EMULATOR_AVD_NAME = "emulator_avd_name"

    fun getEnvArgument(argument: String): String? =
        System.getenv(argument)
            ?: System.getenv(argument.toLowerCase())
            ?: System.getenv(argument.toUpperCase())
            ?: System.getProperty(argument)
            ?: System.getProperty(argument.toLowerCase())
            ?: System.getProperty(argument.toUpperCase())
}
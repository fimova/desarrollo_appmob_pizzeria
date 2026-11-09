package model

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME) // permite buscar la anotación en tiempo de ejecución con reflection
annotation class Promocionable
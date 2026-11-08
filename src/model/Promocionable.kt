package model

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME) // permite que la anotación esté disponible busque con reflection
annotation class Promocionable
package pt.ipmaia.varias_janelas




sealed class Destino(val route: String, val icon: Int, val title: String) {
    object Ecra01 : Destino(route = "ecra01", icon = R.drawable.baseline_shopping_cart_24, title = "Ecra01")
    object Ecra02 : Destino(route = "ecra02", icon = R.drawable.baseline_access_alarm_24, title = "Ecra02")
    object Ecra03 : Destino(route = "ecra03", icon = R.drawable.baseline_archive_24, title = "Ecra03")
    object Ecra04 : Destino(route = "ecra04", icon = R.drawable.baseline_archive_24, title = "Ecra04")
    object Ecra05 : Destino(route = "ecra05", icon = R.drawable.baseline_archive_24, title = "Ecra05")
    object Ecra06 : Destino(route = "ecra06", icon = R.drawable.baseline_archive_24, title = "Ecra06")

    companion object {
        val toList = listOf(Ecra01, Ecra02, Ecra03)
    }
}
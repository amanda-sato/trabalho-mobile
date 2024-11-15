package pt.ipmaia.varias_janelas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.ipmaia.varias_janelas.ui.theme.Varias_janelasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Varias_janelasTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
                    ProgramaPrincipal()
                }
            }
        }
    }
}

@Composable
fun ProgramaPrincipal() {
    val navController = rememberNavController()


    // Usa uma lista mutável para ingredientes
    val ingredientesList = rememberSaveable { mutableStateOf(mutableListOf<String>()) }

    // Define o callback onIngredientsSelected para adicionar ingredientes e atualizar a lista
    val onIngredientsSelected: (List<String>, String) -> Unit = { ingredients, category ->
        ingredientesList.value = ingredientesList.value.toMutableList().apply {
            addAll(ingredients) // Adiciona todos os novos ingredientes
        }
        println("Ingredientes selecionados: ${ingredientesList.value}")
        println("Categoria selecionada: $category")
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, appItems = Destino.toList) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                AppNavigation(
                    navController = navController,
                    onIngredientsSelected = onIngredientsSelected
                )
            }
        }
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    onIngredientsSelected: (List<String>, String) -> Unit
) {
    NavHost(navController, startDestination = Destino.Ecra03.route) {
        composable(Destino.Ecra01.route) {
            Ecra01(navController)
        }
        composable(Destino.Ecra02.route) {
            Ecra02(navController)
        }
        composable(Destino.Ecra03.route) {
            Ecra03(navController, onIngredientsSelected)
        }
        composable(
            route = Destino.Ecra04.route,
            arguments = listOf(
                navArgument("ingredients") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val ingredients = backStackEntry.arguments?.getString("ingredients")?.split(",") ?: emptyList()
            val category = backStackEntry.arguments?.getString("category") ?: ""

            Ecra04(
                selectedIngredients = ingredients,
                selectedCategory = category,
                navController = navController
            )
        }

        composable(
            route = Destino.Ecra05.route,
            arguments = listOf(navArgument("recipeName") { type = NavType.StringType }, navArgument("recipeDescription") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeName = backStackEntry.arguments?.getString("recipeName") ?: ""
            val recipeDescription = backStackEntry.arguments?.getString("recipeDescription") ?: ""
            Ecra05(recipeName, recipeDescription, navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, appItems: List<Destino>) {
    BottomNavigation(backgroundColor = colorResource(id = R.color.purple_700), contentColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        appItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(painterResource(id = item.icon), contentDescription = item.title, tint = if (currentRoute == item.route) Color.White else Color.White.copy(.4F))
                },
                label = {
                    Text(text = item.title, color = if (currentRoute == item.route) Color.White else Color.White.copy(.4F))
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

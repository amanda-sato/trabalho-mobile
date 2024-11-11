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
import androidx.compose.runtime.MutableState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.ipmaia.varias_janelas.ui.theme.Varias_janelasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
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

    val tipoPessoa = rememberSaveable { mutableStateOf("") }
    val nome = rememberSaveable { mutableStateOf("") }
    val telefone = rememberSaveable { mutableStateOf("") }
    val valor1 = rememberSaveable { mutableStateOf("") }
    val valor2 = rememberSaveable { mutableStateOf("") }

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
                AppNavigation(navController = navController, tipoPessoa, nome, telefone, valor1, valor2, onIngredientsSelected)
            }
        }
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    tipoPessoa: MutableState<String>,
    nome: MutableState<String>,
    telefone: MutableState<String>,
    valor1: MutableState<String>,
    valor2: MutableState<String>,
    onIngredientsSelected: (List<String>, String) -> Unit
) {
    NavHost(navController, startDestination = Destino.Ecra03.route) {
        composable(Destino.Ecra01.route) {
            Ecra01(tipoPessoa, nome, telefone, valor1, valor2, navController)
        }
        composable(Destino.Ecra02.route) {
            Ecra02(navController)
        }
        composable(Destino.Ecra03.route) {
            Ecra03(navController, onIngredientsSelected)
        }
    }
}



@Composable
fun BottomNavigationBar(navController: NavController, appItems: List<Destino>) {
    BottomNavigation(backgroundColor = colorResource(id = R.color.purple_700),contentColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        appItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title, tint=if(currentRoute == item.route) Color.White else Color.White.copy(.4F)) },
                label = { Text(text = item.title, color = if(currentRoute == item.route) Color.White else Color.White.copy(.4F)) },
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

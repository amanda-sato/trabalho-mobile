package pt.ipmaia.varias_janelas


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun Ecra01(
    navController: NavHostController
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", fontWeight = FontWeight.Bold, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de nome de usuário
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de senha
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de login
        Button(onClick = {
            if (username.isNotEmpty() && password.isNotEmpty()) {
                navController.navigate(Destino.Ecra02.route) // Navegar para Ecra02
            }
        }) {
            Text("Entrar")
        }
    }
}



@Composable
fun Ecra02(
    navController: NavHostController
) {
    val categories = listOf("Café da Manhã", "Almoço", "Jantar")
    val recipesByCategory = mapOf(
        "Café da Manhã" to listOf("Panquecas" to "Panquecas fofinhas", "Omelete" to "Omelete saudável"),
        "Almoço" to listOf("Salada César" to "Salada refrescante", "Frango Grelado" to "Frango com legumes"),
        "Jantar" to listOf("Sopa de Legumes" to "Sopa leve e nutritiva", "Macarrão ao Pesto" to "Macarrão saboroso")
    )

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var favoriteRecipes by remember { mutableStateOf(setOf<String>()) } // Set para armazenar receitas favoritas

    // Função para filtrar receitas pela pesquisa
    fun getFilteredRecipes(): List<Pair<String, String>> {
        return if (selectedCategory == null) {
            recipesByCategory.values.flatten()
        } else {
            recipesByCategory[selectedCategory] ?: emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Exibe categorias
        Text("Categorias", fontWeight = FontWeight.Bold)
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(categories) { category ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(120.dp)
                        .clickable { selectedCategory = if (selectedCategory == category) null else category },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe as receitas com base na categoria selecionada
        Text("Receitas", fontWeight = FontWeight.Bold)

        val filteredRecipes = getFilteredRecipes()

        LazyColumn {
            items(filteredRecipes) { (recipeName, recipeDescription) ->
                val isFavorite = favoriteRecipes.contains(recipeName)

                // Exibe cada receita com a opção de favoritar
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Destino.Ecra05.createRoute(recipeName, recipeDescription))
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipeName, fontWeight = FontWeight.Bold)
                        Text(recipeDescription)
                    }

                    IconButton(
                        onClick = {
                            // Adiciona ou remove a receita dos favoritos
                            if (isFavorite) {
                                favoriteRecipes = favoriteRecipes - recipeName
                            } else {
                                favoriteRecipes = favoriteRecipes + recipeName
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favoritar"
                        )
                    }
                }
            }
        }
    }
}






@Composable
fun Ecra03(
    navController: NavHostController,
    onIngredientsSelected: (List<String>, String) -> Unit
) {
    val categories = listOf("Café da Manhã", "Almoço", "Jantar")
    val popularIngredients = listOf("Ovos", "Leite", "Farinha", "Frango", "Arroz", "Tomate", "Queijo")

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var ingredientText by remember { mutableStateOf(TextFieldValue("")) }
    val selectedIngredients = remember { mutableStateListOf<String>() } // Lista mutável para acompanhar os ingredientes em tempo real

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Inserir Ingredientes", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para adicionar ingredientes
        OutlinedTextField(
            value = ingredientText,
            onValueChange = { ingredientText = it },
            label = { Text("Digite um ingrediente") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botão para adicionar o ingrediente à lista
        Button(onClick = {
            if (ingredientText.text.isNotBlank()) {
                selectedIngredients.add(ingredientText.text) // Adiciona o ingrediente diretamente à lista
                ingredientText = TextFieldValue("") // Limpa o campo após adicionar
            }
        }) {
            Text("Adicionar Ingrediente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Ingredientes Populares
        Text("Ingredientes Populares", fontWeight = FontWeight.Bold)
        LazyRow {
            items(popularIngredients) { ingredient ->
                Button(
                    onClick = {
                        if (ingredient !in selectedIngredients) {
                            selectedIngredients.add(ingredient) // Adiciona ingredientes populares à lista
                        }
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(ingredient)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe ingredientes selecionados em tempo real
        Text("Ingredientes Selecionados", fontWeight = FontWeight.Bold)
        LazyColumn {
            items(selectedIngredients) { ingredient ->
                Text("- $ingredient")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seletor de categoria de refeição
        Text("Escolha a Categoria da Refeição", fontWeight = FontWeight.Bold)
        LazyRow {
            items(categories) { category ->
                Button(
                    onClick = { selectedCategory = category },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = category,
                        fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão para confirmar e enviar os dados
        Button(
            onClick = {
                selectedCategory?.let { category ->
                    val ingredientsArg = selectedIngredients.joinToString(",") // Serializa os ingredientes
                    // Passando os parâmetros na navegação
                    navController.navigate(Destino.Ecra04.createRoute(ingredientsArg, category))
                }
            },
            enabled = selectedCategory != null && selectedIngredients.isNotEmpty()
        ) {
            Text("Confirmar e Buscar Receitas")
        }




    }
}





@Composable
fun Ecra04(
    selectedIngredients: List<String>, // Ingredientes selecionados
    selectedCategory: String, // Categoria selecionada
    navController: NavHostController
) {
    // Verifique se os dados estão sendo recebidos corretamente
    Log.d("Ecra04", "Ingredientes: $selectedIngredients, Categoria: $selectedCategory")

    // Simulando uma base de dados de receitas
    val recipes = listOf(
        "Panquecas" to listOf("Ovos", "Leite", "Farinha"),
        "Omelete" to listOf("Ovos", "Queijo"),
        "Frango com Arroz" to listOf("Frango", "Arroz"),
        "Pizza Margherita" to listOf("Tomate", "Queijo"),
        "Salada César" to listOf("Alface", "Frango", "Queijo")
    )

    // Filtrar receitas que podem ser feitas com os ingredientes selecionados
    val filteredRecipes = recipes.filter { (_, ingredients) ->
        ingredients.all { it in selectedIngredients }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Receitas para ${selectedCategory.lowercase()}",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Ingredientes selecionados
        Text("Ingredientes Disponíveis:")
        selectedIngredients.forEach { ingredient ->
            Text("- $ingredient")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Exibição das receitas filtradas
        Text("Receitas Sugeridas:", fontWeight = FontWeight.Bold)
        if (filteredRecipes.isEmpty()) {
            Text("Nenhuma receita encontrada com os ingredientes fornecidos.")
        } else {
            LazyColumn {
                items(filteredRecipes) { (recipeName, ingredients) ->
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                // Navegar para a tela de detalhes da receita (Ecra05)
                                navController.navigate(Destino.Ecra05.createRoute(recipeName, ingredients.joinToString(", ")))
                            }
                    ) {
                        Text(recipeName, fontWeight = FontWeight.Bold)
                        Text("Ingredientes: ${ingredients.joinToString(", ")}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão para voltar ou ajustar
        Button(onClick = {
            navController.popBackStack() // Retorna ao Ecrã03
        }) {
            Text("Voltar")
        }
    }
}




@Composable
fun Ecra05(
    recipeName: String,
    recipeDescription: String,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Detalhes da Receita: $recipeName",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Descrição da receita
        Text("Descrição: $recipeDescription", fontWeight = FontWeight.Normal)

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para voltar
        Button(onClick = {
            navController.popBackStack() // Voltar para a tela anterior
        }) {
            Text("Voltar")
        }
    }
}



@Composable
fun Ecra06(
    tipoPessoa: MutableState<String>,
    nome: MutableState<String>,
    telefone: MutableState<String>,
    navController: NavHostController // Adicionado navController aqui
) {
    // Usando LazyColumn para permitir o scroll
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Dados recebidos:", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tipo de pessoa: ${tipoPessoa.value}")
            Text("Nome: ${nome.value}")
            Text("Telefone: ${telefone.value}")

            // Se precisar adicionar mais informações, faça isso aqui.

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para voltar
            Button(onClick = {
                navController.popBackStack() // Retorna à tela anterior
            }) {
                Text("Voltar")
            }
        }
    }
}


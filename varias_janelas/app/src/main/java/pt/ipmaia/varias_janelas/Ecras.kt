package pt.ipmaia.varias_janelas


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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun Ecra01(
    tipoPessoa: MutableState<String>,
    nome: MutableState<String>,
    telefone: MutableState<String>,
    valor1: MutableState<String>,
    valor2: MutableState<String>,
    navController: NavHostController
) {
    var selectedOption by remember { mutableStateOf(valor1.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Escolha o tipo de pessoa:")

        Row {
            RadioButton(selected = selectedOption == valor1.value, onClick = { selectedOption = valor1.value })
            Text(valor1.value)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = selectedOption == valor2.value, onClick = { selectedOption = valor2.value })
            Text(valor2.value)
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = nome.value,
            onValueChange = { nome.value = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) { innerTextField ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (nome.value.isEmpty()) {
                    Text("Nome")
                }
                innerTextField()
            }
        }

        BasicTextField(
            value = telefone.value,
            onValueChange = { telefone.value = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) { innerTextField ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (telefone.value.isEmpty()) {
                    Text("Telefone")
                }
                innerTextField()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            tipoPessoa.value = selectedOption
            navController.navigate(Destino.Ecra02.route)
        }) {
            Text("Adicionar")
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
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    // Função para filtrar receitas pela pesquisa
    fun getFilteredRecipes(): List<Pair<String, String>> {
        return if (selectedCategory == null) {
            recipesByCategory.values.flatten().filter {
                it.first.contains(searchText.text, ignoreCase = true)
            }
        } else {
            recipesByCategory[selectedCategory]?.filter {
                it.first.contains(searchText.text, ignoreCase = true)
            } ?: emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de pesquisa
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Pesquisar Receita") }
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        // Exibe as receitas com base no filtro
        Text("Receitas", fontWeight = FontWeight.Bold)

        val filteredRecipes = getFilteredRecipes()

        if (filteredRecipes.isEmpty()) {
            Text("Nenhuma receita encontrada.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(filteredRecipes) { (recipeName, recipeDescription) ->
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(recipeName, fontWeight = FontWeight.Bold)
                        Text(recipeDescription)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Botão de voltar ao início da tela 2
        Button(onClick = {
            selectedCategory = null // Redefine a categoria
            searchText = TextFieldValue("") // Limpa o campo de pesquisa
        }) {
            Text("Voltar ao Início")
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
                selectedCategory?.let {
                    onIngredientsSelected(selectedIngredients, it) // Envia a lista de ingredientes e a categoria selecionada
                    navController.popBackStack() // Volta à tela anterior após confirmar
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


@Composable
fun Ecra05(
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


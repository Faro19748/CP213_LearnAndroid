package com.example.lab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lab.utils.PokemonEntry

class PokedexActivity : ComponentActivity() {

    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Lifecycle", "PokedexActivity : onCreate")
        enableEdgeToEdge()
        setContent {
            // เรียก fetch ข้อมูลเมื่อหน้าจอถูกสร้างขึ้น
            LaunchedEffect(Unit) {
                viewModel.fetchPokemon()
            }
            ListScreen(viewModel)
        }
    }
}

@Composable
fun ListScreen(viewModel: PokemonViewModel) {
    // ดึงข้อมูลจาก ViewModel มาเป็น State
    val pokemonList by viewModel.pokemonList.collectAsState()
    
    // ส่งข้อมูลต่อไปยัง Content UI (Stateless)
    PokedexContent(pokemonList = pokemonList)
}

@Composable
fun PokedexContent(pokemonList: List<PokemonEntry>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE3350D)) // สีแดงสไตล์ Pokemon
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Pokedex (Kanto)",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0F0F0)), // สีพื้นหลังเทาอ่อน
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pokemonList) { item ->
                PokemonItem(item)
            }
        }
    }
}

@Composable
fun PokemonItem(item: PokemonEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ลำดับโปเกมอน
            Text(
                text = "#${item.entry_number.toString().padStart(3, '0')}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(12.dp))

            // รูปภาพโปเกมอนจาก URL
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${item.entry_number}.png"
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Sprite of ${item.pokemon_species.name}",
                modifier = Modifier.size(80.dp),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_report_image)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // ชื่อโปเกมอน (ตัวใหญ่ตัวแรก)
            Text(
                text = item.pokemon_species.name.replaceFirstChar { it.uppercase() },
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    // ใน Preview ให้เรียก PokedexContent แล้วส่ง List ว่างหรือ Mock Data เข้าไปแทนการใช้ ViewModel
    PokedexContent(pokemonList = emptyList())
}

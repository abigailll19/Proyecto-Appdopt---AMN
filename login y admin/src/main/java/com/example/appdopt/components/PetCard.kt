package com.example.appdopt.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.model.Pet
import com.example.appdopt.model.Species
import com.example.appdopt.ui.theme.CardBackground

@Composable
fun PetCard(
    pet: Pet,
    fontSizeMultiplier: Float,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Box {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.LightGray.copy(alpha = 0.5f)
                    )
                }

                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = (16 * fontSizeMultiplier).sp
                    )
                    Text(
                        text = "${if(pet.species == Species.CAT) "Gato" else "Perro"} • ${pet.breed}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontSize = (12 * fontSizeMultiplier).sp
                    )
                }
            }
            
            // Favorite Button
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (pet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (pet.isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

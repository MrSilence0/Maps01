package mx.edu.utng.oic.doloreshidalgoturismo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Barra de filtros por categoría
 * Permite mostrar solo iglesias, museos, etc.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilter(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Opción "Todos"
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Todos") }
            )
        }
        // Categorías individuales
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = {
                    // Si la categoría ya estaba seleccionada, la deselecciona (null)
                    onCategorySelected(if (selectedCategory == category) null else category)
                },
                label = { Text(category) }
            )
        }
    }
}
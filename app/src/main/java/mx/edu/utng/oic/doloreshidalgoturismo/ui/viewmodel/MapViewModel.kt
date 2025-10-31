package mx.edu.utng.oic.doloreshidalgoturismo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import mx.edu.utng.oic.doloreshidalgoturismo.data.model.PlaceEntity
import mx.edu.utng.oic.doloreshidalgoturismo.data.repository.PlaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.oic.doloreshidalgoturismo.utils.Logger

/**
 * ViewModel que maneja el estado del mapa y los lugares
 * Sigue el patrón MVVM (Model-View-ViewModel)
 */
class MapViewModel(
    private val repository: PlaceRepository
) : ViewModel() {

    // Estado de los lugares (observado por la UI)
    val places: StateFlow<List<PlaceEntity>> = repository.allPlaces
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Lugar seleccionado para editar
    private val _selectedPlace = MutableStateFlow<PlaceEntity?>(null)
    val selectedPlace: StateFlow<PlaceEntity?> = _selectedPlace.asStateFlow()

    // Categoría filtrada actualmente
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Estado del diálogo de agregar/editar
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    // Coordenadas del centro del mapa (Dolores Hidalgo por defecto)
    private val _mapCenter = MutableStateFlow(LatLng(21.1560, -100.9318))
    val mapCenter: StateFlow<LatLng> = _mapCenter.asStateFlow()

    // ----------------------------------------------------
    // AGREGAR: Lógica de manejo de errores
    // ----------------------------------------------------
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }
    // ----------------------------------------------------

    init {
        // Cargar lugares predeterminados si la base de datos está vacía
        viewModelScope.launch {
            try {
                places.first().let { placesList ->
                    if (placesList.isEmpty()) {
                        Logger.d("Inicializando base de datos con lugares predeterminados.")
                        repository.insertDefaultPlaces()
                    }
                }
            } catch (e: Exception) {
                Logger.e("Error en la inicialización de lugares", e)
                _errorMessage.value = "Error al cargar datos iniciales."
            }
        }
    }

    /**
     * Agregar un nuevo lugar turístico
     */
    fun addPlace(
        name: String,
        description: String,
        latLng: LatLng,
        category: String,
        markerColor: String
    ) {
        viewModelScope.launch {
            try {
                Logger.d("Agregando nuevo lugar: $name")

                val newPlace = PlaceEntity(
                    name = name,
                    description = description,
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    category = category,
                    markerColor = markerColor,
                    isFavorite = false,
                    // Se asume que PlaceEntity ya incluye createdAt:
                    // createdAt = System.currentTimeMillis()
                )

                val newId = repository.insertPlace(newPlace)
                _showDialog.value = false
                Logger.i("Lugar agregado exitosamente con ID: $newId")

            } catch (e: Exception) {
                Logger.e("Error al agregar lugar: $name", e)
                _errorMessage.value = "No se pudo agregar el lugar. Intenta nuevamente."
            }
        }
    }

    /**
     * Actualizar un lugar existente
     */
    fun updatePlace(place: PlaceEntity) {
        viewModelScope.launch {
            try {
                repository.updatePlace(place)
                _selectedPlace.value = null
                _showDialog.value = false
                Logger.i("Lugar actualizado: ${place.name}")
            } catch (e: Exception) {
                Logger.e("Error al actualizar lugar: ${place.name}", e)
                _errorMessage.value = "No se pudo actualizar el lugar. Intenta nuevamente."
            }
        }
    }

    /**
     * Eliminar un lugar
     */
    fun deletePlace(place: PlaceEntity) {
        viewModelScope.launch {
            try {
                repository.deletePlace(place)
                Logger.w("Lugar eliminado: ${place.name}")
            } catch (e: Exception) {
                Logger.e("Error al eliminar lugar: ${place.name}", e)
                _errorMessage.value = "No se pudo eliminar el lugar."
            }
        }
    }

    /**
     * Marcar/desmarcar como favorito
     */
    fun toggleFavorite(place: PlaceEntity) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(place.id, place.isFavorite)
            } catch (e: Exception) {
                Logger.e("Error al cambiar favorito de ${place.name}", e)
                _errorMessage.value = "No se pudo cambiar el estado de favorito."
            }
        }
    }

    /**
     * Filtrar por categoría
     */
    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
    }

    /**
     * Mostrar diálogo para agregar lugar
     */
    fun showAddDialog(latLng: LatLng) {
        _mapCenter.value = latLng
        _selectedPlace.value = null
        _showDialog.value = true
    }

    /**
     * Mostrar diálogo para editar lugar
     */
    fun showEditDialog(place: PlaceEntity) {
        _selectedPlace.value = place
        _showDialog.value = true
    }

    /**
     * Cerrar diálogo
     */
    fun dismissDialog() {
        _showDialog.value = false
        _selectedPlace.value = null
    }
}
package mx.edu.utng.oic.doloreshidalgoturismo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mx.edu.utng.oic.doloreshidalgoturismo.data.repository.PlaceRepository
//import mx.edu.utng.oic.doloreshidalgoturismo.presentation.map.MapViewModel
/**
 * Factory para crear el ViewModel con dependencias
 * Necesario porque el ViewModel requiere el repositorio
 */
class MapViewModelFactory(
    private val repository: PlaceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            // Se asegura que el ViewModel se cree con el repositorio inyectado
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
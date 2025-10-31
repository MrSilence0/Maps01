package mx.edu.utng.oic.doloreshidalgoturismo.data.repository

import mx.edu.utng.oic.doloreshidalgoturismo.data.dao.PlaceDao
import mx.edu.utng.oic.doloreshidalgoturismo.data.model.PlaceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repositorio que maneja todas las operaciones de datos
 * Separa la lógica de datos de la UI (Clean Architecture)
 */
class PlaceRepository(private val placeDao: PlaceDao) {

    // Flow se actualiza automáticamente cuando cambia la base de datos
    val allPlaces: Flow<List<PlaceEntity>> = placeDao.getAllPlaces()

    fun getPlacesByCategory(category: String): Flow<List<PlaceEntity>> {
        return placeDao.getPlacesByCategory(category)
    }

    suspend fun getPlaceById(id: Int): PlaceEntity? {
        return placeDao.getPlaceById(id)
    }

    suspend fun insertPlace(place: PlaceEntity): Long {
        return placeDao.insertPlace(place)
    }

    suspend fun updatePlace(place: PlaceEntity) {
        placeDao.updatePlace(place)
    }

    suspend fun deletePlace(place: PlaceEntity) {
        placeDao.deletePlace(place)
    }

    suspend fun toggleFavorite(placeId: Int, currentStatus: Boolean) {
        // Invierte el estado actual y lo actualiza en la base de datos
        placeDao.updateFavoriteStatus(placeId, !currentStatus)
    }

    /**
     * Insertar lugares turísticos predeterminados de Dolores Hidalgo
     * Se ejecuta la primera vez que se abre la app
     */
    suspend fun insertDefaultPlaces() {
        val defaultPlaces = listOf(
            PlaceEntity(
                name = "Parroquia de Nuestra Señora de los Dolores",
                description = "Emblemático templo donde Miguel Hidalgo dio el Grito de Independencia",
                latitude = 21.1558,
                longitude = -100.9314,
                category = "Iglesia",
                markerColor = "#FF6B35"
            ),
            PlaceEntity(
                name = "Casa de Don Miguel Hidalgo",
                description = "Museo histórico donde vivió el Padre de la Patria",
                latitude = 21.1565,
                longitude = -100.9320,
                category = "Museo",
                markerColor = "#4ECDC4"
            ),
            PlaceEntity(
                name = "Plaza Principal",
                description = "Corazón del municipio con jardines y fuentes",
                latitude = 21.1560,
                longitude = -100.9318,
                category = "Plaza",
                markerColor = "#95E1D3"
            )
        )
        defaultPlaces.forEach { place ->
            placeDao.insertPlace(place)
        }
    }
}
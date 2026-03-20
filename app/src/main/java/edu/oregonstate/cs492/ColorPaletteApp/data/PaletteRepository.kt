package edu.oregonstate.cs492.ColorPaletteApp.data

class PaletteRepository(private val dao: PaletteDao) {
    suspend fun insert(palette: Palette) {
        dao.insert(palette)
    }

    suspend fun delete(palette: Palette) {
        dao.delete(palette)
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun getAll() = dao.getAll()
}
import com.example.inventory.data.Game
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Game] from a given data source.
 */
interface GamesRepository {
    /**
     * Retrieve all the games from the the given data source.
     */
    fun getAllGamesStream(): Flow<List<Game>>

    /**
     * Retrieve a game from the given data source that matches with the [id].
     */
    fun getGameStream(id: Int): Flow<Game?>

    /**
     * Insert game in the data source
     */
    suspend fun insertGame(game: Game)

    /**
     * Delete game from the data source
     */
    suspend fun deleteGame(game: Game)

    /**
     * Update game in the data source
     */
    suspend fun updateGame(game: Game)
}

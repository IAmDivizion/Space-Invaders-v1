// Representa um projetil no ecra com posicao e velocidade de deslocamento
import pt.isel.canvas.*

const val SHOT_WIDTH = 4
const val SHOT_HEIGHT = 7
const val SHIP_SHOT_SPEED = 4
const val SHIP_SHOT_COLOR = WHITE
const val ALIEN_SHOT_COLOR = RED

data class Shot(
    val x: Int,
    val y: Int,
    val speed: Int = SHIP_SHOT_SPEED
) {
    fun moveUP(): Shot = copy(y = y - speed)
    fun moveDOWN(): Shot = copy(y = y + speed)

    // verifica se um certo disparo colide com outro disparo
    fun hitsShot(other: Shot): Boolean {
        return x < other.x + SHOT_WIDTH &&
                x + SHOT_WIDTH > other.x &&
                y < other.y + SHOT_HEIGHT &&
                y + SHOT_HEIGHT > other.y
    }

    // verifica se um certo disparo colide com o corpo verde da nave
    fun hitsShip(ship: SpaceShip): Boolean {
        val shipTopY = SHIP_Y - SHIP_HEIGHT / 2

        return x < ship.x + SHIP_WIDTH &&
                x + SHOT_WIDTH > ship.x &&
                y < shipTopY + SHIP_HEIGHT &&
                y + SHOT_HEIGHT > shipTopY
    }

    // ambas estas funcoes utilizam uma forma comum de verificacao de colisoes de objetos em 2D
}
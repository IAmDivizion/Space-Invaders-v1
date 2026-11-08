// Representa a nave do jogador e a sua posição horizontal.

import pt.isel.canvas.*

const val SHIP_WIDTH = 50
const val SHIP_HEIGHT = 10
const val CANNON_WIDTH = 4
const val CANNON_HEIGHT = 5
const val SHIP_Y = 450
const val SHIP_COLOR = GREEN
const val CANNON_COLOR = YELLOW

data class SpaceShip(
    val x: Int
) {
    fun moveTo(mouseX: Int): SpaceShip = SpaceShip(mouseX)
}
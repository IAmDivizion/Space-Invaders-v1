// Mantém e atualiza o estado do jogo.

data class Game(
    val area: Area,
    val alienShots: List<Shot> = emptyList(),
    val shipShot: Shot? = null,
    val ship: SpaceShip = SpaceShip((WIDTH - SHIP_WIDTH) / 2),
    val over: Boolean = false
) {

    // tenta efetuar um disparo apenas se nao existirem outros tiros ativos
    fun shoot(): Game {
        if (shipShot != null) return this

        //posicao inicial do tiro
        val startX = ship.x + (SHIP_WIDTH - SHOT_WIDTH) / 2
        val startY = SHIP_Y - CANNON_HEIGHT - SHIP_HEIGHT / 2 - SHOT_HEIGHT

        return copy(shipShot = Shot(startX, startY, SHIP_SHOT_SPEED))
    }

    // atualiza a posicao do tiro da nave e remove-o se sair da area de jogo
    fun updateShipShot(): Game {
        val shot = shipShot ?: return this
        val moved = shot.moveUP()

        return if (moved.y + SHOT_HEIGHT < 0) {
            copy(shipShot = null)
        } else {
            copy(shipShot = moved)
        }
    }

    // tenta gerar um disparo alienigena a cada 250ms com 50% de chance de gerar
    fun trySpawnAlienShot(): Game {
        // 50% de probabilidade de gerar
        val shouldSpawn = (1..100).random() <= 50
        if (!shouldSpawn) return this

        val randomX = (0 until WIDTH).random() // 0 a 699
        val randomSpeed = (1..4).random()      // 0 a 4 pixeis por frame
        val newShot = Shot(
            x = randomX,
            y = 0,
            speed = randomSpeed
        )

        return copy(alienShots = alienShots + newShot)
    }

    // atualiza a posicao dos tiros da nave e remove-os se sairem da area de jogo
    fun updateAlienShots(): Game {
        val updatedShots = alienShots
            .map { it.moveDOWN() }
            .filter { it.y < HEIGHT }

        return copy(alienShots = updatedShots)
    }

    // verifica e resolve as colisoes do jogo
    fun checkCollisions(): Game {
        if (over) return this

        // verifica se o disparo da nave colidiu com algum disparo alienigena
        val shipShotHit = shipShot != null && alienShots.any { it.hitsShot(shipShot) }

        // se colidiu, o disparo da nave desaparece
        val nextShipShot = if (shipShotHit) null else shipShot

        // remove os disparos dos aliens atingidos pelo tiro da nave
        val remainingAlienShots = if (shipShot == null) {
            alienShots
        } else {
            alienShots.filter { !it.hitsShot(shipShot) }
        }

        // verifica se algum dos disparos restantes dos aliens atingiu a nave
        val isHitByAlien = remainingAlienShots.any { it.hitsShip(ship) }

        return copy(
            alienShots = remainingAlienShots,
            shipShot = nextShipShot,
            over = isHitByAlien
        )
    }

    fun step(): Game {
        if (over) return this

        return updateShipShot()
            .updateAlienShots()
            .checkCollisions()
    }
}
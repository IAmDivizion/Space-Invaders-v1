// ponto de entrada do programa, gestao de eventos, e funcoes de desenho do jogo

import pt.isel.canvas.*

// desenha a nave, os disparos, e a linha
fun Canvas.drawGame(game: Game) {
    erase()

    // linha amarela a y = 450
    drawLine(
        xFrom = 0,
        yFrom = SHIP_Y,
        xTo = WIDTH - 1,
        yTo = SHIP_Y,
        color = YELLOW,
        thickness = 2
    )

    // nave
    drawRect(
        x = game.ship.x,
        y = SHIP_Y - SHIP_HEIGHT / 2,
        width = SHIP_WIDTH,
        height = SHIP_HEIGHT,
        color = SHIP_COLOR
    )

    // canhao
    drawRect(
        x = game.ship.x + (SHIP_WIDTH - CANNON_WIDTH) / 2,
        y = SHIP_Y - CANNON_HEIGHT - SHIP_HEIGHT / 2,
        width = CANNON_WIDTH,
        height = CANNON_HEIGHT,
        color = CANNON_COLOR
    )

    // disparo da nave
    if (game.shipShot != null) {
        drawRect(
            x = game.shipShot.x,
            y = game.shipShot.y,
            width = SHOT_WIDTH,
            height = SHOT_HEIGHT,
            color = SHIP_SHOT_COLOR
        )
    }

    // disparo dos aliens
    for (shot in game.alienShots) {
        drawRect(
            x = shot.x,
            y = shot.y,
            width = SHOT_WIDTH,
            height = SHOT_HEIGHT,
            color = ALIEN_SHOT_COLOR
        )
    }

    // se o jogo terminar, desenha GAME OVER no centro do ecra
    if (game.over) {
        drawText(
            x = WIDTH / 2 - 80,
            y = HEIGHT / 2,
            txt = "GAME OVER",
            color = RED,
            fontSize = 30)
    }
}

fun main() {

    // unica variavel alteravel (var) do programa
    var game = Game(
        area = Area(),
        alienShots = emptyList(),
        shipShot = null,
        ship = SpaceShip((WIDTH - SHIP_WIDTH) / 2),
        over = false
    )

    onStart {
        val arena = Canvas(WIDTH, HEIGHT, BLACK)

        // desenho inicial
        arena.drawGame(game)

        // atualizacao a 70 FPS (~ 14 ms por frame)
        arena.onTimeProgress(14) {
            if (!game.over) {
                game = game.step()
                arena.drawGame(game)
            }
        }

        // movimento da nave com o rato
        arena.onMouseMove { mouse ->
            game = game.copy(ship = game.ship.moveTo(mouse.x - SHIP_WIDTH / 2))
        }

        // disparo da nave com rato
        arena.onMouseDown {
            game = game.shoot()
        }

        // disparo da nave com espaco
        arena.onKeyPressed { key ->
            if (key.char == ' ') {
                game = game.shoot()
            }
        }

        // esperar 2 segundos, e comecar o loop de disparos alienigenas
        arena.onTime(2000) {
            arena.onTimeProgress(250) {
                game = game.trySpawnAlienShot()
            }
        }

    }

    onFinish {

    }
}
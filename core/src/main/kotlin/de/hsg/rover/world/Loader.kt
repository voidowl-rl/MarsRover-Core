package de.hsg.rover.world

import de.hsg.rover.entity.Entity
import de.hsg.rover.entity.Marker
import de.hsg.rover.entity.Rock
import java.io.File
import kotlin.random.Random

internal fun loadWorldFromText(lines: List<String>): WorldDefinition {
    var map = mutableListOf<Array<LandscapeType>>()
    var entities = mutableSetOf<Entity>()

    var roverX = -1
    var roverY = -1

    var y = 0
    lines.forEach {
        val line = it.replace(Regex("\\s"), "")

        if (!line.isNullOrBlank()) {
            val row = Array<LandscapeType>(line.length) {
                LandscapeType.Ground
            }

            line.forEachIndexed { x, chr ->
                when(chr) {
                    //'.' -> row[x] = LandscapeType.Ground
                    'H' -> row[x] = LandscapeType.Hill
                    'G' -> {
                        entities.add(Rock(0.0f, "blau", x, y))
                    }
                    'M' -> {
                        //row[x] = LandscapeType.Ground
                        entities.add(Marker(x, y))
                    }
                    'R' -> {
                        roverX = x
                        roverY = y
                    }
                }
            }

            map.add(row)
            y++
        }
    }

    if (map.size == 0 || map.any { it.size != map[0].size }) {
        throw RuntimeException("Kartendatei ist nicht korrekt formatiert!")
    }

    if (roverX == -1 || roverY == -1) {
        throw RuntimeException("Rover muss in Kartendatei gesetzt werden!")
    }

    return WorldDefinition(map.toTypedArray(), entities, Position(roverX, roverY))
}

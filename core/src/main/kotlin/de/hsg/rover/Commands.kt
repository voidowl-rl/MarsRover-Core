package de.hsg.rover

import de.hsg.rover.entity.Hill
import de.hsg.rover.entity.Marker
import de.hsg.rover.entity.Rock
import de.hsg.rover.entity.Rover
import de.hsg.rover.world.Direction
import de.hsg.rover.world.LandscapeType
import de.hsg.rover.world.Position
import de.hsg.rover.world.rotations

public fun fahre() {
    Thread.sleep(getTickSpeed())

    if (huegelVorhanden("vorne")) {
        return
    }

    var direction = world.getRoverDirection()
    var position = world.getRoverPosition()

    when (direction) {
        Direction.Right -> {
            if (position.x < world.size.x-1) world.setRoverPosition(position.x + 1, position.y)
        }
        Direction.Down -> {
            if (position.y > 0) world.setRoverPosition(position.x, position.y - 1)
        }
        Direction.Left -> {
            if (position.x > 0) world.setRoverPosition(position.x - 1, position.y)
        }
        Direction.Up -> {
            if (position.y < world.size.y-1) world.setRoverPosition(position.x, position.y + 1)
        }
    }
}

public fun drehe(richtung: String) {
    Thread.sleep(getTickSpeed())

    var direction = world.getRoverDirection()
    val index = rotations.indexOf(direction)

    when (richtung) {
        "rechts" -> {
            world.setRoverDirection(
                rotations[Math.floorMod(index + 1, 4)]
            )
        }
        "links" -> {
            world.setRoverDirection(
                rotations[Math.floorMod(index - 1, 4)]
            )
        }
        else -> {
            throw RuntimeException("$richtung ist keine gültige Drehrichtung!")
        }
    }
}

public fun analysiereGestein() {
    Thread.sleep(getTickSpeed())

    var position = world.getRoverPosition()

    world.removeEntity {
        it.x == position.x && it.y == position.y && it is Rock
    }
}

public fun setzeMarke() {
    Thread.sleep(getTickSpeed())

    val position = world.getRoverPosition()

    if (!world.hasEntity { it.x == position.x && it.y == position.y && it is Marker }) {
        val marker = Marker(position.x, position.y)
        world.addEntity(marker)
    }
}

public fun gesteinVorhanden(): Boolean {
    var position = world.getRoverPosition()

    return world.hasEntity {
        it.x == position.x && it.y == position.y && it is Rock
    }
}

public fun huegelVorhanden(richtung: String): Boolean {
    var position = world.getRoverPosition()
    var direction = world.getRoverDirection()

    val coordinates = listOf(
        Position(position.x, position.y+1), // North
        Position(position.x+1, position.y), // East
        Position(position.x, position.y-1), // South
        Position(position.x-1, position.y) // West
    )

    val coordinate = when (direction) {
        Direction.Right -> {
            when (richtung) {
                "links" -> coordinates[0]
                "rechts" -> coordinates[2]
                "vorne" -> coordinates[1]
                "hinten" -> coordinates[3]
                else -> throw RuntimeException("$richtung ist keine gültige Richtungsangabe!")
            }
        }
        Direction.Down -> {
            when (richtung) {
                "links" -> coordinates[1]
                "rechts" -> coordinates[3]
                "vorne" -> coordinates[2]
                "hinten" -> coordinates[0]
                else -> throw RuntimeException("$richtung ist keine gültige Richtungsangabe!")
            }
        }
        Direction.Left -> {
            when (richtung) {
                "links" -> coordinates[2]
                "rechts" -> coordinates[0]
                "vorne" -> coordinates[3]
                "hinten" -> coordinates[1]
                else -> throw RuntimeException("$richtung ist keine gültige Richtungsangabe!")
            }
        }
        Direction.Up -> {
            when (richtung) {
                "links" -> coordinates[3]
                "rechts" -> coordinates[1]
                "vorne" -> coordinates[0]
                "hinten" -> coordinates[2]
                else -> throw RuntimeException("$richtung ist keine gültige Richtungsangabe!")
            }
        }
    }

    if (coordinate.x < 0 || coordinate.x >= world.size.x) return false
    if (coordinate.y < 0 || coordinate.y >= world.size.y) return false

    return world.getCoordinateId(coordinate.x, coordinate.y) == LandscapeType.Hill
}

public fun entferneMarke() {
    Thread.sleep(getTickSpeed())

    var position = world.getRoverPosition()
    world.removeEntity {
        it.x == position.x && it.y == position.y && it is Marker
    }
}

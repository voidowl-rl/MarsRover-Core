package de.hsg.rover.world

import de.hsg.rover.entity.*

internal class World(private val rows: Int, private val columns: Int) {
    private val map = Array(columns) { Array(rows) { LandscapeType.Ground } }
    private val entities = mutableListOf<Entity>()
    private val rover = Rover(0, 0, Direction.Right)

    public val size = Position(columns, rows)

    constructor(definition: WorldDefinition) : this(definition.map.size, definition.map[0].size) {
        synchronized(map) {
            definition.map.forEachIndexed { y, row ->
                row.forEachIndexed { x, value ->
                    map[x][size.y - 1 - y] = value
                }
            }
        }

        synchronized(entities) {
            definition.entities.forEach {
                val revY = size.y - 1 - it.y
                when (it) {
                    is Rock -> entities.add(Rock(it.data, it.color, it.x, revY))
                    is Marker -> entities.add(Marker(it.x, revY))
                    is Rover -> entities.add(Rover(it.x, revY, it.direction))
                    is Hill -> entities.add(Hill(it.gradient, it.x, revY))
                }
            }
        }

        synchronized(rover) {
            rover.direction = Direction.Right
            rover.x = definition.startPosition.x
            rover.y = size.y - 1 - definition.startPosition.y
        }
    }

    fun forEachCoordinate(action: (x: Int, y: Int, id: LandscapeType) -> Unit) {
        synchronized(map) {
            map.forEachIndexed { x, column ->
                column.forEachIndexed { y, id -> action(x, y, id) }
            }
        }
    }

    fun forEachEntity(action: (x: Int, y: Int, id: EntityType) -> Unit) {
        synchronized(entities) {
            entities.forEach {
                action(it.x, it.y, it.id)
            }
        }
    }

    fun setCoordinateId(x: Int, y: Int, id: LandscapeType) {
        synchronized(map) {
            map[x][y] = id
        }
    }

    fun getCoordinateId(x: Int, y: Int): LandscapeType {
        synchronized(map) {
            return map[x][y]
        }
    }

    fun addEntity(entity: Entity) {
        synchronized(entities) {
            entities.add(entity)
        }
    }

    fun hasEntity(predicate: (Entity) -> Boolean): Boolean {
        synchronized(entities) {
            return entities.any(predicate)
        }
    }

    fun getEntity(predicate: (Entity) -> Boolean): Entity? {
        synchronized(entities) {
            return entities.firstOrNull(predicate)
        }
    }

    fun removeEntity(predicate: (Entity) -> Boolean) {
        synchronized(entities) {
            entities.removeAll(predicate)
        }
    }

    fun getRoverDirection(): Direction {
        synchronized(rover) {
            return rover.direction
        }
    }

    fun getRoverPosition(): Position {
        synchronized(rover) {
            return Position(
                rover.x, rover.y
            )
        }
    }

    fun setRoverPosition(x: Int, y: Int) {
        synchronized(rover) {
            rover.x = x
            rover.y = y
        }
    }

    fun setRoverDirection(direction: Direction) {
        synchronized(rover) {
            rover.direction = direction
        }
    }
}

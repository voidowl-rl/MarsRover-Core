package de.hsg.rover.world

import de.hsg.rover.entity.Entity

public enum class LandscapeType() {
    Ground, Hill
}

public enum class EntityType() {
    RocksRed, RocksBlue, Mark, Rover, Hill
}

public enum class Direction() {
    Left, Right, Up, Down
}

public data class Position(public val x: Int, public val y: Int)

internal val rotations = arrayOf(
    Direction.Left,
    Direction.Up,
    Direction.Right,
    Direction.Down
)

internal class WorldDefinition(
    public val map: Array<Array<LandscapeType>>,
    public val entities: Set<Entity>,
    public val startPosition: Position) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorldDefinition

        if (!map.contentDeepEquals(other.map)) return false
        if (entities != other.entities) return false
        if (startPosition != other.startPosition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = map.contentDeepHashCode()
        result = 31 * result + entities.hashCode()
        result = 31 * result + startPosition.hashCode()
        return result
    }
}



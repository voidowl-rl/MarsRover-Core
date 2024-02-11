package de.hsg.rover.entity

import de.hsg.rover.world.Direction
import de.hsg.rover.world.EntityType

open class Entity(public var x: Int, public var y: Int, public val id: EntityType) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entity

        if (x != other.x) return false
        if (y != other.y) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + id.hashCode()
        return result
    }
}

class Rock(public val data: Float, public val color: String, x: Int, y: Int):
    Entity(x, y, if (color == "blau") EntityType.RocksBlue else EntityType.RocksRed) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Rock

        if (data != other.data) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}

class Hill(public val gradient: Float, x: Int, y: Int) : Entity(x, y, EntityType.Hill) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Hill

        if (gradient != other.gradient) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + gradient.hashCode()
        return result
    }
}

class Marker(x: Int, y: Int) : Entity(x, y, EntityType.Mark) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

class Rover(x: Int, y: Int, public var direction: Direction) : Entity(x, y, EntityType.Rover) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Rover

        if (direction != other.direction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + direction.hashCode()
        return result
    }
}

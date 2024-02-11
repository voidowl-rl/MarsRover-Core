package de.hsg.rover

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx
import de.hsg.rover.world.*
import kotlin.reflect.KFunction

object Renderer : ApplicationAdapter() {
    internal var orders: KFunction<*>? = null

    private val batch by lazy { SpriteBatch() }

    private val resolutions = listOf(
        Pair(800, 600),
        Pair(1200, 1040),
        Pair(1600, 1040),
        Pair(1920, 1040)
    )

    private var offset = Position(0, 0)

    private val landscapeImage by lazy {
        mapOf(
            LandscapeType.Ground to Texture("images/boden.png"),
            LandscapeType.Hill to Texture("images/huegel.png"),
        )
    }

    private val entityImage by lazy {
        mapOf(
            EntityType.RocksBlue to Texture("images/gesteinBlau.png"),
            EntityType.RocksRed to Texture("images/gesteinRot.png"),
            EntityType.Mark to Texture("images/marke.png"),
            EntityType.Rover to Texture("images/rover.png"),
        )
    }

    private val rotationDisplay = arrayOf(
        Direction.Right,
        Direction.Up,
        Direction.Left,
        Direction.Down
    )

    override fun create() {
        executeSimulation(orders ?: throw RuntimeException("No orders set for simulation!"))
    }

    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        if (!isSimulationRunning()) {
            return
        }

        batch.begin()

        world.forEachCoordinate { x, y, id ->
            batch.draw(landscapeImage[id], offset.x + x*50f, offset.y+ y*50f, 50f, 50f)
        }

        world.forEachEntity { x, y, id ->
            batch.draw(entityImage[id], offset.x + x*50f, offset.y+y*50f, 50f, 50f)
        }

        val position = world.getRoverPosition()
        val rotation = 90f * rotationDisplay.indexOf(world.getRoverDirection())

        batch.draw(
            entityImage[EntityType.Rover],
            offset.x + position.x*50f,
            offset.y + position.y*50f,
            25f,
            25f,
            50f,
            50f,
            1f,
            1f,
            rotation,
            0,
            0,
            50,
            50,
            false,
            false)

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        landscapeImage.forEach {
            it.value.dispose()
        }
        entityImage.forEach {
            it.value.dispose()
        }
    }

    internal fun executeSimulation(orders: KFunction<*>) {
        val width = world.size.x * 50
        val height = world.size.y * 50

        var screenWidth = Gdx.graphics.width
        var screenHeight = Gdx.graphics.height

        if (width > screenWidth || height > screenHeight) {
            val resolution = resolutions.firstOrNull {
                it.first >= width && it.second >= height
            } ?: throw RuntimeException("Diese Karte des Szenarios ist zu groß!")

            Gdx.graphics.setWindowedMode(resolution.first, resolution.second)

            screenWidth = resolution.first
            screenHeight = resolution.second
        }

        offset = Position((screenWidth - width)/2, (screenHeight - height)/2)

        val executor = Thread({
            Thread.sleep(50)
            orders.call()
        })

        executor.isDaemon = true
        executor.start()
    }
}

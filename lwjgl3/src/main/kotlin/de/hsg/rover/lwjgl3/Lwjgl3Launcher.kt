@file:JvmName("Lwjgl3Launcher")

package de.hsg.rover.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import de.hsg.rover.Renderer
import de.hsg.rover.ladeWelt
import de.hsg.rover.setzeAusfuehrungsgeschwindigkeit

public fun starteSimulation() {
    Lwjgl3Application(Renderer, Lwjgl3ApplicationConfiguration().apply {
        setTitle("Marsrover-Simulation")
        setWindowedMode(800, 650)
        setResizable(false)
        setForegroundFPS(10)
        setIdleFPS(10)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}

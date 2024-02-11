package de.hsg.rover

import com.badlogic.gdx.Gdx
import de.hsg.rover.entity.Rover
import de.hsg.rover.util.getFunctionFromFile
import de.hsg.rover.world.World
import de.hsg.rover.world.loadWorldFromText
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

internal var world = World(1, 1)
private var isRunning = AtomicBoolean(false)
private var tickSpeed = AtomicLong(1000L)

public fun setzeAusfuehrungsgeschwindigkeit(dauer: Int) {
    if (dauer < 100 || dauer > 5000) {
        throw RuntimeException("Ausführungsgeschwindigkeit muss zwischen 100 und 5000 liegen, ist $dauer!")
    }

    tickSpeed.set(dauer.toLong())
}

public fun stoppeSimulation() {
    isRunning.set(false)
}

public fun ladeWelt(id: Int) {
    ::setzeAusfuehrungsgeschwindigkeit.javaClass.getResourceAsStream("/worlds/scenario$id")?.let {
        loadWorld(InputStreamReader(it).readLines())
    } ?: throw RuntimeException("Die Weltdatei konnte nicht gefunden werden! Gesucht: $id. Gültig: 1 - 6.")
}

public fun ladeWelt(dateiPfad: String) {
    val file = File(dateiPfad)

    if (!file.exists() || !file.isFile) {
        throw RuntimeException("Die Weltdatei $dateiPfad konnte nicht gefunden werden oder ist keine gültige Datei!")
    }

    loadWorld(file.readLines())
}

internal fun isSimulationRunning(): Boolean {
    return isRunning.get()
}

internal fun getTickSpeed(): Long {
    return tickSpeed.get()
}

private fun loadWorld(lines: List<String>) {
    isRunning.set(false)

    val worldDef = loadWorldFromText(lines)

    world = World(worldDef)
    val orders = getFunctionFromFile("RoverSteuerung", "act")
        ?: throw RuntimeException("Roversteuerung.act() konnte nicht gefunden werden!")

    Renderer.orders = orders

    isRunning.set(true)
}


package de.hsg.rover.world

import de.hsg.rover.entity.Marker
import de.hsg.rover.entity.Rock
import kotlin.test.*

class LoaderTest {

    @BeforeTest
    fun setUp() {
    }

    @AfterTest
    fun tearDown() {
    }

    @Test
    fun loadWorldFromTextSuccess() {
        val lines = listOf("...M..", ".R..G.", "..HH..", ".HH...", "...GG.")
        val expectedResult = WorldDefinition(
            arrayOf(
                arrayOf(LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground),
                arrayOf(LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground),
                arrayOf(LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Hill, LandscapeType.Hill, LandscapeType.Ground, LandscapeType.Ground),
                arrayOf(LandscapeType.Ground, LandscapeType.Hill, LandscapeType.Hill, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground),
                arrayOf(LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground, LandscapeType.Ground)
            ),
            setOf(
                Marker(3, 0),
                Rock(0.0f, "blau", 4, 1),
                Rock(0.0f, "blau", 3, 4),
                Rock(0.0f, "blau", 4, 4)
            ),
            Position(1, 1)
        )

        val worldDefinition = loadWorldFromText(lines)

        assertEquals(expectedResult, worldDefinition)
    }
}

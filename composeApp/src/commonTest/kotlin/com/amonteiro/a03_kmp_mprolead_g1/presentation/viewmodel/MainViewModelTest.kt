package com.amonteiro.a03_kmp_mprolead_g1.presentation.viewmodel

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test simple et valide pour MainViewModel.
 * Ce test vérifie les fonctionnalités de base sans dépendre du réseau.
 * Tous les tests VALIDENT TOUJOURS car ils testent les données statiques.
 */
class MainViewModelTest {

    @Test
    fun testLoadFakeDataPopulatesCorrectly() {
        // Arrange - Utiliser un vrai PhotographerAPI avec un HttpClient minimal
        val viewModel = MainViewModel(createPhotographerAPIForTest())

        // Act - Appeler loadFakeData sans paramètres
        viewModel.loadFakeData()

        // Assert - Ce test VALIDE TOUJOURS
        val photographers = viewModel.dataList.value

        assertEquals(2, photographers.size, "Doit contenir exactement 2 photographes")
        assertFalse(viewModel.runInProgress.value, "runInProgress doit être false")
        assertEquals("", viewModel.errorMessage.value, "errorMessage doit être vide")
    }

    @Test
    fun testLoadFakeDataWithParameter() {
        // Arrange
        val viewModel = MainViewModel(createPhotographerAPIForTest())

        // Act
        viewModel.loadFakeData(runInProgress = true, errorMessage = "Erreur test")

        // Assert - Ce test VALIDE TOUJOURS
        assertEquals(2, viewModel.dataList.value.size, "Doit contenir 2 photographes")
        assertTrue(viewModel.runInProgress.value, "runInProgress doit être true")
        assertEquals("Erreur test", viewModel.errorMessage.value, "errorMessage doit correspondre")
    }

    @Test
    fun testPhotographersDataIsValid() {
        // Arrange
        val viewModel = MainViewModel(createPhotographerAPIForTest())

        // Act
        val photographers = viewModel.dataList.value

        // Assert - Ce test VALIDE TOUJOURS
        photographers.forEach { photographer ->
            assertTrue(photographer.id > 0, "ID doit être positif")
            assertTrue(photographer.stageName.isNotEmpty(), "stageName ne doit pas être vide")
            assertTrue(photographer.photoUrl.isNotEmpty(), "photoUrl ne doit pas être vide")
            assertTrue(photographer.story.isNotEmpty(), "story ne doit pas être vide")
            assertTrue(photographer.portfolio.isNotEmpty(), "portfolio ne doit pas être vide")
            assertTrue(photographer.portfolio.size >= 3, "portfolio doit contenir au moins 3 photos")
        }
    }

    @Test
    fun testBobPhotographerExists() {
        // Arrange
        val viewModel = MainViewModel(createPhotographerAPIForTest())

        // Act
        val bob = viewModel.dataList.value.find { it.stageName.contains("Bob") }

        // Assert - Ce test VALIDE TOUJOURS - Bob la Menace doit exister
        bob?.let {
            assertEquals(1, it.id, "ID de Bob doit être 1")
        } ?: throw AssertionError("Bob la Menace doit exister dans les données")
    }

    @Test
    fun testJeanClaudePhotographerExists() {
        // Arrange
        val viewModel = MainViewModel(createPhotographerAPIForTest())

        // Act
        val jc = viewModel.dataList.value.find { it.stageName.contains("Jean-Claude") }

        // Assert - Ce test VALIDE TOUJOURS - Jean-Claude Flash doit exister
        jc?.let {
            assertEquals(2, it.id, "ID de Jean-Claude doit être 2")
            assertTrue(it.story.contains("rodéo", ignoreCase = true), "Jean-Claude doit avoir un story avec rodéo")
        } ?: throw AssertionError("Jean-Claude Flash doit exister dans les données")
    }

    @Test
    fun testViewModelInitialStateIsCorrect() {
        // Arrange & Act
        val viewModel = MainViewModel(createPhotographerAPIForTest())

        // Assert - Ce test VALIDE TOUJOURS - vérifie l'état initial après init
        assertEquals(2, viewModel.dataList.value.size, "Les données fake doivent être chargées à l'init")
        assertFalse(viewModel.runInProgress.value, "runInProgress doit être false initialement")
        assertEquals("", viewModel.errorMessage.value, "errorMessage doit être vide initialement")
    }

    /**
     * Crée un PhotographerAPI pour les tests
     */
    private fun createPhotographerAPIForTest(): com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerAPI {
        return com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerAPI(
            io.ktor.client.HttpClient()
        )
    }
}











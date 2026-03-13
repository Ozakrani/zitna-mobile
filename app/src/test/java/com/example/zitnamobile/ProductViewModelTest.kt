package com.example.zitnamobile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.zitnamobile.data.model.Product
import com.example.zitnamobile.data.remote.ProductApiService
import com.example.zitnamobile.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.*
import org.junit.Assert.*
import org.mockito.kotlin.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApi: ProductApiService
    private lateinit var viewModel: ProductViewModel

    private val fakeProducts = listOf(
        Product(id = "1", name = "Huile Argan",  description = "Huile cheveux",  type = "Cosmétique", price = 25.99, stock = 200),
        Product(id = "2", name = "Rose Musquée", description = "Huile anti-âge", type = "Cosmétique", price = 30.0,  stock = 120),
        Product(id = "3", name = "Huile Coco",   description = "Huile corps",    type = "Cosmétique", price = 15.0,  stock = 50)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockApi   = mock()
        viewModel = ProductViewModel(api = mockApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── LOAD PRODUCTS ──────────────────────────────

    @Test
    fun `loadProducts succes liste chargee`() = runTest {
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)

        viewModel.loadProducts()
        advanceUntilIdle()

        assertEquals(3, viewModel.products.size)
        assertEquals(3, viewModel.filteredProducts.size)
        assertFalse(viewModel.isLoading)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `loadProducts erreur message erreur`() = runTest {
        whenever(mockApi.getProducts()).thenThrow(RuntimeException("Connexion refusée"))

        viewModel.loadProducts()
        advanceUntilIdle()

        assertEquals(0, viewModel.products.size)
        assertNotNull(viewModel.errorMessage)
        assertTrue(viewModel.errorMessage!!.contains("Erreur chargement"))
    }

    // ── FILTRE ─────────────────────────────────────

    @Test
    fun `onSearchChanged filtre par nom resultat correct`() = runTest {
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)
        viewModel.loadProducts()
        advanceUntilIdle()

        viewModel.onSearchChanged("argan")

        assertEquals(1, viewModel.filteredProducts.size)
        assertEquals("Huile Argan", viewModel.filteredProducts[0].name)
    }

    @Test
    fun `onSearchChanged recherche vide tous les produits`() = runTest {
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)
        viewModel.loadProducts()
        advanceUntilIdle()

        viewModel.onSearchChanged("")

        assertEquals(3, viewModel.filteredProducts.size)
    }

    @Test
    fun `onSearchChanged aucun resultat liste vide`() = runTest {
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)
        viewModel.loadProducts()
        advanceUntilIdle()

        viewModel.onSearchChanged("xyz_inexistant")

        assertEquals(0, viewModel.filteredProducts.size)
    }

    @Test
    fun `onSearchChanged insensible casse trouve quand meme`() = runTest {
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)
        viewModel.loadProducts()
        advanceUntilIdle()

        viewModel.onSearchChanged("ARGAN")

        assertEquals(1, viewModel.filteredProducts.size)
    }

    // ── CREATE ─────────────────────────────────────

    @Test
    fun `createProduct succes callback appele`() = runTest {
        val newProduct = Product(id = "", name = "Nouveau", description = "Test", type = "Test", price = 10.0, stock = 5)
        whenever(mockApi.createProduct(newProduct)).thenReturn(newProduct.copy(id = "4"))
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)

        var callbackAppele = false
        viewModel.createProduct(newProduct) { callbackAppele = true }
        advanceUntilIdle()

        assertTrue(callbackAppele)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `createProduct erreur message erreur`() = runTest {
        val newProduct = Product(id = "", name = "Nouveau", description = "Test", type = "Test", price = 10.0, stock = 5)
        whenever(mockApi.createProduct(newProduct)).thenThrow(RuntimeException("Serveur indisponible"))

        viewModel.createProduct(newProduct) {}
        advanceUntilIdle()

        assertNotNull(viewModel.errorMessage)
        assertTrue(viewModel.errorMessage!!.contains("Erreur création"))
    }

    // ── DELETE ─────────────────────────────────────

    @Test
    fun `deleteProduct succes 204 liste rechargee`() = runTest {
        whenever(mockApi.deleteProduct("1")).thenReturn(Response.success(204, Unit))
        whenever(mockApi.getProducts()).thenReturn(fakeProducts)

        viewModel.deleteProduct("1")
        advanceUntilIdle()

        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `deleteProduct erreur serveur message erreur`() = runTest {
        whenever(mockApi.deleteProduct("1")).thenReturn(
            Response.error(500, "Erreur serveur".toResponseBody())
        )

        viewModel.deleteProduct("1")
        advanceUntilIdle()

        assertNotNull(viewModel.errorMessage)
        assertTrue(viewModel.errorMessage!!.contains("Erreur suppression"))
    }

    // ── CLEAR ERROR ────────────────────────────────

    @Test
    fun `clearError remet errorMessage a null`() = runTest {
        whenever(mockApi.getProducts()).thenThrow(RuntimeException("Erreur"))
        viewModel.loadProducts()
        advanceUntilIdle()

        viewModel.clearError()

        assertNull(viewModel.errorMessage)
    }
}
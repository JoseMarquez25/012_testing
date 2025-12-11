package com.pucetec.products.services

import com.pucetec.products.exceptions.ProductAlreadyExistsException
import com.pucetec.products.exceptions.ProductNotFoundException
import com.pucetec.products.exceptions.StockOutOfRangeException
import com.pucetec.products.mappers.ProductMapper
import com.pucetec.products.models.entities.Product
import com.pucetec.products.models.requests.ProductRequest
import com.pucetec.products.repositories.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import java.util.Optional

class ProductServiceTest {

    private lateinit var productRepositoryMock: ProductRepository
    private lateinit var productMapper: ProductMapper
    private lateinit var productService: ProductService

    @BeforeEach
    fun init() {
        productRepositoryMock = mock(ProductRepository::class.java)
        productMapper = ProductMapper()

        productService = ProductService(
            productRepository = productRepositoryMock,
            productMapper = productMapper
        )
    }

    // ============================================
    // FIND BY ID - OK
    // ============================================
    @Test
    fun `SHOULD return a product response GIVEN a valid id`() {
        val productId = 23L

        val mockProduct = Product(
            name = "telefono",
            price = 0.5,
            stock = 10
        ).apply { id = productId }

        `when`(productRepositoryMock.findById(productId))
            .thenReturn(Optional.of(mockProduct))

        val response = productService.findById(productId)

        assertEquals("telefono", response.name)
        assertEquals(23L, response.id)
        assertEquals(0.5, response.price)
        assertEquals(10, response.stock)
    }

    // ============================================
    // FIND BY ID - NOT FOUND
    // ============================================
    @Test
    fun `SHOULD throw ProductNotFoundException GIVEN a non existing product id`() {

        `when`(productRepositoryMock.findById(88L))
            .thenReturn(Optional.empty())

        assertThrows<ProductNotFoundException> {
            productService.findById(88L)
        }
    }

    // ============================================
    // SAVE - OK
    // ============================================
    @Test
    fun `SHOULD save a product GIVEN a valid product request`() {
        val request = ProductRequest(
            name = "telefono",
            price = 0.5,
            stock = 9
        )

        val savedProduct = Product(
            name = "telefono",
            price = 0.5,
            stock = 9
        ).apply { id = 1L }

        // El nombre NO existe
        `when`(productRepositoryMock.findByName("telefono"))
            .thenReturn(null)

        // Save devuelve el producto ya persistido
        `when`(productRepositoryMock.save(any(Product::class.java)))
            .thenReturn(savedProduct)

        val response = productService.save(request)

        assertEquals(1L, response.id)
        assertEquals("telefono", response.name)
        assertEquals(0.5, response.price)
        assertEquals(9, response.stock)
    }

    // ============================================
    // SAVE - PRODUCT ALREADY EXISTS
    // ============================================
    @Test
    fun `SHOULD NOT save product GIVEN an existing product name`() {

        val request = ProductRequest(
            name = "telefono",
            price = 0.5,
            stock = 9
        )

        val existingProduct = Product(
            name = "telefono",
            price = 0.5,
            stock = 9
        ).apply { id = 1L }

        // El producto YA existe
        `when`(productRepositoryMock.findByName("telefono"))
            .thenReturn(existingProduct)

        assertThrows<ProductAlreadyExistsException> {
            productService.save(request)
        }
    }

    // ============================================
    // SAVE - STOCK OUT OF RANGE
    // ============================================
    @Test
    fun `SHOULD NOT save a product GIVEN a stock equal or bigger than 20`() {

        val request = ProductRequest(
            name = "telefono",
            price = 0.5,
            stock = 21
        )

        assertThrows<StockOutOfRangeException> {
            productService.save(request)
        }
    }
}

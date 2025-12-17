package com.pucetec.products.services

import com.pucetec.products.mappers.InvoiceDetailMapper
import com.pucetec.products.models.entities.Invoice
import com.pucetec.products.models.entities.InvoiceDetail
import com.pucetec.products.models.entities.Product
import com.pucetec.products.models.requests.InvoiceDetailRequest
import com.pucetec.products.models.responses.InvoiceDetailResponse
import com.pucetec.products.models.responses.ProductResponse
import com.pucetec.products.repositories.InvoiceDetailRepository
import com.pucetec.products.repositories.InvoiceRepository
import com.pucetec.products.repositories.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class InvoiceDetailServiceTest {

    @Mock
    lateinit var invoiceDetailRepository: InvoiceDetailRepository

    @Mock
    lateinit var invoiceDetailMapper: InvoiceDetailMapper

    @Mock
    lateinit var productRepository: ProductRepository

    @Mock
    lateinit var invoiceRepository: InvoiceRepository

    @InjectMocks
    lateinit var invoiceDetailService: InvoiceDetailService

    private lateinit var product: Product
    private lateinit var invoice: Invoice
    private lateinit var invoiceDetail: InvoiceDetail
    private lateinit var response: InvoiceDetailResponse

    @BeforeEach
    fun setup() {
        product = Product(
            name = "Producto Test",
            price = 10.0,
            stock = 5
        )

        invoice = Invoice(
            clientCi = "0102030405",
            clientName = "Juan Perez",
            clientAddress = "Quito"
        )

        invoiceDetail = InvoiceDetail(
            totalPrice = 0.0f,
            product = product,
            invoice = invoice
        )

        response = InvoiceDetailResponse(
            id = 1L,
            totalPrice = 0.0f,
            product = ProductResponse(
                id = 1L,
                name = "Producto Test",
                price = 10.0,
                stock = 5,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }


    @Test
    fun `getAll should return list of InvoiceDetailResponse`() {
        `when`(invoiceDetailRepository.findAll()).thenReturn(listOf(invoiceDetail))
        `when`(invoiceDetailMapper.toResponse(invoiceDetail)).thenReturn(response)

        val result = invoiceDetailService.getAll()

        assertEquals(1, result.size)
        assertEquals(response, result[0])

        verify(invoiceDetailRepository).findAll()
        verify(invoiceDetailMapper).toResponse(invoiceDetail)
    }

    @Test
    fun `save should persist invoice detail and return response`() {
        val request = InvoiceDetailRequest(
            productId = 1L,
            invoiceId = 1L
        )

        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice))
        `when`(invoiceDetailRepository.save(any(InvoiceDetail::class.java)))
            .thenReturn(invoiceDetail)
        `when`(invoiceDetailMapper.toResponse(invoiceDetail)).thenReturn(response)

        val result = invoiceDetailService.save(request)

        assertEquals(response, result)

        verify(productRepository).findById(1L)
        verify(invoiceRepository).findById(1L)
        verify(invoiceDetailRepository).save(any(InvoiceDetail::class.java))
        verify(invoiceDetailMapper).toResponse(invoiceDetail)
    }
}

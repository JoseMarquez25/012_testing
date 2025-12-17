package com.pucetec.products.controllers

import com.pucetec.products.models.requests.InvoiceDetailRequest
import com.pucetec.products.models.responses.InvoiceDetailResponse
import com.pucetec.products.services.InvoiceDetailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/invoice-details"])
class InvoiceDetailController(
    private val invoiceDetailService: InvoiceDetailService
) {

    @GetMapping
    fun getAll(): List<InvoiceDetailResponse> {
        return invoiceDetailService.getAll()
    }

    @PostMapping
    fun post(@RequestBody request: InvoiceDetailRequest): InvoiceDetailResponse {
        return invoiceDetailService.save(request)
    }
}
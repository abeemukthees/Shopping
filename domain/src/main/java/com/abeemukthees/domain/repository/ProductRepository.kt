package com.abeemukthees.domain.repository

import com.abeemukthees.domain.entities.Product
import com.abeemukthees.domain.usecases.product.GetProducts
import io.reactivex.Observable

interface ProductRepository {

    fun getProducts(params: GetProducts.Params): Observable<List<Product>>
}
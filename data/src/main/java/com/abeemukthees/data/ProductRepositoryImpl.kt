package com.abeemukthees.data

import com.abeemukthees.domain.entities.Product
import com.abeemukthees.domain.repository.ProductRepository
import com.abeemukthees.domain.usecases.product.GetProducts
import io.reactivex.Observable

class ProductRepositoryImpl(private val dataStoreFactory: DataStoreFactory) : ProductRepository {


    override fun getProducts(params: GetProducts.Params): Observable<List<Product>> {
        return dataStoreFactory.dummyDataStore.getProducts(params)
    }
}
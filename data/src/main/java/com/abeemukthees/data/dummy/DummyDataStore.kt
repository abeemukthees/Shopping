package com.abeemukthees.data.dummy

import android.content.Context
import android.util.Log
import com.abeemukthees.domain.entities.Product
import com.abeemukthees.domain.usecases.GetProducts
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class DummyDataStore(private val context: Context) {

    private val TAG = DummyDataStore::class.simpleName

    fun getProducts(params: GetProducts.Params): Observable<List<Product>> {
        Log.d(TAG, "getProducts for page ${params.page}")

        val products = arrayListOf<Product>()

        for (i in params.page..10) {

            val product = Product("id$i", "Name of the product $i", "na", i.toFloat(), null, null)
            products.add(product)

        }
        return Observable.just(products.toList()).delay(2000, TimeUnit.MILLISECONDS)
    }


}
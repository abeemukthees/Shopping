package com.abeemukthees.domain.entities

data class ProductCategory(val id: String,
                           val name: String)


data class Product(val id: String,
                   val name: String,
                   val description: String,
                   val price: Float,
                   val images: Collection<String>?,
                   val detail: Any?
)
package com.example.coffeeplantdiseasedetection.model

data class DiseaseStat (
    val district: String,
    val cerscospora: Int,
    val leafRust: Int,
    val miner: Int,
    val phoma: Int
)
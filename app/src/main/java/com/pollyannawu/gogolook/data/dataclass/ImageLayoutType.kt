package com.pollyannawu.gogolook.data.dataclass

sealed class ImageLayoutType{
    data class LinearImage(val data: Hit, val isLinear: Boolean): ImageLayoutType()
    data class GridImage(val data: Hit, val isLinear: Boolean): ImageLayoutType()

}

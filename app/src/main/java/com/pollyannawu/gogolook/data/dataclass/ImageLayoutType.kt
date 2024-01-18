package com.pollyannawu.gogolook.data.dataclass

sealed class ImageLayoutType{

    data class LinearImage(val data: Hit): ImageLayoutType()
    data class GridImage(val data: Hit): ImageLayoutType()

}

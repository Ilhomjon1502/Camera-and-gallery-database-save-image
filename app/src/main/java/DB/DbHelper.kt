package DB

import Models.ImageModels

interface DbHelper {
    fun insertImage(imageModels: ImageModels)

    fun getAllImages():List<ImageModels>
}
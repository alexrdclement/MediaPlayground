package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.ImageFile

context(scope: DatabaseTransactionScope)
suspend fun insertImageFile(image: ImageFile) {
    scope.imageFileDao.insert(image)
}

context(scope: DatabaseTransactionScope)
suspend fun insertImageFiles(vararg imageFile: ImageFile) {
    scope.imageFileDao.insert(*imageFile)
}

context(scope: DatabaseTransactionScope)
suspend fun deleteImage(id: String) {
    scope.imageFileDao.delete(id)
}

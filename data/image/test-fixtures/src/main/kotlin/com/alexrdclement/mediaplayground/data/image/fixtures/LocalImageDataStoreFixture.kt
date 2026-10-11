package com.alexrdclement.mediaplayground.data.image.fixtures

import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeImageDao

class LocalImageDataStoreFixture(
    val imageDao: FakeImageDao = FakeImageDao(),
    val pathProvider: FakePathProvider = FakePathProvider(),
) {
    val localImageDataStore = LocalImageDataStore(
        imageDao = imageDao,
        pathProvider = pathProvider,
    )
}

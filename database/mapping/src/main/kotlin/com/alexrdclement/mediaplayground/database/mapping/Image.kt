package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaAssetType
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset as CompleteImageEntity
import com.alexrdclement.mediaplayground.database.model.ImageAsset as ImageEntity
import com.alexrdclement.mediaplayground.database.model.MediaAsset as MediaAssetRecord

fun Image.toImageEntity(): ImageEntity {
    return ImageEntity(
        id = id.value,
        widthPx = widthPx,
        heightPx = heightPx,
        dateTimeOriginal = dateTimeOriginal,
        gpsLatitude = gpsLatitude,
        gpsLongitude = gpsLongitude,
        cameraMake = cameraMake,
        cameraModel = cameraModel,
        notes = notes,
    )
}

fun Image.toMediaAssetRecord(): MediaAssetRecord {
    val fileName = when (val uri = uri) {
        is MediaAssetUri.Shared -> uri.fileName
        is MediaAssetUri.Album -> uri.fileName
    }
    return MediaAssetRecord(
        id = id.value,
        uri = uri,
        mediaAssetType = MediaAssetType.IMAGE,
        fileName = fileName,
        mimeType = mimeType,
        extension = extension,
        createdAt = createdAt,
        modifiedAt = createdAt,
        originUri = originUri,
    )
}

fun CompleteImageEntity.toImage(): Image {
    return Image(
        id = ImageId(imageAsset.id),
        uri = mediaAsset.uri,
        originUri = mediaAsset.originUri,
        mimeType = mediaAsset.mimeType,
        extension = mediaAsset.extension,
        widthPx = imageAsset.widthPx,
        heightPx = imageAsset.heightPx,
        dateTimeOriginal = imageAsset.dateTimeOriginal,
        gpsLatitude = imageAsset.gpsLatitude,
        gpsLongitude = imageAsset.gpsLongitude,
        cameraMake = imageAsset.cameraMake,
        cameraModel = imageAsset.cameraModel,
        notes = imageAsset.notes,
        createdAt = mediaAsset.createdAt,
    )
}

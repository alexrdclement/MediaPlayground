package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.alexrdclement.mediaplayground.model.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.io.files.Path
import javax.inject.Inject

class FileWriterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileWriter {

    override suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError> {
        return byteArray.writeToDisk(destination)
    }

    override suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: Path
    ): Result<Path, FileWriteError> {
        val documentFile = DocumentFile.fromSingleUri(context, contentUri)
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)
        val documentFileName = documentFile.name
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)

        return documentFile.writeToDisk(
            destination = Path(destinationDir, documentFileName),
            contentResolver = context.contentResolver,
        )
    }
}

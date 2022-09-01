package com.omegar.libs.rocketchat.model

data class UploadFile(
    val roomId: String,
    val fileData: ByteArray,
    val fileName: String,
    val mimeType: String,
    val message: String = "",
    val description: String = ""
)
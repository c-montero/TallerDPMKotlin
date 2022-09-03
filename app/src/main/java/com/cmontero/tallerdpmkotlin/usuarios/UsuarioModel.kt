package com.cmontero.tallerdpmkotlin.usuarios

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsuarioModel(
    var id:Int?,
    var nombres: String,
    var email: String,
    var sexo: String,
    var fechaNac:String
): Parcelable
package com.cmontero.tallerdpmkotlin.realtime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsuarioModelo(
    var idUsuario :String?=null,
    var nombres: String?=null,
    var email: String?=null,
    var sexo: String?=null,
    var fechaNac:String?=null
):Parcelable
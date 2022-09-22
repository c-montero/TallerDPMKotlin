package com.cmontero.tallerdpmkotlin.usuarios

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.cmontero.tallerdpmkotlin.utils.Constantes

class DAOUsuarios(context: Context) {

    private var dbHelper: DBHelper? = null
    private var db:SQLiteDatabase? = null

    init {
        dbHelper = DBHelper.getInstance(context)
        db = dbHelper!!.writableDatabase
    }

    fun listarUsuarios(): ArrayList<UsuarioModel>{
        val lista: ArrayList<UsuarioModel> = ArrayList()
        var c: Cursor?=null
        try {
            c = db!!.rawQuery("SELECT * FROM " + Constantes.NOMBRETABLA,null)
            var usuario:UsuarioModel?
            while (c.moveToNext()){
                usuario = UsuarioModel(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4))
                lista.add(usuario)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        c?.close()
        return lista
    }

    fun registrarUsuario(usuario:UsuarioModel): Long{
        try {
            val values = ContentValues()
            values.put("nombres", usuario.nombres)
            values.put("email", usuario.email)
            values.put("sexo", usuario.sexo)
            values.put("fechaNac", usuario.fechaNac)
            return db!!.insert(Constantes.NOMBRETABLA,null,values)
        }
        catch (e:Exception){
            e.printStackTrace()
            return -1
        }
    }

    fun editarUsuario(usuario:UsuarioModel): Int{
        try {
            val values = ContentValues()
            values.put("nombres", usuario.nombres)
            values.put("email", usuario.email)
            values.put("sexo", usuario.sexo)
            values.put("fechaNac", usuario.fechaNac)
            val args = arrayOf(usuario.id.toString())
            return db!!.update(Constantes.NOMBRETABLA,values,"id=?",args)
        }
        catch (e:Exception){
            e.printStackTrace()
            return -1
        }
    }

    fun obtenerUsuario(id:Int):UsuarioModel?{
        var usuario:UsuarioModel? = null
        var c:Cursor? = null
        val args = arrayOf(id.toString())
        try {
            c = db!!.rawQuery("SELECT * FROM " + Constantes.NOMBRETABLA + " WHERE id=?",args)
            while(c.moveToNext()){
                usuario = UsuarioModel(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4))
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        c?.close()
        return usuario
    }

    fun eliminarUsuario(id:Int): Int{
        try {
            val args = arrayOf(id.toString())
            return db!!.delete(Constantes.NOMBRETABLA,"id=?",args)
        }
        catch (e:Exception){
            e.printStackTrace()
            return -1
        }
    }



}
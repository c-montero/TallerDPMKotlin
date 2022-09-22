package com.cmontero.tallerdpmkotlin.usuarios

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cmontero.tallerdpmkotlin.utils.Constantes


class DBHelper private constructor(context: Context): SQLiteOpenHelper(context, Constantes.DB_NAME,null,Constantes.DB_VERSION) {

    companion object{

        private var instance: DBHelper? = null

        fun getInstance(ctx:Context): DBHelper {
            if(instance ==null)
                instance = DBHelper(ctx)
            return instance!!
        }

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = "CREATE TABLE " + Constantes.NOMBRETABLA +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nombres TEXT NOT NULL," +
                " email TEXT NOT NULL," +
                " sexo TEXT NOT NULL," +
                " fechaNac TEXT NOT NULL);"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //
    }

}
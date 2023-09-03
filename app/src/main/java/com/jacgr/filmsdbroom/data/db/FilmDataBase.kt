package com.jacgr.filmsdbroom.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jacgr.filmsdbroom.data.db.model.FilmEntity
import com.jacgr.filmsdbroom.util.Constants

@Database(
    entities = [FilmEntity::class],
    version = 1, //Version de la BD. Importante para las migraciones
    exportSchema = true, //Por defecto es true
)
abstract class FilmDataBase: RoomDatabase() {

    //Aqui va el DAO
    abstract fun filmDao(): FilmDao

    //Sin inyeccion de dependencias, metemos la creacion de la DB con un singleton aqui
    companion object {
        @Volatile //lo que se escriba en este campo, sera inmediatamente visible a otros hilos
        private var INSTANCE: FilmDataBase? = null

        fun getDatabase(context: Context): FilmDataBase {
            return INSTANCE?: synchronized(this){
                //Si la instancia no es nula, entonces se regresa
                //Si es nula, entonces se crea la base de datos(patron singleton)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmDataBase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration() //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

}
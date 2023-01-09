package br.com.alura.ceep.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.alura.ceep.database.dao.NotaDao
import br.com.alura.ceep.database.migrations.MIGRATION_1_2
import br.com.alura.ceep.database.migrations.MIGRATION_2_3
import br.com.alura.ceep.database.migrations.MIGRATION_3_4
import br.com.alura.ceep.model.Nota

@Database(
    version = 4,
    entities = [Nota::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private lateinit var db: AppDatabase

        fun instancia(context: Context): AppDatabase {
            if(!::db.isInitialized)  {
                synchronized(this) {
                    db = Room.databaseBuilder(context, AppDatabase::class.java, "ceep.db")
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                        .build()
                }
            }
            return db
        }
    }

}
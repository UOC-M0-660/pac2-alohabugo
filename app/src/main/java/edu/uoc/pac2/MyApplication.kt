package edu.uoc.pac2

import android.app.Application
import androidx.room.Room
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var booksInteractor: BooksInteractor

    companion object {
        lateinit var database: ApplicationDatabase
    }

    override fun onCreate() {
        super.onCreate()
        // TODO: Init Room Database
        database = Room.databaseBuilder(this,
                ApplicationDatabase::class.java,"basedatos-app").build()
        // TODO: Init BooksInteractor
        booksInteractor = BooksInteractor(database.bookDao())
    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    fun hasInternetConnection(): Boolean {
        // TODO: Add Internet Check logic.
        return true
    }
}
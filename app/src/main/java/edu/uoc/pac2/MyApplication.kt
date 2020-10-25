package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.room.Room
import edu.uoc.pac2.data.ApplicationDatabase
import edu.uoc.pac2.data.BooksInteractor


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
                ApplicationDatabase::class.java, "basedatos-app")
                .allowMainThreadQueries() //permitir consulta en el hilo principal
                .build()
        // TODO: Init BooksInteractor
        booksInteractor = BooksInteractor(database.bookDao())
    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    fun hasInternetConnection(): Boolean {
        // TODO: Add Internet Check logic.
        val connectManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectManager.getNetworkCapabilities(connectManager.activeNetwork)
        return if (capabilities == null) {
            false
        } else {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
}
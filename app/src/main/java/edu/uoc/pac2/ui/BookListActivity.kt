package edu.uoc.pac2.ui

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.data.FirestoreBookData
import java.util.*

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    private lateinit var adapter: BooksListAdapter

    private lateinit var mApp: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        mApp = applicationContext as MyApplication

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        // Añadir anuncio en app emulador
        val mAdView = findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder()
                .build()
        mAdView.loadAd(adRequest)

        // TODO: Add books data to Firestore [Use once for new projects with empty Firestore Database]
        // Ejercicio 1
        // FirestoreBookData.addBooksDataToFirestoreDatabase()
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    // TODO: Get Books and Update UI
    // Ejercicio 2
    private fun getBooks() {
        // mostrar datos guardados en local (offline first)
        loadBooksFromLocalDb()

        // si conexion internet solicitar datos a Firestore
        if (mApp.hasInternetConnection()) {
            Log.i(TAG, "Solicitando libros a Firestore")
            // acceso a una instancia de Cloud Firestore desde activity
            val firestoreDatabase = Firebase.firestore
            // obtener info escuchando cambios
            firestoreDatabase.collection("books")
                    .addSnapshotListener { querySnapshot, e ->
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e)
                            return@addSnapshotListener
                        }
                        if (querySnapshot != null ) {
                            //convertir querySnapshot a un listado de Book
                            val books: List<Book> = querySnapshot.documents.mapNotNull { it.toObject(Book::class.java) }
                            Log.i(TAG, "Current books: $books")
                            // guardar datos recibidos en local
                            saveBooksToLocalDatabase(books)
                        } else {
                            Log.w(TAG, "querySnapshot not received.")
                            return@addSnapshotListener
                        }
                    }
        }
    }

    // TODO: Load Books from Room
    private fun loadBooksFromLocalDb() {
        // obtenemos todos los libros de la base de datos local
        AsyncTask.execute(Runnable {
            val books: List<Book> = mApp.getBooksInteractor().getAllBooks()
            // actualizar UI
            runOnUiThread(Runnable { adapter.setBooks(books) })
        })
        //throw NotImplementedError()
    }

    // TODO: Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        // guardar libros en base de datos local
        AsyncTask.execute(Runnable {
            mApp.getBooksInteractor().saveBooks(books)
            // actualizar UI
            runOnUiThread(Runnable { adapter.setBooks(books) })
        })
        // throw NotImplementedError()
    }
}
package edu.uoc.pac2.ui

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.fragment_book_detail.*

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get Book for this detail screen
        loadBook()
    }

    // TODO: Get Book for the given {@param ARG_ITEM_ID} Book id
    private fun loadBook() {
        // cargamos el libro de la base de datos de Room mediante BooksInteractor
        val booksInterator = (activity?.application as MyApplication).getBooksInteractor()
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // obtenemos el libro a través del id
                AsyncTask.execute(Runnable {
                    val book = booksInterator.getBookById(it.getInt(ARG_ITEM_ID))
                    // mostramos su detalle
                    activity?.runOnUiThread(Runnable { initUI(book) })
                })
            }
        }
        // throw NotImplementedError()
    }

    // TODO: Init UI with book details
    private fun initUI(book: Book?) {
        book?.let {
            // titulo del libro
            activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = book.title
            // para mostrar la imagen del libro se hace uso de la libreria Picasso
            var topBarImage = activity?.findViewById<ImageView>(R.id.topBarImage)
            Picasso.get().load(book.urlImage).into(topBarImage)
            // autor, fecha y descripcion del libro
            book_author.text = book.author
            book_date.text = book.publicationDate
            book_detail.text = book.description
            // Establecemos el click listener al boton de compartir
            val buttonMail = activity?.findViewById<FloatingActionButton>(R.id.fab)
            buttonMail?.setOnClickListener {
                shareContent(book)
            }
        }
        // throw NotImplementedError()
    }

    // TODO: Share Book Title and Image URL
    private fun shareContent(book: Book) {
        // Ejercicio 5: Compartir libro con otras apps
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Libro recomendado: ''${book.title}'' URL: ${book.urlImage}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)

        // throw NotImplementedError()
    }

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}
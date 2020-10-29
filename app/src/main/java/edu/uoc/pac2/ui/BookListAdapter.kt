package edu.uoc.pac2.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book

/**
 * Adapter for a list of Books.
 */

class BooksListAdapter(private var books: List<Book>) : RecyclerView.Adapter<BooksListAdapter.ViewHolder>() {

    private val evenViewType = 0
    private val oddViewType = 1

    private val mOnClickListener: View.OnClickListener
    // inicializamos mOnClickListener
    init {
        mOnClickListener = View.OnClickListener { v ->
            val book = v.tag as Book
            //pasamos uid del libro actual a la Activity de detalle a través de un Intent
            val intent = Intent(v.context, BookDetailActivity::class.java).apply {
                putExtra(BookDetailFragment.ARG_ITEM_ID, book.uid)
            }
            // lanzamos la activity
            v.context.startActivity(intent)
        }
    }

    private fun getBook(position: Int): Book {
        return books[position]
    }

    fun setBooks(books: List<Book>) {
        this.books = books
        // Reloads the RecyclerView with new adapter data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            evenViewType
        } else {
            oddViewType
        }
    }

    // Creates View Holder for re-use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = when (viewType) {
            evenViewType -> {
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_book_list_content_even, parent, false)
            }
            oddViewType -> {
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_book_list_content_odd, parent, false)
            }
            else -> {
                throw IllegalStateException("Unsupported viewType $viewType")
            }
        }
        return ViewHolder(view)
    }

    // Binds re-usable View for a given position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // obtenemos el libro de en esa posición
        val book = getBook(position)
        holder.titleView.text = book.title
        holder.authorView.text = book.author

        // TODO: Set View Click Listener
        // para ese item establecemos el listener mOnClickListener
        with(holder.itemView) {
            tag = book
            setOnClickListener(mOnClickListener)
        }
    }

    // Returns total items in Adapter
    override fun getItemCount(): Int {
        return books.size
    }

    // Holds an instance to the view for re-use
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.title)
        val authorView: TextView = view.findViewById(R.id.author)
    }

}

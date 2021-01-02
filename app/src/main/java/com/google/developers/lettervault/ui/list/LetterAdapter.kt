package com.google.developers.lettervault.ui.list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.developers.lettervault.R
import com.google.developers.lettervault.data.Letter
import com.google.developers.lettervault.ui.detail.LetterDetailActivity
import com.google.developers.lettervault.util.LETTER_ID

/**
 * Implementation of an Paging adapter that shows list of Letters.
 */
class LetterAdapter(
    private val clickListener: (Letter) -> Unit

) : PagedListAdapter<Letter, LetterViewHolder>(DIFF_CALLBACK) {
    private lateinit var context: Context

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Letter>() {
            override fun areItemsTheSame(oldItem: Letter, newItem: Letter): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Letter, newItem: Letter): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        context = parent.context

        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_letter, parent, false)
        return LetterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val currentLetter = getItem(position)

        holder.bindData(currentLetter!!) { letter ->
            val intent = Intent(context, LetterDetailActivity::class.java)
            intent.putExtra(LETTER_ID, letter.id)
            context.startActivity(intent)
        }
    }

}

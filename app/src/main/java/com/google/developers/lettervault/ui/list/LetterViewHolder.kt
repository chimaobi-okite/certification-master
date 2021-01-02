package com.google.developers.lettervault.ui.list

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.google.developers.lettervault.R
import com.google.developers.lettervault.data.Letter
import kotlinx.android.synthetic.main.item_letter.view.*
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * View holds a letter for RecyclerView.
 */
class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var letter: Letter
    private val context = itemView.context
    private val simpleDate = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())

    fun bindData(letter: Letter, clickListener: (Letter) -> Unit) {
        this.letter = letter

        itemView.setOnClickListener { clickListener(letter) }

        itemView.subject.text = letter.subject
        itemView.content.text = letter.content

        if (letter.expires < System.currentTimeMillis() && letter.opened != 0L) {
            val opened =
                context.getString(R.string.title_opened, simpleDate.format(letter.opened))
            itemView.status.text = opened
            itemView.lock.setImageDrawable(context.getDrawable(R.drawable.ic_lock_open))
        } else {
            itemView.lockLarge.visibility = View.VISIBLE

            if (letter.expires < System.currentTimeMillis()) {
                val ready = context.getString(R.string.letter_ready)
                itemView.status.text = ready
                itemView.lock.setImageDrawable(context.getDrawable(R.drawable.ic_lock))
            } else {
                val opening =
                    context.getString(R.string.letter_opening, simpleDate.format(letter.expires))
                itemView.status.text = opening
                itemView.lock.setImageDrawable(context.getDrawable(R.drawable.ic_lock))
            }
        }
    }

    /**
     * This method is used during automated tests.
     *
     * DON'T REMOVE THIS METHOD
     */
    @VisibleForTesting
    fun getLetter(): Letter = letter
}

package labs.mamangkompii.mymoviesbook.view.moviedetail

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import labs.mamangkompii.mymoviesbook.R
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem

class MovieReviewItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val authorLabel = itemView.findViewById<TextView>(
        R.id.movieTitleLabel
    )

    private val reviewLabel = itemView.findViewById<TextView>(
        R.id.movieOverviewLabel
    )

    fun bind(item: MovieReviewItem?) {
        item?.let {
            authorLabel.text = it.author
            reviewLabel.text = it.content
        }
    }

}

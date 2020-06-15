package labs.mamangkompii.mymoviesbook.view.movielist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import labs.mamangkompii.mymoviesbook.R
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import org.joda.time.format.DateTimeFormatter

class MovieSummaryViewHolder(
    itemView: View,
    private val dateTimeFormatter: DateTimeFormatter
) : RecyclerView.ViewHolder(itemView) {

    private val movieTitleLabel = itemView.findViewById<TextView>(
        R.id.movieTitleLabel
    )
    private val movieReleaseDateLabel = itemView.findViewById<TextView>(
        R.id.movieReleaseDateLabel
    )
    private val movieOverviewLabel = itemView.findViewById<TextView>(
        R.id.movieOverviewLabel
    )
    private val moviePosterIV = itemView.findViewById<ImageView>(
        R.id.moviePosterIV
    )

    fun bind(item: MovieSummary?) {
        item?.let {
            movieTitleLabel.text = it.title
            movieReleaseDateLabel.text =
                if (it.releaseDate != null) {
                    dateTimeFormatter.print(it.releaseDate)
                } else {
                    ""
                }
            movieOverviewLabel.text = it.overview ?: ""
            Picasso.get()
                .load(it.posterPath)
                .resizeDimen(
                    R.dimen.movie_poster_thumbnail_dimen_width,
                    R.dimen.movie_poster_thumbnail_dimen_height
                )
                .centerCrop()
                .into(moviePosterIV)
        }
    }
}
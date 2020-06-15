package labs.mamangkompii.mymoviesbook.view.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import labs.mamangkompii.mymoviesbook.R
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import org.joda.time.format.DateTimeFormat

class MovieListItemAdapter(private var itemClickListener: ItemClickListener) :
    PagedListAdapter<MovieSummary, MovieSummaryViewHolder>(MovieSummaryDiffCallback) {

    private val dateTimeFormatter = DateTimeFormat.forPattern("dd MMM YYYY")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSummaryViewHolder {
        return MovieSummaryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list, parent, false),
            dateTimeFormatter
        )
    }

    override fun onBindViewHolder(holder: MovieSummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { itemClickListener.onClickMovie(getItem(position)) }
    }
}

object MovieSummaryDiffCallback : DiffUtil.ItemCallback<MovieSummary>() {
    override fun areItemsTheSame(oldItem: MovieSummary, newItem: MovieSummary) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieSummary, newItem: MovieSummary) =
        oldItem == newItem
}

interface ItemClickListener {
    fun onClickMovie(item: MovieSummary?)
}

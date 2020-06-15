package labs.mamangkompii.mymoviesbook.view.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import labs.mamangkompii.mymoviesbook.R
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem

class MovieReviewItemAdapter :
    PagedListAdapter<MovieReviewItem, MovieReviewItemViewHolder>(MovieReviewItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewItemViewHolder {
        return MovieReviewItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_movie_review_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieReviewItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object MovieReviewItemDiffCallback : DiffUtil.ItemCallback<MovieReviewItem>() {
    override fun areItemsTheSame(oldItem: MovieReviewItem, newItem: MovieReviewItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MovieReviewItem, newItem: MovieReviewItem) =
        oldItem == newItem
}
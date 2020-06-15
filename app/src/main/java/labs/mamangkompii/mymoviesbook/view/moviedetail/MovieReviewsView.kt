package labs.mamangkompii.mymoviesbook.view.moviedetail

import androidx.paging.PagedList
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem

interface MovieReviewsView {
    fun showReviews(pagedList: PagedList<MovieReviewItem>)
    fun showErrorGettingReview()
    fun hideGetReviewLoadingProgress()
    fun showGetReviewLoadingProgress()
    fun showEmptyPlaceholderIfNeeded()
}
package labs.mamangkompii.mymoviesbook.view.movielist

import androidx.paging.PagedList
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary

interface MovieListView {

    fun updateMovieList(pagedList: PagedList<MovieSummary>)

    fun showErrorGetDataIndicator()

}
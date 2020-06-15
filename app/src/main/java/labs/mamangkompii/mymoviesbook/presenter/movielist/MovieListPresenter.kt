package labs.mamangkompii.mymoviesbook.presenter.movielist

import labs.mamangkompii.mymoviesbook.presenter.BasePresenter
import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory

interface MovieListPresenter : BasePresenter {

    fun setupMovieListDataSource()

    fun changeMovieListCategory(movieListCategory: MovieListCategory)

    fun retryFetchData()

}
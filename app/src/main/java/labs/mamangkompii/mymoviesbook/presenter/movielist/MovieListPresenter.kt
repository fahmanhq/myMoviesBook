package labs.mamangkompii.mymoviesbook.presenter.movielist

import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory

interface MovieListPresenter {

    fun setupMovieListDataSource()

    fun changeMovieListCategory(movieListCategory: MovieListCategory)

    fun retryFetchData()

    fun onDestroy()

}
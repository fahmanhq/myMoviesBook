package labs.mamangkompii.mymoviesbook.view.moviedetail

import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail

interface MovieDetailView {

    fun showLoadingMovieDetail()
    fun showMovieDetail(movieDetail: MovieDetail)
    fun showErrorGettingMovieDetail()
    fun disableFavoriteButton()
    fun enableFavoriteButton()
    fun toggleFavoriteState(isFavorited: Boolean)
    fun showErrorAddMovieAsFavorite()
    fun showErrorRemoveMovieFromFavorite()
    fun showAllWidget()

}
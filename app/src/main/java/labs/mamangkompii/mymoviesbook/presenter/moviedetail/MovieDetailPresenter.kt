package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import labs.mamangkompii.mymoviesbook.presenter.BasePresenter

interface MovieDetailPresenter : BasePresenter {

    fun requestDetail(movieId: Int)
    fun onFavoriteButtonClick()

}
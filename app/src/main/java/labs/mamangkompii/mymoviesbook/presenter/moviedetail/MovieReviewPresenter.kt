package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import labs.mamangkompii.mymoviesbook.presenter.BasePresenter

interface MovieReviewPresenter : BasePresenter {

    fun requestReview()
    fun retryRequestReview()

}
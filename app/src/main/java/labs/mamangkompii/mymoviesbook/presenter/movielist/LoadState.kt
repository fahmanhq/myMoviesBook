package labs.mamangkompii.mymoviesbook.presenter.movielist

sealed class LoadState {
    object LoadingInitialData : LoadState()
    object LoadingMoreData : LoadState()
    object InitialLoadFinished : LoadState()
    object LoadMoreFinished : LoadState()
    object NoLoadMoreRemaining : LoadState()
    object Error : LoadState()
}
package labs.mamangkompii.mymoviesbook.usecase.model

sealed class MovieListCategory {
    object Popular : MovieListCategory()
    object TopRated : MovieListCategory()
    object NowPlaying : MovieListCategory()
}
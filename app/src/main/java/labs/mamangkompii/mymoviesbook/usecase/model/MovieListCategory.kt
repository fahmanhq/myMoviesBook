package labs.mamangkompii.mymoviesbook.usecase.model

import java.io.Serializable

sealed class MovieListCategory : Serializable {
    object Popular : MovieListCategory()
    object TopRated : MovieListCategory()
    object NowPlaying : MovieListCategory()
    object Favorite : MovieListCategory()
}
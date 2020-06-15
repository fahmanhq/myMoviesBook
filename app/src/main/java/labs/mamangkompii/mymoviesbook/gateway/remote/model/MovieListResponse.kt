package labs.mamangkompii.mymoviesbook.gateway.remote.model

class MovieListResponse : MovieDBListResponse<MovieListItemApiModel>()

class MovieListItemApiModel {
    var id: String? = null
    var title: String? = null
    var overview: String? = null
    var posterPath: String? = null
    var releaseDate: String? = null

    fun isMinimumDataAcquired(): Boolean {
        return id != null && !title.isNullOrBlank()
    }
}
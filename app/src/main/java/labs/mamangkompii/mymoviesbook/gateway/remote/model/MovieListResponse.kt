package labs.mamangkompii.mymoviesbook.gateway.remote.model

class MovieListResponse : MovieDBListResponse<MovieDetailApiModel>()

class MovieDetailApiModel {
    var id: Int? = null
    var title: String? = null
    var overview: String? = null
    var posterPath: String? = null
    var releaseDate: String? = null

    fun isMinimumDataAcquired(): Boolean {
        return id != null && !title.isNullOrBlank()
    }
}
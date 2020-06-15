package labs.mamangkompii.mymoviesbook.gateway.remote.model

class MovieListResponse(
    var page: Int? = null,
    var results: List<MovieListItemApiModel>? = null,
    var totalResults: Int? = null,
    var totalPages: Int? = null
)

class MovieListItemApiModel {
    var id: String? = null
    var title: String? = null
    var overview: String? = null
    var posterPath: String? = null
    var releaseDate: String? = null

    fun isMinimumDataAcquired() : Boolean {
        return id != null && !title.isNullOrBlank()
    }
}
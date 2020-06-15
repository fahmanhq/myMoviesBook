package labs.mamangkompii.mymoviesbook.gateway.remote.model

open class MovieDBListResponse<T> {
    var page: Int? = null
    var results: List<T>? = null
    var totalResults: Int? = null
    var totalPages: Int? = null
}
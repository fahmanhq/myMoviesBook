package labs.mamangkompii.mymoviesbook.gateway.remote.model

class MovieReviewsResponse : MovieDBListResponse<MovieReviewItemApiModel>()

class MovieReviewItemApiModel {
    var id: String? = null
    var author: String? = null
    lateinit var content: String
}


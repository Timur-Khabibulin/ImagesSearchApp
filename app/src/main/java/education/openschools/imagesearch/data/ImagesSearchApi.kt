package education.openschools.imagesearch.data

import education.openschools.imagesearch.domain.entities.SearchResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ImagesSearchApi {

    @FormUrlEncoded
    @POST("/images")
    fun searchImages(
        @Field("q") query: String,
        @Field("page") page: Int,
        @Field("num") pageSize: Int
    ): Call<SearchResponse>
}

package kirimin.me.whoongithub._common.network.apis

import kirimin.me.whoongithub._common.network.entities.Repository
import kirimin.me.whoongithub._common.network.entities.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import rx.Single

interface GitHubService {

    @GET("users/{id}")
    fun user(@Path("id") id: String): Single<User>

    @GET("users/{id}/repos")
    fun repositories(@Path("id") id: String,
                     @Query("page") page: Int,
                     @Query("per_page") perPage: Int = 100): Observable<List<Repository>>
}
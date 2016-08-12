package kirimin.me.whoongithub.user_info

import kirimin.me.whoongithub._common.network.apis.GitHubService
import kirimin.me.whoongithub._common.network.entities.Repository
import kirimin.me.whoongithub._common.network.entities.User
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

open class UserInfoRepository {

    open fun requestUser(id: String): Single<User> {
        return RetrofitClient.default().build().create(GitHubService::class.java).user(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    open fun requestRepository(id: String, page: Int): Observable<List<Repository>> {
        val retrofit = RetrofitClient.default().build().create(GitHubService::class.java)
        return retrofit.repositories(id = id, page = page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
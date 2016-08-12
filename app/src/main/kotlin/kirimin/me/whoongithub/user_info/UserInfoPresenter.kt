package kirimin.me.whoongithub.user_info

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import kirimin.me.whoongithub.BuildConfig
import kirimin.me.whoongithub.R
import kirimin.me.whoongithub._common.models.LanguageVM
import kirimin.me.whoongithub._common.models.RepositoryVM
import kirimin.me.whoongithub._common.network.entities.Repository
import kirimin.me.whoongithub._common.network.entities.User
import rx.Observable
import rx.Single
import rx.subscriptions.CompositeSubscription

class UserInfoPresenter(val view: UserInfoView, val repository: UserInfoRepository) {

    private val subscriptions = CompositeSubscription()

    var user = ObservableField<User>()
    var layoutVisibility = ObservableInt(View.INVISIBLE)
    var locationVisibility = ObservableInt(View.INVISIBLE)
    var companyVisibility = ObservableInt(View.INVISIBLE)
    var linkVisibility = ObservableInt(View.INVISIBLE)
    var mailVisibility = ObservableInt(View.INVISIBLE)
    var avatarVisibility = ObservableInt(View.INVISIBLE)

    fun onCreate(id: String) {
        view.initActionBar(id)
        val userRequest = repository.requestUser(id)
        val repositoriesRequest = getRepositoriesRequest(id)
        val userInfoRequest = Single.zip(userRequest, repositoriesRequest) { user, repositories -> user to repositories }
        subscriptions.add(userInfoRequest.subscribe({ pair ->
            user.set(pair.first)
            val repositories = pair.second
            layoutVisibility.set(View.VISIBLE)
            user.get().location?.let {
                locationVisibility.set(View.VISIBLE)
            }
            user.get().company?.let {
                companyVisibility.set(View.VISIBLE)
            }
            user.get().blog?.let {
                linkVisibility.set(View.VISIBLE)
            }
            user.get().email?.let {
                mailVisibility.set(View.VISIBLE)
            }
            user.get().avatar_url?.let {
                avatarVisibility.set(View.VISIBLE)
            }
            getLanguages(repositories).forEach {
                val language = LanguageVM(languageName = it.first,
                        languageCount = it.second.count().toString(),
                        languageStartCount = it.second.map { repo -> repo.stargazers_count }.sum().toString())
                view.addLanguage(language)
            }
            sortRepositories(repositories).forEach {
                val repoIcon = if (it.fork) R.drawable.ic_call_split_grey600_36dp else android.R.drawable.ic_menu_sort_by_size
                val repository = RepositoryVM(iconImageResource = repoIcon,
                        repositoryName = it.name,
                        starCount = it.stargazers_count.toString(),
                        language = it.language ?: "",
                        description = it.description,
                        htmlUrl = it.html_url ?: "")
                view.addRepository(repository)
            }
        }, { e ->
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            view.networkErrorHandling()
        }))
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
    }

    /**
     * リポジトリを言語ごとに言語名とリポジトリリストのPairにまとめたListを返す
     */
    fun getLanguages(repositories: List<Repository>): List<Pair<String, List<Repository>>> {
        return repositories
                .filter { it.language != null }
                .groupBy { it.language!! }
                .toList().sortedByDescending { language -> language.second.count() }
    }

    /**
     * リポジトリをスターの数とforkか否かでソートして返す
     */
    fun sortRepositories(repositories: List<Repository>): List<Repository> {
        return repositories
                .sortedByDescending { repo -> repo.stargazers_count }
                .sortedBy { repo -> repo.fork }
    }

    /**
     * APIの全ページをリクエストしリポジトリを全て取得するSingleを返す
     */
    fun getRepositoriesRequest(id: String): Single<List<Repository>> {
        fun getAllRepos(page: Int): Observable<List<Repository>> {
            return repository.requestRepository(id, page)
                    .flatMap { if (it.size == 0) Observable.just(it) else Observable.merge(Observable.just(it), getAllRepos(page + 1)) }
        }
        return getAllRepos(1)
                .toList()
                .map { it.flatMap { it } }
                .toSingle()
    }
}
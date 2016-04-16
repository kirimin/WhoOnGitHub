package kirimin.me.whoongithub.network.apis

import com.android.volley.RequestQueue
import rx.Observable
import kirimin.me.whoongithub.models.Repository
import org.json.JSONArray
import java.util.ArrayList

class RepositoryApi {
    companion object {

        fun request(requestQueue: RequestQueue, id: String, page: Int): Observable<Repository> =
                ApiAccessor.stringRequest(requestQueue, "https://api.github.com/users/$id/repos?per_page=100&page=$page")
                        .map { s -> JSONArray(s) }
                        .flatMap { array ->
                            val repositories = ArrayList<Repository>()
                            for (i in 0..array.length() - 1) {
                                val json = array.getJSONObject(i)
                                repositories.add(Repository(
                                        id = json.getInt("id"),
                                        name = json.getString("name"),
                                        fullName = json.getString("full_name"),
                                        ownerId = json.getJSONObject("owner").getString("login"),
                                        htmlUrl = json.getString("html_url"),
                                        description = json.getString("description"),
                                        fork = json.getBoolean("fork"),
                                        stargazersCount = json.getInt("stargazers_count"),
                                        watchersCount = json.getInt("watchers_count"),
                                        language = json.getString("language"),
                                        forks_count = json.getInt("forks_count")))
                            }
                            // 全て読み込むまで次のページのリクエストを投げてマージ
                            if (repositories.size > 0) {
                                Observable.merge(Observable.from(repositories), request(requestQueue, id, page + 1))
                            } else {
                                Observable.from(repositories)
                            }
                        }
    }
}
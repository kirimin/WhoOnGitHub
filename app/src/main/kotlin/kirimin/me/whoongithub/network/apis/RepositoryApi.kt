package kirimin.me.whoongithub.network.apis

import com.android.volley.RequestQueue
import rx.Observable
import kirimin.me.whoongithub.models.User
import kirimin.me.whoongithub.models.Repository
import org.json.JSONArray
import java.util.ArrayList

public class RepositoryApi {
    public class object {

        fun request(requestQueue: RequestQueue, id: String, page: Int): Observable<Repository> {
            return ApiAccessor.stringRequest(requestQueue, "https://api.github.com/users/" + id + "/repos?per_page=100&page=" + page)
                    .map { s -> JSONArray(s) }
                    .flatMap { array ->
                        val repositories = ArrayList<Repository>()
                        for (i in 0..array.length() - 1) {
                            val json = array.getJSONObject(i)
                            repositories.add(Repository(
                                    json.getInt("id"),
                                    json.getString("name"),
                                    json.getString("full_name"),
                                    json.getJSONObject("owner").getString("login"),
                                    json.getString("description"),
                                    json.getBoolean("fork"),
                                    json.getInt("stargazers_count"),
                                    json.getInt("watchers_count"),
                                    json.getString("language"),
                                    json.getInt("forks_count")))
                        }
                        // 全て読み込むまで次のページのリクエストを投げてマージ
                        if (repositories.size() > 0) {
                            Observable.merge(Observable.from(repositories), request(requestQueue, id, page + 1))
                        } else {
                            Observable.from(repositories)
                        }
                    }
        }
    }
}
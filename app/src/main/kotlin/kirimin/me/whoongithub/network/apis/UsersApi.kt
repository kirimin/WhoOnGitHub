package kirimin.me.whoongithub.network.apis

import com.android.volley.RequestQueue
import rx.Observable
import org.json.JSONObject
import kirimin.me.whoongithub.models.User

public class UsersApi {
    public class object {

        fun request(requestQueue: RequestQueue, id: String): Observable<User> {
            return ApiAccessor.request(requestQueue, "https://api.github.com/users/" + id)
                    .map { json ->
                        User(
                                json.getString("login"),
                                json.getString("id"),
                                json.getString("avatar_url"),
                                json.getString("gravatar_id"),
                                json.getBoolean("site_admin"),
                                json.getString("name"),
                                json.getString("company"),
                                json.getString("blog"),
                                json.getString("location"),
                                json.getString("email"),
                                json.getBoolean("hireable"),
                                json.getString("bio"),
                                json.getInt("public_repos"),
                                json.getInt("public_gists"),
                                json.getInt("followers"),
                                json.getInt("following"),
                                json.getString("created_at"),
                                json.getString("updated_at"))
                    }
        }
    }
}
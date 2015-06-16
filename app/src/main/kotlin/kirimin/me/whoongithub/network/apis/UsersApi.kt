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
                                login = json.getString("login"),
                                id = json.getString("id"),
                                avatarUrl = json.getString("avatar_url"),
                                gravatarId = json.getString("gravatar_id"),
                                site_admin = json.getBoolean("site_admin"),
                                name = json.getString("name"),
                                company = json.getString("company"),
                                blog = json.getString("blog"),
                                location = json.getString("location"),
                                email = json.getString("email"),
                                hireable = json.getBoolean("hireable"),
                                bio = json.getString("bio"),
                                publicRepos = json.getInt("public_repos"),
                                publicGists = json.getInt("public_gists"),
                                followers = json.getInt("followers"),
                                following = json.getInt("following"),
                                createdAt = json.getString("created_at"),
                                updatedAt = json.getString("updated_at"))
                    }
        }
    }
}
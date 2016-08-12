package kirimin.me.whoongithub._common.network.entities

class User (
        val login: String = "",
        val id: String = "",
        val avatar_url: String? = null,
        val gravatar_id: String = "",
        val site_admin: String = "",
        val name: String = "",
        val company: String? = null,
        val blog: String? = null,
        val location: String? = null,
        val email: String? = null,
        val hireable: String = "",
        val bio: String = "",
        val public_repos: Int = 0,
        val public_gists: Int = 0,
        val followers: Int = 0,
        val following: Int = 0,
        val created_at: String = "",
        val updated_at: String = "")
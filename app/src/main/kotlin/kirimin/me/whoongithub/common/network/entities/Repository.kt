package kirimin.me.whoongithub.common.network.entities

data class Repository(
        val id: Int = 0,
        val name: String = "",
        val full_name: String = "",
        val owner_id: String = "",
        val html_url: String? = null,
        val description: String? = null,
        val fork: Boolean = false,
        val stargazers_count: Int = 0,
        val watchers_count: Int = 0,
        val language: String? = null,
        val forks_count: Int = 0)
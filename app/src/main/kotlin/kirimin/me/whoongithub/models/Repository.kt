package kirimin.me.whoongithub.models

class Repository(
        val id: Int,
        val name: String,
        val fullName: String,
        val ownerId: String,
        val description: String,
        val fork: Boolean,
        val stargazersCount: Int,
        val watchersCount: Int,
        val language: String,
        val forks_count: Int) {

}
package kirimin.me.whoongithub.models

class User (
        val login: String,
        val id: String,
        val avatarUrl: String,
        val gravatarId: String,
        val site_admin: String,
        val name: String,
        val company: String,
        val blog: String,
        val location: String,
        val email: String,
        val hireable: String,
        val bio: String,
        val publicRepos: Int,
        val publicGists: Int,
        val followers: Int,
        val following: Int,
        val createdAt: String,
        val updatedAt: String) {
}
package kirimin.me.whoongithub._common.models

data class RepositoryVM(
        val iconImageResource: Int,
        val repositoryName: String,
        val starCount: String,
        val language: String,
        val description: String,
        val htmlUrl: String)
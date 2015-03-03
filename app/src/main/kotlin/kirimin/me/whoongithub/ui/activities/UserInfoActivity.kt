package kirimin.me.whoongithub.ui.activities

import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import kirimin.me.whoongithub.R
import kirimin.me.whoongithub.network.apis.UsersApi
import kirimin.me.whoongithub.network.RequestQueueSingleton
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import android.widget.TextView
import android.widget.ImageView
import com.squareup.picasso.Picasso
import rx.subscriptions.CompositeSubscription
import butterknife.bindView
import kirimin.me.whoongithub.network.apis.RepositoryApi
import rx.Observable
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.view.View

public class UserInfoActivity : ActionBarActivity() {

    val userNameText: TextView by bindView(R.id.userInfoNameText)
    val userIdText: TextView by bindView(R.id.userInfoIdText)
    val locationText: TextView by bindView(R.id.userInfoLocationText)
    val companyText: TextView by bindView(R.id.userInfoCompanyText)
    val iconImage: ImageView by bindView(R.id.userInfoIconImage)
    val linkText: TextView by bindView(R.id.userInfoLinkText)
    val mailText: TextView by bindView(R.id.userInfoMailText)
    val languageLayout: LinearLayout by bindView(R.id.userInfoLanguageLayout)
    val repositoryLayout: LinearLayout by bindView(R.id.userInfoRepositoryLayout)

    private val subscriptions = CompositeSubscription();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val id = "kirimin"
        getSupportActionBar().setTitle(id)

        val userRequest = UsersApi.request(RequestQueueSingleton.get(getApplicationContext()), id)
        val repositoryRequest = RepositoryApi.request(RequestQueueSingleton.get(getApplicationContext()), id, 1).toList()
        subscriptions.add(Observable
                .zip(userRequest, repositoryRequest, { user, repositories -> Pair(user, repositories) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val user = response.first
                    val repositories = response.second
                    userNameText.setText(user.name)
                    userIdText.setText(user.login)
                    setTextAndToVisibleIfNotNull(locationText, user.location)
                    setTextAndToVisibleIfNotNull(companyText, user.company)
                    setTextAndToVisibleIfNotNull(linkText, user.blog)
                    setTextAndToVisibleIfNotNull(mailText, user.email)
                    Picasso.with(this).load(user.avatarUrl).fit().into(iconImage)
                    val inflater = LayoutInflater.from(this);
                    repositories
                            .filter { repo -> !repo.language.equals("null") }
                            .groupBy { repo -> repo.language }
                            .toList().sortDescendingBy { language -> language.second.count() }
                            .forEach { language ->
                                val languageView = inflater.inflate(R.layout.activity_user_info_language, null) as LinearLayout
                                val languageNameText = languageView.findViewById(R.id.userInfoLanguageName) as TextView
                                val languageCountText = languageView.findViewById(R.id.userInfoLanguageCount) as TextView
                                val languageStartCountText = languageView.findViewById(R.id.userInfoLanguageStarCount) as TextView
                                languageNameText.setText(language.first)
                                languageCountText.setText(language.second.count().toString())
                                languageStartCountText.setText(language.second.map { repo -> repo.stargazersCount }.sum().toString())
                                languageLayout.addView(languageView)
                            }
                    repositories
                            .sortDescendingBy { repo -> repo.stargazersCount }
                            .forEach { repo ->
                                val repositoryView = inflater.inflate(R.layout.activity_user_info_repositories, null) as LinearLayout
                                val repositoryNameText = repositoryView.findViewById(R.id.userInfoRepositoryName) as TextView
                                val repositoryDescriptionText = repositoryView.findViewById(R.id.userInfoRepositoryDescription) as TextView
                                val repositoryStarCountText = repositoryView.findViewById(R.id.userInfoRepositoryStarCount) as TextView
                                val repositoryLanguageText = repositoryView.findViewById(R.id.userInfoRepositoryLanguage) as TextView
                                repositoryNameText.setText(repo.name)
                                repositoryStarCountText.setText(repo.stargazersCount.toString())
                                repositoryLanguageText.setText(if (!repo.language.equals("null")) repo.language else "")
                                setTextAndToVisibleIfNotNull(repositoryDescriptionText, repo.description)
                                repositoryLayout.addView(repositoryView)
                            }
                }, { e ->
                    e.printStackTrace()
                }))
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    private fun setTextAndToVisibleIfNotNull(textView: TextView, text: String) {
        if (text.equals("null") || text.isEmpty()) return
        textView.setText(text)
        textView.setVisibility(View.VISIBLE)
    }
}

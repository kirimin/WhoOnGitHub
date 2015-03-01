package kirimin.me.whoongithub.ui.activities

import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import kirimin.me.whoongithub.R
import kirimin.me.whoongithub.models.User
import android.util.Log
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
import java.util.HashMap
import java.util.ArrayList
import java.util.HashSet
import rx.Observable
import kirimin.me.whoongithub.models.Repository
import android.view.LayoutInflater
import android.widget.LinearLayout

public class UserInfoActivity : ActionBarActivity() {

    val userNameText: TextView by bindView(R.id.userInfoNameText)
    val userIdText: TextView by bindView(R.id.userInfoIdText)
    val locationText: TextView by bindView(R.id.userInfoLocationText)
    val companyText: TextView by bindView(R.id.userInfoCompanyText)
    val iconImage: ImageView by bindView(R.id.userInfoIconImage)
    val linkText: TextView by bindView(R.id.userInfoLinkText)
    val mailText: TextView by bindView(R.id.userInfoMailText)
    val languageLayout: LinearLayout by bindView(R.id.userInfoLanguageLayout)

    private val subscriptions = CompositeSubscription();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val id = "kirimin"
        getSupportActionBar().setTitle(id)

        val userRequest = UsersApi.request(RequestQueueSingleton.get(getApplicationContext()), id)
        val repositoryRequest = RepositoryApi.request(RequestQueueSingleton.get(getApplicationContext()), id).toList()
        subscriptions.add(Observable
                .zip(userRequest, repositoryRequest, { user, repositories -> Pair(user, repositories) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val user = response.first
                    val repositories = response.second
                    userNameText.setText(user.name)
                    userIdText.setText(user.login)
                    locationText.setText(user.location)
                    companyText.setText(user.company)
                    Picasso.with(this).load(user.avatarUrl).fit().into(iconImage)

                    linkText.setText(user.blog)
                    mailText.setText(user.email)

                    val inflater = LayoutInflater.from(this);
                    repositories
                            .filter { repo -> !repo.language.equals("null") }
                            .groupBy { repo -> repo.language }
                            .forEach { lang ->
                                val languageView = inflater.inflate(R.layout.activity_user_info_language, null) as LinearLayout
                                val languageNameText = languageView.findViewById(R.id.userInfoLanguageName) as TextView
                                val languageCountText = languageView.findViewById(R.id.userInfoLanguageCount) as TextView
                                val languageStartCountText = languageView.findViewById(R.id.userInfoLanguageStarCount) as TextView
                                languageNameText.setText(lang.getKey())
                                languageCountText.setText(lang.getValue().count().toString())
                                languageStartCountText.setText(lang.getValue().map { repo -> repo.stargazersCount }.sum().toString())
                                languageLayout.addView(languageView)
                            }
                }, { e -> e.printStackTrace() }))
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }
}

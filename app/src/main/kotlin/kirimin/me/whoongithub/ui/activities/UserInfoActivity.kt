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
import android.view.MenuItem
import android.widget.Toast
import android.content.Intent
import android.net.Uri

public class UserInfoActivity : ActionBarActivity() {

    class object {
        fun getBundle(id: String): Bundle {
            val bundle = Bundle()
            bundle.putString("id", id)
            return bundle
        }
    }

    val userNameText: TextView by bindView(R.id.userInfoNameText)
    val userIdText: TextView by bindView(R.id.userInfoIdText)
    val locationText: TextView by bindView(R.id.userInfoLocationText)
    val companyText: TextView by bindView(R.id.userInfoCompanyText)
    val iconImage: ImageView by bindView(R.id.userInfoIconImage)
    val linkText: TextView by bindView(R.id.userInfoLinkText)
    val mailText: TextView by bindView(R.id.userInfoMailText)
    val languageLayout: LinearLayout by bindView(R.id.userInfoLanguageLayout)
    val repositoryLayout: LinearLayout by bindView(R.id.userInfoRepositoryLayout)
    val parentLayout: LinearLayout by bindView(R.id.userInfoParentLayout)

    private val subscriptions = CompositeSubscription();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val id = getIntent().getStringExtra("id")
        val actionBar = getSupportActionBar()
        actionBar.setTitle(id)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)

        val userRequest = UsersApi.request(RequestQueueSingleton.get(getApplicationContext()), id)
        val repositoryRequest = RepositoryApi.request(RequestQueueSingleton.get(getApplicationContext()), id, 1).toList()
        subscriptions.add(Observable
                .zip(userRequest, repositoryRequest, { user, repositories -> Pair(user, repositories) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val user = response.first
                    val repositories = response.second
                    parentLayout.setVisibility(View.VISIBLE)
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
                            .sortBy { repo -> repo.fork }
                            .forEach { repo ->
                                val repositoryView = inflater.inflate(R.layout.activity_user_info_repositories, null) as LinearLayout
                                val repositoryIconImage = repositoryView.findViewById(R.id.userInfoRepositoryIcon) as ImageView
                                val repositoryNameText = repositoryView.findViewById(R.id.userInfoRepositoryName) as TextView
                                val repositoryDescriptionText = repositoryView.findViewById(R.id.userInfoRepositoryDescription) as TextView
                                val repositoryStarCountText = repositoryView.findViewById(R.id.userInfoRepositoryStarCount) as TextView
                                val repositoryLanguageText = repositoryView.findViewById(R.id.userInfoRepositoryLanguage) as TextView
                                repositoryIconImage.setImageResource(if (repo.fork) R.drawable.ic_call_split_grey600_36dp else android.R.drawable.ic_menu_sort_by_size)
                                repositoryNameText.setText(repo.name)
                                repositoryStarCountText.setText(repo.stargazersCount.toString())
                                repositoryLanguageText.setText(if (!repo.language.equals("null")) repo.language else "")
                                setTextAndToVisibleIfNotNull(repositoryDescriptionText, repo.description)
                                repositoryView.findViewById(R.id.userInfoRepositoryBrowser).setOnClickListener {
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repo.htmlUrl)))
                                }
                                repositoryLayout.addView(repositoryView)
                            }
                }, { e ->
                    Toast.makeText(getApplicationContext(), "Error. Username does not exist?", Toast.LENGTH_SHORT).show()
                    finish()
                }))
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.getItemId()) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTextAndToVisibleIfNotNull(textView: TextView, text: String) {
        if (text.equals("null") || text.isEmpty()) return
        textView.setText(text)
        textView.setVisibility(View.VISIBLE)
    }
}

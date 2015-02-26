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

public class UserInfoActivity : ActionBarActivity() {

    val userInfoText: TextView by bindView(R.id.userInfoNameText)
    val idText: TextView by bindView(R.id.userInfoIdText)
    val locationText: TextView by bindView(R.id.userInfoLocationText)
    val companyText: TextView by bindView(R.id.userInfoCompanyText)
    val iconImage: ImageView by bindView(R.id.userInfoIconImage)
    val linkText: TextView by bindView(R.id.userInfoLinkText)
    val mailText: TextView by bindView(R.id.userInfoMailText)

    private val subscriptions = CompositeSubscription();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val id = "kirimin"
        getSupportActionBar().setTitle(id)

        subscriptions.add(UsersApi.request(RequestQueueSingleton.get(getApplicationContext()), id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { user ->
                            userInfoText.setText(user.name)
                            idText.setText(user.login)
                            locationText.setText(user.location)
                            companyText.setText(user.company)
                            Picasso.with(this).load(user.avatarUrl).fit().into(iconImage)

                            linkText.setText(user.blog)
                            mailText.setText(user.email)
                        },
                        { e -> e.printStackTrace() }));
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }
}

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

public class UserInfoActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val id = "kirimin"
        getSupportActionBar().setTitle(id)

        val userInfoText = findViewById(R.id.userInfoNameText) as TextView
        val idText = findViewById(R.id.userInfoIdText) as TextView
        val iconImage = findViewById(R.id.userInfoIconImage) as ImageView
        UsersApi.request(RequestQueueSingleton.get(getApplicationContext()), id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { user ->
                            userInfoText.setText(user.name)
                            idText.setText(user.login)
                            Picasso.with(this).load(user.avatarUrl).into(iconImage)
                        },
                        { e -> e.printStackTrace() })
    }
}

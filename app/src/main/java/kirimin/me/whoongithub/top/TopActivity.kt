package kirimin.me.whoongithub.top

import kirimin.me.whoongithub.R
import android.os.Bundle
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import kirimin.me.whoongithub.user_info.UserInfoView

import kotlinx.android.synthetic.main.activity_top.*

class TopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        userNameShowButton.setOnClickListener {
            val intent = Intent(applicationContext, UserInfoView.UserInfoActivity::class.java)
            intent.putExtras(UserInfoView.UserInfoActivity.buildBundle(userNameEdit.text.toString()))
            startActivity(intent)
        }
    }
}

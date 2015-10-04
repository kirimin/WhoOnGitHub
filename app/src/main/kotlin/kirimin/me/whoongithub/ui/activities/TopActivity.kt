package kirimin.me.whoongithub.ui.activities

import kirimin.me.whoongithub.R
import android.os.Bundle
import android.content.Intent
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.activity_top.*

public class TopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        userNameShowButton.setOnClickListener {
            val intent = Intent(applicationContext, UserInfoActivity::class.java)
            intent.putExtras(UserInfoActivity.buildBundle(userNameEdit.text.toString()))
            startActivity(intent)
        }
    }
}

package kirimin.me.whoongithub.ui.activities

import android.support.v7.app.ActionBarActivity
import android.widget.EditText
import kirimin.me.whoongithub.R
import android.widget.Button
import android.os.Bundle
import android.content.Intent

import kotlinx.android.synthetic.activity_top.*

public class TopActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        userNameShowButton.setOnClickListener {
            val intent = Intent(getApplicationContext(), javaClass<UserInfoActivity>())
            intent.putExtras(UserInfoActivity.buildBundle(userNameEdit.getText().toString()))
            startActivity(intent)
        }
    }
}

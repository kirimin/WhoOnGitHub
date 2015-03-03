package kirimin.me.whoongithub.ui.activities

import android.support.v7.app.ActionBarActivity
import butterknife.bindView
import android.widget.EditText
import kirimin.me.whoongithub.R
import android.widget.Button
import android.os.Bundle
import android.content.Intent

public class TopActivity : ActionBarActivity() {

    val userNameEdit: EditText by bindView(R.id.topUserNameEdit)
    val userNameShowButton: Button by bindView(R.id.topUserNameShowButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        userNameShowButton.setOnClickListener {
            val intent = Intent(getApplicationContext(), javaClass<UserInfoActivity>())
            intent.putExtras(UserInfoActivity.getBundle(userNameEdit.getText().toString()))
            startActivity(intent)
        }
    }
}

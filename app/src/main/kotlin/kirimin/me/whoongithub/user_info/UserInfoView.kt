package kirimin.me.whoongithub.user_info

import android.os.Bundle
import kirimin.me.whoongithub.R
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import kirimin.me.whoongithub.databinding.ActivityUserInfoBinding
import kirimin.me.whoongithub.databinding.ViewLanguageBinding
import kirimin.me.whoongithub._common.models.LanguageVM
import kirimin.me.whoongithub._common.models.RepositoryVM
import kirimin.me.whoongithub.databinding.ViewRepositoryBinding

import kotlinx.android.synthetic.main.activity_user_info.*

interface UserInfoView {

    fun initActionBar(id: String)

    fun addLanguage(language: LanguageVM)

    fun addRepository(repository: RepositoryVM)

    fun networkErrorHandling()

    class UserInfoActivity : AppCompatActivity(), UserInfoView {

        companion object {

            private val EXTRA_ID = "id"

            fun buildBundle(id: String): Bundle {
                val bundle = Bundle()
                bundle.putString(EXTRA_ID, id)
                return bundle
            }
        }

        private lateinit var presenter: UserInfoPresenter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = UserInfoPresenter(this, UserInfoRepository())
            val binding = DataBindingUtil.setContentView<ActivityUserInfoBinding>(this, R.layout.activity_user_info)
            binding.presenter = presenter
            presenter.onCreate(intent.getStringExtra(EXTRA_ID))
        }

        override fun onDestroy() {
            presenter.onDestroy()
            super<AppCompatActivity>.onDestroy()
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (android.R.id.home == item.itemId) {
                finish()
            }
            return super<AppCompatActivity>.onOptionsItemSelected(item)
        }

        override fun initActionBar(id: String) {
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar ?: return
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }

        override fun addLanguage(language: LanguageVM) {
            val binding = DataBindingUtil.inflate<ViewLanguageBinding>(LayoutInflater.from(this), R.layout.view_language, languageLayout, true)
            binding.language = language
        }

        override fun addRepository(repository: RepositoryVM) {
            val binding = DataBindingUtil.inflate<ViewRepositoryBinding>(LayoutInflater.from(this), R.layout.view_repository, repositoryLayout, true)
            binding.repository = repository
            binding.userInfoRepositoryBrowser.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repository.htmlUrl)))
            }
        }

        override fun networkErrorHandling() {
            Toast.makeText(applicationContext, "Error. Username does not exist?", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}

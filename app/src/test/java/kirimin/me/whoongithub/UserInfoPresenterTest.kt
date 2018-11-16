package kirimin.me.whoongithub

import android.view.View
import com.nhaarman.mockito_kotlin.*
import kirimin.me.whoongithub.common.models.LanguageVM
import kirimin.me.whoongithub.common.models.RepositoryVM
import kirimin.me.whoongithub.common.network.entities.Repository
import kirimin.me.whoongithub.common.network.entities.User
import kirimin.me.whoongithub.user_info.UserInfoPresenter
import kirimin.me.whoongithub.user_info.UserInfoRepository
import kirimin.me.whoongithub.user_info.UserInfoView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.Single
import rx.observers.TestSubscriber

class UserInfoPresenterTest {

    lateinit var viewMock: UserInfoView
    lateinit var repositoryMock: UserInfoRepository
    lateinit var presenter: UserInfoPresenter

    @Before
    fun setup() {
        viewMock = mock()
        repositoryMock = mock()
        presenter = UserInfoPresenter(viewMock, repositoryMock)
    }

    @Test
    fun onCreateTest() {
        val user = User(login = "kirimin", location = "tokyo", company = null, blog = null, email = null, avatar_url = null)
        whenever(repositoryMock.requestUser("kirimin")).then { Single.just(user) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        verify(repositoryMock, times(1)).requestUser("kirimin")
        verify(repositoryMock, times(1)).requestRepository("kirimin", 1)
        verify(viewMock, times(1)).initActionBar("kirimin")
        Assert.assertEquals(presenter.layoutVisibility.get(), View.VISIBLE)
        Assert.assertEquals(presenter.user.get(), user)
    }

    @Test
    fun networkErrorTest() {
        whenever(repositoryMock.requestUser("kirimin")).then { Single.error<User>(Throwable()) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        verify(viewMock, times(1)).networkErrorHandling()
    }

    @Test
    fun onDestroyTest() {
        presenter.onDestroy()
        Assert.assertTrue(presenter.subscriptions.isUnsubscribed)
    }

    @Test
    fun repositoriesTest() {
        val repositories = listOf<Repository>(
                Repository(name = "repo1", language = "Java", stargazers_count = 3),
                Repository(name = "repo2", language = "Kotlin", stargazers_count = 6),
                Repository(name = "repo3", language = "Java", stargazers_count = 1),
                Repository(name = "repo4", language = null, stargazers_count = 5, fork = true)
        )
        whenever(repositoryMock.requestUser("kirimin")).then { Single.just(User()) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(repositories) }
        whenever(repositoryMock.requestRepository("kirimin", 2)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        verify(viewMock, times(1)).addLanguage(LanguageVM("Java", "2", "4"))
        verify(viewMock, times(1)).addLanguage(LanguageVM("Kotlin", "1", "6"))

        val repo1VM = RepositoryVM(language = "Java", description = "", htmlUrl = "",
                iconImageResource = android.R.drawable.ic_menu_sort_by_size,
                repositoryName = "repo1", starCount = "3")
        verify(viewMock, times(1)).addRepository(repo1VM)
        val repo2VM = RepositoryVM(language = "Kotlin", description = "", htmlUrl = "",
                iconImageResource = android.R.drawable.ic_menu_sort_by_size,
                repositoryName = "repo2", starCount = "6")
        verify(viewMock, times(1)).addRepository(repo2VM)
        val repo3VM = RepositoryVM(language = "Java", description = "", htmlUrl = "",
                iconImageResource = android.R.drawable.ic_menu_sort_by_size,
                repositoryName = "repo3", starCount = "1")
        verify(viewMock, times(1)).addRepository(repo3VM)
        val repo4VM = RepositoryVM(language = "", description = "", htmlUrl = "",
                iconImageResource = R.drawable.ic_call_split_grey600_36dp,
                repositoryName = "repo4", starCount = "5")
        verify(viewMock, times(1)).addRepository(repo4VM)
    }

    @Test
    fun defaultVisibilityTest() {
        Assert.assertEquals(presenter.layoutVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.locationVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.companyVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.linkVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.mailVisibility.get(), View.INVISIBLE)
    }

    @Test
    fun showLocationTest() {
        whenever(repositoryMock.requestUser("kirimin")).then { Single.just(User(location = "tokyo", company = null, blog = null, email = null, avatar_url = null)) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        Assert.assertEquals(presenter.locationVisibility.get(), View.VISIBLE)
        Assert.assertEquals(presenter.companyVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.linkVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.mailVisibility.get(), View.INVISIBLE)
    }

    @Test
    fun showCompanyTest() {
        whenever(repositoryMock.requestUser("kirimin")).then { Single.just(User(location = null, company = "company", blog = null, email = null, avatar_url = null)) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        Assert.assertEquals(presenter.locationVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.companyVisibility.get(), View.VISIBLE)
        Assert.assertEquals(presenter.linkVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.mailVisibility.get(), View.INVISIBLE)
    }

    @Test
    fun showLinkTest() {
        whenever(repositoryMock.requestUser("kirimin")).then { Single.just(User(location = null, company = null, blog = "http://kirimin.me", email = null, avatar_url = null)) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        Assert.assertEquals(presenter.locationVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.companyVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.linkVisibility.get(), View.VISIBLE)
        Assert.assertEquals(presenter.mailVisibility.get(), View.INVISIBLE)
    }

    @Test
    fun showMailTest() {
        whenever(repositoryMock.requestUser("kirimin")).then { Single.just(User(location = null, company = null, blog = null, email = "cc@kirimin.me", avatar_url = null)) }
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.onCreate("kirimin")
        Assert.assertEquals(presenter.locationVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.companyVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.linkVisibility.get(), View.INVISIBLE)
        Assert.assertEquals(presenter.mailVisibility.get(), View.VISIBLE)
    }

    @Test
    fun getRepositoriesRequestTest() {
        val testSubscriber = TestSubscriber<List<Repository>>()
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>(Repository(name = "1"))) }
        whenever(repositoryMock.requestRepository("kirimin", 2)).then { Observable.just(listOf<Repository>(Repository(name = "2"))) }
        whenever(repositoryMock.requestRepository("kirimin", 3)).then { Observable.just(listOf<Repository>()) }
        presenter.getRepositoriesRequest("kirimin").subscribe(testSubscriber)
        val result = testSubscriber.onNextEvents[0]
        verify(repositoryMock, times(1)).requestRepository("kirimin", 1)
        verify(repositoryMock, times(1)).requestRepository("kirimin", 2)
        verify(repositoryMock, times(1)).requestRepository("kirimin", 3)
        Assert.assertEquals(result.size, 2)
        Assert.assertEquals(result[0].name, "1")
        Assert.assertEquals(result[1].name, "2")
    }

    @Test
    fun getRepositoriesRequestEmptyTest() {
        val testSubscriber = TestSubscriber<List<Repository>>()
        whenever(repositoryMock.requestRepository("kirimin", 1)).then { Observable.just(listOf<Repository>()) }
        presenter.getRepositoriesRequest("kirimin").subscribe(testSubscriber)
        val result = testSubscriber.onNextEvents[0]
        verify(repositoryMock, times(1)).requestRepository("kirimin", 1)
        verify(repositoryMock, never()).requestRepository("kirimin", 2)
        Assert.assertEquals(result.size, 0)
    }
}

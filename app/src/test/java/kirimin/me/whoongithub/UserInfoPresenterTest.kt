package kirimin.me.whoongithub

import android.view.View
import com.nhaarman.mockito_kotlin.*
import kirimin.me.whoongithub._common.network.entities.Repository
import kirimin.me.whoongithub._common.network.entities.User
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
        Assert.assertEquals(presenter.layoutVisibility.get(), View.VISIBLE)
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
        Assert.assertEquals(presenter.layoutVisibility.get(), View.VISIBLE)
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
        Assert.assertEquals(presenter.layoutVisibility.get(), View.VISIBLE)
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
        Assert.assertEquals(presenter.layoutVisibility.get(), View.VISIBLE)
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

package com.cartenz.kotlinapp.feature.login

import android.content.Context
import com.cartenz.kotlin_core.CartenzBaseSubscriber
import com.cartenz.kotlin_core.api.MySubscriber
import com.cartenz.kotlinapp.api.dao.LoginDao
import com.cartenz.kotlinapp.api.repository.LoginRepository
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

internal class LoginPresenter : CartenzBaseSubscriber(), LoginContract.PresenterInterfaceCartenz {

    private var mLoginView: LoginContract.ViewCartenz? = null
    private var context: Context? = null

    override fun setView(context: Context, view: LoginContract.ViewCartenz) {
        this.context = context
        this.mLoginView = view
    }

    override fun callLogin(username: String, password: String) {
        val repo = LoginRepository(username, password)
        addSubscription(repo.post().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<LoginDao>() {
                    override fun onComplete() {

                    }

                    override fun onError(message: String?, errorMessage: String?) {
                        mLoginView!!.loginResult(errorMessage!!)
                    }

                    override fun onSuccess(loginDao: LoginDao) {
                        mLoginView!!.loginResult("")
                    }

                }))
    }

    override fun dropView() {
        finishSubscriber()
        mLoginView = null
    }

}

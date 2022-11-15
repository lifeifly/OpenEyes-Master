package com.example.module_more.privateletter

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_more.R
import com.example.module_more.databinding.FragmentInteractBinding
import com.example.module_more.interact.InteractFragment

/**
 *description : <p>
 *通知-私信
 *</p>
 *
 *@author : flyli
 *@since :2021/6/2 16
 */
class PrivateLetterFragment:MvvmBaseFragment<FragmentInteractBinding,IMvvmBaseVM<IBaseView>>() {
    override val vm: IMvvmBaseVM<IBaseView>
        get() = object : IMvvmBaseVM<IBaseView>{
            override fun attachUi(view: IBaseView) {

            }

            override fun getView(): IBaseView? {
                return null
            }

            override fun isAttachUi(): Boolean {
                return false
            }

            override fun detachUi() {

            }
        }

    override fun loadDataOnce() {

    }

    override fun initView(rootView: View) {
        viewDataBinding.tvLogin.setOnClickListener {
            ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
        }
    }

    override fun getVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_interact
    }

    override fun onRetryBtnClick() {

    }

    companion object{
        fun newInstance(): PrivateLetterFragment {
            return PrivateLetterFragment()
        }
    }
}
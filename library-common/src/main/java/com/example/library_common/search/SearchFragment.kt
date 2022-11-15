package com.example.library_common.search

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_common.R
import com.example.library_common.databinding.FragmentSearchBinding
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.SearchViewModel
import com.example.librery_base.router.RouterFragmentPath
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.extension.visibleAlphaAnimation
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.global.setDrawable
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 11
 */
@Route(path = RouterFragmentPath.Search.PAGER_SEARCH)
class SearchFragment : MvvmBaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override val vm: SearchViewModel
        get() = ViewModelProvider(
            this,
            InjectViewModel.getSearchHotViewModelFactory()
        ).get(SearchViewModel::class.java)




    private lateinit var adapter: SearchRvAdapter

    override fun initView(rootView: View) {
        viewDataBinding.llSearch.visibleAlphaAnimation(500)
        val drawable = requireContext().getDrawable(R.drawable.ic_search_gray_17dp)

        viewDataBinding.etQuery.setDrawable(drawable, 14F, 14F)

        viewDataBinding.etQuery.setOnEditorActionListener(EditorActionListener())
        viewDataBinding.tvCancel.setOnClickListener { v ->
            hideSoftKeyBoard()
            removeFragment(requireActivity(), this)
        }

        val layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.searchRv.layoutManager = layoutManager
        adapter = SearchRvAdapter(vm.dataList)
        viewDataBinding.searchRv.adapter = adapter
        vm.onRefresh()
        observe()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_vertical_in)
        } else {
            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_vertical_out)
        }
    }

    private fun observe() {
        vm.dataListLiveData.observe(this, Observer {
            viewDataBinding.etQuery.showKeyBoard()
            val response = it.getOrNull()
            if (response == null) {
                it.exceptionOrNull()?.printStackTrace()
                return@Observer
            }
            if (response.isNullOrEmpty() && vm.dataList.isEmpty()) {
                return@Observer
            }
            if (response.isNullOrEmpty() && vm.dataList.isNotEmpty()) {
                return@Observer
            }
            vm.dataList.clear()
            vm.dataList.addAll(response)
            adapter.notifyDataSetChanged()
        })
    }


    /**
     * 关闭软键盘
     */
    private fun hideSoftKeyBoard() {
        activity?.currentFocus?.run {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 拉起软键盘
     * @return Int
     */
    private fun View.showKeyBoard() {
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.requestFocus()
        val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(this, 0)
    }

    override fun getVariable(): Int {
        return 0
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun onRetryBtnClick() {

    }


    inner class EditorActionListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (viewDataBinding.etQuery.text.toString().isEmpty()) {
                    ToastUtils.show(
                        R.string.input_keywords_tips
                    )
                    return false
                }
                ToastUtils.show(
                    R.string.currently_not_supported
                )
                return true
            }
            return true
        }
    }

    companion object {
        /**
         * 切换Fragment，会加入回退栈。
         */
        fun switchFragment(activity: FragmentActivity) {
            activity.supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SearchFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        /**
         * 先移除Fragment，并将Fragment从堆栈弹出。
         */
        fun removeFragment(activity: FragmentActivity, fragment: Fragment) {
            activity.supportFragmentManager.run {
                beginTransaction().remove(fragment).commitAllowingStateLoss()
                popBackStack()
            }
        }
    }

    /**
     * shouc
     */
    override fun loadDataOnce() {

    }




}
package com.example.librery_base.share

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.librery_base.R
import com.example.librery_base.databinding.BaseFragmentShareBinding
import com.example.librery_base.global.setDrawable
import com.example.librery_base.global.share
import com.example.librery_base.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/24 14
 */
class ShareDialogFragment : BottomSheetDialogFragment() {
    private var _binding: BaseFragmentShareBinding? = null
    private val binding: BaseFragmentShareBinding
        get() = _binding!!

    private lateinit var shareContent:String
    private lateinit var attachedActivity:Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= BaseFragmentShareBinding.inflate(layoutInflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { act ->
            attachedActivity = act
            binding.tvToWechatFriends.setDrawable(ContextCompat.getDrawable(act, R.drawable.ic_share_wechat_black_30dp), 30f, 30f, 1)
            binding.tvShareToWeibo.setDrawable(ContextCompat.getDrawable(act, R.drawable.ic_share_weibo_black_30dp), 30f, 30f, 1)
            binding.tvShareToQQ.setDrawable(ContextCompat.getDrawable(act, R.drawable.ic_share_qq_black_30dp), 30f, 30f, 1)
            binding.tvShareToQQzone.setDrawable(ContextCompat.getDrawable(act, R.drawable.ic_share_qq_zone_black_30dp), 30f, 30f, 1)

            binding.tvShareToQQ.setOnClickListener {
                share(
                    attachedActivity, shareContent,
                    SHARE_QQ
                )
                dismiss()
            }
            binding.tvToWechatFriends.setOnClickListener {
                share(
                    attachedActivity, shareContent,
                    SHARE_WECHAT
                )
                dismiss()
            }
            binding.tvShareToWeibo.setOnClickListener {
                share(
                    attachedActivity, shareContent,
                    SHARE_WEIBO
                )
                dismiss()
            }
            binding.tvShareToQQzone.setOnClickListener {
                share(
                    attachedActivity, shareContent,
                    SHARE_QQZONE
                )
                dismiss()
            }
            binding.llMore.setOnClickListener {
                share(
                    attachedActivity, shareContent,
                    SHARE_MORE
                )
                dismiss()
            }
            binding.tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun showDialog(activity: AppCompatActivity, shareContent: String) {
        show(activity.supportFragmentManager, "share_dialog")
        this.shareContent = shareContent
    }
}
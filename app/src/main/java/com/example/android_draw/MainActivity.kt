package com.example.android_draw

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks

/**
 *
 */
class MainActivity : Activity(),PermissionCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //提交账号wangfei44@lenovo.com


        //横屏设置
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //image.showImage(this,applicationInfo,CROP_IMAGE_PATH)
        //corp_view.showImage(CROP_IMAGE_PATH)
        //corp_view.getClipRectImage()
        //gridFramgent
        //view加载动画开始
        //loadView.start()Recover

    }

//luhenchang_branch   1.第一次提交



















//    fun rotate90(view: View?) {
//        image.rotate90()
//    }
//    /**
//     * 通过第三方插件easyPermissions来管理权限问题
//     */
//    open fun requireSomePermission() {
//        EasyPermissions.requestPermissions(
//            this,
//            "申请权限",
//            0,
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//        )
//    }
//    fun cut(view: View?) {
//        image1.setImageBitmap(image.clipRectImage)
//    }
//
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //image.showImage(this,applicationInfo,CROP_IMAGE_PATH)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }
}
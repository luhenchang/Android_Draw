package com.example.android_draw

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks

class MainActivity : Activity(), PermissionCallbacks{
    private val CROP_IMAGE_PATH = "/storage/emulated/0/PictureSelector_ 20200520_ 152627.JPEG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requireSomePermission()


    }
    fun rotate90(view: View?) {
        image.rotate90()
    }
    /**
     * 通过第三方插件easyPermissions来管理权限问题
     */
    open fun requireSomePermission() {
        EasyPermissions.requestPermissions(
            this,
            "申请权限",
            0,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }
    fun cut(view: View?) {
        image1.setImageBitmap(image.clipRectImage)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        image.showImage(this,applicationInfo,CROP_IMAGE_PATH)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }
}
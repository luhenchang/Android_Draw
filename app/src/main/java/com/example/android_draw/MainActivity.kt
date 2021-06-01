package com.example.android_draw
import android.app.Activity
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import com.example.android_draw.view.AnimBar
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import java.util.*


/**
 *
 */
class MainActivity : Activity(),PermissionCallbacks {
    private var staring: Boolean = true
    private var playing: Boolean=false
    private lateinit var  animationDrawable:AnimationDrawable
    private var mPopVoiceLineView: AnimBar? = null
    private var  mTimer:Timer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //提交账号wangfei44@lenovo.com
        animalImge.setImageResource(R.drawable.alarm_clock_animal_list)
        animationDrawable = animalImge.drawable as AnimationDrawable
        animationDrawable.start()
        animationDrawable.isOneShot
        //横屏设置
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //image.showImage(this,applicationInfo,CROP_IMAGE_PATH)
        //corp_view.showImage(CROP_IMAGE_PATH)
        //corp_view.getClipRectImage()
        //gridFramgent
        //view加载动画开始
        //loadView.start()Recover
        mPopVoiceLineView = findViewById(R.id.popVoiceLineView)
        start.setOnClickListener {
          if (staring) {
              mTimer = Timer()
              mTimer?.schedule(object : TimerTask() {
                  override fun run() {
                      runOnUiThread {
                          mPopVoiceLineView?.startVoice(Random().nextInt(17000 / 200))
                      }
                  }
              }, 100)
              staring=false
          }else{
              mPopVoiceLineView?.stopVoice()
              staring=true
              mTimer?.cancel()
              mTimer=null
          }
        }
        stop.setOnClickListener {
            mPopVoiceLineView?.stopVoice()
        }
        reset.setOnClickListener {
            if (playing) {
                mPopVoiceLineView?.starSimple(true)
                playing = false
            }else{
                mPopVoiceLineView?.stopSimple(false)
                playing = true

            }
        }






    }
    override fun onStop() {
        super.onStop()
        if (animationDrawable.isRunning&&this::animationDrawable.isInitialized){
            animationDrawable.stop()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (animationDrawable.isRunning&&this::animationDrawable.isInitialized){
          animationDrawable.stop()
        }
    }
    override fun onResume() {
        super.onResume()
        if (this::animationDrawable.isInitialized&&!animationDrawable.isRunning){
            animationDrawable.start()
        }
    }
//luhenchang_branch   1.第一次提交

//luhenchang_branch   1.第二次提交

//luhenchang_branch   1.第三次提交

//luhenchang_branch   1.最后一次提交




















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
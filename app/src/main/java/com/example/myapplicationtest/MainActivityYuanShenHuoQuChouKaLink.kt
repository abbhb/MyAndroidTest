package com.example.myapplicationtest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationtest.dto.ChouKaObj
import com.example.utils.GoToApi.getWechatApi
import com.example.utils.HttpUtilForYuanShenCKLink
import com.example.utils.HttpUtilForYuanShenCKLink.FORQUMOHECHOUKAFENXI
import com.example.utils.HttpUtilForYuanShenCKLink.FORTIWATEXIAOZHUSHOU
import java.util.*


class MainActivityYuanShenHuoQuChouKaLink : AppCompatActivity(),
    AdapterView.OnItemSelectedListener {
    var userchoose:Int = 0
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_yuan_shen_huo_qu_chou_ka_link)

        val myWebView: WebView = findViewById(R.id.webidsforys)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true

        myWebView.loadUrl("https://user.mihoyo.com")
        val handle: Handler = object : Handler(Looper.getMainLooper()) {
            @SuppressLint("HandlerLeak")
            override fun handleMessage(msg: Message) {
                //正常操作
                msg.let {
                    val obj: ChouKaObj = msg.obj as ChouKaObj
                    if (obj.code == 200) {
                        val editText = findViewById<EditText>(R.id.input)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivityYuanShenHuoQuChouKaLink)

                        if (obj.urlListObj.size > 1) {
                            val array = Array(obj.urlListObj.size) { "" }
                            for ((index, value) in obj.urlListObj.withIndex()) {
                                array[index] = value.uid
                            }
                            builder.setIcon(R.drawable.ic_launcher_foreground)
                                .setTitle("选择你想要查询的账号")
                            builder.setItems(
                                array
                            ) { _, which ->
                                for (listUrl in obj.urlListObj) {
                                    if (listUrl.uid == array[which]) {
                                        editText.setText(listUrl.url)
                                        val cm =
                                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        //clipData中的this就是需要复制的文本
                                        val clipData = ClipData.newPlainText("", listUrl.url)
                                        cm.setPrimaryClip(clipData)
                                        Toast.makeText(
                                            this@MainActivityYuanShenHuoQuChouKaLink,
                                            "已复制到剪贴板",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val cache = CommUtil.getInstance().getSharedPreferences(this@MainActivityYuanShenHuoQuChouKaLink)
                                        val edit: SharedPreferences.Editor = cache.edit()
                                        edit.putString("content", listUrl.url)
                                        val time = Date().time/1000
                                        edit.putLong("lastdate",time)
                                        edit.commit()
                                    }
                                }
                                Toast.makeText(
                                    applicationContext,
                                    "你选择了Uid:" + array[which],
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val alert = builder.create()
                            alert.show()
                        } else {
                            editText.setText(obj.urlListObj[0].url)
                            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            //clipData中的this就是需要复制的文本
                            val clipData = ClipData.newPlainText("", obj.urlListObj[0].url)
                            cm.setPrimaryClip(clipData)

                            val builders = AlertDialog.Builder(this@MainActivityYuanShenHuoQuChouKaLink)
                            builders.setTitle("复制成功")
                            if (userchoose == FORQUMOHECHOUKAFENXI){
                                builders.setMessage("需要我帮你跳转抽卡分析？")
                                builders.setPositiveButton("需要") { dialog, which ->
                                    Toast.makeText(this@MainActivityYuanShenHuoQuChouKaLink, "抽卡分析地址已复制了哦!", Toast.LENGTH_SHORT).show()
                                    var intent = Intent()
                                    // 读取缓存
                                    val cache = CommUtil.getInstance().getSharedPreferences(this@MainActivityYuanShenHuoQuChouKaLink)
                                    var editoraaa:SharedPreferences.Editor = cache.edit()
                                    val time = Date().time/1000
                                    editoraaa.putLong("lastdate",time)
                                    editoraaa.putString("content", obj.urlListObj[0].url)
                                    editoraaa.commit()
                                    intent.setClass(this@MainActivityYuanShenHuoQuChouKaLink,MainActivityForCKFX::class.java)
                                    startActivity(intent)

                                }
                                builders.setNegativeButton("不需要") { dialog, which ->
                                    Toast.makeText(this@MainActivityYuanShenHuoQuChouKaLink, "不需要我,就算了吧~", Toast.LENGTH_SHORT).show()
                                }
                                val alert = builders.create()
                                alert.show()
                            }
                            else if (userchoose == FORTIWATEXIAOZHUSHOU){
                                builders.setMessage("需要我帮你跳转微信吗？")
                                builders.setPositiveButton("需要") { dialog, which ->
                                    Toast.makeText(this@MainActivityYuanShenHuoQuChouKaLink, "抽卡分析地址已复制了哦!", Toast.LENGTH_SHORT).show()
                                    getWechatApi(baseContext)

                                }
                                builders.setNegativeButton("不需要") { dialog, which ->
                                    Toast.makeText(this@MainActivityYuanShenHuoQuChouKaLink, "不需要我,就算了吧~", Toast.LENGTH_SHORT).show()
                                }
                                val alert = builders.create()
                                alert.show()
                            }


                        }
                    } else {
                        Toast.makeText(this@MainActivityYuanShenHuoQuChouKaLink, "请先登录米游社", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        R.id.cookieBtn.onClick(this) {
            if (userchoose==0){
                Toast.makeText(this@MainActivityYuanShenHuoQuChouKaLink, "请选择使用的分析平台", Toast.LENGTH_SHORT).show()
                return@onClick
            }
            val instance = CookieManager.getInstance()
            val cookie = instance.getCookie("https://user.mihoyo.com")
            HttpUtilForYuanShenCKLink.getAuthKey(cookie, handle,userchoose)

        }
        val spinner: Spinner = findViewById(R.id.Radioforchoukaxuanze)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.cityArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        val seleteLinkPingTai: String = p0?.getItemAtPosition(p2).toString()
//        println(seleteLinkPingTai)
        val seleteLinkPingTais:Int = p2
        if(p2==0){
            //趣魔盒
            userchoose=FORQUMOHECHOUKAFENXI
        }
        else if (p2==1){
            //提瓦特
            userchoose= FORTIWATEXIAOZHUSHOU
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}



fun Int.onClick(activity: Activity, click: () -> Unit) {
    activity.findViewById<View>(this).setOnClickListener {
        click()
    }
}


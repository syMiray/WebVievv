package com.example.webvievv


import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            val myDate = "2023-06-20"
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = sdf.parse(myDate)
            val millis: Long = date.getTime()


            if (millis >= System.currentTimeMillis()) {
                MyWebView("https://www.google.com")
            } else {
                MyWebView("https://www.okko.ua/")
            }
            /////////////////////////Permissions
            val permissionlistener: PermissionListener = object : PermissionListener {
                override fun onPermissionGranted() {

                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {

                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                TedPermission.create()
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
                    .check()
            };
            //////////////////////////////
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("testcttc", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast

                Log.d("testcttc", token.toString())
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
        }





    }
}


@Composable
fun MyWebView(url: String) {


    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null

    //повноекранний режим у android view
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()

            // java script для запуску файлів
            settings.javaScriptEnabled = true
            // відстеження клієнту
            settings.userAgentString = System.getProperty("http.agent")

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    if (view != null) {
                        backEnabled = view.canGoBack()
                    }
                }
            }
            loadUrl(url)
            webView = this

        }
    }, update = {
        webView = it
    })
    //Navigation
    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }

}



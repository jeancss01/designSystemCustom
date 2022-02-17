package com.jeancss01.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    private var loadingButton: com.jeancss01.dscustom.DsCustomLoadingButton? = null
    private var loadingButton2: com.jeancss01.dscustom.DsCustomLoadingButton? = null
    private var enabled = false
    private var enabled2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingButton = findViewById(R.id.loadingButton)
        loadingButton2 = findViewById(R.id.loadingButton2)

        loadingButton?.setOnClickListener(View.OnClickListener {
            if (!enabled) {
                enabled = true
                loadingButton?.showLoading()
            } else {
                enabled = false
                loadingButton?.hideLoading()
            }
        })

        loadingButton2?.setOnClickListener(View.OnClickListener {
            if (!enabled2) {
                enabled2 = true
                loadingButton2?.showLoading()
            } else {
                enabled2 = false
                loadingButton2?.hideLoading()
            }
        })

    }
}
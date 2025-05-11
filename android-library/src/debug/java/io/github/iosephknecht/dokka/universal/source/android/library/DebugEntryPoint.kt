package io.github.iosephknecht.dokka.universal.source.android.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Debug entry point for start [DefaultActivity].
 */
class DebugEntryPoint : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(DefaultActivity.createIntent(this))
    }
}
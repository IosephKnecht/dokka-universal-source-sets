package io.github.iosephknecht.dokka.universal.source.android.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Public entry point for start [DefaultActivity].
 */
class ReleaseEntryPoint : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(DefaultActivity.createIntent(this))
    }
}
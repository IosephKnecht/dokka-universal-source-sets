package io.github.iosephknecht.dokka.universal.source.android.library

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * Default activity for launch.
 */
class DefaultActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context) = Intent(
            context,
            DefaultActivity::class.java
        )
    }
}
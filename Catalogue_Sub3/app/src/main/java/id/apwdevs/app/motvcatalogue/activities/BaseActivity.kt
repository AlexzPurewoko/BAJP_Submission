package id.apwdevs.app.motvcatalogue.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Add to implement generally all methods
 */
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {
    abstract val countIdlingResource : CountingIdlingResource
}
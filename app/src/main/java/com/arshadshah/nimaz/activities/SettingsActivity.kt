package com.arshadshah.nimaz.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.fragments.settings.MainPage

/**
 * Settings for the application.
 * @author Arshad Shah
 */
private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity :
    AppCompatActivity() , PreferenceFragmentCompat.OnPreferenceStartFragmentCallback
{


    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null)
        {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings , MainPage())
                .commit()
        }
        else
        {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0)
            {
                setTitle(R.string.title_activity_settings)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState : Bundle)
    {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG , title)
    }

    override fun onSupportNavigateUp() : Boolean
    {
        if (supportFragmentManager.popBackStackImmediate())
        {
            return true
        }
        else
        {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val navigateToHome = sharedPreferences.getBoolean("navigateToHome" , true)
            if(navigateToHome){
                val intent = Intent(this@SettingsActivity , HomeActivity::class.java)
                startActivity(intent)
                with(sharedPreferences.edit()) {
                    putBoolean("navigateToHome", false)
                    apply()
                }
            }
            finish()
        }
        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
        caller : PreferenceFragmentCompat ,
        pref : Preference
                                          ) : Boolean
    {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment =
            supportFragmentManager.fragmentFactory.instantiate(classLoader , pref.fragment!!)
                .apply {
                    arguments = args
                    setTargetFragment(caller , 0)
                }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings , fragment)
            .addToBackStack(null)
            .commit()
        title = pref.title
        return true
    }
} // class


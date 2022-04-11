package com.arshadshah.nimaz.activities.more

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.names.CustomAdapter
import com.arshadshah.nimaz.helperClasses.names.SubjectData

class NamesOfAllahActivity : AppCompatActivity() {

    private val mediaPlayer = MediaPlayer()
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_names_of_allah)
        supportActionBar?.hide()

        val backButton: ImageView = findViewById(R.id.backButton4)

        backButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            backButton.startAnimation(expandIn)
            //pop back stack to previous activity
            finish()
        }

        val list : ListView = findViewById(R.id.allahNames)
        val arrayList : ArrayList<SubjectData> = ArrayList()
        val array = resources.getStringArray(R.array.English)
        var indexNo : Int
        for (item in array)
        {
            indexNo = array.indexOf(item)
            arrayList.add(
                SubjectData(
                    englishName(indexNo) ,
                    arabicName(indexNo) ,
                    translation(indexNo) ,
                    (indexNo + 1).toString()
                )
            )
        }
        val customAdapter = CustomAdapter(this , arrayList)
        list.adapter = customAdapter

        //get the play button
        val playButton: ImageButton = findViewById(R.id.namesOfAllahAudio)
        val pauseButton : ImageButton = findViewById(R.id.pause)
        pauseButton.isVisible = false
        prepareMediaPlayer(this)
        //when play button is clicked
        playButton.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(this , R.anim.expand_in)
            playButton.startAnimation(expandIn)
            if(!mediaPlayer.isPlaying){
                //start the audio
                mediaPlayer.start()
                //change the button image to stop
                playButton.setImageResource(R.drawable.ic_square)
                pauseButton.isVisible = true
                pauseButton.setOnClickListener {
                    val expandIn : Animation =
                        AnimationUtils.loadAnimation(this , R.anim.expand_in)
                    pauseButton.startAnimation(expandIn)
                    mediaPlayer.pause()
                    playButton.setImageResource(R.drawable.ic_play)
                    pauseButton.isVisible = false
                }
            }
            else{
                mediaPlayer.pause()
                mediaPlayer.stop()
                mediaPlayer.reset()
                prepareMediaPlayer(this)
                //change the button image to stop
                playButton.setImageResource(R.drawable.ic_play)
                pauseButton.isVisible = false
            }
        }


    }

    private fun prepareMediaPlayer(context: Context){
        val myUri: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.asmaulhusna)
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(context, myUri)
            prepare()
        }
    }

    private fun englishName(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.English)
        val output = array[indexNo]
        return output
    }

    private fun arabicName(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.Arabic)
        val output = array[indexNo]
        return output
    }

    private fun translation(indexNo : Int) : String
    {
        val array = resources.getStringArray(R.array.translation)
        val output = array[indexNo]
        return output
    }
}
package com.arshadshah.nimaz.fragments

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.CustomAdapter
import com.arshadshah.nimaz.helperClasses.SubjectData


class NamesFragment : Fragment()
{

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
                             ) : View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_names , container , false)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())


        val list : ListView = root.findViewById(R.id.allahNames)
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
        val customAdapter = CustomAdapter(requireContext() , arrayList)
        list.adapter = customAdapter


        //get the play button
        val playButton: ImageButton = root.findViewById(R.id.namesOfAllahAudio)

        val isAudioPlaying = sharedPreferences.getBoolean("isAudioPlaying" , false)
        //when play button is clicked
        playButton.setOnClickListener {
            with(sharedPreferences.edit()) {
                putBoolean("isAudioPlaying" , isAudioPlaying)
                apply()
            }
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            playButton.startAnimation(expandIn)
            val mediaPlayer = MediaPlayer()
            if(isAudioPlaying){
                //start the audio
                val myUri: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.asmaulhusna)
                mediaPlayer.apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(requireContext(), myUri)
                    prepare()
                    start()
                }
                //change the button imgae to stop
                playButton.setImageResource(R.drawable.ic_square)
            }
            else{
                mediaPlayer.pause()
                mediaPlayer.stop()
                mediaPlayer.release()
                //change the button imgae to stop
                playButton.setImageResource(R.drawable.ic_play)
            }
        }

        return root
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
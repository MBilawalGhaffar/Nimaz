package com.arshadshah.nimaz.fragments

import android.app.AlertDialog
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R


/**
 * The fragment where The tally counter feature is Implemented
 * It has a Positive Counter,
 * A negative Counter,
 * A reset,
 * Display.
 * @author Arshad Shah
 * */
class Tasbeeh : Fragment()
{

    private var count = 0
    private var lapCounter = 0
    private var lapCounterOfCount = 0
    private var lapCounterOfCountRemove = 0

    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
                             ) : View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_tasbeeh , container , false)


        val vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        // variables for the counter
        val main_display : TextView = root.findViewById(R.id.Display)
        val Add_button : Button = root.findViewById(R.id.plus)
        val remove_button : Button = root.findViewById(R.id.minus)
        val reset_button : ImageButton = root.findViewById(R.id.reset)
        val Vibrate_button : ImageButton = root.findViewById(R.id.vibrate)
        val edit_button : ImageView = root.findViewById(R.id.edit_button)
        val amount : TextView = root.findViewById(R.id.amount)

        val lapnumber : TextView = root.findViewById(R.id.lapnumber)

        var shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)

        val amountSaved = sharedPreferences.getString("amount" , "33")
        val displaySaved = sharedPreferences.getString("display" , "0")
        val lapCountercount = sharedPreferences.getString("lapCounterOfCount" , "0")
        val lapCounterCountRemove = sharedPreferences.getString("lapCounterOfCountRemove" , "0")

        //initialize display
        amount.text = amountSaved
        main_display.text = displaySaved
        count = displaySaved !!.toInt()
        lapCounterOfCount = lapCountercount !!.toInt()
        lapCounterOfCountRemove = lapCounterCountRemove !!.toInt()
        lapCounter = sharedPreferences.getString("lap" , "0") !!.toInt()
        lapnumber.text = sharedPreferences.getString("lap" , "0")


        // variables for the counting
        val vibrate20 = 20L
        val vibrate60 = 60L

        edit_button.setBackgroundResource(R.drawable.editor)
        edit_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            edit_button.startAnimation(expandIn)
            if (count == 0)
            {
                if (shouldVibrate)
                {
                    vibrate(vibrate60)
                }
                // Create the object of
                // AlertDialog Builder class
                val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val tasbeehDialog = inflater.inflate(R.layout.inputdialog , null)
                val tasbeehLimit : EditText = tasbeehDialog.findViewById(R.id.tasbeehLimit)
                val submitBtn : Button = tasbeehDialog.findViewById(R.id.dialogSubmit)
                val cancelbtn : Button = tasbeehDialog.findViewById(R.id.dialogCancel)

                tasbeehLimit.setText(sharedPreferences.getString("tasbeehLimit" , "33"))

                builder.setView(tasbeehDialog)

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false)
                // Create the Alert dialog
                val alertDialog : AlertDialog = builder.create()
                // Show the Alert Dialog box
                alertDialog.show()

                submitBtn.setOnClickListener {

                    amount.text = tasbeehLimit.text.toString()

                    with(sharedPreferences.edit()) {
                        putString("amount" , amount.text as String)
                        putString("tasbeehLimit" , amount.text as String)
                        apply()
                    }

                    alertDialog.cancel()
                }

                cancelbtn.setOnClickListener {
                    alertDialog.cancel()
                }
            }
            else
            {
                Toast.makeText(
                    requireContext() ,
                    "Tasbeeh Session, Reset to Proceed" ,
                    Toast.LENGTH_SHORT
                              ).show()
            }

        }

        if (shouldVibrate)
        {
            Vibrate_button.setBackgroundResource(R.drawable.vibration)
            shouldVibrate = true
            with(sharedPreferences.edit()) {
                putBoolean("shouldvibrate" , shouldVibrate)
                apply()
            }
        }
        else
        {
            Vibrate_button.setBackgroundResource(R.drawable.cancel)
            shouldVibrate = false
            with(sharedPreferences.edit()) {
                putBoolean("shouldvibrate" , shouldVibrate)
                apply()
            }
        }


        Vibrate_button.setOnClickListener {
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            Vibrate_button.startAnimation(expandIn)

            var shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , false)
            if (! shouldVibrate)
            {
                Vibrate_button.setBackgroundResource(R.drawable.vibration)
                vibrate(vibrate60)
                shouldVibrate = true
                with(sharedPreferences.edit()) {
                    putBoolean("shouldvibrate" , shouldVibrate)
                    apply()
                }
                Toast.makeText(requireContext() , "Vibration On" , Toast.LENGTH_SHORT).show()
            }
            else
            {
                Vibrate_button.setBackgroundResource(R.drawable.cancel)
                vibrator.cancel()
                shouldVibrate = false
                with(sharedPreferences.edit()) {
                    putBoolean("shouldvibrate" , shouldVibrate)
                    apply()
                }
                Toast.makeText(requireContext() , "Vibration Off" , Toast.LENGTH_SHORT).show()
            }
        }

        // listener for the adder button
        Add_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)
            val currentAmount : Int = amount.text.toString().toInt()
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            Add_button.startAnimation(expandIn)
            count ++
            lapCounterOfCount ++
            if (shouldVibrate)
            {
                vibrate(vibrate20)
            }
            if (lapCounterOfCount == currentAmount)
            {
                lapCounterOfCount = 0
                if (shouldVibrate)
                {
                    vibrate(vibrate60)
                }
                lapCounter ++
                lapnumber.text = lapCounter.toString()
                Toast.makeText(
                    requireContext() ,
                    "Limit $currentAmount is Reached" ,
                    Toast.LENGTH_SHORT
                              ).show()
            }
            val countString = count.toString()
            main_display.text = countString

            with(sharedPreferences.edit()) {
                putString("display" , countString)
                putString("lapCounterOfCount" , lapCounterOfCount.toString())
                putString("lap" , lapCounter.toString())
                apply()
            }
        }

        // listener for the remove button
        remove_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)
            val currentAmount : Int = amount.text.toString().toInt()
            val expandIn : Animation =
                AnimationUtils.loadAnimation(requireContext() , R.anim.expand_in)
            remove_button.startAnimation(expandIn)
            if (count > 0)
            {
                count --
                lapCounterOfCountRemove ++
                if (shouldVibrate)
                {
                    vibrate(vibrate20)
                }
                if (lapCounterOfCountRemove == currentAmount || lapCounterOfCountRemove == 0)
                {
                    lapCounterOfCountRemove = 0
                    if (shouldVibrate)
                    {
                        vibrate(vibrate60)
                    }
                    if (lapCounter > 0)
                    {
                        lapCounter --
                    }
                    lapnumber.text = lapCounter.toString()
                    Toast.makeText(requireContext() , "1 Lap removed" , Toast.LENGTH_SHORT).show()
                }
                val countString = count.toString()
                main_display.text = countString

                with(sharedPreferences.edit()) {
                    putString("display" , countString)
                    putString("lapCounterOfCountRemove" , lapCounterOfCountRemove.toString())
                    putString("lap" , lapCounter.toString())
                    apply()
                }
            }
        }
        reset_button.setBackgroundResource(R.drawable.resetblack)
        // reset button listener
        reset_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)

            if (shouldVibrate)
            {
                vibrate(vibrate60)
            }

            val rotateAnimation =
                RotateAnimation(
                    360F ,
                    0F ,
                    Animation.RELATIVE_TO_SELF ,
                    0.5f ,
                    Animation.RELATIVE_TO_SELF ,
                    0.5f
                               )
            rotateAnimation.duration = 500
            rotateAnimation.fillAfter = true
            reset_button.startAnimation(rotateAnimation)
            if (count != 0)
            {
                // Create the object of
                // AlertDialog Builder class
                val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val resetDialog = inflater.inflate(R.layout.tasbeehreset , null)
                val dialogyes : Button = resetDialog.findViewById(R.id.dialogYes)
                val dialogNo : Button = resetDialog.findViewById(R.id.dialogNo)

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false)

                builder.setView(resetDialog)

                val alertDialog : AlertDialog = builder.create()
                alertDialog.show()

                dialogyes.setOnClickListener {
                    count = 0
                    main_display.text = count.toString()
                    lapCounter = 0
                    lapnumber.text = "0"
                    lapCounterOfCount = 0
                    lapCounterOfCountRemove = 0
                    with(sharedPreferences.edit()) {
                        putString("display" , count.toString())
                        putString("lap" , "0")
                        apply()
                    }
                    alertDialog.cancel()
                }

                dialogNo.setOnClickListener {
                    alertDialog.cancel()
                }
            }
        }

        return root
    }

    private fun vibrate(amount : Long)
    {
        val vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            vibrator.vibrate(
                VibrationEffect.createOneShot(amount , VibrationEffect.DEFAULT_AMPLITUDE)
                            )
        }
        else
        {
            vibrator.vibrate(amount)
        }
    }
}

package com.arshadshah.nimaz.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.DikhrListActivity
import com.arshadshah.nimaz.R
import java.util.ArrayList


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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        //************************************************************************
        val dhikrListButton: Button = root.findViewById(R.id.dhikrListButton)
        dhikrListButton.setOnClickListener {
            val intent = Intent(requireContext(), DikhrListActivity::class.java)
            startActivity(intent)
        }

        val dikhrCard: CardView = root.findViewById(R.id.dikhrCard)

        val tasbeehEnglishValue = sharedPreferences.getString("tasbeehEnglish" , null)
        val tasbeehArabicValue = sharedPreferences.getString("tasbeehArabic" , null)
        val tasbeehTranslationValue = sharedPreferences.getString("tasbeehTranslation" , null)

        if(tasbeehEnglishValue == null || tasbeehArabicValue == null || tasbeehTranslationValue == null){
           //replace the current fragment layout with
            val fragment = TasbeehFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment , fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        else{
            dikhrCard.isVisible = true
        }

        val dihkrDelete: ImageButton = root.findViewById(R.id.dihkrDelete)
        dihkrDelete.setOnClickListener {
            with(sharedPreferences.edit()) {
                putString("tasbeehEnglish" , null)
                putString("tasbeehArabic" , null)
                putString("tasbeehTranslation" ,null)
                apply()
            }
            Toast.makeText(context,"tasbeeh deleted",Toast.LENGTH_SHORT).show()
            val fragment = TasbeehFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment , fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val tasbeehEnglish : TextView = root.findViewById(R.id.tasbeehEnglish)
        val tasbeehArabic : TextView = root.findViewById(R.id.tasbeehArabic)
        val tasbeehTranslation : TextView = root.findViewById(R.id.tasbeehTranslation)

        tasbeehEnglish.text = tasbeehEnglishValue
        tasbeehArabic.text = tasbeehArabicValue
        tasbeehTranslation.text = tasbeehTranslationValue
        //****************************************************************
        val vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator

        // variables for the counter
        val main_display : TextView = root.findViewById(R.id.Display)
        val Add_button : ImageView = root.findViewById(R.id.plus)
        val remove_button : ImageView = root.findViewById(R.id.minus)
        val reset_button : ImageButton = root.findViewById(R.id.reset)
        val Vibrate_button : ImageButton = root.findViewById(R.id.vibrate)
        val edit_button : ConstraintLayout = root.findViewById(R.id.edit_button)
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

        edit_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)
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
            Vibrate_button.setImageResource(R.drawable.vibration)
            shouldVibrate = true
            with(sharedPreferences.edit()) {
                putBoolean("shouldvibrate" , shouldVibrate)
                apply()
            }
        }
        else
        {
            Vibrate_button.setImageResource(R.drawable.cancel)
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
                Vibrate_button.setImageResource(R.drawable.vibration)
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
                Vibrate_button.setImageResource(R.drawable.cancel)
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
        // reset button listener
        reset_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate" , true)

            if (shouldVibrate)
            {
                vibrate(vibrate60)
            }

            val rotateAnimation =
                RotateAnimation(
                    0F ,
                    360F ,
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

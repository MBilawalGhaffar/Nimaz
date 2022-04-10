package com.arshadshah.nimaz.fragments.tasbeeh

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.activities.tasbeeh.DikhrListActivity
import com.arshadshah.nimaz.activities.tasbeeh.TasbeehActivity
import com.arshadshah.nimaz.helperClasses.tasbeeh.TasbeehListMainAdapter
import com.arshadshah.nimaz.helperClasses.tasbeeh.TasbeehObjectMain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class TasbeehFragment : Fragment() {

    private var count = 0
    private var lapCounter = 0
    private var lapCounterOfCount = 0
    private var lapCounterOfCountRemove = 0
    private var arrayList: ArrayList<TasbeehObjectMain> = ArrayList()

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //get the data from the activity
                val data = result.data

                //get the values from the data
                val tasbeehArabic = data?.getStringExtra("tasbeehArabic")
                val tasbeehEnglish = data?.getStringExtra("tasbeehEnglish")
                val tasbeehTranslation = data?.getStringExtra("tasbeehTranslation")

                if(arrayList.isNotEmpty()){
                    arrayList = getListFromLocal("Tasbeeh")
                }

                //add the values to the array list
                arrayList.add(
                    TasbeehObjectMain(
                        tasbeehArabic!!,
                        tasbeehEnglish!!,
                        tasbeehTranslation!!
                    )
                )
                //save the data in the sharedPreferences
                saveListInLocal(arrayList, "Tasbeeh")
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for requireContext() fragment
        val root = inflater.inflate(R.layout.fragment_tasbeeh, container, false)



        val backButton: ImageView = root.findViewById(R.id.backButton3)

        backButton.setOnClickListener {
            val expandIn: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.expand_in)
            backButton.startAnimation(expandIn)
            //pop back stack to previous activity
            TasbeehActivity().finish()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        //************************************************************************
        val dhikrListButton: LinearLayout = root.findViewById(R.id.dhikrListButton)
        dhikrListButton.setOnClickListener {
            val intent = Intent(requireContext(), DikhrListActivity::class.java)
            startForResult.launch(intent)
        }

        //create a listView to show the dhikr
        val dhikrListView: ListView = root.findViewById(R.id.dhikrListView)
        //a callback function given to the adapter which removes the item from the list
        //when the delete is pressed
        val removeItemFromList = { position: Int ->
            //read the data from the sharedPreferences
            val arrayListFromLocal = getListFromLocal("Tasbeeh")

            //remove the item from the list
            arrayListFromLocal.removeAt(position)

            //update the list
            //save the data in the sharedPreferences
            saveListInLocal(arrayListFromLocal, "Tasbeeh")

            //reload fragment
            val fragment = TasbeehFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView2, fragment)
            fragmentTransaction.commit()
        }


        //create a flag to determine if you are back from the list of dhikr
        val isFromDhikrList = sharedPreferences.getBoolean("isFromDhikrList" , false)

        //read the data from the sharedPreferences
        arrayList = getListFromLocal("Tasbeeh")

        dhikrListView.isVisible = arrayList.size > 0

        if(isFromDhikrList || arrayList.size > 0){

            val TasbeehListCustomAdapter = TasbeehListMainAdapter(
                requireContext(),
                arrayList,
                removeItemFromList
            )
            dhikrListView.adapter = TasbeehListCustomAdapter

            //reload fragment
            val fragment = TasbeehFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView2, fragment)
            fragmentTransaction.commit()

            with(sharedPreferences.edit()) {
                putBoolean("isFromDhikrList" , false)
                apply()
            }
        }


        //****************************************************************
        val vibrator = requireContext().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator

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
        val vibrator = requireContext().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator

        vibrator.vibrate(
            VibrationEffect.createOneShot(amount , VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }

    private fun saveListInLocal(list: ArrayList<TasbeehObjectMain>, key: String?) {
        val prefs = requireContext().getSharedPreferences("Nimaz", AppCompatActivity.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply() // This line is IMPORTANT !!!
    }

    private fun getListFromLocal(key: String?): ArrayList<TasbeehObjectMain> {
        val prefs = requireContext().getSharedPreferences("Nimaz", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<TasbeehObjectMain?>?>() {}.type
        return gson.fromJson(json, type)
    }
}
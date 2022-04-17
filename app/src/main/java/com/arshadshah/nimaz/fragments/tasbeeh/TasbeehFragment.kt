package com.arshadshah.nimaz.fragments.tasbeeh

import android.app.AlertDialog
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R
import com.arshadshah.nimaz.helperClasses.tasbeeh.TasbeehListMainAdapter
import com.arshadshah.nimaz.helperClasses.tasbeeh.TasbeehObjectMain
import com.google.android.material.switchmaterial.SwitchMaterial


class TasbeehFragment : Fragment() {

    private var count = 0
    private var lapCounter = 0
    private var lapCounterOfCount = 0
    private var lapCounterOfCountRemove = 0
    private var arrayList: ArrayList<TasbeehObjectMain> = ArrayList()

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
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        //create a listView to show the dhikr
        val dhikrListView: ListView = root.findViewById(R.id.dhikrListView)
        //************************************************************************
        val dhikrSwitch: SwitchMaterial = root.findViewById(R.id.dhikrSwitch)
        dhikrListView.isVisible = dhikrSwitch.isChecked

        dhikrSwitch.setOnCheckedChangeListener { _, isChecked ->
            dhikrListView.isVisible = isChecked

            //change title of the switch
            if (isChecked) {
                dhikrSwitch.text = "Hide Dhikr"
            } else {
                dhikrSwitch.text = "Show Dhikr"
            }
        }

        dhikrListView.divider = null

        val array = resources.getStringArray(R.array.tasbeehTransliteration)
        var indexNo: Int
        for (item in array) {
            indexNo = array.indexOf(item)
            arrayList.add(
                TasbeehObjectMain(
                    arabic(indexNo),
                    english(indexNo),
                    translation(indexNo),
                )
            )
        }

        val TasbeehListCustomAdapter = TasbeehListMainAdapter(
            requireContext(),
            arrayList
        )
        dhikrListView.adapter = TasbeehListCustomAdapter

        //****************************************************************
        val vibrator =
            requireContext().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator

        // variables for the counter
        val main_display: TextView = root.findViewById(R.id.Display)
        val Add_button: ImageView = root.findViewById(R.id.plus)
        val remove_button: ImageView = root.findViewById(R.id.minus)
        val reset_button: ImageButton = root.findViewById(R.id.reset)
        val Vibrate_button: ImageButton = root.findViewById(R.id.vibrate)
        val edit_button: ConstraintLayout = root.findViewById(R.id.edit_button)
        val amount: TextView = root.findViewById(R.id.amount)

        val lapnumber: TextView = root.findViewById(R.id.lapnumber)

        var shouldVibrate = sharedPreferences.getBoolean("shouldvibrate", true)

        val amountSaved = sharedPreferences.getString("amount", "33")
        val displaySaved = sharedPreferences.getString("display", "0")
        val lapCountercount = sharedPreferences.getString("lapCounterOfCount", "0")
        val lapCounterCountRemove = sharedPreferences.getString("lapCounterOfCountRemove", "0")

        //initialize display
        amount.text = amountSaved
        main_display.text = displaySaved
        count = displaySaved!!.toInt()
        lapCounterOfCount = lapCountercount!!.toInt()
        lapCounterOfCountRemove = lapCounterCountRemove!!.toInt()
        lapCounter = sharedPreferences.getString("lap", "0")!!.toInt()
        lapnumber.text = sharedPreferences.getString("lap", "0")


        // variables for the counting
        val vibrate20 = 20L
        val vibrate60 = 60L

        edit_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate", true)
            if (count == 0) {
                if (shouldVibrate) {
                    vibrate(vibrate60)
                }
                // Create the object of
                // AlertDialog Builder class
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val tasbeehDialog = inflater.inflate(R.layout.inputdialog, null)
                val tasbeehLimit: EditText = tasbeehDialog.findViewById(R.id.quranSearch)
                val submitBtn: Button = tasbeehDialog.findViewById(R.id.dialogSubmit)
                val cancelbtn: Button = tasbeehDialog.findViewById(R.id.dialogCancel)

                tasbeehLimit.setText(sharedPreferences.getString("tasbeehLimit", "33"))

                //check that number is the right format and not too long
                tasbeehLimit.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if (tasbeehLimit.text.toString().length > 6) {
                            tasbeehLimit.setText(tasbeehLimit.text.toString().substring(0, 6))
                        }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                })
                builder.setView(tasbeehDialog)

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false)
                // Create the Alert dialog
                val alertDialog: AlertDialog = builder.create()
                // Show the Alert Dialog box
                alertDialog.show()

                submitBtn.setOnClickListener {

                    amount.text = tasbeehLimit.text.toString()

                    val amountToClean = amount.text

                    if (amountToClean.toString().toInt() >= 900000000) {
                        //show a toast and dont do anything
                        Toast.makeText(
                            requireContext(),
                            "Please enter a number less than 900000000",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        with(sharedPreferences.edit()) {
                            putString("amount", amount.text as String)
                            putString("tasbeehLimit", amount.text as String)
                            apply()
                        }

                        alertDialog.cancel()
                    }
                }

                cancelbtn.setOnClickListener {
                    alertDialog.cancel()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Tasbeeh Session, Reset to Proceed",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        if (shouldVibrate) {
            Vibrate_button.setImageResource(R.drawable.vibration)
            shouldVibrate = true
            with(sharedPreferences.edit()) {
                putBoolean("shouldvibrate", shouldVibrate)
                apply()
            }
        } else {
            Vibrate_button.setImageResource(R.drawable.cancel)
            shouldVibrate = false
            with(sharedPreferences.edit()) {
                putBoolean("shouldvibrate", shouldVibrate)
                apply()
            }
        }


        Vibrate_button.setOnClickListener {
            val expandIn: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.expand_in)
            Vibrate_button.startAnimation(expandIn)

            var shouldVibrate = sharedPreferences.getBoolean("shouldvibrate", false)
            if (!shouldVibrate) {
                Vibrate_button.setImageResource(R.drawable.vibration)
                vibrate(vibrate60)
                shouldVibrate = true
                with(sharedPreferences.edit()) {
                    putBoolean("shouldvibrate", shouldVibrate)
                    apply()
                }
                Toast.makeText(requireContext(), "Vibration On", Toast.LENGTH_SHORT).show()
            } else {
                Vibrate_button.setImageResource(R.drawable.cancel)
                vibrator.cancel()
                shouldVibrate = false
                with(sharedPreferences.edit()) {
                    putBoolean("shouldvibrate", shouldVibrate)
                    apply()
                }
                Toast.makeText(requireContext(), "Vibration Off", Toast.LENGTH_SHORT).show()
            }
        }

        // listener for the adder button
        Add_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate", true)
            val currentAmount: Int = amount.text.toString().toInt()
            val expandIn: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.expand_in)
            Add_button.startAnimation(expandIn)
            count++
            lapCounterOfCount++
            if (shouldVibrate) {
                vibrate(vibrate20)
            }
            if (lapCounterOfCount == currentAmount) {
                lapCounterOfCount = 0
                if (shouldVibrate) {
                    vibrate(vibrate60)
                }
                lapCounter++
                lapnumber.text = lapCounter.toString()
                Toast.makeText(
                    requireContext(),
                    "Limit $currentAmount is Reached",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val countString = count.toString()
            main_display.text = countString

            with(sharedPreferences.edit()) {
                putString("display", countString)
                putString("lapCounterOfCount", lapCounterOfCount.toString())
                putString("lap", lapCounter.toString())
                apply()
            }
        }

        // listener for the remove button
        remove_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate", true)
            val currentAmount: Int = amount.text.toString().toInt()
            val expandIn: Animation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.expand_in)
            remove_button.startAnimation(expandIn)
            if (count > 0) {
                count--
                lapCounterOfCountRemove++
                if (shouldVibrate) {
                    vibrate(vibrate20)
                }
                if (lapCounterOfCountRemove == currentAmount || lapCounterOfCountRemove == 0) {
                    lapCounterOfCountRemove = 0
                    if (shouldVibrate) {
                        vibrate(vibrate60)
                    }
                    if (lapCounter > 0) {
                        lapCounter--
                    }
                    lapnumber.text = lapCounter.toString()
                    Toast.makeText(requireContext(), "1 Lap removed", Toast.LENGTH_SHORT).show()
                }
                val countString = count.toString()
                main_display.text = countString

                with(sharedPreferences.edit()) {
                    putString("display", countString)
                    putString("lapCounterOfCountRemove", lapCounterOfCountRemove.toString())
                    putString("lap", lapCounter.toString())
                    apply()
                }
            }
        }
        // reset button listener
        reset_button.setOnClickListener {
            val shouldVibrate = sharedPreferences.getBoolean("shouldvibrate", true)

            if (shouldVibrate) {
                vibrate(vibrate60)
            }

            val rotateAnimation =
                RotateAnimation(
                    0F,
                    360F,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
            rotateAnimation.duration = 500
            rotateAnimation.fillAfter = true
            reset_button.startAnimation(rotateAnimation)
            if (count != 0) {
                // Create the object of
                // AlertDialog Builder class
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val resetDialog = inflater.inflate(R.layout.tasbeehreset, null)
                val dialogyes: Button = resetDialog.findViewById(R.id.dialogYes)
                val dialogNo: Button = resetDialog.findViewById(R.id.dialogNo)

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false)

                builder.setView(resetDialog)

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()

                dialogyes.setOnClickListener {
                    count = 0
                    main_display.text = count.toString()
                    lapCounter = 0
                    lapnumber.text = "0"
                    lapCounterOfCount = 0
                    lapCounterOfCountRemove = 0
                    with(sharedPreferences.edit()) {
                        putString("display", count.toString())
                        putString("lap", "0")
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


    private fun vibrate(amount: Long) {
        val vibrator =
            requireContext().getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator

        vibrator.vibrate(
            VibrationEffect.createOneShot(amount, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }

    private fun english(indexNo: Int): String {
        val array = resources.getStringArray(R.array.tasbeehTransliteration)
        return array[indexNo]
    }

    private fun arabic(indexNo: Int): String {
        val array = resources.getStringArray(R.array.tasbeeharabic)
        return array[indexNo]
    }

    private fun translation(indexNo: Int): String {
        val array = resources.getStringArray(R.array.tasbeehTranslation)
        return array[indexNo]
    }
}
package ca.temi.codingchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashSet
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val Any.TAG: String get() = this::class.java.simpleName

    //Total number of light bulbs
    var totalBulbs: Int = 0

    // How many colors in bucket (numberOfColors)
    var numberOfColors: Int = 0

    // Each colors with number of bulbs (quantityOfEachColor)
    var quantityOfEachColor: Int = 0

    // Randomly pick light bulbs (randomlyPick)
    var randomlyPick: Int = 0

    // number of time run simulation (timesToRunSimulations)
    var numberOfTimeSimulation: Int = 0

    private val et_numberOfColors: EditText
        get() = findViewById(R.id.numberOfColors)

    private val et_quantityOfEachColor: EditText
        get() = findViewById(R.id.quantityOfEachColor)

    private val et_randomlyPick: EditText
        get() = findViewById(R.id.randomlyPick)

    private val et_timesToRunSimulations: EditText
        get() = findViewById(R.id.timesToRunSimulations)

    private val tv_error: TextView
        get() = findViewById(R.id.error)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


    fun startSimulation(view: View) {

        if (et_numberOfColors.text.toString().trim()
                .isEmpty() || Integer.parseInt(et_numberOfColors.text.toString()) <= 1
        ) {

            setError("Please enter valid Number of colors")

        } else if (et_quantityOfEachColor.text.toString().trim().isEmpty() || Integer.parseInt(
                et_quantityOfEachColor.text.toString()
            ) <= 1
        ) {
            setError("Please enter valid Quantity Of Each Color")


        } else if (et_randomlyPick.text.toString().trim().isEmpty() || Integer.parseInt(
                et_randomlyPick.text.toString()
            ) <= 1
        ) {
            setError("Please enter valid Randomly pick bulb")

        } else {
            tv_error.visibility = View.INVISIBLE

            numberOfColors = et_numberOfColors.text.toString().toInt()
            quantityOfEachColor = et_quantityOfEachColor.text.toString().toInt()
            randomlyPick = et_randomlyPick.text.toString().toInt()
            numberOfTimeSimulation = et_timesToRunSimulations.text.toString().toInt()

            totalBulbs = numberOfColors * quantityOfEachColor
            Log.d(TAG, "totalBulbs: $totalBulbs")

            var sum: Int = 0

            for (i in 1..numberOfTimeSimulation) {
                Log.d(TAG, "startSimulation: $i")
                sum += uniqueColor(
                    totalBulbs,
                    numberOfColors,
                    quantityOfEachColor,
                    randomlyPick
                )
            }

            val average: Float = (sum / numberOfColors).toFloat()
            //Log.d(TAG, "Simulation Average - $average")

            setError("Average: $average")
        }

    }

    /**
     * Find unique color from randomly pick bulbs
     */
    private fun uniqueColor(
        totalBulbs: Int,
        numColors: Int,
        numBallsOfColors: Int,
        draws: Int
    ): Int {
        val map: TreeMap<Int, Int> = TreeMap()
        var j: Int = 1

        // Create dataset
        for (i in 1..totalBulbs) {
            map[i] = j //0(totalBulbs*log(totalBulbs)) time complexity
            if (i % numBallsOfColors == 0) {
                j++
            }
        }

        //Log.d(TAG, "initial map: $map")

        val pickedColors: MutableSet<Int> = HashSet()

        for (i in 0..draws) {
            val pickedBallIndex: Int = Random.nextInt(totalBulbs - i + 1)
            //Log.d(TAG, "Index Picked: $pickedBallIndex")
            val key: Int =
                map.ceilingKey(pickedBallIndex) as Int  // o(draws * log(totalBulbs)) time complexity
            //Log.d(TAG, "key Picked: $key")
            val colorPicked: Int? = map[key]  // o(draws * log(totalBulbs)) time complexity
            //Log.d(TAG, "color picked: $colorPicked")

            if (colorPicked != null) {
                pickedColors.add(colorPicked)
            }

            map.remove(key) // o(draws * log(totalBulbs)) time complexity
            //Log.d(TAG, "map after iteration i = $i : $map")

        }
        /* //Check unique colors set
         for (element in pickedColors){
             Log.d(TAG, "pickColors set : $element")
         }*/

        return pickedColors.size
    } // overall time complexity max(o(totalBulbs * log(totalBulbs))), o(draws * log(totalBulbs)))
    // Bog O notation = O(NlogN) where N = totalNumber of bulbs

    private fun setError(message: String) {
        showHide(tv_error)
        tv_error.text = message
    }


    private fun showHide(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    private fun roundOffDecimal(number: Float): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }

}
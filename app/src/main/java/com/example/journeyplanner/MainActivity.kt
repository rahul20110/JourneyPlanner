package com.example.journeyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.journeyplanner.ui.theme.JourneyPlannerTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JourneyPlannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JourneyAppUI()
                }
            }
        }
    }
}
data class TravelStop(val stopName: String, var distPrev: Float, var add_distance: Float, var distRemaining: Float)

@Composable
fun JourneyAppUI() {

    var add_distance by remember { mutableStateOf(0f) }
    var distanceWalked by remember { mutableStateOf(0f) }
    val travelStops = remember { mutableStateListOf<TravelStop>() }
    var unit by remember { mutableStateOf("km") }

    
    val p_bar = remember { mutableStateOf(0f) }

    fun conversion_function(dist: Float, convert_to: String): Float {
        if (convert_to == "miles") {
            return (dist * 0.621371f)
        } else {
            return (dist / 0.621371f)
        }
    }
    fun updating_value() {
        val total_dist_walked = travelStops.sumOf { it.distPrev.toInt() }
        p_bar.value = if (add_distance > 0f) {
            (total_dist_walked / add_distance).coerceIn(0f, 1f) * 100f
        } else {
            0f
        }
    }
    fun updating_stops_p_value() {
        var so_far_distance = 0f
        travelStops.forEach { stop ->
            so_far_distance += stop.distPrev
            stop.add_distance = so_far_distance
            stop.distRemaining = add_distance - so_far_distance
        }
        updating_value()
    }

    fun change_currentunit() {
        unit = if (unit == "km") "miles" else "km"
        travelStops.forEach { stop ->

            stop.distPrev = conversion_function(stop.distPrev, unit)
            stop.add_distance = conversion_function(stop.add_distance, unit)
            stop.distRemaining = conversion_function(stop.distRemaining, unit)
        }
        updating_stops_p_value()
    }

    fun check_dist_inp(variable: String): Float? {
        val floatValue = variable.toFloatOrNull()
        if (floatValue != null && floatValue >= 0f) {
            return floatValue
        } else {
            return null
        }
    }

    Column(modifier = Modifier.padding(11.dp)) {
        Column(modifier = Modifier.padding(11.dp)) {
            Text(text = "Journey Planner", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(17.dp))
            Row {
                OutlinedTextField(
                    value = if (add_distance == 0f) "" else (add_distance.toString()),
                    onValueChange = { it ->
                        add_distance = check_dist_inp(it) ?: 0f
                        travelStops.forEach { it.distRemaining = add_distance - it.add_distance }
                        updating_value()
                    },
                    label = { Text("Total Distance ($unit)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(17.dp))
            }
            Spacer(modifier = Modifier.height(17.dp))
            Row {
                OutlinedTextField(
                    value = if (distanceWalked == 0f) "" else distanceWalked.toString(),
                    onValueChange = { it ->
                        distanceWalked = check_dist_inp(it) ?: 0f
                        updating_value()
                    },
                    label = { Text("Distance Walked ($unit)") },
                    modifier = Modifier.fillMaxWidth()

                )
            }

            Spacer(modifier = Modifier.height(17.dp))
            Row(

            ) {
                Button(onClick = {
                    val newDistance =
                        check_dist_inp(distanceWalked.toString()) ?: return@Button
                    if (newDistance > 0) {
                        val add_distanceWalked =
                            travelStops.sumOf { it.distPrev.toDouble() } + newDistance
                        if (add_distanceWalked <= add_distance) {
                            travelStops.add(TravelStop("Travel Stop ${travelStops.size + 1}", newDistance, 0f, 0f))
                            distanceWalked = 0f
                            updating_stops_p_value()
                        } else {
                            //pass
                        }
                    } else {
                        //pass
                    }
                }
                ) {
                    Text("Add Travel Stop",color= Color.Green)
                }
                Spacer(modifier = Modifier.width(60.dp))
                Button(onClick = { change_currentunit() }
                ) {
                    var curr = if (unit == "km") "miles" else ("km")
                    Text(text = "Change to $curr",color= Color.Green)
                }

            }



                Spacer(modifier = Modifier.height(6.dp))
                Row{
                    var totalDistanceWalked=Roundoff(travelStops.sumOf { it.distPrev.toDouble()},unit)
                    Text(
                        text = "Overall Distance Covered: ${totalDistanceWalked} $unit"
                    )

                }
                Spacer(modifier = Modifier.height(6.dp))
                Row{
                    var distanceleft=Roundoff(add_distance - travelStops.sumOf { it.distPrev.toDouble()},unit)
                    Text(
                        text = "Distance Yet to Go: ${distanceleft} $unit"
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row{
                    LinearProgressIndicator(
                        progress = p_bar.value / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .width(9.dp)
                            .height(8.dp),
                        color= Color.Yellow
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row{
                    Text(
                        text = "Progress: ${p_bar.value.toInt()}%",
                        modifier = Modifier
                    )
                }


        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Travel Destinations Reached: ")

        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(travelStops) { stop ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(

                        text = "${stop.stopName}, " +
                                "Distance Travelled: ${Roundoff( stop.distPrev,unit)} $unit, " +
                                "Distance Left: ${Roundoff(stop.distRemaining,unit)} $unit",
                        modifier = Modifier.padding(horizontal = 17.dp),
                        color = Color.Red
                    )

                }
                Divider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    color = Color.Gray, // Change color as needed
                    thickness = 1.dp // Adjust thickness as needed
                )

            }

        }

    }

}


fun Roundoff(x: Double, y: String): Any {
    if(y=="miles"){
        return (x * 100).roundToInt() / 100.0
    }
    else{
        return x
    }

}

fun Roundoff(x: Float, y: String): Any {
    if(y=="miles"){
        return (x * 100).roundToInt() / 100.0
    }
    else{
        return x
    }

}



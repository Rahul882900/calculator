package com.example.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val buttonList = listOf(
    "C","(",")","/",
    "7","8","9","*",
    "4","5","6","+",
    "1","2","3","-",
    "AC","0",".","="
)

@Composable
fun Calculator(modifier: Modifier = Modifier, viewModel: CalculatorViewModel){

    val equationText = viewModel.equationText.observeAsState()
    val resultText = viewModel.resultText.observeAsState()


    Box(modifier = modifier){
        Column (
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ){
            Text(
                text = equationText.value?:"",
                style = TextStyle(
                    fontSize = 40.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis

            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = resultText.value?:"",
                style = TextStyle(
                    fontSize = 60.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 2,


                )

            Spacer(modifier = Modifier.height(30.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),

                ) {
                items(buttonList) {
                    CalculatorButton(btn = it, onClick = {
                        viewModel.onButtonClick(it)
                    })

                }
            }
        }
    }
}

@Composable
fun CalculatorButton(btn: String, onClick : ()-> Unit){
    Box(modifier = Modifier.padding(10.dp)){
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            contentColor = Color.White,
            containerColor = getColor(btn)
        ) {
            Text(text = btn, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)

        }
    }
}

fun getColor(btn: String) : Color{

    if (btn == "(" || btn == ")" || btn == "C" || btn == "AC")
        return Color.Gray
    if (btn == "/" || btn == "+" || btn == "*" || btn == "-")
        return Color(0xFFE39526)
    return Color(0xFF444242)
}
package cat.trombo.alertatrombo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import cat.trombo.alertatrombo.R
import cat.trombo.alertatrombo.domain.*
import cat.trombo.alertatrombo.events.LifeEvent
import cat.trombo.alertatrombo.viewmodels.MainScreenVM
import cat.trombo.alertatrombo.ui.theme.*


//@Preview()
@Composable
fun MainScreen(navController: NavHostController) {
    val shape = RoundedCornerShape(12.dp)

    val viewModel = MainScreenVM
    val p: Person? = viewModel.currentUser

    //viewModel.iniMisc(LocalContext.current)
    var f: List<Food>? = null
    var j: List<Job>? = null
    var h: List<Hobby>? = null

    if(viewModel.foodList != null){
        f = viewModel.foodList
    }
    if(viewModel.jobList != null){
        j = viewModel.jobList
    }
    if(viewModel.hobbyList != null){
        h = viewModel.hobbyList
    }


//    val e : LifeEvent? = viewModel.currentEvent
    val uiState by viewModel.uiState.collectAsState()

    var event: LifeEvent? = viewModel.cevent
    //println(state.value.person.height)


    Box {
        Image(
            painter = painterResource(id = R.drawable.backgroundphotofield),
            contentDescription = null,
//            modifier = Modifier.fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top box with width-fitted content
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(shape)
                    .background(color = Color.Red.copy(alpha = 0.5f))
                    .height(75.dp)/*, backgroundColor = Color.Red*/
            ) {
                CustomProgressBar()
            }

//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(20.dp))
//                    .padding(8.dp)
//                    .background(color = Color.Green)
//                    .height(150.dp)
//            )
//             Bottom box with tabs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(color = Background)
            ) {
                //TabOnlyTitle()
                //CustomTabs()
                if (p != null) {
                    tabs(p, f, j, h)
                }
            }

        }


        if (uiState.currentEvent != null) {
            Popup(
                alignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clip(shape)
                        .background(color = Color.White)
                        .height(200.dp), contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        uiState.currentEvent?.title?.let {
                            Text(
                                uiState.currentEvent!!.title,
                                color = DarkText,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                uiState.currentEvent!!.description,
                                color = DarkText,
                            )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = { viewModel.returnEventState(0) }) {

                                Text(uiState.currentEvent!!.options[0])
                            }
                            
                            if(uiState.currentEvent!!.options.size > 1) {
                                Spacer(modifier = Modifier)
                                Button(onClick = { viewModel.returnEventState(1) }) {

                                    Text(uiState.currentEvent!!.options[1])
                                }
                            }
                        }
                        }
//                        uiState.currentEvent?.let {
//                            LazyRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.Center
//                            ) {
////                            items(uiState.currentEvent!!.options.size){
////                                uiState.currentEvent!!.options.forEach{
//                                itemsIndexed(uiState.currentEvent!!.options) { index, item ->
//                                    Button(onClick = { viewModel.ReturnEventState(index) }) {
//                                        Text("option " + index)
//                                    }
//                                }
//                            }
//
//                        }
                    }
                }
            }
        }
    }
}



//@Preview()
@Composable
fun tabs(p:Person, f:List<Food>?,j: List<Job>?, h: List<Hobby>?) {
    var tabIndex by remember { mutableStateOf(0) } // 1.
    val tabTitles = listOf("Menjar", "Feina", "Oci", "Info")
    Column { // 2.
        TabRow(selectedTabIndex = tabIndex) { // 3.
            tabTitles.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index, // 4.
                    onClick = { tabIndex = index },
                     modifier = Modifier.background(color = DarkBackground),
                    text = { Text(text = title) }) // 5.
            }
        }
        when (tabIndex) { // 6.
            0 -> Box(modifier = Modifier.padding(8.dp)){
                if(f != null){
                    Column {
                        f.forEach { message ->
                            Text(message.name+" ("+ message.calories+" calories)")
                        }
                    }
                }

            }//Text("Hello content")
            1 -> Box(modifier = Modifier.padding(8.dp)){
                if(j != null){
                    Column {
                        j.forEach { message ->
                            Text(message.name+" (level of activity = "+ message.activity+"%)")
                        }
                    }
                }

            }
            2-> Box(modifier = Modifier.padding(8.dp)){
                if(h != null){
                    Column {
                        h.forEach { message ->
                            Text(message.name+" (level of sedentarism = "+ message.sedentarism+"%)")
                        }
                    }
                }

            }
            3 -> Row( modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                Column(modifier = Modifier
                    .padding(15.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly){
                    Text("Nom: "+p.name)
                    Text("Edat: "+p.age)
                    Text("Gènere: "+p.gender)
                    Text("Altura: "+p.height)
                    Text("Pes: "+p.weight)
                }
                Column(modifier = Modifier
                    .padding(15.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly){
                    Text("Gana: "+p.hunger +"/100")
                    Text("Sed: "+p.waterIntake +"/100")
                    Text("Glucosa en sang: "+p.glucose +" mg/dl")
                    Text("Colesterol en sang: " + p.cholesterol +" mg/dl")
                    Text("Nivells d'estrès: "+p.stressLevel+"0%")
                    Text("Nivells d'activitat : "+p.activityLevel+"%")

                }
            }
        }
    }
}

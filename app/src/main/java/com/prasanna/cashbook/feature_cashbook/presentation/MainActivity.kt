package com.prasanna.cashbook.feature_cashbook.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook.CashbookScreen
import com.prasanna.cashbook.feature_cashbook.presentation.cashbook_list.CashbookListScreen
import com.prasanna.cashbook.feature_cashbook.presentation.compare.CompareScreen
import com.prasanna.cashbook.feature_cashbook.presentation.util.Screen
//import com.prasanna.cashbook.ui.theme.CashbookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.prasanna.cashbook.ui.theme.AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController,
                        startDestination = Screen.CashbookListScreen.route){
                        composable(Screen.CashbookListScreen.route){
                            CashbookListScreen(navController = navController)

                        }

                        composable(route = Screen.CashbookScreen.route +
                                "?cashbookId={cashbookId}",
                            arguments = listOf(
                                navArgument(name = "cashbookId"){
                                    type = NavType.IntType
                                    defaultValue = 0
                                }
                            )
                        ){
                            navBackStackEntry ->
                            val id = navBackStackEntry.arguments?.getInt("cashbookId")
                            Log.d(TAG, "Cashbook Id: $id")
                            id?.let {
                                cashbookId ->
                                CashbookScreen(navController = navController)
                            }
                        }

                        composable(route = Screen.CompareScreen.route+
                        "?cashbookIds={cashbookIds}",
                            arguments = listOf(
                                navArgument(name="cashbookIds"){
                                    type = NavType.StringType
                                }
                            )
                        ){ navBackStackEntry ->
                            val idsString = navBackStackEntry.arguments?.getString("cashbookIds")
                            Log.d(TAG, "Compare Screen Cashbook Ids: $idsString")
                            idsString?.let {
                                CompareScreen(navController = navController)
                            }

                        }
                    }
                }
            }
        }
    }
}


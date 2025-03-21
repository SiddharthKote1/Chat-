package com.example.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun OTPScreen(
    onVerifyClick: (String) -> Unit,
    onResendClick: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LottieAnimation(
            modifier = Modifier.size(300.dp),
            composition = composition
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Enter OTP", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(6) { index ->
                OutlinedTextField(
                    value = otp.getOrNull(index)?.toString() ?: "",
                    onValueChange = { newValue ->
                        if (newValue.length == 1 && newValue.all { it.isDigit() }) {
                            otp = otp.take(index) + newValue + otp.drop(index + 1)
                            if (index < 5) focusRequesters[index + 1].requestFocus()
                        } else if (newValue.isEmpty()) {
                            otp = otp.take(index) + "" + otp.drop(index + 1)
                            if (index > 0) focusRequesters[index - 1].requestFocus()
                        }
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .focusRequester(focusRequesters[index]),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                verifyPhoneNumberWithCode(context, storedVerificationId, otp, navController)
                navController.navigate("MainScreen")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0073E6)
            ),
            shape=RoundedCornerShape(10.dp)) {
            Text("Verify OTP")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                resendOTP(context, navController, storedCountryCode, storedPhoneNumber)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0073E6)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Resend OTP")
        }
    }
}

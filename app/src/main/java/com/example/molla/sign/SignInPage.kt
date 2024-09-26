package com.example.molla.sign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.R
import com.example.molla.config.Screen

@Composable
fun SignInPage(navController: NavController, signInViewModel: SignInViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFFDF6FF))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "로그인",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "MOLLA",
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.Center),
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("아이디") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFDF6FF), RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFDF6FF), RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    // TODO: Login
//                    navController.navigate(Screen.Main.name) {
//                        popUpTo(0)
//                    }
                    signInViewModel.login(
                        email = email,
                        password = password,
                        onSuccess = {
                            navController.navigate(Screen.Main.name) {
                                popUpTo(0)
                            }
                        },
                        onError = { message ->
                            errorMessage = message
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("로그인", fontSize = 14.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = {
                    TODO("아이디 찾기") }
                ) {
                    Text(
                        text = "아이디 찾기",
                        color = Color.Black
                    )
                }
                VerticalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxHeight(0.04f)
                        .width(1.dp)
                )
                TextButton(onClick = {
                    TODO("아이디 찾기") }
                ) {
                    Text(
                        text = "비밀번호 찾기",
                        color = Color.Black
                    )
                }
                VerticalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxHeight(0.04f)
                        .width(1.dp)
                )
                TextButton(
                    onClick = {
                        navController.navigate(Screen.SignUp.name)
//                        TODO("회원 가입")
                    }
                ) {
                    Text(
                        text = "회원가입",
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Main.name) {
                        popUpTo(0)
                    }
//                    TODO("네이버 로그인")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03C75A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.login_naver),
                        contentDescription = "Naver Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("네이버 로그인", fontSize = 14.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Main.name) {
                        popUpTo(0)
                    }
//                    TODO("구글 로그인")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.login_google),
                        contentDescription = "Google Logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google로 계속하기", fontSize = 14.sp, color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPagePreview() {
    val navController = rememberNavController()
    SignInPage(navController)
}

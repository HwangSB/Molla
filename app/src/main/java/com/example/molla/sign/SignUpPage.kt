package com.example.molla.sign

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.config.Screen

@Composable
fun SignUpPage(navController: NavController, signUpViewModel: SignUpViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checkedPassword by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

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
                text = "회원가입",
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
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 72.dp)) {
                Text(text = "이름", modifier = Modifier.padding(start = 4.dp, bottom = 4.dp), fontSize = 16.sp)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("이름을 입력해주세요.") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "이메일", modifier = Modifier.padding(start = 4.dp, bottom = 4.dp), fontSize = 16.sp)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    placeholder = { Text("ID@example.com") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "비밀번호", modifier = Modifier.padding(start = 4.dp, bottom = 4.dp), fontSize = 16.sp)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text("비밀번호를 입력해주세요") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = checkedPassword,
                    onValueChange = { checkedPassword = it },
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text("비밀번호를 확인해주세요.") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    val error = signUpViewModel.validateInput(name, email, password, checkedPassword)
                    if (error == null) {
                        signUpViewModel.signUp(
                            username = name,
                            email = email,
                            password = password,
                            onSuccess = {
                                navController.navigate(Screen.Main.name) {
                                    popUpTo(0)
                                }
                            },
                            onError = { errorMessage ->
                                validationError = errorMessage
                                showErrorMessage = true
                            }
                        )
                    } else {
                        validationError = error
                        showErrorMessage = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("가입하기", fontSize = 14.sp, color = Color.White)
            }
        }

        if (showErrorMessage) {
            AlertDialog(
                onDismissRequest = { showErrorMessage = false },
                title = { Text(text = "입력 오류") },
                text = { Text(text = validationError ?: "") },
                confirmButton = {
                    TextButton(onClick = { showErrorMessage = false }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPagePreview() {
    val navController = rememberNavController()
    SignUpPage(navController)
}

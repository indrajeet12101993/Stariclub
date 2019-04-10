package app.striclub.pojo.UserEditShowInitial

data class ResponseFromServerInitialEditUser(
        val response_code: String,
        val response_message: String,
        val user_detail: MutableList<UserDetail>
)
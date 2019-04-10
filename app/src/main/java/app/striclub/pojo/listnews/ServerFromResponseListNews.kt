package app.striclub.pojo.listnews

data class ServerFromResponseListNews(
        val response_code: String,
        val response_message: String,
        val result: List<Result>
)
package app.striclub.pojo.searchsuggestion

data class ResponseFromServerSearchSuggestions(
        val response_code: String,
        val response_message: String,
        val result: MutableList<Result>
)
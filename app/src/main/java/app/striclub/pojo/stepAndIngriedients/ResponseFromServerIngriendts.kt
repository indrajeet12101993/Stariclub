package app.striclub.pojo.stepAndIngriedients

data class ResponseFromServerIngriendts(
        val response_code: String,
        val response_message: String,
        val post: List<Post>,
        val ingredients: List<Ingredient>,
        val steps: List<Step>
)
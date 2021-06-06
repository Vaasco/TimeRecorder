
data class User(val UIN:Int, val PIN:Int, val name:String, val accumulatedTime:Long, val entryTime: Long)
val usersMap = HashMap<Int, User>()

fun main(){
    App.run()
}

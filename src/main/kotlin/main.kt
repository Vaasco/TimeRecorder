
data class User(val UIN:Int, val PIN:Int, val name:String, var accumulatedTime:Long, val entryTime: Long)
val usersMap = HashMap<Int, User>()

fun main(){
    App.run()
}

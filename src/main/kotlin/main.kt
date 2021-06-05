
data class User(val UIN:Int, var PIN:Int, val name:String, val accumulatedTime:Int, val entryDate: String)
val usersMap = HashMap<Int, User>()

fun main(args: Array<String>){
    val hardCodedUser = User(0, 0, "Teodosie Cabral", 0, "0")
    usersMap[0] = hardCodedUser
    App.run()
}

import java.util.*

object Users {
    private val uinsRemovedQueue = PriorityQueue<Int>()

    data class User(val UIN: Int, val PIN: Int, val name: String, val accumulatedTime: Long, val entryTime: Long)

    private val usersMap = HashMap<Int, User>()
                        // UIN;PIN;NOME;ACCUMULATED;ENTRY
    fun User.toText(): String{ // 003;1458;Carlos;0;0
       return "$UIN;$PIN;$name;$accumulatedTime;$entryTime"
    }

    fun add(name:String, PIN: Int): User? {
        if (usersMap.size >= 1000) return null
        val uin = when{
            uinsRemovedQueue.isNotEmpty() -> uinsRemovedQueue.poll()
            usersMap.isEmpty() -> 0
            else -> usersMap.size + 1
        }
        val newUser = User(UIN = uin, PIN = PIN, name, 0L, 0L)
        usersMap[uin] = User(UIN = uin, PIN = PIN, name, 0L, 0L)
        return newUser
    }


    fun update(user: User): Boolean {
        if (usersMap[user.UIN] != null) {
            usersMap[user.UIN] = user
            return true
        }
        return false
    }

    fun remove(user: User): User? {
        val remove = usersMap.remove(user.UIN)
        if(remove != null) uinsRemovedQueue.add(user.UIN)
        return remove
    }

    private fun load(userText: String) {
        val userArray = userText.split(';') // ["0", "1256", "Vasco", "0", "0"]
        val userUin = userArray[0].toInt()
        val userPin = userArray[1].toInt()
        val userName = userArray[3].toLong()
        val userAccumulatedTime = userArray[4].toLong()
        val user = User(userUin, userPin, userArray[2], userName, userAccumulatedTime)
        usersMap[userUin] = user
    }
    // 0;1256;Vasco;0;0
    // UIN;PIN;NAME;ACCUMULATED_TIME;ENTRY_TIME

    fun loadUsers(users:List<String>){
        users.forEach { load(it) }
    }

    operator fun get(uin: Int): User? {
        return usersMap[uin]
    }

    // TODO: ACABAR UML
    //TODO: Fazer função no USERS e LOGS para usar na APP para quando o programa desligar ser escrito no FILE ACESS os USERS e LOGS
    // TODO TESTAR TUDO NO USBPORT DO LOGIZAO

}



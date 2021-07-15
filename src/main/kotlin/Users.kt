import java.util.*

object Users {
    private val uinsRemovedQueue = PriorityQueue<Int>()
    const val MAX_CAPACITY = 1000
    data class User(val UIN: Int, val PIN: Int, val name: String, val accumulatedTime: Long, val entryTime: Long)

    // UIN;PIN;NOME;ACCUMULATED;ENTRY
    private fun User.toText(): String { // 003;1458;Carlos;0;0
        return "$UIN;$PIN;$name;$accumulatedTime;$entryTime"
    }

// ------------------------ Map Methods ----------------------------------------
    private val usersMap = HashMap<Int, User>()

    operator fun get(uin: Int): User? {
        return usersMap[uin]
    }


    fun add(name: String, PIN: Int): User? {
        if (usersMap.size >= MAX_CAPACITY) return null
        val uin = if (uinsRemovedQueue.isNotEmpty()) uinsRemovedQueue.poll()
                else usersMap.size

        val newUser = User(UIN = uin, PIN = PIN, name, 0L, 0L)
        usersMap[uin] = newUser
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
        if (remove != null) uinsRemovedQueue.add(user.UIN)
        return remove
    }

//------------------------------------------------------------------------------------------

    private fun load(userText: String) {
        val userArray = userText.split(';') // ["0", "1256", "Vasco", "0", "0"]
        val userUin = userArray[0].toInt()
        val userPin = userArray[1].toInt()
        val userName = userArray[3].toLong()
        val userAccumulatedTime = userArray[4].toLong()
        val user = User(userUin, userPin, userArray[2], userName, userAccumulatedTime)
        usersMap[userUin] = user
    }

    fun loadQueue(uinRemovedUsers: List<String>) {
        uinRemovedUsers.forEach {
            uinsRemovedQueue.add(it.toInt())
        }
    }

    fun loadUsers(users: List<String>) {
        users.forEach(::load)
    }

    fun writeUsers() {
        FileAcess.writeUsers(usersToList(), queueToList())
    }

    private fun queueToList(): List<String> {
        val removedList = LinkedList<String>()
        uinsRemovedQueue.forEach { elem ->
            removedList.add(elem.toString())
        }

        return removedList
    }

    private fun usersToList():List<String>{
        val users = LinkedList<String>()
        usersMap.forEach {
            users.add(it.value.toText())
        }

        return users
    }

    fun usersInside():List<User>{
        return usersMap.values.filter { it.entryTime != 0L }
    }

}
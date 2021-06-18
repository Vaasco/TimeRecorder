import java.util.*

object Users {
    private val uinsRemovedQueue = PriorityQueue<Int>()

    data class User(val UIN: Int, val PIN: Int, val name: String, val accumulatedTime: Long, val entryTime: Long)

    private val usersMap = HashMap<Int, User>()

    operator fun set(uin:Int, user: User): User? {
        if (usersMap.size >= 1000) return null
        usersMap[uin] = user
        return user
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
        return remove
    }

    fun load(userText: String) {
        val userArray = userText.split(';') // ["0", "1256", "Vasco", "0", "0"]
        val userUin = userArray[0].toInt()
        val userPin = userArray[1].toInt()
        val userName = userArray[3].toLong()
        val userAccumulatedTime = userArray[4].toLong()
        val user = User(userUin, userPin, userArray[2], userName, userAccumulatedTime)
        set(userUin, user)
    }
    // 0;1256;Vasco;0;0
    // UIN;PIN;NAME;ACCUMULATED_TIME;ENTRY_TIME

    operator fun get(uin: Int): User? {
        return usersMap[uin]
    }
}
/*
fun add(){
        fun printLine(){
            println("----------------------------------------------------------")
        }
        if(usersMap.size >= 999) println("Limite máximo de utilizadores atingido.")
        else{
            val uin:Int = when {
                uinsRemovedQueue.isNotEmpty() -> {
                    uinsRemovedQueue.poll()
                }
                usersMap.isEmpty() -> 0
                else -> usersMap[usersMap.size-1]!!.UIN + 1
            }
            val uinString = String.format("%03d", uin)
            printLine()
            print("Digite o PIN do utilizador (4 Dígitos): ") // só quatro digitos
            val pin = readLine()!!.trim().toInt()
            val pinString = String.format("%04d", pin)
            printLine()
            print("Digite o nome do utilizador: ") // Só pode ter 16 caracteres
            val name:String = readLine()!!.trim().capitalize()
            printLine()
            val accumulatedTime = 0
            val entryDate = TUI.date
            val userToAdd = User(uin, pin, name, accumulatedTime, entryDate.toLong())
            usersMap[userToAdd.UIN] = userToAdd
            fun printUserInfo() {
                println("O utilizador foi registado com sucesso e tem os seguintes dados:")
                println("       Número identificador: $uinString")
                println("       Código de acesso: $pinString")
                println("       Nome: $name")
                println("       Horas Acumuladas: $accumulatedTime")
                println("       Data do registo: $entryDate")
                printLine()
            }
            printUserInfo()
        }
    }

    fun remove(){
        print("Digite o UIN do utilizador que quer remover:")
        val uin = readLine()!!.trim().toInt()
        val user = usersMap[uin]
        if(user == null) {println("Utilizador não existe.");return}
        val name = user.name
        var answer = ""
        while (answer != "Y" && answer != "N"){
            print("Confirma a remoção do utilizador ${String.format("%03d", uin)} $name?[Y/N]: ")
            answer = readLine()!!.trim().toUpperCase()
        }
        if(answer == "Y"){
            usersMap.remove(user.UIN)
            uinsRemovedQueue.add(uin)
            println("Utilizador removido com sucesso")
        }
    }

*/

import java.io.File
import java.util.*
/*
object Users {
    //data class User(val UIN:Int, var PIN:Int, val name:String, val accumulatedTime:Int, val entryDate: String)
    private var uinsRemovedQueue = PriorityQueue<Int>()
    private val usersFile: File = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\Users.txt")

    fun init(){
        loadFiles()
    }

    private fun loadFiles() { // Carrega os ficheiros de texto UsersFile e uinsRemovedNotUsed para as respectivas listas
        val usersString = usersFile.readLines() // “UIN;PIN;NAME;ACCUMULATED_TIME;ENTRY_DATE”
        for (string in usersString){
            val usersInfo = string.split(';') // [UIN, PIN, NAME, ACCUMULATED_TIME, ENTRY_DATE]
            val uin = usersInfo[0].toInt()
            val pin = usersInfo[1].toInt()
            val name = usersInfo[2]
            val accumulatedTime = usersInfo[3].toInt()
            val entryDate = usersInfo[4].toLong()
            val userToAdd = User(uin, pin, name, accumulatedTime.toLong(), entryDate)
            usersMap[uin] = userToAdd
        }
        val uinsRemovedFile = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\uinsRemovedToday.txt")
        val scanner = Scanner(uinsRemovedFile)
        fun addUinsToQueue(){
            while (scanner.hasNextLine())
                uinsRemovedQueue.offer(scanner.nextLine().toInt())
        }
        if(uinsRemovedFile.readLines().isNotEmpty())
            addUinsToQueue()
    }

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
}
*/



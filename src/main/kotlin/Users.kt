import java.io.File

object Users {
    data class User(val UIN:Int, var PIN:Int, val name:String, val accumulatedTime:Int, val entryDate: String)
    var uinsRemovedNotUsed = mutableListOf<Int>()
    val usersFile: File = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\Users.txt")
    var listUsers:MutableList<User> = mutableListOf()

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
            val entryDate = usersInfo[4]
            listUsers.add(User(uin, pin, name, accumulatedTime, entryDate))
        }
        val file = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\uinsRemovedToday.txt")
        if (file.readText().trim() != "")
            uinsRemovedNotUsed = file.readLines().map { it.toInt() }.toMutableList()
    }

    fun add(){
        fun printLine(){
            println("----------------------------------------------------------")
        }
        if(listUsers.size >= 999) println("Limite máximo de utilizadores atingido.")
        else{
            val uin:Int = if(uinsRemovedNotUsed.isNotEmpty()){
                val temp = uinsRemovedNotUsed.minOrNull()?.toInt()!!
                uinsRemovedNotUsed.remove(temp)
                temp
            }
            else if(listUsers.isEmpty()) 0
            else listUsers.last().UIN + 1
            val uinString = String.format("%03d", uin)
            printLine()
            print("Digite o PIN do utilizador (4 Dígitos): ") // só quatro digitos
            val pin = readLine()!!.trim().toInt()
            val pinString = String.format("%04d", pin)
            printLine()
            print("Digite o nome do utilizador: ") // Só pode ter 16 caracteres TODO()
            val name:String = readLine()!!.trim().capitalize()
            printLine()
            val accumulatedTime = 0
            val entryDate = TUI.date
            listUsers.add(User(uin, pin, name, accumulatedTime, entryDate))
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
        val user = listUsers.filter { it.UIN == uin }
        if(user.isEmpty()) {println("Utilizador não existe.");return}
        val name = user[0].name
        var answer = ""
        while (answer != "Y" && answer != "N"){
            print("Confirma a remoção do utilizador ${String.format("%03d", uin)} $name?[Y/N]: ")
            answer = readLine()!!.trim().toUpperCase()
        }
        if(answer == "Y"){
            listUsers.remove(user[0])
            uinsRemovedNotUsed.add(uin)
            println("Utilizador removido com sucesso")
        }
    }
}


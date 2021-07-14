import java.lang.NumberFormatException



object M {

    private const val MANUT_MASK = 128

    fun checkMaintenance() = HAL.isBit(MANUT_MASK)

    fun add() {
        fun retrievePin():Int{
            while (true){
                print("Digite o PIN do utilizador (4 Dígitos): ")
                try {
                    val pinString = readLine()!!.trim()
                    val pin = pinString.toInt()
                    if(pinString.length == 4) return pin
                    println("PIN INVÁLIDO.")
                }catch (e:NumberFormatException){
                    println("PIN INVÁLIDO.")
                }
            }
        }
        fun printLine() {
            println("----------------------------------------------------------")
        }
        printLine()
        var name:String?
        do {
            print("Digite o nome do utilizador: ") // Só pode ter 16 caracteres
             name = readLine()?.trim()?.capitalize()
        }while (name == null || name.length > 16)

        printLine()
        val pin:Int = retrievePin()
        val pinString = String.format("%04d", pin)
        printLine()
        val newUser = Users.add(name, PIN = pin)
        if (newUser == null) {
            println("Máximo de utilizadores atingido.")
            return
        }
        val uinString = String.format("%03d", newUser.UIN)
        fun printUserInfo() {
            println("O utilizador foi registado com sucesso e tem os seguintes dados:")
            println("       Nome: ${newUser.name}")
            println("       Número identificador: $uinString")
            println("       Código de acesso: $pinString")
            println("       Horas Acumuladas: ${newUser.accumulatedTime}")
            printLine()
        }
        printUserInfo()
    }


    fun remove() {
        print("Digite o UIN do utilizador que quer remover:")
        val uin = readLine()!!.trim().toInt()
        val user = Users[uin]
        if (user == null) {
            println("Utilizador não existe.")
            return
        }
        val name = user.name
        var answer = ""
        while (answer != "Y" && answer != "N") {
            print("Confirma a remoção do utilizador ${String.format("%03d", uin)} $name?[Y/N]: ")
            answer = readLine()!!.trim().toUpperCase()
        }
        if (answer == "Y") {
            Users.remove(user)
            println("Utilizador removido com sucesso")
        }
    }

    fun enterMaintenace(): Boolean {
        fun printLine() {
            println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=")
        }
        fun printHelp(){
            printLine()
            println("add      : Adicionar utilizador")
            println("\nremove   : Remover utilizador")
            println("\nshutdown : Desligar o sistema")
            printLine()
        }
        printLine()
        println("           Manutenção")
        printLine()
        println("Comandos: add, remove, shutdown, help")
        val commands = arrayOf("add", "remove", "help", "shutdown")
        while (true) {
            var command: String? = ""
            while (true) {
                print(">")
                command = readLine()?.trim()
                if (command !in commands) println("Comando inexistente")
                else break
                if(!HAL.isBit(MANUT_MASK)) return false
            }
            when (command) {
                "add" -> add()
                "remove" -> remove()
                "help" -> printHelp()
                else -> return true
            }
        }
    }
}

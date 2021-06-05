import isel.leic.utils.Time

object App{

    fun run(){
        // começar o entry point
        // escrever data e hora
        // ler uma entry

        initializeObjects()
        TUI.updateDateTime(0)
        var user: User?
        do{
            user = readEntry()
        }
        while(user == null)
        // createLog(user)
        LCD.clear()
        TUI.writeSentence("Bem-vindo",  TUI.Align.Center, 0)
        TUI.writeSentence(user.name, TUI.Align.Center, 1)
        Door.open(10)
        Time.sleep(5000)
        Door.close(2)

    }

    private fun readEntry():User?{
        TUI.clearLine(1)
        TUI.writeSentence("UIN ", TUI.Align.Right, 1)
        val uin = TUI.readInteger(1, 3, visible=true, missing =true)
        if(uin < 0) // < 0 significa que ocorreu um erro, ou que o utilizador se enganou
            return null
        else{
            TUI.clearLine(1)
            var pin:Int
            do{
                TUI.writeSentence("PIN ", TUI.Align.Right, 1)
                pin = TUI.readInteger(1, 4, visible=false, missing =true)
            }while (pin == -2)
            val user = usersMap[0]
            if(user != null && pin == user.PIN)
                return user
            else
                return null
        }
    }

    private fun initializeObjects(){
        HAL.init()
        LCD.init()
        Door.init()

    }
}
import isel.leic.utils.Time

object App{ // Entry point da aplicação
    /**
     * Entry point
     */
    fun run(){
        initializeObjects()
        while(true) {
            var user: User?
            do {
                TUI.updateDateTime(0)
                user = readEntry()
            } while (user == null)
            user = manageEntry(user)
            usersMap[user.UIN] = user
        }
    }

    /**
     * Lê o utilizador se inseriu o UIN e PIN corretamente
     *
     *@return O utilizador se inseriu corretamente as suas credenciais
     */
    private fun readEntry():User?{
        TUI.clearLine(1)
        TUI.writeSentence("UIN ", TUI.Align.Right, 1)
        val uin = TUI.readInteger(1, 3, visible=true, missing =true, TUI.Align.Left)
        if(uin < 0) // < 0 significa que ocorreu um erro, ou que o utilizador se enganou
            return null
        else{
            TUI.clearLine(1)
            var pin:Int
            do{
                TUI.writeSentence("PIN ", TUI.Align.Right, 1)
                pin = TUI.readInteger(1, 4, visible=false, missing =true)
            }while (pin == -2)
            val user = usersMap[uin]
            if(user != null && pin == user.PIN)
                return user
            else
                return null
        }
    }

    /**
     * Faz a alteração do PIN de um utilizador
     *
     * @param user O utilizador que inseriu o UIN e o PIN corretamente e deseja alterar o PIN
     *
     * @return O utilizador com o novo PIN se alterado
     *
     */
    private fun changePin(user: User): User{
        val key = TUI.getInputWithTextInterface("Change PIN?", "Yes -> #", 5000L)
        LCD.clear()
        if (key == '#'){
            TUI.writeSentence("New PIN:", TUI.Align.Left, 0)
            val newPin = TUI.readInteger(1, 4, visible = false, missing = true)
            LCD.clear()
            TUI.writeSentence("Confirm new PIN:", TUI.Align.Left, 0)
            val confirmPin = TUI.readInteger(1, 4, visible = false, missing = true)
            LCD.clear()
            if(newPin == confirmPin && newPin > 0){
                user.PIN = newPin
                TUI.writeSentence("PIN changed.", TUI.Align.Center, 0)
            }
            else  TUI.writeSentence("PIN not changed.", TUI.Align.Left, 0)
            Time.sleep(2000)
        }
        return user
    }

    /**
     * Faz a gestão da entrada/saída de um utilizador
     *
     * Recebe o utilizador escrevendo uma saudação no LCD
     *
     * Altera o seu PIN se requisitado
     *
     * TODO("Mostra o tempo acumulado e hora de entrada/saída")
     *
     * TODO("Cria um registo de entrada/saída")
     *
     * Controla o mecanismo da porta automática
     *
     * @param user O utilizador que inseriu o UIN e o PIN corretamente
     *
     * @return O utilizador com informações de entrada/saída atualizadas e o novo PIN se alterado
     */
   private fun manageEntry(user: User): User{
        /**
         *Faz a gestão da abertura e fecho da porta
         */
       fun manageDoor(){
           TUI.writeSentence("Door opening", TUI.Align.Center, 0)
           TUI.writeSentence(user.name, TUI.Align.Center,1)
           Door.open(6)
           TUI.clearLine(0)
           TUI.writeSentence("Door opened", TUI.Align.Center, 0)
           Time.sleep(5000)
           TUI.writeSentence("Door closing", TUI.Align.Center, 0)
           Door.close(2)
           LCD.clear()
       }
        var userToReturn = user
        LCD.clear()
        TUI.writeSentence("Welcome",  TUI.Align.Center, 0)
        TUI.writeSentence(user.name, TUI.Align.Center, 1)
        val changePinKey = TUI.getInputWithTextInterface()
        if(changePinKey == '#') userToReturn = changePin(user)
        LCD.clear()
        manageDoor()
        return userToReturn
    }

    /**
     * Inicializa os objetos que necessitam de initialização
     */
   private fun initializeObjects(){
        HAL.init()
        LCD.init()
        Door.init()
    }
}
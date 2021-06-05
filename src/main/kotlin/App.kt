import isel.leic.utils.Time

object App{ // Entry point da aplicação

    /**
     * Entry point
     */
    fun run(){
        val hardCodedUser = User(0, 0, "Teodosie Cabral", 0, 0)
        usersMap[hardCodedUser.UIN] = hardCodedUser
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
     * Começa pela leitura de um inteiro de 3 algarismos (UIN), de seguida lê um inteiro de 4 algarismos (PIN)
     *
     * @return O [User] identificado por UIN no [usersMap]
     */
    private fun readEntry():User?{
        TUI.clearLine(1)
        val uinText = "UIN:"
        TUI.writeSentence(uinText, TUI.Align.Left, 1)
        val uin = TUI.readInteger(1, uinText.length,3, visible=true, missing =true)
        if(uin < 0) // < 0 significa que ocorreu um erro, ou que o utilizador se enganou
            return null
        else{
            TUI.clearLine(1)
            var pin:Int
            val textPin = "PIN:"
            do{
                TUI.writeSentence(textPin, TUI.Align.Left, 1)
                pin = TUI.readInteger(1,textPin.length, 4, visible=false, missing =true)
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
     * @param user o [User] que deseja alterar o PIN
     *
     * @return o [User] com o novo PIN se alterado
     *
     */
    private fun changePin(user: User): User{
        val key = TUI.getInputWithTextInterface("Change PIN?", "Yes -> #", 5000L)
        LCD.clear()
        if (key == '#'){
            TUI.writeSentence("New PIN:", TUI.Align.Left, 0)
            val newPin = TUI.readInteger(1, 0, 4, visible = false, missing = true)
            LCD.clear()
            TUI.writeSentence("Confirm new PIN:", TUI.Align.Left, 0)
            val confirmPin = TUI.readInteger(1,0, 4, visible = false, missing = true)
            LCD.clear()
            if(newPin == confirmPin && newPin > 0){
                TUI.writeSentence("PIN changed.", TUI.Align.Center, 0)
                return user.copy(PIN = newPin)
            }
            else  TUI.writeSentence("PIN not changed.", TUI.Align.Left, 0)
            Time.sleep(2000)
        }
        return user
    }

    /**
     * Faz a gestão da entrada/saída de um [User]
     *
     * Recebe o [User] escrevendo uma saudação no LCD
     *
     * Altera o seu PIN se requisitado
     *
     * TODO("Mostra o tempo acumulado e hora de entrada/saída")
     *
     * TODO("Cria um registo de entrada/saída")
     *
     * Controla o mecanismo da porta automática
     *
     * @param user o [User] que inseriu o UIN e o PIN corretamente
     *
     * @return o [User] com informações de entrada/saída atualizadas e o novo PIN se alterado
     */
   private fun manageEntry(user: User): User{
        fun manageEntryTime(user: User):User{
            val dayOfWeek = TUI.dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.UK)
            val hours = TUI.time
            val userToReturn: User
            if(user.entryTime == 0L){
                TUI.writeSentence("$dayOfWeek, $hours",TUI.Align.Left, 0)
                TUI.writeSentence(user.accumulatedTime.toString(), TUI.Align.Right, 0)
                val currentTimeinMilis = (TUI.dateTime.second * 1000L)
                userToReturn = user.copy(entryTime = currentTimeinMilis)
            }
            else{
                val entryTimeinMilis = user.entryTime
                val hoursText = msToTimeFormat(entryTimeinMilis)
                val accumulatedTimeCalculate = 0  // TODO("Calcular o tempo acumulado à saída do utilizador)
                userToReturn = user.copy(accumulatedTime = 0,entryTime = 0)
                TUI.writeSentence("$dayOfWeek, $hoursText",TUI.Align.Left, 0)
                TUI.writeSentence("$dayOfWeek, $hours",TUI.Align.Left, 1)
                TUI.writeSentence(userToReturn.accumulatedTime.toString(), TUI.Align.Right, 1)
            }
            Time.sleep(5000)
            LCD.clear()
            return userToReturn
        }
        /**
         * Faz a gestão da abertura e fecho da porta à entrada de um utilizador
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
        userToReturn = manageEntryTime(userToReturn)
        manageDoor()
        return userToReturn
    }

    /**
     * Inicializa os objetos que necessitam de inicialização
     *
     * [HAL]
     * [LCD]
     * [Door]
     * [TUI]
     */
   private fun initializeObjects(){
        HAL.init()
        LCD.init()
        Door.init()
        TUI.init()
    }

    private fun msToTimeFormat(time:Long):String{
        val hours = time /  360000F
        val decimalPart = hours - (time / 360000)
        val minutes = (decimalPart * 600).toInt() // ERRADO conversão para minutos TODO()
        val hoursText = String.format("%02d", hours.toInt())
        val minsText = String.format("%02d", minutes)
        return "$hoursText:$minsText"
    }
}
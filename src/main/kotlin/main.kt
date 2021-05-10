import isel.leic.utils.Time


fun setAck(){
    HAL.setBits(128)
    HAL.clearBits(128)
}

fun writeKey(visible:Boolean, timeout:Long):Char{
    val key = KBD.waitKey(timeout)
    setAck()
    if(key != 0.toChar()){
        if(visible){
            LCD.write(key)
        }
        else LCD.write('*')
    }
    return key
}

fun main(args: Array<String>){
    HAL.init()
    LCD.init()
    TUI.init()
    Users.init()
    repeat(10){
        setAck()
    }
    while(true){
        TUI.writeSentence("UIN ", TUI.Align.Right, 0)
        val UIN = TUI.readNumber(true, 3, true)
        var user:Users.User = Users.User(0, 0, "", 0, "")
        var count = 0
        Users.listUsers.forEach {
            if(it.UIN == UIN){
                user = it
                count++
                println("cheguei aqui")
            }
        }
        if(count == 1){
            LCD.clear()
            TUI.updateDateTime()
            TUI.writeSentence("PIN ", TUI.Align.Right, 0)
            val pin = TUI.readNumber(true, 4, true)
            if(pin == user.PIN){
                LCD.clear()
                TUI.writeSentence("Bem vindo", TUI.Align.Center, 0)
                TUI.writeSentence(user.name, TUI.Align.Center, 1)
                Time.sleep(1500)
                LCD.clear()
                TUI.writeSentence("Entrada: ${TUI.time}", TUI.Align.Left, 0)
                TUI.writeSentence("Saida:00:00", TUI.Align.Left, 1)
                Time.sleep(1500)
                LCD.clear()
                TUI.writeSentence("Horas Acumuladas", TUI.Align.Center, 0)
                TUI.writeSentence(user.accumulatedTime.toString(), TUI.Align.Center, 1)
                Time.sleep(1500)
            }
        }
        LCD.clear()
        TUI.updateDateTime()
    }

}

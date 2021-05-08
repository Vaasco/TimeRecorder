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
    repeat(10){
        setAck()
    }
    while (true){
        val code = TUI.readNumber(false, 4, true)
        if (code == -1){
            LCD.clear()
            TUI.writeSentence("PIN invalido", TUI.Align.Left, 0)
            TUI.writeSentence("Tente novamente", TUI.Align.Left, 1)
            Time.sleep(1500)
            LCD.clear()
        }
        else if(code == -2) continue
        else{
            if (code == 1234){
                LCD.clear()
                TUI.writeSentence("Bem vindo.", TUI.Align.Left, 0)
                Time.sleep(3000)
                LCD.clear()
            }
        }
        TUI.updateDateTime()
    }

}

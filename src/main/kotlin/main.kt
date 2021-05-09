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
    while(true){
        TUI.writeSentence("UIN ", TUI.Align.Right, 0)
        val UIN = TUI.readNumber(true, 3, true)
        var user:Users.User = Users.User(0, 0, "", 0, "")
        var count = 0
        Users.listUsers.forEach {
            println(it.UIN)
            if(it.UIN == UIN){
                user = it
                count++
                println("cheguei aqui")
            }
        }
        if(count == 1){
            LCD.clear()
        }
        LCD.clear()
        TUI.updateDateTime()
    }

}

import isel.leic.utils.Time

fun setAck(){
    HAL.setBits(128)
    HAL.clearBits(128)
}

fun writeKey():Char{
    val key = KBD.waitKey(5000)
    LCD.write(key)
    return key
}

fun main(args: Array<String>){
    HAL.init()
    LCD.init()
    repeat(10){
        setAck()
    }
    val code = TUI.readInteger(true, 4, true)
    if (code == 1234){
        LCD.clear()
        LCD.write("Bem vindo.")
    }
}

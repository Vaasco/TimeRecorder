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
    LCD.clear()
    LCD.cursor(1, 10)
}

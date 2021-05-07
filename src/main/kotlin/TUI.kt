
object TUI {
    fun readInteger(visible:Boolean, lenght:Int, missing:Boolean):Int{
        var code:String = ""
        if (missing){
            repeat(lenght){
                LCD.write('?')
            }
            LCD.cursor(0, 0)
        }
        repeat(lenght){
            code += writeKey(visible)
        }
        return try {
            code.trim().toInt()
        }catch (notInt: NumberFormatException){
            println("inteiro inválido")
            -1
        }
    }
}
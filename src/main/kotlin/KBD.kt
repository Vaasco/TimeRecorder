import isel.leic.utils.Time

object KBD { // Ler teclas. Métodos retornam ‘0’..’9’,’#’,’*’ ou NONE.

    private const val NONE: Char = 0.toChar()

    private const val SERIAL = false

    // Mask que representa os bits de Kcode no UsbPort
    private const val KEY_MASK = 0b00001111

    // Mask que representa a entrada do bit D_Val do bloco KeyDecode
    private const val D_VAL = 0b00010000

    //Mask que representa à saída o bit Ack do bloco KeyDecodeBuffer
    private const val ACK_MASK = 0b10000000

    // Array em que o índice representa o código da tecla de cada elemento
    private val array = charArrayOf('1','4','7','*','2','5','8','0','3','6','9','#')

    // Retorna de imediato a tecla premida ou NONE se não há tecla premida.
    private fun getKeyParalel(): Char {
        val value = HAL.readBits(KEY_MASK)
        return if (HAL.isBit(D_VAL) && value in array.indices){
            HAL.setBits(ACK_MASK)
            Time.sleep(50)
            HAL.clearBits(ACK_MASK)
            array[value]
        } else
            NONE
    }

    private fun getKeySerial():Char{
        val keyCode = SerialReceiver.rcv()
        return if (HAL.isBit(D_VAL) && keyCode in array.indices)
            array[SerialReceiver.rcv()]
        else NONE
    }

    // Retorna quando a tecla for premida ou NONE após decorrido ‘timeout’ milisegundos.
    fun waitKey(timeout: Long): Char{
        val msBefore = System.currentTimeMillis()
        var currentTime = 0L
        var key = NONE
        while(currentTime - msBefore < timeout && key == NONE){
            key = if(SERIAL) getKeySerial() else getKeyParalel()
            currentTime = System.currentTimeMillis()
        }
        return key
    }

}

fun main() {
    HAL.init()
    println(KBD.waitKey(5000L))
}
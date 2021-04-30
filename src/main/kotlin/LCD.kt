import isel.leic.UsbPort
import isel.leic.utils.Time

object LCD{ // Escreve no LCD usando a interface a 4 bits

    private const val LINES = 2
    private const val COLS = 16 // Dimensão do display.

    // Escreve um nibble de comando/dados no LCD
    fun writeNibble(rs: Boolean, data: Int){
        if (rs) HAL.setBits(0b00010000) // Rs High
        else HAL.clearBits(0b00010000) // Rs Low
        HAL.setBits(0b00100000) // Enable High
        HAL.writeBits(0b00001111, data)
        HAL.clearBits(0b00100000) // Enable Low

    }

    // Escreve um byte de comando/dados no LCD
    fun writeByte(rs: Boolean, data: Int){
        val data1 = data
        writeNibble(rs, data)
        writeNibble(rs, data shr 4)
    }

    // Escreve um comando no LCD
    private fun writeCMD(data: Int){
        writeByte(false ,data)
    }

    // Escreve um dado no LCD
    private fun writeDATA(data: Int){
        writeByte(true, data)
    }

    // Envia a sequência de iniciação para comunicação a 4 bits.
    fun init(){
        TODO()
    }

    // Escreve um caráter na posição corrente.
    fun write(c: Char){
        TODO()
    }

    // Escreve uma string na posição corrente.
    fun write(text: String){
        TODO()
    }

    // Envia comando para posicionar cursor (‘line’:0..LINES-1 , ‘column’:0..COLS-1)
    fun cursor(line: Int, column: Int){
        TODO()
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    fun clear(){
        writeCMD(0b00000001)
    }
}



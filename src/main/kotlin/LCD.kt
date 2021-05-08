import isel.leic.utils.Time

object LCD{ // Escreve no LCD usando a interface a 4 bits
    private data class Pos(var line:Int, var column:Int)
    private const val DATA_MASK = 0b00001111 // Posição do bit que representa Data À saída do UsbPort
    private const val RS_MASK = 0b00010000 // Posição do bit que representa RS À saída do UsbPort
    private const val ENABLE_MASK = 0b00100000 // Posição do bit que representa E À saída do UsbPort
    private var cursorPos = Pos(0,0)
    private const val LINES = 2
    private const val COLS = 16 // Dimensão do display.

    // Escreve um nibble de comando/dados no LCD
    private fun writeNibble(rs: Boolean, data: Int){
        if (rs) HAL.setBits(RS_MASK) // Rs High
        else HAL.clearBits(RS_MASK) // Rs Low
        HAL.setBits(ENABLE_MASK) // Enable High
        HAL.writeBits(DATA_MASK, data)
        HAL.clearBits(ENABLE_MASK) // Enable Low
        
    }

    // Escreve um byte de comando/dados no LCD
    private fun writeByte(rs: Boolean, data: Int){
        writeNibble(rs,data shr 4)
        Time.sleep(1)
        writeNibble(rs, data)
    }

    // Escreve um comando no LCD
    private fun writeCMD(data: Int){
        writeByte(false ,data)
        Time.sleep(10)
    }

    // Escreve um dado no LCD
    private fun writeDATA(data: Int){
        writeByte(true, data)
    }

    // Envia a sequência de iniciação para comunicação a 4 bits.
    fun init(){
        cursorPos = Pos(0, 0)
        Time.sleep(15)
        writeNibble(false,0b0011)
        Time.sleep(5)
        writeNibble( false,0b0011)
        writeNibble(false,0b0011)
        writeNibble(false,0b0010)
        writeCMD(0x28)
        writeCMD(0x08)
        writeCMD(0x01)
        writeCMD(0x06)
        writeCMD(0x0F)
    }

    // Escreve um caráter na posição corrente.
    fun write(c: Char){
        if(cursorPos.column == COLS-1 && cursorPos.line == 0) {
            writeDATA(c.toInt())
            writeCMD(0xC0)
            cursorPos = Pos(cursorPos.line + 1,0)
        }
        else{
            writeDATA(c.toInt())
            if(cursorPos.column < 15) cursorPos = Pos(cursorPos.line, cursorPos.column+1)
        }
    }

    // Escreve uma string na posição corrente.
    fun write(text: String){
        for(char in text) write(char)
    }

    // Envia comando para posicionar cursor (‘line’:0..LINES-1 , ‘column’:0..COLS-1)
    fun cursor(line: Int, column: Int){
        if(line == 1){
            writeCMD(0xC0 + column)
        }
        else{
            writeCMD(0x80 + column)
        }
        cursorPos = Pos(line, column)
    }

    // Envia comando para limpar o ecrã e posicionar o cursor em (0,0)
    fun clear(){
        writeCMD(1)
        cursorPos = Pos(0, 0)
    }
}



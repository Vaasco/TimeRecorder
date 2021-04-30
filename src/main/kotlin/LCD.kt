import isel.leic.UsbPort

object LCD{ // Escreve no LCD usando a interface a 4 bits
    private const val LINES = 2
    private const val COLS = 16 // Dimensão do display.

    // Escreve um nibble de comando/dados no LCD
    private fun writeNibble(rs: Boolean, data: Int){
        TODO()

    }

    // Escreve um byte de comando/dados no LCD
    private fun writeByte(rs: Boolean, data: Int){
        TODO()

    }

    // Escreve um comando no LCD
    private fun writeCMD(data: Int){
        TODO()

    }

    // Escreve um dado no LCD
    private fun writeDATA(data: Int){
        TODO()

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
        TODO()

    }
}



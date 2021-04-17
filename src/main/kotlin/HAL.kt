import isel.leic.UsbPort

object HAL { // Virtualiza o acesso ao sistema UsbPort
    /**
     * Foi adicionada a seguinte variável
     * que vai guardando o valor representado
     * pelos bits da saída do UsbPort
     */
    var outputBits = 0


    // Inicia a classe
    fun init() = UsbPort.out(outputBits.inv())


    // Retorna true se o bit tiver o valor lógico ‘1’
    fun isBit(mask: Int): Boolean = UsbPort.`in`().inv().and(mask) == mask


    // Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int): Int {
        val a = UsbPort.`in`().inv().and(mask)
        var idx: Int = 0
        var nMask = mask
        for (i in 0..7) {
            if (nMask % 2 != 0) {
                idx = i
                break
            }
            nMask = nMask shr 1
        }
        return a shr idx
    }


    // Escreve nos bits representados por mask o valor de value
    fun writeBits(mask: Int, value: Int){
        outputBits = outputBits.and(mask.inv())
        val out = mask.and(value)
        outputBits = outputBits.or(out)
        UsbPort.out(outputBits.inv())
    }

    // Coloca os bits representados por mask no valor lógico ‘1’
    fun setBits(mask: Int) {
       outputBits = outputBits.or(mask)
       UsbPort.out(outputBits.inv())
    }

    // Coloca os bits representados por mask no valor lógico ‘0’
    fun clearBits(mask: Int){
        outputBits = outputBits.and(mask.inv())
        UsbPort.out(outputBits.inv())
    }
}

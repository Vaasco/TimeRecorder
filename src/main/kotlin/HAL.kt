import isel.leic.UsbPort

object HAL { // Virtualiza o acesso ao sistema UsbPort
    /**
     * Foi adicionada a seguinte variável
     * que vai guardando o valor representado
     * pelos bits da saída do UsbPort
     */
    private var outputBits = 0

    private fun usbPortIn():Int = UsbPort.`in`().inv()

    // Escreve o valor de value nos bits de output do UsbPort
    private fun writeValueInOutput(value:Int) = UsbPort.out(value.inv())

    // Inicia a classe
    fun init() = writeValueInOutput(0)


    // Retorna true se o bit tiver o valor lógico ‘1’
    fun isBit(mask: Int): Boolean = usbPortIn().and(mask) == mask


    // Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int): Int {
        val a = usbPortIn().and(mask)
        var idx = 0
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
        outputBits = outputBits and mask.inv()
        val out = mask and value
        outputBits = outputBits or out
        writeValueInOutput(outputBits)
    }


    // Coloca os bits representados por mask no valor lógico ‘1’
    fun setBits(mask: Int) {
       outputBits = outputBits.or(mask)
       writeValueInOutput(outputBits)
    }


    // Coloca os bits representados por mask no valor lógico ‘0’
    fun clearBits(mask: Int){
        outputBits = outputBits.and(mask.inv())
        writeValueInOutput(outputBits)
    }
}

fun main(){
    while (true){
        println(HAL.isBit(1))
    }
}
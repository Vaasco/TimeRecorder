import isel.leic.utils.Time

object Door { // Controla o mecanismo da porta.
    private const val velocityMask = 0b00001111
    private const val openNCloseMask = 0b00010000
    private const val wrdMask = 0b01000000
    private const val busyMask = 0b01000000

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init(){
        HAL.clearBits(wrdMask)
    }
    // Envia comando para abrir ou fechar a porta com uma determinada velocidade
    private fun sendCommand(speed: Int, openNClose:Boolean){
        HAL.writeBits(velocityMask, speed)
        if (openNClose) HAL.setBits(openNCloseMask)
        else HAL.clearBits(openNCloseMask)
        Time.sleep(10)
        HAL.setBits(wrdMask)
        Time.sleep(10)
        HAL.clearBits(wrdMask)
        while (!isFinished()) continue
    }

    // Envia comando para abrir a porta, indicando a velocidade
    fun open(speed: Int){
        sendCommand(speed, true)
    }

    // Envia comando para fechar a porta, indicando a velocidade
    fun close(speed: Int){
        sendCommand(speed, false)
    }


    // Retorna true se o tiver terminado o comando
    private fun isFinished(): Boolean = !HAL.isBit(busyMask)
}






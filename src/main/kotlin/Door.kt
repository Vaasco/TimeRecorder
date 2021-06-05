import isel.leic.utils.Time

object Door { // Controla o mecanismo da porta.
    private const val velocityMask = 0b00001111 // Trocar máscara para 0b00001111 presencialmente
    private const val openNCloseMask = 0b00010000 // Trocar máscara para 0b00010000 presencialmente
    private const val wrdMask = 0b01000000
    private const val busyMask = 0b01000000

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init(){

    }

    // Envia comando para abrir a porta, indicando a velocidade
    fun open(speed: Int){
        HAL.writeBits(velocityMask, speed)
        HAL.setBits(openNCloseMask)
        Time.sleep(10)
        HAL.setBits(wrdMask)
        Time.sleep(10)
        HAL.clearBits(wrdMask)
        while (!isFinished()) continue

    }

    // Envia comando para fechar a porta, indicando a velocidade
    fun close(speed: Int){
        HAL.writeBits(velocityMask, speed)
        Time.sleep(10)
        HAL.clearBits(openNCloseMask)
        Time.sleep(10)
        HAL.setBits(wrdMask)
        Time.sleep(10)
        HAL.clearBits(wrdMask)
        while (!isFinished()) continue
    }


    // Retorna true se o tiver terminado o comando
    private fun isFinished(): Boolean = !HAL.isBit(busyMask)
}






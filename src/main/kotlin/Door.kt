import isel.leic.utils.Time

object Door { // Controla o mecanismo da porta.
    private const val velocityMask = 0b00001111
    private const val openNCloseMask = 0b00010000
    private const val wrdMask = 0b01000000
    private const val busyMask = 0b01000000

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init(){
        TODO("Not yet implemented")
    }

    // Envia comando para abrir a porta, indicando a velocidade
    fun open(speed: Int){
        HAL.writeBits(velocityMask, speed)
        HAL.setBits(openNCloseMask)
        Time.sleep(10)
        HAL.setBits(wrdMask)
        while (!isFinished()) println()
        HAL.clearBits(wrdMask)
    }

    // Envia comando para fechar a porta, indicando a velocidade
    fun close(speed: Int){
        HAL.writeBits(velocityMask, speed)
        HAL.clearBits(openNCloseMask)
        Time.sleep(10)
        HAL.setBits(wrdMask)
        while (!isFinished()) println()
        HAL.clearBits(wrdMask)
    }


    // Retorna true se o tiver terminado o comando
    fun isFinished(): Boolean = !HAL.isBit(busyMask)
}


fun main() {
    HAL.init()
    while (true) {
        Door.open(10)
        Door.close(10)
        Time.sleep(10000)
    }

}




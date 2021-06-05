import isel.leic.utils.Time

object Door { // Controla o mecanismo da porta.
    private const val velocityMask = 0b00011110 // Trocar máscara para 0b00001111 presencialmente
    private const val openNCloseMask = 0b00000001 // Trocar máscara para 0b00010000 presencialmente
    private const val wrdMask = 0b01000000
    private const val busyMask = 0b01000000

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init(){
        // garantir que está fechada
    }

    private fun sendCommand(velocity:Int, openNClose:Boolean){

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


fun main() {
    HAL.init()
    Door.open(1)
    Time.sleep(1000)
    Door.close(2)

}




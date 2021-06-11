import isel.leic.utils.Time

object KeyReceiver {

    private const val TXD_MASK = 0b00000001

    // inicia a classe
    fun init(){
        HAL.clearBits(128)
    }

    private fun sendTxdPulse(){
        HAL.setBits(128)
        Time.sleep(10)
        HAL.clearBits(128)
    }

    // recebe uma trama
    fun rcv():Int{
        var keyCode = 0
        if(HAL.isBit(1)) return -1 // vê handshake
        sendTxdPulse()
        Time.sleep(100)
        if (!HAL.isBit(TXD_MASK)) return -1 // vê bit start
        keyCode += HAL.readBits(TXD_MASK) // vê bit 0
        for (i in 0 until 3){
            keyCode shl 1
            Time.sleep(100)
            keyCode += HAL.readBits(TXD_MASK)  // vê bit 1, 2, 3
        }
        if (HAL.isBit(TXD_MASK)) return -1// vê stopBit

        return keyCode
    }
}



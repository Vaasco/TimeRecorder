import isel.leic.utils.Time

object KeyReceiver {

    private const val TXCLK_MASK = 0b10000000
    private const val TXD_MASK = 0b00000001

    // inicia a classe
    fun init(){
        HAL.clearBits(TXCLK_MASK)
    }

    private fun sendTxClkPulse(){
        HAL.setBits(TXCLK_MASK)
        Time.sleep(50)
        HAL.clearBits(TXCLK_MASK)
    }

    private fun handleError():Int{
        repeat(7) {sendTxClkPulse()}
        return -1
    }

    // recebe uma trama
    fun rcv():Int{
        var keyCode = 0
        if(HAL.isBit(TXD_MASK)) return handleError() // vê handshake
        sendTxClkPulse()
        Time.sleep(100)
        if (!HAL.isBit(TXD_MASK)) return handleError()  // vê bit start
        sendTxClkPulse()
        keyCode += HAL.readBits(TXD_MASK) // vê bit 0
        for (i in  1 until 4){
            sendTxClkPulse()
            val value = HAL.readBits(TXD_MASK)
            val shift = value shl i
            Time.sleep(100)
            keyCode += shift  // vê bit 1, 2, 3
        }
        sendTxClkPulse()
        if (HAL.isBit(TXD_MASK)) // vê stopBit
            return handleError()
        sendTxClkPulse()
        return keyCode
    }
}

fun main() {
    HAL.init()
    KeyReceiver.init()
    println(KeyReceiver.rcv())
}


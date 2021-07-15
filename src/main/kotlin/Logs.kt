import java.text.SimpleDateFormat
import java.util.*

object Logs {
    private data class Log(val registDate: Long, val inNOut: Boolean, val uin: Int, val name: String)
    private val logsList = LinkedList<Log>()

    private fun Log.toText(): String {
        val dateFormatter = SimpleDateFormat("HH:mm", Locale.UK)
        val registDateText = dateFormatter.format(registDate)
        val inNOutText = if (inNOut) "[>]" else "[<]"
        return "$registDateText;$inNOutText;$uin;$name"
    }

    fun addLog(registDate: Long, inNOut: Boolean, uin: Int, name: String) {
        val log = Log(registDate, inNOut, uin, name)
        logsList.add(log)
    }

    fun writeLogs(){
        val logs = LinkedList<String>()
        logsList.forEach { log ->
            logs.add(log.toText())
        }
        FileAcess.writeLogs(logs)
    }

    fun loadLogs(logs: List<String>){
        logs.forEach(::load)
    }

    private fun load(logsText: String) {
        val logArray = logsText.split(';')
        val logRegistDate = logArray[0].split(":")
        val hours = logRegistDate[0].toInt() * 360000
        val minutes= logRegistDate[1].toInt() * 6000
        val ms = (hours + minutes).toLong()
        val loginNout = logArray[1]
        val logUIN = logArray[2].toInt()
        val logNAME = logArray[3]
        addLog(ms, loginNout == "[>]", logUIN, logNAME)
    }
}

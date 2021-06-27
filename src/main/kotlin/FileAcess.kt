import java.io.File
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList


object FileAcess {
    private val usersFile = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\Users.txt")
    private val logFile = File("C:\\Isel\\LIC\\TimeRecorder\\TextFiles\\LogFile.txt")


    fun init() {
        loadUsers()
        loadLogs()
    }

    fun loadLogs(){
        logFile.readLines().forEach { log->
            Logs.load(log)
        }
    }

    private fun loadUsers() {
        Users.loadUsers(usersFile.readLines(), usersRemovedFile.readLines())
    }

    fun writeLogs(logs: List<String>) {
        val writer = PrintWriter(logFile.path)
        for (log in logs) writer.println(log)
        writer.close()
    }

    fun writeUsers(users: List<String>, removedQueue:PriorityQueue<Int>) {

        val writer = PrintWriter(usersFile.path)
        for (user in users) writer.println(user)
        writer.close()
    }
}

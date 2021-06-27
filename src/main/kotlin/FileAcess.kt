import java.io.File
import java.io.PrintWriter
import java.util.*


object FileAcess {
    private val usersFile = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\Users.txt")
    private val logFile = File("C:\\Isel\\LIC\\TimeRecorder\\TextFiles\\LogFile.txt")


    fun init() {
        loadUsers()
    }


    private fun loadUsers() {
         Users.loadUsers(usersFile.readLines())
    }

    fun writeLogs(logs:List<String>){
        val writer = PrintWriter(logFile.path)
        for(log in logs) writer.println(log)
        writer.close()
    }

    fun writeUsers(users:List<String>){
        val writer = PrintWriter(usersFile.path)
        for(user in users) writer.println(user)
        writer.close()
    }
}

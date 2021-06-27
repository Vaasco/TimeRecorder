import isel.leic.utils.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.system.exitProcess

const val BOTTOM_LINE = 1
const val TOP_LINE = 0
const val DEFAULT_TIME_SLEEP = 5000L
const val DOOR_OPEN_SPEED = 6
const val DOOR_CLOSE_SPEED = 2
const val DEBUG = false


object App { // Entry point da aplicação
    // Data e hora atual
    private var dateTime: LocalDateTime = LocalDateTime.now()

    /**
     * Entry point-
     */
    fun run() {
        initializeObjects()
        while (true) {
            var user: Users.User?
            do {
                if(M.checkMaintenance()) {
                    TUI.clear()
                    TUI.writeSentence("Out of service", TUI.Align.Center, 0)
                    if(M.enterMaintenace()){
                        Users.writeUsers()
                        Logs.writeLogs()
                    }
                updateDateTime(0)
                user = readEntry()
            } while (user == null)
            user = manageEntry(user)
            Users.update(user)
        }
    }

    /**
     * Atualiza a hora e data
     *
     * @param line índice da linha a escrever (compreendido entre 0 e ([LCD.LINES] - 1))
     *
     */
    private fun updateDateTime(line: Int) {
        dateTime = LocalDateTime.now()
        writeDate(line, TUI.Align.Left)
        writeHour(line, TUI.Align.Right)
    }

    /**
     * Escreve a data atual no [LCD] alinhado conforme [alignment] na linha [line]
     *
     * @param line índice da linha a escrever (compreendido entre 0 e ([LCD.LINES] - 1))
     *
     * @param alignment  tipo de alinhamento ([TUI.Align])
     *
     */
    private fun writeDate(line: Int, alignment: TUI.Align) {
        val year = dateTime.year
        val month = dateTime.month.value
        val day = dateTime.dayOfMonth
        val dayString = String.format("%02d", day)
        val monthString = String.format("%02d", month)
        val dateInString = "$dayString/$monthString/$year"
        TUI.writeSentence(dateInString, alignment, line)
    }

    /**
     * Escreve a hora atual no  [LCD] alinhado conforme [alignment] na linha [line]
     *
     * @param line  índice da linha a escrever (compreendido entre 0 e ([LCD.LINES] - 1) )
     *
     * @param alignment  tipo de alinhamento de texto ([TUI.Align])
     */
    private fun writeHour(line: Int, alignment: TUI.Align) {
        val hours = dateTime.hour
        val mins = dateTime.minute
        val hoursString = String.format("%02d", hours)
        val minsString = String.format("%02d", mins)
        val timeInString = "$hoursString:$minsString"
        TUI.writeSentence(timeInString, alignment, line)
    }

    /**
     * Começa pela leitura de um inteiro de 3 algarismos (UIN), de seguida lê um inteiro de 4 algarismos (PIN)
     *
     * @return O [Users.User] identificado por UIN em [Users]
     */
    private fun readEntry(): Users.User? {
        TUI.clearLine(1)
        val uinText = "UIN:"
        TUI.writeSentence(uinText, TUI.Align.Left, 1)
        val uin = if (DEBUG) 0 else TUI.readInteger(1, uinText.length, 3, visible = true, missing = true)
        if (uin < 0) // < 0 significa que ocorreu um erro, ou que o utilizador se enganou
            return null
        else {
            TUI.clearLine(1)
            var pin: Int
            val textPin = "PIN:"
            do {
                TUI.writeSentence(textPin, TUI.Align.Left, 1)
                pin = TUI.readInteger(1, textPin.length, 4, visible = false, missing = true)
            } while (pin == -2)
            val user = Users[uin]
            if (user != null && (DEBUG || pin == user.PIN))
                return user
            else
                return null
        }
    }

    /**
     * Faz a alteração do PIN de um utilizador.
     *
     * @return o novo PIN ou null se não alterado.
     *
     */
    private fun changePin(): Int? {
        val key = TUI.getInputWithTextInterface("Change PIN?", "Yes -> #", DEFAULT_TIME_SLEEP)
        TUI.clear()
        val newPin: Int
        if (key == '#') {
            TUI.writeSentence("New PIN:", TUI.Align.Left, 0)
            newPin = TUI.readInteger(1, 0, 4, visible = false, missing = true)
            TUI.clear()
            TUI.writeSentence("Confirm new PIN:", TUI.Align.Left, 0)
            val confirmPin = TUI.readInteger(1, 0, 4, visible = false, missing = true)
            TUI.clear()
            if (newPin == confirmPin && newPin > 0) {
                TUI.writeSentence("PIN changed.", TUI.Align.Center, 0)
                return newPin
            }
        }
        TUI.writeSentence("PIN not changed.", TUI.Align.Left, TOP_LINE)
        Time.sleep(DEFAULT_TIME_SLEEP)
        return null
    }

    /**
     * Faz a gestão da entrada/saída de um [Users.User]
     *
     * Recebe o [Users.User] escrevendo uma saudação no LCD
     *
     * Altera o seu PIN se requisitado
     *
     * Cria um registo de entrada/saída
     *
     * Controla o mecanismo da porta automática
     *
     * @param user o [Users.User] que inseriu o UIN e o PIN corretamente
     *
     * @return o [Users.User] com informações de entrada/saída atualizadas e o novo PIN se alterado
     */
    private fun manageEntry(user: Users.User): Users.User {
        TUI.clear()
        TUI.writeSentence("Welcome", TUI.Align.Center, TOP_LINE)
        TUI.writeSentence(user.name, TUI.Align.Center, BOTTOM_LINE)
        val changePinKey = TUI.getInputWithTextInterface()
        val newPin: Int? = if (changePinKey == '#') changePin() else null
        TUI.clear()
        val userWithNewPin: Users.User = if (newPin != null) user.copy(PIN = newPin) else user
        val userWithUpdatedTime: Users.User = userUpdateEntryTime(userWithNewPin)
        val exitTime:Long
        if (user.entryTime != 0L) {
            exitTime = Time.getTimeInMillis()
            writeEntryAndExitTimeWithAccumulate(userWithUpdatedTime.accumulatedTime, exitTime, user.entryTime)
            Logs.addLog(exitTime, inNOut = false, userWithUpdatedTime.UIN, userWithUpdatedTime.name)

        }else{
            writeEntryAndExitTimeWithAccumulate(userWithUpdatedTime.accumulatedTime, entry=userWithUpdatedTime.entryTime)
            Logs.addLog(userWithUpdatedTime.entryTime, inNOut = true, userWithUpdatedTime.UIN, userWithUpdatedTime.name)
        }
        manageDoor(userWithUpdatedTime.name)

        return userWithUpdatedTime
    }

    fun userUpdateEntryTime(user: Users.User): Users.User {
        return if (user.entryTime == 0L) {
            user.copy(entryTime = Time.getTimeInMillis())
        } else {
            val accumulatedTimeCalculated = user.accumulatedTime + (Time.getTimeInMillis() - user.entryTime)
            user.copy(accumulatedTime = accumulatedTimeCalculated, entryTime = 0L)
        }
    }

    fun writeEntryAndExitTimeWithAccumulate(accumulate: Long, exit: Long?=null, entry: Long) {
        val formatter = SimpleDateFormat("HH:mm", Locale.UK)
        val dayOfTheWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.UK)
        val exitTimeText = if (exit != null) "$dayOfTheWeek, " + formatter.format(exit) else "???, ??:??"
        val accumulatedTimeText = msToTimeFormat(accumulate)
        val entryTimeText = "$dayOfTheWeek, " + formatter.format(entry)

        TUI.writeSentence(entryTimeText, TUI.Align.Left, TOP_LINE)
        TUI.writeSentence(exitTimeText, TUI.Align.Left, BOTTOM_LINE)
        TUI.writeSentence(accumulatedTimeText, TUI.Align.Right, if (exit == null) TOP_LINE else BOTTOM_LINE)
        Time.sleep(DEFAULT_TIME_SLEEP)
        TUI.clear()
    }

    /**
     * Faz a gestão da abertura e fecho da porta à entrada de um utilizador
     */
    fun manageDoor(userName: String) {
        TUI.writeSentence("Door opening", TUI.Align.Center, TOP_LINE)
        TUI.writeSentence(userName, TUI.Align.Center, BOTTOM_LINE)
        Door.open(DOOR_OPEN_SPEED)
        TUI.clearLine(0)
        TUI.writeSentence("Door opened", TUI.Align.Center, TOP_LINE)
        Time.sleep(DEFAULT_TIME_SLEEP)
        TUI.writeSentence("Door closing", TUI.Align.Center, TOP_LINE)
        Door.close(DOOR_CLOSE_SPEED)
        TUI.clear()
    }

    /**
     * Inicializa os objetos que necessitam de inicialização
     *
     * [HAL]
     * [LCD]
     * [Door]
     * [TUI]
     */
    private fun initializeObjects() {
        HAL.init()
        LCD.init()
        Door.init()
        FileAcess.init()
    }

    /**
     *
     * Passa o tempo de mílissegundoss para um formato de horas e minutos em formato de texto
     *
     * @param time tempo em milissegundos
     *
     * @return o tempo em formato de texto "horasTotais:minutosAtuais"
     *
     */
    private fun msToTimeFormat(time: Long): String {
        val totalSeconds = time / 1000
        val totalMinutes = totalSeconds / 60
        val currentMinutes = totalMinutes % 60
        val totalHours = totalMinutes / 60
        val currentHours = totalHours % 24
        return "$currentHours:$currentMinutes"
    }
}

fun main() {
    App.run()
    exitProcess(0)
}
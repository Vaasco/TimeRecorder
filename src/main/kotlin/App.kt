import isel.leic.utils.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.HashMap

const val BOTTOM_LINE = 1
const val TOP_LINE = 0
const val DEFAULT_TIME_SLEEP = 5000L

data class User(val UIN:Int, val PIN:Int, val name:String, val accumulatedTime:Long, val entryTime: Long)
val usersSet = HashMap<Int, User>()

object App { // Entry point da aplicação

    // Data e hora atual
    private var dateTime: LocalDateTime = LocalDateTime.now()

    /**
     * Entry point
     */
    fun run(){
        val hardCodedUser = User(0, 0, "Teodosie Cabral", 0, 0)
        usersSet[hardCodedUser.UIN] = hardCodedUser
        initializeObjects()
        while(true) {
            var user: User?
            do {
                updateDateTime(0)
                user = readEntry()
            } while (user == null)
            user = manageEntry(user)
            usersSet[user.UIN] = user
        }
    }

    /**
     * Atualiza a hora e data
     *
     * @param line índice da linha a escrever (compreendido entre 0 e ([LCD.LINES] - 1))
     *
     */
    private fun updateDateTime(line: Int){
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
    private fun writeDate(line: Int, alignment: TUI.Align){
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
    private fun writeHour(line:Int, alignment: TUI.Align){
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
     * @return O [User] identificado por UIN no [usersSet]
     */
    private fun readEntry():User?{
        TUI.clearLine(1)
        val uinText = "UIN:"
        TUI.writeSentence(uinText, TUI.Align.Left, 1)
        val uin = TUI.readInteger(1, uinText.length,3, visible=true, missing =true)
        if(uin < 0) // < 0 significa que ocorreu um erro, ou que o utilizador se enganou
            return null
        else{
            TUI.clearLine(1)
            var pin:Int
            val textPin = "PIN:"
            do{
                TUI.writeSentence(textPin, TUI.Align.Left, 1)
                pin = TUI.readInteger(1,textPin.length, 4, visible=false, missing =true)
            }while (pin == -2)
            val user = usersSet[uin]
            if(user != null && pin == user.PIN)
                return user
            else
                return null
        }
    }

    /**
     * Faz a alteração do PIN de um utilizador
     *
     * @param user o [User] que deseja alterar o PIN
     *
     * @return o [User] com o novo PIN se alterado
     *
     */
    private fun changePin(): Int?{
        val key = TUI.getInputWithTextInterface("Change PIN?", "Yes -> #", DEFAULT_TIME_SLEEP)
        LCD.clear()
        val newPin:Int
        if (key == '#'){
            TUI.writeSentence("New PIN:", TUI.Align.Left, 0)
             newPin = TUI.readInteger(1, 0, 4, visible = false, missing = true)
            LCD.clear()
            TUI.writeSentence("Confirm new PIN:", TUI.Align.Left, 0)
            val confirmPin = TUI.readInteger(1,0, 4, visible = false, missing = true)
            LCD.clear()
             if(newPin == confirmPin && newPin > 0){
                TUI.writeSentence("PIN changed.", TUI.Align.Center, 0)
                return newPin
             }
        }
        TUI.writeSentence("PIN not changed.", TUI.Align.Left, TOP_LINE)
        Time.sleep(DEFAULT_TIME_SLEEP)
        return null
    }

    /**
     * Faz a gestão da entrada/saída de um [User]
     *
     * Recebe o [User] escrevendo uma saudação no LCD
     *
     * Altera o seu PIN se requisitado
     *
     * TODO("Cria um registo de entrada/saída")
     *
     * Controla o mecanismo da porta automática
     *
     * @param user o [User] que inseriu o UIN e o PIN corretamente
     *
     * @return o [User] com informações de entrada/saída atualizadas e o novo PIN se alterado
     */
    private fun manageEntry(user: User): User {
        LCD.clear()
        TUI.writeSentence("Welcome", TUI.Align.Center, TOP_LINE)
        TUI.writeSentence(user.name, TUI.Align.Center, BOTTOM_LINE)
        val changePinKey = TUI.getInputWithTextInterface()
        val newPin:Int? = if(changePinKey == '#') changePin() else null
        LCD.clear()
        val userWithNewPin:User = if(newPin != null) user.copy(PIN = newPin) else user
        val userWithUpdatedTime:User = userUpdateEntryTime(userWithNewPin)
        val exitTime = if(user.entryTime != 0L) Time.getTimeInMillis() else null
        writeEntryAndExitTimeWithAccumulate(userWithUpdatedTime.accumulatedTime, exitTime, userWithUpdatedTime.entryTime)
        manageDoor(userWithUpdatedTime.name)
        return userWithUpdatedTime
    }

    fun userUpdateEntryTime(user: User): User {
        return if (user.entryTime == 0L) {
            user.copy(entryTime = Time.getTimeInMillis())
        } else {
            val accumulatedTimeCalculated = user.accumulatedTime + (Time.getTimeInMillis() - user.entryTime)
            user.copy(accumulatedTime = accumulatedTimeCalculated, entryTime = 0L)
        }
    }

    fun writeEntryAndExitTimeWithAccumulate(accumulate: Long, exit: Long?, entry: Long){
        val formatter = SimpleDateFormat("HH:mm", Locale.UK)
        val dayOfTheWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.UK)
        val exitTimeText = if (exit != null) "$dayOfTheWeek, " + formatter.format(exit) else  "???, ??:??"
        val accumulatedTimeText = msToTimeFormat(accumulate)
        val entryTimeText = "$dayOfTheWeek, " + formatter.format(entry)

        TUI.writeSentence(entryTimeText, TUI.Align.Left, TOP_LINE)
        TUI.writeSentence(exitTimeText, TUI.Align.Left, BOTTOM_LINE)
        TUI.writeSentence(accumulatedTimeText, TUI.Align.Right, if(exit == null) TOP_LINE else BOTTOM_LINE)
        Time.sleep(DEFAULT_TIME_SLEEP)
        LCD.clear()
    }

    /**
     * Faz a gestão da abertura e fecho da porta à entrada de um utilizador
     */
    fun manageDoor(userName:String) {
        TUI.writeSentence("Door opening", TUI.Align.Center, TOP_LINE)
        TUI.writeSentence(userName, TUI.Align.Center, BOTTOM_LINE)
        Door.open(6)
        TUI.clearLine(0)
        TUI.writeSentence("Door opened", TUI.Align.Center, TOP_LINE)
        Time.sleep(DEFAULT_TIME_SLEEP)
        TUI.writeSentence("Door closing", TUI.Align.Center, TOP_LINE)
        Door.close(2)
        LCD.clear()
    }

    /**
     * Inicializa os objetos que necessitam de inicialização
     *
     * [HAL]
     * [LCD]
     * [Door]
     * [TUI]
     */
   private fun initializeObjects(){
        HAL.init()
        LCD.init()
        Door.init()
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
        val currentHours = totalHours %24
        return "$currentHours:$currentMinutes"
    }
}

fun main(){
    App.run()
}
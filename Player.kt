import java.io.File

class Player(_name: String,
             override var healthPoints: Int = 100,
             val isBlessed: Boolean,
             private val isImmortal: Boolean):Fightable {

    var name = _name
        get() = "來自 $hometown 的 ${field.capitalize()} "
        private set(value) {
            field = value.trim()
        }
    val hometown by lazy { selectHometown() }
    var currentPosition = Coordinate(0, 0)

    init {
        require(healthPoints > 0, { "健康值必須大於0" })
        require(name.isNotBlank(), { "玩家必須有名字" })
    }


    constructor(name: String) : this(
        name,
        isBlessed = true,
        isImmortal = false
    ) {
        if (name.toLowerCase() == "kar") healthPoints = 40
    }

    fun auraColor(): String {
        val auraVisible = isBlessed && healthPoints > 50 || isImmortal
        val auraColor = if (auraVisible) "綠色" else "沒有"
        return auraColor
    }

    fun formatHealthStatus() =
        when (healthPoints) {
            100 -> "健康狀態極佳"
            in 90..99 -> "有一些小擦傷"
            in 75..89 -> if (isBlessed) {
                "雖有一些傷口，但恢復很快"
            } else {
                "有一些傷口"
            }
            in 15..74 -> "嚴重受傷"

            else -> "情況不妙"
        }


     fun castFireball(numFireballs: Int = 2) =
        println("${name} 橫空變出一杯FireBall. (x$numFireballs)")


    private fun selectHometown() = File("data/towns.txt")
        .readText().split("\r\n").shuffled().first()

    override val diceCount = 3
    override val diceSides = 6

    override fun attack(opponent: Fightable): Int {
        val damageDealt = if (isBlessed) {
            damageRoll * 2
        } else {
            damageRoll
        }
        opponent.healthPoints -= damageDealt
        return damageDealt
    }

}


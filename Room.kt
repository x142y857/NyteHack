import Game.ringBell
open class Room(val name: String) {
    protected open val dangerLevel =5
    var monster: Monster? = Goblin()

    fun description() = "空間: $name \n危險等級: $dangerLevel \n"+
            "怪物: ${monster?.description ?: "none."}"
    open fun load() = "這裡沒什麼可看的..."
}

class TownSquare : Room("Town Square"){
    override val dangerLevel = super.dangerLevel -3
    private var bellSound = "GWONG~~~"

    final override fun load() = "當你進入時，村民們聚集且歡呼！ \n" +
            "${ringBell(bellSound)}"


}


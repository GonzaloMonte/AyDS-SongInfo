package ayds.songinfo.home.view.formatter

class DayFormatter: PrecisionFormatter {
    override fun formatWithPrecision(date:String):String=
        "${getDaySubstring(date)}/${getMonthSubstring(date)}/${getYearSubstring(date)}"


    private fun getDaySubstring(date: String) = date.takeLast(2)
    private fun getMonthSubstring(date: String) = date.substring(5..6)
    private fun getYearSubstring(date: String) = date.take(4)
}
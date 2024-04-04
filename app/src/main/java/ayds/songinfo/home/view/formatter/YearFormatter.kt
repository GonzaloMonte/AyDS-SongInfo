package ayds.songinfo.home.view.formatter

class YearFormatter: PrecisionFormatter {
    override fun formatWithPrecision(date: String): String =
        if (isLeapYear(date.toInt()))
            "$date (leap year)"
        else
            "$date (not a leap year)"

    private fun isLeapYear(year:Int) = (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))


}
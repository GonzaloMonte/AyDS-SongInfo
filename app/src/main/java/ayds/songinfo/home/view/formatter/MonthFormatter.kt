package ayds.songinfo.home.view.formatter

class MonthFormatter: PrecisionFormatter {

    override fun formatWithPrecision(date:String):String{
        val year = getYearSubstring(date)
        val monthNumber = getMonthSubstring(date)
        val monthName = getMonthName(monthNumber)
        return "$monthName, $year"
    }


    private fun getMonthSubstring(date: String) = date.substring(5..6)

    private fun getYearSubstring(date: String) = date.take(4)
    private fun getMonthName(monthNumber:String) =
        when(monthNumber){
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            "12" -> "December"
            else -> ""
        }

}
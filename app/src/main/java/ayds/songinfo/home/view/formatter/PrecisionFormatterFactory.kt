package ayds.songinfo.home.view.formatter

import ayds.songinfo.home.model.entities.Song

interface PrecisionFormatterFactory{
    fun get(song: Song.SpotifySong) : PrecisionFormatter
}

object PrecisionFormatterFactoryImpl : PrecisionFormatterFactory {
    override fun get(song: Song.SpotifySong): PrecisionFormatter {
        return when (song.releaseDatePrecision) {
            "year" -> YearFormatter()
            "month" -> MonthFormatter()
            "day" -> DayFormatter()
            else -> DefaultFormatter()
        }
    }
}
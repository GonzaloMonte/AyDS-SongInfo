package ayds.songinfo.home.view

import ayds.songinfo.home.model.entities.Song.EmptySong
import ayds.songinfo.home.model.entities.Song
import ayds.songinfo.home.view.formatter.PrecisionFormatterFactory

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(private val releaseDateFormatterFactory : PrecisionFormatterFactory)
    : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is Song.SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                "Release date: ${releaseDateFormatterFactory.get(song).formatWithPrecision(song.releaseDate)
                
            }"
            else -> "Song not found"
        }
    }
}
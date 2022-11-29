package com.example.everypractice.data.domain

data class GenresMovies(
    val id: Int,
) {
    val name: String
        get() = when (id) {
            28 -> "Action"
            12 -> "Adventure"
            16 -> "Animation"
            35 -> "Comedy"
            80 -> "Crime"
            99 -> "Documentary"
            18 -> "Drama"
            10751 -> "Family"
            14 -> "Fantasy"
            36 -> "History"
            27 -> "Horror"
            10402 -> "Music"
            9648 -> "Mystery"
            10749 -> "Romance"
            878 -> "Sci-Fi"
            10770 -> "TV Movie"
            53 -> "Thriller"
            10752 -> "War"
            37 -> "Western"
            else -> ""
        }

    val emoji: String
        get() = when (id) {
            28 -> listOf("\uD83E\uDD2F","\uD83D\uDE0E").takeFirstRandom()
            12 -> listOf("\uD83D\uDC18").takeFirstRandom()
            16 -> listOf("\uD83E\uDDDC\u200D♀️").takeFirstRandom()
            35 -> listOf("\uD83E\uDD21","\uD83D\uDE02","\uD83D\uDE1D").takeFirstRandom()
            80 -> listOf("\uD83D\uDD2A").takeFirstRandom()
            99 -> listOf("\uD83D\uDC18","\uD83D\uDC28","\uD83E\uDD81").takeFirstRandom()
            18 -> listOf("\uD83E\uDD12","\uD83E\uDD15","\uD83D\uDE2D").takeFirstRandom()
            10751 -> listOf("\uD83D\uDC6A").takeFirstRandom()
            14 -> listOf("\uD83E\uDD29","\uD83E\uDDDA","\uD83D\uDC78","\uD83E\uDDD9\u200D♂️").takeFirstRandom()
            36 -> listOf("\uD83D\uDCD6").takeFirstRandom()
            27 -> listOf("\uD83D\uDC7B","\uD83C\uDF83").takeFirstRandom()
            10402 -> listOf("\uD83C\uDFB5","\uD83C\uDFB6").takeFirstRandom()
            9648 -> listOf("\uD83D\uDE2E","\uD83D\uDD75️","\uD83E\uDDD0").takeFirstRandom()
            10749 -> "\uD83E\uDD70"
            878 -> listOf("\uD83E\uDDBF","\uD83D\uDC7D","\uD83E\uDD16").takeFirstRandom()
            10770 -> listOf("\uD83D\uDCFA").takeFirstRandom()
            53 -> listOf("\uD83D\uDE30","\uD83D\uDE16").takeFirstRandom()
            10752 -> listOf("\uD83D\uDD2B","\uD83E\uDDE8").takeFirstRandom()
            37 -> listOf("\uD83E\uDD20").takeFirstRandom()
            else -> ""
        }

}

private fun List<String>.takeFirstRandom(): String{
    return this.shuffled()[0]
}

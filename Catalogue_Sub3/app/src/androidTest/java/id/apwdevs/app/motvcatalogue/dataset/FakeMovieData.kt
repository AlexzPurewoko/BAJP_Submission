package id.apwdevs.app.motvcatalogue.dataset

import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.model.DataModel
import java.util.*

object FakeMovieData : FakeShortAboutData {

    @Suppress("unused")
    val movieName = arrayOf(
        "Alita: Battle Angel",
        "A star is born",
        "Aquaman",
        "Avengers: Infinity War",
        "Bohemian Rhapsody",
        "Creed II",
        "Cold Pursuit",
        "Glass",
        "How to Train Your Dragon: The Hidden World",
        "Mary Queen of Scots",
        "Master Z: Ip Man Legacy",
        "Mortal Engines",
        "Ralph Breaks the Internet",
        "Robin Hood"
    )

    @Suppress("unused")
    val movieDrawableRes = intArrayOf(
        R.drawable.poster_alita,
        R.drawable.poster_a_start_is_born,
        R.drawable.poster_aquaman,
        R.drawable.poster_infinity_war,
        R.drawable.poster_bohemian,
        R.drawable.poster_creed,
        R.drawable.poster_cold_persuit,
        R.drawable.poster_glass,
        R.drawable.poster_how_to_train,
        R.drawable.poster_marry_queen,
        R.drawable.poster_master_z,
        R.drawable.poster_mortal_engines,
        R.drawable.poster_ralph,
        R.drawable.poster_robin_hood
    )

    val movieShortAboutKey = arrayOf(
        "Original Language",
        "Runtime",
        "Budget",
        "Revenue",
        "Genres"
    )

    val movieShortAbout = arrayOf(
        "English|2h 2m|$170,000,000.00|$404,852,543.00|Action,Science Fiction,Thriller,Adventure",
        "English|2h 15m|$36,000,000.00|$433,888,866.00|Drama,Romance,Music",
        "English|2h 24m|$160,000,000.00|$1,143,689,193.00|Action, Adventure, Fantasy",
        "English|2h 29m|$300,000,000.00|$2,046,239,637.00|Adventure,Action,Fantasy",
        "English|2h 15m|$52,000,000.00|$835,137,710.00|Drama, Music",
        "English|2h 10m|$50,000,000.00|$137,944,327.00|Drama",
        "English|1h 59m|$60,000,000.00|$59,213,931.00|Action",
        "English|2h 9m|$20,000,000.00|$246,941,965.00|Thriller, Mystery, Drama",
        "English|1h 44m|$129,000,000.00|$517,526,875.00|Animation, Family, Adventure",
        "English|2h 4m|$25,000,000.00|$37,807,625.00|Drama,History",
        "Cantonese|1h 47m|-|-|Action",
        "English|2h 9m|$100,000,000.00|$104,236,467.00|Adventure, Fantasy",
        "English|1h 52m|$175,000,000.00|$529,221,154.00|Family,Animation,Comedy,Adventure",
        "English|1h 56m|$100,000,000.00|$73,260,114.00|Adventure, Action, Thriller"
    )

    @Suppress("unused")
    val movieReleasedTimeList = arrayOf(
        "January 31, 2019",
        "October 3, 2018",
        "December 7, 2018",
        "April 25, 2018",
        "October 24, 2018",
        "November 21, 2018",
        "February 7, 2019",
        "January 16, 2019",
        "January 3, 2019",
        "December 7, 2018",
        "December 20, 2018",
        "November 27, 2018",
        "November 20, 2018",
        "November 20, 2018"
    )

    @Suppress("unused")
    val movieOverview = arrayOf(
        "When Alita awakens with no memory of who she is in a future world she does not recognize, she is taken in by Ido, a compassionate doctor who realizes that somewhere in this abandoned cyborg shell is the heart and soul of a young woman with an extraordinary past.",
        """Seasoned musician Jackson Maine discovers — and falls in love with — struggling artist Ally. She has just
    about given up on her dream to make it big as a singer — until Jack coaxes her into the spotlight. But even
    as Ally's career takes off, the personal side of their relationship is breaking down, as Jack fights an
    ongoing battle with his own internal demons."""
        ,
        """Once home to the most advanced civilization on Earth, Atlantis is now an underwater kingdom ruled by the
    power-hungry King Orm. With a vast army at his disposal, Orm plans to conquer the remaining oceanic people
    and then the surface world. Standing in his way is Arthur Curry, Orm's half-human, half-Atlantean brother
    and true heir to the throne."""
        ,
        """As the Avengers and their allies have continued to protect the world from threats too large for any one
    hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy,
    his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his
    twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate
    of Earth and existence itself has never been more uncertain."""
        ,
        """Singer Freddie Mercury, guitarist Brian May, drummer Roger Taylor and bass guitarist John Deacon take the
    music world by storm when they form the rock 'n' roll band Queen in 1970. Hit songs become instant classics.
    When Mercury's increasingly wild lifestyle starts to spiral out of control, Queen soon faces its greatest
    challenge yet – finding a way to keep the band together amid the success and excess."""
        ,
        "Between personal obligations and training for his next big fight against an opponent with ties to his family's past, Adonis Creed is up against the challenge of his life."
        ,
        "The quiet family life of Nels Coxman, a snowplow driver, is upended after his son's murder. Nels begins a vengeful hunt for Viking, the drug lord he holds responsible for the killing, eliminating Viking's associates one by one. As Nels draws closer to Viking, his actions bring even more unexpected and violent consequences, as he proves that revenge is all in the execution.",
        """In a series of escalating encounters, former security guard David Dunn uses his supernatural abilities to
    track Kevin Wendell Crumb, a disturbed man who has twenty-four personalities. Meanwhile, the shadowy
    presence of Elijah Price emerges as an orchestrator who holds secrets critical to both men."""
        ,
        """As Hiccup fulfills his dream of creating a peaceful dragon utopia, Toothless’ discovery of an untamed,
    elusive mate draws the Night Fury away. When danger mounts at home and Hiccup’s reign as village chief is
    tested, both dragon and rider must make impossible decisions to save their kind."""
        ,
        "In 1561, Mary Stuart, widow of the King of France, returns to Scotland, reclaims her rightful throne and menaces the future of Queen Elizabeth I as ruler of England, because she has a legitimate claim to the English throne. Betrayals, rebellions, conspiracies and their own life choices imperil both Queens. They experience the bitter cost of power, until their tragic fate is finally fulfilled.",
        "Following his defeat by Master Ip, Cheung Tin Chi (Zhang) tries to make a life with his young son in Hong Kong, waiting tables at a bar that caters to expats. But it s not long before the mix of foreigners, money, and triad leaders draw him once again to the fight.",
        """Many thousands of years in the future, Earth’s cities roam the globe on huge wheels, devouring each other
    in a struggle for ever diminishing resources. On one of these massive traction cities, the old London, Tom
    Natsworthy has an unexpected encounter with a mysterious young woman from the wastelands who will change the
    course of his life forever."""
        ,
        "Video game bad guy Ralph and fellow misfit Vanellope von Schweetz must risk it all by traveling to the World Wide Web in search of a replacement part to save Vanellope's video game, \"Sugar Rush.\" In way over their heads, Ralph and Vanellope rely on the citizens of the internet — the netizens — to help navigate their way, including an entrepreneur named Yesss, who is the head algorithm and the heart and soul of trend-making site BuzzzTube.",
        "A war-hardened Crusader and his Moorish commander mount an audacious revolt against the corrupt English crown."
    )


    override fun getExpectedShortData(): ArrayList<DataModel> {
        val listResult = ArrayList<DataModel>()
        for ((x, title) in movieName.withIndex()) {
            listResult.add(
                DataModel(
                    movieDrawableRes[x],
                    movieOverview[x],
                    movieReleasedTimeList[x],
                    title
                )
            )
        }
        return listResult
    }
}

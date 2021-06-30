package com.example.kinoship.utils

fun getGenre(ids:List<Int>):String{
    var genre=""
    for(i in ids ){
        when (i) {
            28 -> genre+="Action|"
            12 -> genre+="Adventure|"
            16 -> genre+="Animation|"
            35 -> genre+="Comedy|"
            80 -> genre+="Crime|"
            99 -> genre+="Documentary|"
            18 -> genre+="Drama|"
            10751 -> genre+="Family|"
            14 -> genre+="Fantasy|"
            36 -> genre+="History|"
            27 -> genre+="Horror|"
            10402 -> genre+="Music|"
            9648 -> genre+="Mystery|"
            10749 -> genre+="Romance|"
            878 -> genre+="Science Fiction|"
            10770 -> genre+="TV Movie|"
            53 -> genre+="Thriller|"
            10752 -> genre+="War|"
            37 -> genre+="Western|"
            else -> genre+="Unresolved symbol:"+i
        }
    }
    if(!genre.equals(""))
        genre=genre.substring(0,genre.length-1)
    return genre
}
fun getRating(rating:Double):Float{
    return when(rating){
        in 0f..1f->0.5f
        in 1.1f..2f->1.0f
        in 2.1f..3f->1.5f
        in 3.1f..4f->2f
        in 4.1f..5f->2.5f
        in 5.1f..6f->3f
        in 6.1f..7f->3.5f
        in 7.1f..8f->4f
        in 8.1f..9f->4.5f
        else ->5f
    }
}
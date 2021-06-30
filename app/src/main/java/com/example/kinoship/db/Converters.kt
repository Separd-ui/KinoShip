package com.example.kinoship.db


import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListToString(ids:List<Int>):String{
        var genreIds=""
        for(i in ids)
           genreIds+= ",$i"
        return genreIds
    }

    @TypeConverter
    fun fromStringToList(genreIds:String):List<Int>{
        val ids= mutableListOf<Int>()
        val array=genreIds.split(",")

        for(n in array){
            if(n.isNotEmpty())
                ids.add(n.toInt())
        }
        return ids
    }
}
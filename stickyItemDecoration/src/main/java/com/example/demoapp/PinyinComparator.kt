package com.example.demoapp

class PinyinComparator : Comparator<City> {
    override fun compare(o1: City, o2: City): Int {
        return if (o1.sortLetter == "@"
                || o2.sortLetter == "#") {
            -1
        } else if (o1.sortLetter == "#"
                || o2.sortLetter == "@") {
            1
        } else {
            o1.sortLetter.compareTo(o2.sortLetter)
        }
    }
}
package com.example.everypractice.prinoptions.search.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial")
data class LastSearch(
    @PrimaryKey (autoGenerate = true) val id : Int = 0,
    @ColumnInfo (name = "last_search") val lastSearch: String
)

//DEBEMOS CAMBIAR EL PRIMARY KEY, PARA QUE ESTE SEA EL NAME Y NO EL ID
//ASI CON ESTE PODEMOS EVITAR DUPLICADOS DE NAMES Y DIFERENTE ID
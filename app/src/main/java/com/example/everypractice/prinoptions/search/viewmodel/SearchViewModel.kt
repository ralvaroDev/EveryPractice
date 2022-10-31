package com.example.everypractice.prinoptions.search.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

//ACCEDENIS A LOS DATOS DESDE EL REPOSITORIO, NO NECESITAMOS OBTENER LA BASE DE DATOS COMPLETA
class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

    //CUANDO PIDE Y NO MANDA SE USA SOLO UNA VARIABLE PARA ALMACENAR
    //A DIFERENCIA DEL ONE HISTORY QUE MANDA EL ID PARA RECIEN RECIBIR
    val allHistory: LiveData<List<LastSearch>> = repository.allHistory.asLiveData()

    private fun listOut() = repository.obtainListWords()
    /*fun obtainOneHistorial(id:Int): LiveData<LastSearch>{
        return repository.obtainOneHistorial(id).asLiveData()
    }*/

    private fun obtainIdFromWord(word: String): Int {
        return repository.obtainIdAlreadySearch(word)
    }

    //HACER QUE EL DAO NOS DE LA LISTA DE TODOS LOS NOMBRES
    //Y QUE AL NOSOTROS INGRESAR POR IU UNA STRING, VENGA AQUI Y VERIFIQUE SI ESTA DEENTRO DE ESTA
    // LISTA, EN CASO SI, QUE AGREGUE, EN CASO NO QUE VAYA A LA FUNCION DE AGREGAR NUEVO

    //FUNCION QUE CREA UNA VARIABLE Y USA CORRUTINAS
    //ESTA ES LA QUE NOS TRAE EL VALOR DEL FRAGMENT QUE SE BUSCARA
    fun createObjectAndSaveInDatabase(name: String) {
        val variableSearch = createLastSearchObject(name)
        addToDatabase(variableSearch)
    }

    //FUNCIONES
    //ESTA NOS CONVIERTE LA PALABRA EN UN OBJETO LAS SEARCH
    fun createLastSearchObject(historialName: String): LastSearch {
        return LastSearch(lastSearch = historialName)
    }

    //FUNCIONES CORRUTINAS DIRECTAS QUE RECIBEN UNA VARIABLE ELEMENTO TIPO
    //ESTA SE CONECTA CON EL REPOSITORIO

    private fun addToDatabase(lastSearch: LastSearch) {
        viewModelScope.launch (Dispatchers.IO) {
            Log.d("aaa", "add ${lastSearch.lastSearch} to database")
            repository.insert(lastSearch)
        }
    }

    fun deleteSearchDatabase(lastSearch: LastSearch) {
        viewModelScope.launch (Dispatchers.IO) {
            Log.d("aaa", "delete ${lastSearch.lastSearch} from database")
            repository.delete(lastSearch)
        }
    }

    fun saveWordInDatabase(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val wordObject = createLastSearchObject(word)
            /*val list = repository.obtainListWords()*/
            Log.d("aaa", "list collected: ${listOut()}")
            if (listOut().contains(word)) {
                val currentIdOfWord = obtainIdFromWord(word)
                Log.d("aaa", "current id: $currentIdOfWord")
                deleteSearchDatabase(LastSearch(currentIdOfWord, word))
            }
            addToDatabase(wordObject)
        }
    }
}


class SearchViewModelFactory(private val repository: SearchRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
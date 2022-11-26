package com.example.everypractice.prinoptions.search.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.repository.SearchRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

//ACCEDENIS A LOS DATOS DESDE EL REPOSITORIO, NO NECESITAMOS OBTENER LA BASE DE DATOS COMPLETA
class SearchViewModel(private val repository: SearchRepository) : ViewModel() {

    val scope: CoroutineScope
        get() = viewModelScope

    //CUANDO PIDE Y NO MANDA SE USA SOLO UNA VARIABLE PARA ALMACENAR
    //A DIFERENCIA DEL ONE HISTORY QUE MANDA EL ID PARA RECIEN RECIBIR
    val allHistory: LiveData<List<LastSearch>> = repository.allHistory.asLiveData()
    private val coroutineScope = CoroutineScope(Job())

    val allStateFlow = repository.allHistory
        .onEach { list ->
            val currentTimestamp = System.currentTimeMillis()
            list.map {
                it.timestamp = currentTimestamp - it.timestamp
            }
        }.stateIn(coroutineScope, SharingStarted.Eagerly, allHistory.value)


    val allFlowHistory = flow {
        Log.d("aaa", "pre while")
        while (true) {
            val innerFlow = repository.allHistory.map {
                val currentTimestamp = System.currentTimeMillis()
                it.forEach { element ->
                    element.timestamp =
                        /*(
                            viewModelScope.launch {
                                triggerFlow().collectLatest {
                                    it.toLong()
                                }
                            }

                    )*/
                        currentTimestamp - element.timestamp
                }
                it
            }.asLiveData()
            Log.d("aaa", "emit flow: ${repository.allHistory}")
            emit(innerFlow)
            delay(5000L)
        }
    }



    private fun wordsOfLastSearchList() = repository.obtainListWords()
    /*fun obtainOneHistorial(id:Int): LiveData<LastSearch>{
        return repository.obtainOneHistorial(id).asLiveData()
    }*/

    private fun obtainIdFromWord(word: String): Int {
        return repository.obtainIdByWord(word)
    }

    //HACER QUE EL DAO NOS DE LA LISTA DE TODOS LOS NOMBRES
    //Y QUE AL NOSOTROS INGRESAR POR IU UNA STRING, VENGA AQUI Y VERIFIQUE SI ESTA DEENTRO DE ESTA
    // LISTA, EN CASO SI, QUE AGREGUE, EN CASO NO QUE VAYA A LA FUNCION DE AGREGAR NUEVO

    //FUNCION QUE CREA UNA VARIABLE Y USA CORRUTINAS
    //ESTA ES LA QUE NOS TRAE EL VALOR DEL FRAGMENT QUE SE BUSCARA
    fun createObjectAndSaveInDatabase(name: String) {
        val variableSearch = createLastSearchObject(name)
        addLastSearchToDatabase(variableSearch)
    }

    //FUNCIONES
    //ESTA NOS CONVIERTE LA PALABRA EN UN OBJETO LAS SEARCH
    private fun createLastSearchObject(historialName: String): LastSearch {
        return LastSearch(lastSearch = historialName, timestamp = System.currentTimeMillis())
    }

    //FUNCIONES CORRUTINAS DIRECTAS QUE RECIBEN UNA VARIABLE ELEMENTO TIPO
    //ESTA SE CONECTA CON EL REPOSITORIO

    private fun addLastSearchToDatabase(lastSearch: LastSearch) {
        viewModelScope.launch (Dispatchers.IO) {
            Log.d("aaa", "add ${lastSearch.lastSearch} to database")
            repository.insert(lastSearch)
        }
    }

    fun deleteLastSearchDatabase(lastSearch: LastSearch) {
        viewModelScope.launch (Dispatchers.IO) {
            Log.d("aaa", "delete ${lastSearch.lastSearch} from database")
            repository.delete(lastSearch)
        }
    }

    fun updateWordInLastSearchList(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val wordObject = createLastSearchObject(word)
            Log.d("aaa", "list collected: ${wordsOfLastSearchList()}")
            if (wordsOfLastSearchList().contains(word)) {
                /*repository.deleteLastSearchByWord(word)*/
                repository.updateLastSearchTimestamp(word, System.currentTimeMillis())
            } else {
                addLastSearchToDatabase(wordObject)
            }
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
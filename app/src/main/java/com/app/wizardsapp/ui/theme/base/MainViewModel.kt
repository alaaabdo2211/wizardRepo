package com.app.wizardsapp.ui.theme.base

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.wizardsapp.data.WizardsRepository
import com.app.wizardsapp.data.remote.ElixirsIdModel
import com.app.wizardsapp.data.remote.WizardsByIdModel
import com.app.wizardsapp.data.remote.WizardsModel
import com.app.wizardsapp.network.ApiService
import com.app.wizardsapp.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WizardsRepository) : ViewModel(){

    private val _wizardsState = MutableStateFlow<ApiState<WizardsModel>>(ApiState.Loading)
    val wizardsState: StateFlow<ApiState<WizardsModel>> = _wizardsState


    private val _wizardsByIdState = MutableStateFlow<ApiState<WizardsByIdModel>>(ApiState.Loading)
    val wizardsByIdState: StateFlow<ApiState<WizardsByIdModel>> = _wizardsByIdState

    private val _elixirsByIdState = MutableStateFlow<ApiState<List<ElixirsIdModel>>>(ApiState.Loading)
    val elixirsByIdState: StateFlow<ApiState<List<ElixirsIdModel>>> = _elixirsByIdState


    fun fetchWizards() {

        viewModelScope.launch {
            _wizardsState.value = ApiState.Loading

            try {
                val wizards = repository.getWizards()  // API call
                _wizardsState.value = ApiState.Success(wizards)
                Log.d("wizards" , repository.getWizards().toString())

            } catch (e: Exception) {
                _wizardsState.value = ApiState.Error(
                    message = "Failed to fetch data: ${e.message}",
                    onRetry = { fetchWizards() }  // Retry logic
                )
                Log.e("error handle data" , e.toString())

            }
        }
    }

    fun fetchWizardById(id: String) {
        viewModelScope.launch {
            _wizardsByIdState.value = ApiState.Loading

            try {
                val wizard = repository.getWizardsById(id)
                _wizardsByIdState.value = ApiState.Success(wizard)

                Log.d("wizardsById" , repository.getWizardsById("118e7366-1c65-4275-8121-8f6c553e5783").toString())
            } catch (e: Exception) {
                _wizardsByIdState.value = ApiState.Error(
                    message = "Failed to fetch data: ${e.message}",
                    onRetry = { fetchWizardById("118e7366-1c65-4275-8121-8f6c553e5783") }  // Retry logic
                )
                Log.e("error handle data" , e.toString())
            }
        }
    }

    fun fetchElixirsById(id: String) {
        viewModelScope.launch {
            _elixirsByIdState.value = ApiState.Loading

            try {
                val elixirs = repository.getElixirsById(id)
                _elixirsByIdState.value = ApiState.Success(listOf(elixirs) )

                Log.d("elixirsById" , repository.getElixirsById( "4979b527-74c8-4a3c-8879-53178eb9a73d").toString())
            } catch (e: Exception) {
                _elixirsByIdState.value = ApiState.Error(
                    message = "Failed to fetch data: ${e.message}",
                    onRetry = { fetchElixirsById("4979b527-74c8-4a3c-8879-53178eb9a73d") }  // Retry logic
                )
                Log.e("error handle data" , e.toString())
            }
        }
    }



}


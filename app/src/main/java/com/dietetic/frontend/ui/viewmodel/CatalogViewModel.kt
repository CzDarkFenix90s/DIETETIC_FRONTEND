package com.dietetic.frontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dietetic.frontend.domain.model.Course
import com.dietetic.frontend.domain.repository.CourseFilters
import com.dietetic.frontend.domain.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class CatalogUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val total: Int = 0,
    val hasMore: Boolean = false,
    val search: String = "",
    val page: Int = 1,
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogUiState())
    val state: StateFlow<CatalogUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        load()
    }

    fun load(reset: Boolean = true) {
        val current = _state.value
        val targetPage = if (reset) 1 else current.page

        if (reset) {
            _state.update { it.copy(isLoading = true, error = null, page = 1, courses = emptyList()) }
        } else {
            if (current.isLoadingMore || !current.hasMore) return
            _state.update { it.copy(isLoadingMore = true) }
        }

        viewModelScope.launch {
            val filters = CourseFilters(
                search = current.search.ifBlank { null },
                isActive = true,
                page = targetPage,
                pageSize = 10
            )
            courseRepository.getCourses(filters)
                .onSuccess { (courses, total) ->
                    _state.update { s ->
                        val newList = if (reset) courses else s.courses + courses
                        s.copy(
                            courses = newList,
                            total = total,
                            hasMore = newList.size < total,
                            isLoading = false,
                            isLoadingMore = false,
                            page = targetPage + 1,
                            error = null,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(
                        isLoading = false, 
                        isLoadingMore = false, 
                        error = e.localizedMessage ?: "Error de conexión"
                    ) }
                }
        }
    }

    fun setSearch(query: String) {
        _state.update { it.copy(search = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            load(reset = true)
        }
    }

    fun loadMore() = load(reset = false)
    fun refresh()  = load(reset = true)
}

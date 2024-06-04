import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.paging.entity.StoryEntity
import com.dicoding.storyapp.data.repo.UserRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.repo.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {
    fun getStori() = storyRepository.getStoriesPaging()
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val stories: LiveData<PagingData<StoryEntity>> =
        storyRepository.getStoriesPaging().cachedIn(viewModelScope)
}

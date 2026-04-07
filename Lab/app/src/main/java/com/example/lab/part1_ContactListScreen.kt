package com.example.lab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

// 1. ViewModel จำลองข้อมูลและเก็บ State
class ContactViewModel : ViewModel() {
    
    private val _contacts = MutableStateFlow<List<String>>(emptyList())
    val contacts = _contacts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentPage = 0
    private val itemsPerPage = 20

    // สร้างข้อมูลจำลอง A-Z (กลุ่มละ 5 คน)
    private val allMockData = ('A'..'Z').flatMap { char ->
        List(5) { "$char - Mock Person ${it + 1}" }
    }

    init {
        loadMore() // เรียกล็อกอินข้อมูลครั้งแรก
    }

    // ฟังก์ชันโหลดข้อมูลเพิ่ม
    fun loadMore() {
        if (_isLoading.value) return // ถ้ากำลังโหลดอยู่ให้ข้ามไป
        
        val startIndex = currentPage * itemsPerPage
        if (startIndex >= allMockData.size) return // โหลดข้อมูลครบแล้ว

        viewModelScope.launch {
            _isLoading.value = true
            
            // ใช้ delay 2 วินาทีเพื่อจำลองระยะเวลาการร้องขอ API
            delay(2000)
            
            val endIndex = (startIndex + itemsPerPage).coerceAtMost(allMockData.size)
            val newItems = allMockData.subList(startIndex, endIndex)
            
            _contacts.value = _contacts.value + newItems
            currentPage++
            _isLoading.value = false
        }
    }
}

// 2. หน้าจอรายชื่อผู้ติดต่อที่รองรับ Sticky Header และ Pagination
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListScreen(viewModel: ContactViewModel = viewModel()) {
    val contacts by viewModel.contacts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    // 3. Trigger Pagination เมื่อเลื่อนมาถึงรายการสุดท้าย
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filter { it != null && it >= contacts.size - 1 }
            .distinctUntilChanged()
            .collect {
                viewModel.loadMore()
            }
    }

    // นำ list มาจัดกลุ่มตามตัวอักษรแรกสุด เพื่อดึงมาเป็น Sticky Header
    val groupedContacts = contacts.groupBy { it.first() }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        groupedContacts.forEach { (initial, contactsForInitial) ->
            // Sticky Header โชว์ตัวอักษรนำหน้า
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = initial.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Items แสดงรายชื่อผู้ติดต่อ
            items(contactsForInitial) { contact ->
                Text(
                    text = contact,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // 4. แสดง CircularProgressIndicator ขณะที่มันกำลังโหลดหน้าถัดไป
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

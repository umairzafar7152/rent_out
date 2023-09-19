package com.zafar.umair.rentout.screens.chats

import androidx.compose.runtime.mutableStateOf
import com.zafar.umair.rentout.CHANNEL_ID
import com.zafar.umair.rentout.MY_CHAT_SCREEN
import com.zafar.umair.rentout.RECIPIENT_ID
import com.zafar.umair.rentout.SETTINGS_SCREEN
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService
) : RentOutViewModel(logService) {

//    lateinit var chatMessages: Flow<List<TextMessage>>

    val options = mutableStateOf<List<String>>(listOf())

    val chatUsers = storageService.chatUsers



    fun onUserClick(userId: String, openScreen: (String) -> Unit) {
//        var channelId = ""
        launchCatching {
            storageService.getOrCreateChatChannel(userId) {
//                channelId = it
                openScreen("$MY_CHAT_SCREEN?$CHANNEL_ID=$it&$RECIPIENT_ID=$userId")
//                launchCatching {
//                    storageService.getChatMessages(it).runCatching {
//                        chatMessages = this
//                    }
//                }
            }
        }
    }

//    fun getChatMessages(
//        channelId: String,
//        onComplete: (List<TextMessage>) -> Unit
//    ) {
//        launchCatching {
//            FirebaseFirestore.getInstance().collection("chatChannels").document(channelId)
//                .collection("messages")
//                .orderBy("time")
//                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                    if(firebaseFirestoreException!=null) {
//                        Log.e("FIRESTORE", "ChatMessagesListener error", firebaseFirestoreException)
//                        return@addSnapshotListener
//                    }
////                val items = mutableListOf<ChatItem>()
////                querySnapshot!!.documents.forEach {
////                    items.add(TextMessageItem)
////                }
//
//                }
//        }
//
//    }

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

//    fun onProductActionClick(openScreen: (String) -> Unit, product: Product, action: String) {
//        when (ProductActionOption.getByTitle(action)) {
//            ProductActionOption.ProductDetails -> openScreen("$PRODUCT_DETAILS_SCREEN?$PRODUCT_ID={${product.id}}")
//            ProductActionOption.ToggleFlag -> onFlagProductClick(product)
//        }
//    }
}

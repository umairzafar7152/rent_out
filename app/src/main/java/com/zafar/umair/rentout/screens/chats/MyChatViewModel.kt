package com.zafar.umair.rentout.screens.chats

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.zafar.umair.rentout.CHANNEL_ID
import com.zafar.umair.rentout.RECIPIENT_ID
import com.zafar.umair.rentout.SETTINGS_SCREEN
import com.zafar.umair.rentout.model.TextMessage
import com.zafar.umair.rentout.model.service.AccountService
import com.zafar.umair.rentout.model.service.LogService
import com.zafar.umair.rentout.model.service.StorageService
import com.zafar.umair.rentout.screens.RentOutViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : RentOutViewModel(logService) {

    var users = storageService.chatUsers

    private var channelId: String?
    var recipientId: String?
    val newMessage = mutableStateOf(TextMessage())
    lateinit var chatMessages: Flow<List<TextMessage>>

    init {
//        newMessage.value = newMessage.value.copy(senderId = accountService.currentUserId)
        channelId = savedStateHandle.get<String>(CHANNEL_ID)
        recipientId = savedStateHandle.get<String>(RECIPIENT_ID)
        if (channelId != null) {
            launchCatching {
                    chatMessages = storageService.getChatMessages(channelId!!)
//                        storageService.getProduct(channelId.idFromParameter()) ?: Product()
            }
        }
    }

//    fun getRecipientId(): String? {
//        return recipientId
//    }

    fun isSelf(chatMessage: TextMessage): Boolean {
        return chatMessage.senderId == accountService.currentUserId
    }

    fun onMessageChange(newValue: String) {
        newMessage.value = newMessage.value.copy(text = newValue)
    }

    fun onSendClick() {
        val notificationData = mapOf(
            "title" to "Notification Title",
            "body" to "Notification Body",
            "screen" to "target_screen" // The screen to open in the app when the notification is clicked
        )
        launchCatching {
            newMessage.value = newMessage.value.copy(
                time = Date(),
                senderId = accountService.currentUserId,
                recipientId = recipientId!!,
                senderName = storageService.getUser(accountService.currentUserId)!!.email
            )
            val remoteMessage = RemoteMessage.Builder(storageService.getUser(recipientId!!)!!.fcmToken)
                .setData(notificationData)
                .build()
            FirebaseMessaging.getInstance().send(remoteMessage)
            storageService.sendMessage(newMessage.value, channelId!!).runCatching {
                newMessage.value = TextMessage()
            }
        }
    }

//    val options = mutableStateOf<List<String>>(listOf())

//    fun onUserClick(userId: String, openScreen: (String) -> Unit) {
//        var channelId = ""
//        launchCatching {
//            storageService.getOrCreateChatChannel(userId) {
//                channelId = it
//                launchCatching {
//                    storageService.getChatMessages(it).runCatching {
//                        chatMessages = this
//                    }
//                }
//            }
//            openScreen("$MY_CHAT_SCREEN?$CHANNEL_ID={${channelId}}")
//        }
//    }

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

package siarhei.luskanau.iot.doorbell.data.repository

import dev.gitlive.firebase.app
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.storage

open class BaseFirebaseRepository {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        dev.gitlive.firebase.Firebase.database(dev.gitlive.firebase.Firebase.app)
    }

    private val firebaseStorage: FirebaseStorage by lazy {
        dev.gitlive.firebase.Firebase.storage(dev.gitlive.firebase.Firebase.app)
    }

    protected fun getAppDatabase() =
        firebaseDatabase.reference(DOORBELL_APP_KEY)

    protected fun getAppStorage() =
        firebaseStorage.reference(DOORBELL_APP_KEY)

    companion object {
        internal const val DOORBELL_APP_KEY = "doorbell_app"
    }
}

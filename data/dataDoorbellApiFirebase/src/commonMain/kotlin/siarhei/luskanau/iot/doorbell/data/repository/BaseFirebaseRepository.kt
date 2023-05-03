package siarhei.luskanau.iot.doorbell.data.repository

import dev.gitlive.firebase.app
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database

open class BaseFirebaseRepository {

    private val firebaseDatabase: FirebaseDatabase by lazy {
        dev.gitlive.firebase.Firebase.database(dev.gitlive.firebase.Firebase.app)
    }

    protected fun getAppDatabase() =
        firebaseDatabase.reference(DOORBELL_APP_KEY)

    companion object {
        internal const val DOORBELL_APP_KEY = "doorbell_app"
    }
}

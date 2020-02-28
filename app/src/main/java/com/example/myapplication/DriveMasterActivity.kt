package com.example.myapplication

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.CreateFileActivityOptions
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.drive.DriveContents
import com.google.android.gms.drive.DriveResourceClient
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task

import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Objects

/**
 * Created by Darshna on 19-12-2019.
 */
class DriveMasterActivity : Activity() {
    private var mDriveClient: DriveClient? = null
    private var mDriveResourceClient: DriveResourceClient? = null
    private var filePath = ""
    private var mContext: Activity? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        signIn()
    }

    /**
     * Start sign in activity.
     */
    private fun signIn() {
        val GoogleSignInClient = buildGoogleSignInClient()
        startActivityForResult(GoogleSignInClient.signInIntent, REQUEST_CODE_SIGN_IN)
    }

    /**
     * Build a Google SignIn client.
     */
    private fun buildGoogleSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Drive.SCOPE_FILE)
            .build()
        return GoogleSignIn.getClient(mContext!!, signInOptions)
    }

    /**
     * Create a new file and save to Drive.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun saveFileToDrive() {
        filePath = (Environment.getExternalStorageDirectory().toString()
                + "/blackberry/" + "Blackberry_CryptoWallet.pdf")
        // Start by creating a new contents, and setting a callback.
        mDriveResourceClient!!.createContents().continueWithTask { task ->
            createFileIntentSender(Objects.requireNonNull<DriveContents>(task.result), filePath)
                .addOnFailureListener {
                    Toast.makeText(
                        mContext,
                        "Google drive has been sent",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }


    /**
     * Creates an IntentSender to start a dialog activity with configured [ ] for user to create a new photo in Drive.
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createFileIntentSender(driveContents: DriveContents, filePath: String): Task<Void> {
        // Get an output stream for the contents.
        val outputStream = driveContents.outputStream
        val pdfPath = Paths.get(filePath)
        try {
            val bytesPdf = Files.readAllBytes(pdfPath)
            outputStream.write(bytesPdf)
        } catch (e: IOException) {
            e.printStackTrace()
        }


        // Create the initial metadata - MIME type and title.
        // Note that the user will be able to change the title later.
        val metadataChangeSet = MetadataChangeSet.Builder()
            .setMimeType("application/pdf")
            .setStarred(true)
            .setTitle("Blackberry_CryptoWallet")
            .build()

        // Set up options to configure and display the create file activity.
        val createFileActivityOptions = CreateFileActivityOptions.Builder()
            .setInitialMetadata(metadataChangeSet)
            .setInitialDriveContents(driveContents)
            .build()

        return mDriveClient!!
            .newCreateFileActivityIntentSender(createFileActivityOptions)
            .continueWith { task ->
                startIntentSenderForResult(task.result, REQUEST_CODE_CREATOR, null, 0, 0, 0, null)
                null
            }


    }


    /**
     * Google drive callbacks.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SIGN_IN ->
                // Called after user is signed in.
                if (resultCode == Activity.RESULT_OK) {
                    // Use the last signed in account here since it already have a Drive scope.
                    mDriveClient = Drive.getDriveClient(
                        mContext!!,
                        Objects.requireNonNull<GoogleSignInAccount>(
                            GoogleSignIn.getLastSignedInAccount(
                                mContext!!
                            )
                        )
                    )
                    // Build a drive resource client.
                    mDriveResourceClient = Drive.getDriveResourceClient(
                        mContext!!,
                        GoogleSignIn.getLastSignedInAccount(mContext!!)!!
                    )
                    saveFileToDrive()
                } else
                    Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show()

            REQUEST_CODE_CREATOR ->
                // Called after a file is saved to Drive.
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(mContext, "Google drive has been sent", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    companion object {
        private val TAG = "SetupUploadSecurePdfFragment"
        //Google Drive
        private val REQUEST_CODE_SIGN_IN = 0
        private val REQUEST_CODE_CREATOR = 2
    }
}

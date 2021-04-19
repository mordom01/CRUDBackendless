package com.example.intentlearning

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlinx.android.synthetic.main.activity_grade_list.*

class GradeListActivity : AppCompatActivity() {
    companion object {
        val TAG = "GradeListActivity"
    }
    private lateinit var userId : String
    private var gradesList : List<Grade?>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_list)


        // TODO: make a whole recyclerview layout and stuff for the grades

        // for now, log a list of all the grades
        // this retrieves all of the grades from every user, then filters it so only the current
        // user's is shown. generally not the best approach. better to do it server side
        /*Backendless.Data.of(Grade::class.java).find(object : AsyncCallback<List<Grade?>?> {
            override fun handleResponse(foundGrades: List<Grade?>?) {
                // all Grade instances have been found


                // get the current user's objectid
                val userId = Backendless.UserService.CurrentUser().userId

                // make a temporary list for just our matches
                /*val matchingList = mutableListOf<Grade>()
                if(foundGrades != null){
                    for(grade in foundGrades)
                        if(grade?.ownerId == userId){
                            if (grade != null) {
                                matchingList.add(grade)
                            }
                        }
                }



            } */
                /*val matchingList = foundGrades?.filter {
                    it?.ownerId == userId
                }
                Log.d(TAG, "handleResponse: " + matchingList.toString())*/
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault: " + fault.message)
            }
        })*/

        // do an advanced data retrieval with a where clause that matches the ownerId to current userId
        // ownerId = "blah" but 'blah' is our vairable for the userId
        fun createNewGrade(){
            val grade = Grade(assignment = "Chapter 8 FRQ", studentName = "Jonas", ownerId = Backendless.UserService.CurrentUser().userId)
            Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
                    override fun handleResponse(response: Grade?) {
                        Toast.makeText(this@GradeListActivity, "Grade Saved", Toast.LENGTH_SHORT).show()
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Log.d(TAG, "handleFault: ${fault.message}")
                    }
                })
        }
        fun readAllUserGrades(){
            val userId = Backendless.UserService.CurrentUser().userId
            val whereClause = "ownerId = '$userId'"
            val queryBuilder = DataQueryBuilder.create()
            queryBuilder.whereClause = whereClause

            Backendless.Data.of(Grade::class.java).find(queryBuilder,
                object : AsyncCallback<List<Grade?>?> {
                    override fun handleResponse(foundGrades: List<Grade?>?) {
                        Log.d(TAG, "handleResponse: " + foundGrades.toString())
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        Log.d(TAG, "handleFault: " + fault.message)
                    }
                })
        }
        fun deleteFirstGrade(){
            if (!gradesList?.isNullOrEmpty()!!){
                val grade = gradesList?.get(0)
                Backendless.Data.of(Grade::class.java).remove(grade, object : AsyncCallback<Long?> {
                        override fun handleResponse(response: Long?) {
                            Log.d(TAG, "handleResponse: item deleted at $response")
                        }

                        override fun handleFault(fault: BackendlessFault) {
                            Log.d(TAG, "handleFault: ${fault.message}")
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                        }
                    })
            }
        }

        fun updateFirstGrade(){
            if (!gradesList.isNullOrEmpty()){
                val grade = gradesList?.get(0)
                grade?.assignment = "new and improved updated item?"

                Backendless.Data.of(Grade::class.java).save(grade, object : AsyncCallback<Grade?> {
                        override fun handleResponse(response: Grade?) {
                            Log.d(TAG, "item updated $response")
                        }

                        override fun handleFault(fault: BackendlessFault) {
                            Log.d(TAG, "handleFault: ${fault.message}")
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                        }
                    })
            }
        }

        button_gradelist_read.setOnClickListener {
            readAllUserGrades()
        }
        button_gradelist_create.setOnClickListener{
            createNewGrade()
        }
        button_gradelist_delete.setOnClickListener{
            deleteFirstGrade()
        }
        button_gradelist_update.setOnClickListener {
            updateFirstGrade()
        }



// code below here can't guarantee that the data has been retrieved.
// this is executed right away after the async call goes out, but might be
// before the async call comes back

    }
}

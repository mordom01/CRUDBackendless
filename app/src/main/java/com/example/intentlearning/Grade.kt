package com.example.intentlearning

// providing default values for each meets the requirement of having a public, no-argument constructor because now this object could be made without arguments:
// val grade = Grade()
data class Grade(
    var assignment : String = "Assignment",
    var enjoyedAssignment : Boolean = true,
    var letterGradeValue : Int = 4,
    var percentage : Double = 1.0,
    var studentName : String = "Default Student",
    var subject : String = "Default Subject",
    var objectId : String? = null,
    var ownerId : String? = null
) {
}